package com.example.login.repository;

import com.example.login.model.FoodCategory;
import com.example.login.model.Fooditem;
import com.example.login.model.FoodStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Fooditem, Long> {

    // --- 🔹 Basic Queries ---
    List<Fooditem> findByCategory(FoodCategory category);
    List<Fooditem> findByStatus(FoodStatus status);

    // ✅ CORRECTED: Includes items expiring today or later
    List<Fooditem> findByStatusAndExpiryDateGreaterThanEqual(FoodStatus status, LocalDate expiryDate);

    List<Fooditem> findByStatusOrderByCreatedAtDesc(FoodStatus status);

    // --- 🔹 Admin-based Queries ---
    List<Fooditem> findByPostedById(Long adminId);

    // --- 🔹 User Claims ---
    List<Fooditem> findByClaimedBy(Long userId);

    // --- 🔹 Search Queries ---
    @Query("""
        SELECT f FROM Fooditem f 
        WHERE LOWER(f.title) LIKE LOWER(CONCAT('%', :query, '%')) 
           OR LOWER(f.description) LIKE LOWER(CONCAT('%', :query, '%'))
    """)
    List<Fooditem> searchByTitleOrDescription(@Param("query") String query);

    List<Fooditem> findByLocationContainingIgnoreCase(String location);
    List<Fooditem> findByDescriptionContainingIgnoreCase(String keyword);
    List<Fooditem> findByDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(String query, String query1);

    // --- 🔹 Analytics & Statistics ---
    @Query("SELECT COUNT(f) FROM Fooditem f")
    Long getTotalFoodItems();

    Long countByStatus(FoodStatus status);

    // --- 🔹 Haversine Distance Search (Geo-based) ---
    @Query(value = """
        SELECT *, (
            6371 * acos(
                cos(radians(:userLat)) * cos(radians(location_lat)) * cos(radians(location_lng) - radians(:userLng)) +
                sin(radians(:userLat)) * sin(radians(location_lat))
            )
        ) AS distance
        FROM food_items
        WHERE status = 'AVAILABLE' AND expiry_date >= CURRENT_DATE
        HAVING distance <= :radiusKm
        ORDER BY distance ASC
    """, nativeQuery = true)
    List<Fooditem> findAvailableFoodNearLocation(
            @Param("userLat") double userLat,
            @Param("userLng") double userLng,
            @Param("radiusKm") double radiusKm
    );

    // --- 🔹 Expiry Management ---
    @Transactional
    @Modifying
    @Query("UPDATE Fooditem f SET f.status = :newStatus WHERE f.status = :availableStatus AND f.expiryDate < :currentDate")
    int markExpiredFood(
            @Param("newStatus") FoodStatus newStatus,
            @Param("availableStatus") FoodStatus availableStatus,
            @Param("currentDate") LocalDate currentDate
    );
}