//package com.example.login.model;
//
//// src/main/java/com/example/foodrescue/entity/Feedback.java
//
//
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "feedback")
//public class Feedback {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false, columnDefinition = "TEXT")
//    private String message;
//
//    @Column(nullable = false)
//    private Integer rating; // 1-5 stars
//
//    @Column(nullable = false)
//    private String userUsername; // Who submitted the feedback
//    @Column(name = "user_id")
//    private Long userId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "related_food_id") // Optional: link to FoodItem if feedback is about a specific food
//    private Fooditem relatedFood;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @PrePersist
//    protected void onCreate() {
//        createdAt = LocalDateTime.now();
//        updatedAt = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    protected void onUpdate() {
//        updatedAt = LocalDateTime.now();
//    }
//
//    // Default constructor
//    public Feedback() {}
//
//    // Parameterized constructor
//    public Feedback(String message, Integer rating, String userUsername, Fooditem relatedFood) {
//        this.message = message;
//        this.rating = rating;
//        this.userUsername = userUsername;
//        this.relatedFood = relatedFood;
//    }
//
//    // Getters and Setters
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public Integer getRating() {
//        return rating;
//    }
//
//    public void setRating(Integer rating) {
//        this.rating = rating;
//    }
//
//    public String getUserUsername() {
//        return userUsername;
//    }
//
//    public void setUserUsername(String userUsername) {
//        this.userUsername = userUsername;
//    }
//
//    public Fooditem getRelatedFood() {
//        return relatedFood;
//    }
//
//    public void setRelatedFood(Fooditem relatedFood) {
//        this.relatedFood = relatedFood;
//    }
//
//    public LocalDateTime getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(LocalDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public LocalDateTime getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(LocalDateTime updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    public void setStatus(String pending) {
//    }
//
//    public void setUserId(Long userId) {
//    }
//}
//
package com.example.login.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String message;
    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
