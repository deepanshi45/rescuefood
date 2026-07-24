package com.example.login.service;

import com.example.login.model.*;
import com.example.login.repository.ClaimRepository;
import com.example.login.repository.FoodRepository;
import com.example.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClaimService {

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private UserRepository userRepository;

    // --- 🔹 User claims a food item ---
    @Transactional
    public Claim createClaim(Long foodId, Long userId) {
        Fooditem foodItem = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food item not found with ID: " + foodId));

        if (foodItem.getStatus() != FoodStatus.AVAILABLE) {
            throw new RuntimeException("This item is no longer available to claim.");
        }

        // Defensive guard against a food item somehow already having a pending claim
        // (e.g., a concurrent request) — prevents two users claiming the same item.
        if (claimRepository.findByFoodItemIdAndStatus(foodId, ClaimStatus.PENDING).isPresent()) {
            throw new RuntimeException("This item has already been claimed by another user.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Claim claim = Claim.builder()
                .foodItem(foodItem)
                .user(user)
                .status(ClaimStatus.PENDING)
                .claimedAt(LocalDateTime.now())
                .build();
        claimRepository.save(claim);

        // Immediately mark the item CLAIMED so no one else can claim it while
        // this claim is pending admin review.
        foodItem.setStatus(FoodStatus.CLAIMED);
        foodItem.setClaimedBy(userId); // kept in sync for backward compatibility
        foodRepository.save(foodItem);

        return claim;
    }

    // --- 🔹 Admin approves or rejects a pending claim ---
    @Transactional
    public Claim decideClaim(Long claimId, boolean approve) {
        Claim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Claim not found with ID: " + claimId));

        if (claim.getStatus() != ClaimStatus.PENDING) {
            throw new RuntimeException("This claim has already been decided.");
        }

        Fooditem foodItem = claim.getFoodItem();

        if (approve) {
            claim.setStatus(ClaimStatus.APPROVED);
            foodItem.setStatus(FoodStatus.DONATED);
        } else {
            claim.setStatus(ClaimStatus.REJECTED);
            foodItem.setStatus(FoodStatus.AVAILABLE);
            foodItem.setClaimedBy(null);
        }
        claim.setDecidedAt(LocalDateTime.now());

        foodRepository.save(foodItem);
        return claimRepository.save(claim);
    }

    // --- 🔹 Find the currently pending claim for a food item (used by admin approve/decline) ---
    public Optional<Claim> getPendingClaimForFood(Long foodId) {
        return claimRepository.findByFoodItemIdAndStatus(foodId, ClaimStatus.PENDING);
    }

    // --- 🔹 Claim history for a user, newest first ---
    public List<Claim> getClaimsByUser(Long userId) {
        return claimRepository.findByUserIdOrderByClaimedAtDesc(userId);
    }
}
