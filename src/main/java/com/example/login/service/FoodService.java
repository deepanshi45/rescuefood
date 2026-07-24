package com.example.login.service;

import com.example.login.dto.FoodDTO;
import com.example.login.model.Admin;
import com.example.login.model.FoodCategory;
import com.example.login.model.FoodStatus;
import com.example.login.model.Fooditem;
import com.example.login.repository.AdminRepository;
import com.example.login.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private AdminRepository adminRepository;

    // --- 🥗 Post a new food item ---
    public Fooditem postFood(FoodDTO foodDTO) {

        if (foodDTO.getPostedBy() == null) {
            throw new SecurityException("Poster ID (postedBy) cannot be null.");
        }
        if (foodDTO.getExpiryDate() == null || foodDTO.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Food expiry date must be today or in the future.");
        }

        // Fetch Admin entity
        Long adminId = foodDTO.getPostedBy();
        Admin poster = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Poster (Admin) not found with ID: " + adminId));

        // Validate category
        if (foodDTO.getCategory() == null || foodDTO.getCategory().isBlank()) {
            throw new RuntimeException("Food category is required.");
        }
        FoodCategory category;
        try {
            category = FoodCategory.valueOf(foodDTO.getCategory().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid food category: " + foodDTO.getCategory());
        }

        // Create and populate Fooditem
        Fooditem foodItem = new Fooditem();
        foodItem.setTitle(foodDTO.getTitle());
        foodItem.setDescription(foodDTO.getDescription());
        foodItem.setLocation(foodDTO.getLocation());
        foodItem.setCategory(category);

        // Safe quantity conversion
        if (foodDTO.getQuantity() != null) {
            if (foodDTO.getQuantity().compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0 ||
                    foodDTO.getQuantity().compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) < 0) {
                throw new RuntimeException("Quantity value is too large or too small.");
            }
            foodItem.setQuantity(foodDTO.getQuantity().intValue());
        } else {
            foodItem.setQuantity(null);
        }

        foodItem.setUnit(foodDTO.getUnit());
        foodItem.setExpiryDate(foodDTO.getExpiryDate());
        foodItem.setPostedBy(poster);
        foodItem.setStatus(FoodStatus.AVAILABLE);
        foodItem.setImages(foodDTO.getImages()); // persist uploaded image path(s), if any

        return foodRepository.save(foodItem);
    }

    // --- 🔹 Get all food items (Admin view) ---
    public List<Fooditem> getAllFoodItems(String location, String keyword) {
        if (location != null && !location.isEmpty()) {
            return foodRepository.findByLocationContainingIgnoreCase(location);
        }
        if (keyword != null && !keyword.isEmpty()) {
            return foodRepository.findByDescriptionContainingIgnoreCase(keyword);
        }
        return foodRepository.findAll();
    }

    public List<Fooditem> getAllActiveFoodItems() {
        return foodRepository.findAll();
    }

    // --- 🔹 User claims ---
    public List<Fooditem> getClaimsByUserId(Long userId) {
        return foodRepository.findByClaimedBy(userId);
    }

    // --- 🔹 Claim food ---
    public Fooditem claimFood(Long foodId, Long userId) {
        Optional<Fooditem> food = foodRepository.findById(foodId);
        if (food.isPresent()) {
            Fooditem item = food.get();
            if (item.getStatus() == FoodStatus.AVAILABLE) {
                item.setStatus(FoodStatus.CLAIMED);
                item.setClaimedBy(userId);
                return foodRepository.save(item);
            } else {
                throw new RuntimeException("Food item is no longer available");
            }
        }
        throw new RuntimeException("Food item not found with ID: " + foodId);
    }

    // --- 🔹 Update food status (Accept/Decline) ---
    public void updateStatus(Long foodId, FoodStatus newStatus) {
        Fooditem foodItem = foodRepository.findById(foodId)
                .orElseThrow(() -> new RuntimeException("Food item not found for status update."));

        foodItem.setStatus(newStatus);

        // If relisting, clear claimedBy field
        if (newStatus == FoodStatus.AVAILABLE) {
            foodItem.setClaimedBy(null);
        }

        foodRepository.save(foodItem);
    }

    // --- 🔹 Get food by ID ---
    public Optional<Fooditem> getFoodById(Long id) {
        return foodRepository.findById(id);
    }

    // ✅ CORRECTED: Calling the new repository method
    // --- 🔹 Get available food for users ---
    public List<Fooditem> getAvailable() {
        return foodRepository.findByStatusAndExpiryDateGreaterThanEqual(FoodStatus.AVAILABLE, LocalDate.now());
    }

    // --- 🔹 Explore page search (by food name and/or category) ---
    public List<Fooditem> searchAvailableFood(String keyword, String categoryName) {
        String normalizedKeyword = (keyword != null && !keyword.isBlank()) ? keyword.trim() : null;

        FoodCategory category = null;
        if (categoryName != null && !categoryName.isBlank()) {
            try {
                category = FoodCategory.valueOf(categoryName.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                // Unknown/invalid category value: ignore the filter rather than failing the search
                category = null;
            }
        }

        return foodRepository.searchAvailableFood(FoodStatus.AVAILABLE, LocalDate.now(), normalizedKeyword, category);
    }

    // --- 🔹 Count by status ---
    public Long countByStatus(FoodStatus status) {
        return foodRepository.countByStatus(status);
    }

    // --- 🔹 Search ---
    public List<Fooditem> searchFood(String query) {
        if (query == null || query.isBlank()) return getAvailable();
        return foodRepository.findByDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(query, query);
    }
}