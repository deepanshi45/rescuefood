
package com.example.login.repository;

import com.example.login.model.Claim;
import com.example.login.model.ClaimStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository<Claim, Long> {

    // --- 🔹 Claim history for a user (newest first) ---
    List<Claim> findByUserIdOrderByClaimedAtDesc(Long userId);

    // --- 🔹 The currently-active claim for a food item, if any ---
    // Used by the admin approve/reject flow, and as a guard against
    // a food item somehow having two PENDING claims at once.
    Optional<Claim> findByFoodItemIdAndStatus(Long foodItemId, ClaimStatus status);

    // --- 🔹 Dashboard analytics ---
    long countByStatus(ClaimStatus status);
}