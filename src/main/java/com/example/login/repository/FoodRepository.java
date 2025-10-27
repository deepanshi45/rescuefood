package com.example.login.repository;

// src/main/java/com/example/foodrescue/repository/FoodRepository.java


//
//import com.example.login.model.FoodCategory;
//import com.example.login.model.Fooditem;
//import com.example.login.model.FoodStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Repository
//public interface FoodRepository extends JpaRepository<Fooditem, Long> {
//
//    List<Fooditem> findByCategory(FoodCategory category);
//
//    List<Fooditem> findByStatus(FoodStatus status);
//
//    List<Fooditem> findByStatusAndExpiryDateAfter(FoodStatus status, LocalDate expiryDate);
//
//    @Query("SELECT f FROM FoodItem f WHERE LOWER(f.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(f.description) LIKE LOWER(CONCAT('%', :query, '%'))")
//    List<Fooditem> searchByTitleOrDescription(@Param("query") String query);
//
//    // Get recent available foods
//    @Query("SELECT f FROM FoodItem f WHERE f.status = 'AVAILABLE' ORDER BY f.createdAt DESC")
//    List<Fooditem> findRecentAvailableFoods(int limit);
//
//    // Get all available foods
//    List<Fooditem> findByStatus(FoodStatus.AVAILABLE);
//
//    // Get total count
//    @Query("SELECT COUNT(f) FROM FoodItem f")
//    Long getTotalFoodItems();
//
//    // Get by status for history
//    List<Fooditem> findByStatusOrderByCreatedAtDesc(FoodStatus status);
//
//    // Get foods posted by admin
//    List<Fooditem> findByPostedById(Long adminId);
//}


import com.example.login.model.FoodCategory;
import com.example.login.model.Fooditem;
import com.example.login.model.FoodStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Fooditem, Long> {

    List<Fooditem> findByCategory(FoodCategory category);

    List<Fooditem> findByStatus(FoodStatus status);

    List<Fooditem> findByStatusAndExpiryDateAfter(FoodStatus status, LocalDate expiryDate);

    @Query("SELECT f FROM Fooditem f WHERE LOWER(f.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(f.description) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Fooditem> searchByTitleOrDescription(@Param("query") String query);

    // Get recent available foods (pass status from service)
    @Query("SELECT f FROM Fooditem f WHERE f.status = :status ORDER BY f.createdAt DESC")
    List<Fooditem> findRecentFoodsByStatus(@Param("status") FoodStatus status);

    // Get total count
    @Query("SELECT COUNT(f) FROM Fooditem f")
    Long getTotalFoodItems();

    // Get by status for history
    List<Fooditem> findByStatusOrderByCreatedAtDesc(FoodStatus status);

    // Get foods posted by admin
    List<Fooditem> findByPostedById(Long adminId);

    List<Fooditem> findByLocationContainingIgnoreCase(String location);

    List<Fooditem> findByDescriptionContainingIgnoreCase(String keyword);

    List<Fooditem> findByDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(String query, String query1);
}

