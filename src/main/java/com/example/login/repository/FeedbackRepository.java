//package com.example.login.repository;
//
//// src/main/java/com/example/foodrescue/repository/FeedbackRepository.java
//
//
//
//import com.example.login.model.Feedback;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@Repository
//public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
//
//    // Assuming Feedback entity has fields like: id, message, rating (int 1-5), userUsername, createdAt
//
//    List<Feedback> findByUserUsernameOrderByCreatedAtDesc(String userUsername);
//
//    // Get all feedbacks
//    List<Feedback> findAllByOrderByCreatedAtDesc();
//
//    // Average rating
//    @Query("SELECT AVG(f.rating) FROM Feedback f")
//    Double getAverageRating();
//
//    // Get feedbacks for a specific user
//    @Query("SELECT f FROM Feedback f WHERE f.userUsername = :username")
//    List<Feedback> getFeedbacksByUser(@Param("username") String username);
//
//    // Count feedbacks
//    @Query("SELECT COUNT(f) FROM Feedback f")
//    Long getTotalFeedbacks();
//
//    List<Feedback> findByUserIdOrderByCreatedAtDesc(Long userId);
//
//    Optional<Feedback> findByIdAndUserId(Long feedbackId, Long userId);
//}
//

//
//package com.example.login.repository;
//
//import com.example.login.model.Feedback;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
//    List<Feedback> findByUserUsernameOrderByCreatedAtDesc(String userUsername);
//    List<Feedback> findAllByOrderByCreatedAtDesc();
//
//    @Query("SELECT AVG(f.rating) FROM Feedback f")
//    Double getAverageRating();
//
//    @Query("SELECT f FROM Feedback f WHERE f.userUsername = :username")
//    List<Feedback> getFeedbacksByUser(@Param("username") String username);
//
//    @Query("SELECT COUNT(f) FROM Feedback f")
//    Long getTotalFeedbacks();
//
//    List<Feedback> findByUserIdOrderByCreatedAtDesc(Long userId);
//    Optional<Feedback> findByIdAndUserId(Long feedbackId, Long userId);
//}


package com.example.login.repository;

import com.example.login.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    // 🔹 Get all feedback sorted by creation date (descending)
    List<Feedback> findAllByOrderByCreatedAtDesc();

    // 🔹 Get feedbacks submitted by a specific user
    List<Feedback> findByUserIdOrderByCreatedAtDesc(Long userId);

    // 🔹 Find feedback by ID and user (used for delete access control)
    Optional<Feedback> findByIdAndUserId(Long id, Long userId);
}
