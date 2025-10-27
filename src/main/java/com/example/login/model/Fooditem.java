package com.example.login.model;

// src/main/java/com/example/foodrescue/entity/Fooditem.java



import jakarta.persistence.*;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "food_items")
public class Fooditem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodCategory category; // Assume an enum: e.g., FRUITS, VEGETABLES, etc.

    private Integer quantity; // e.g., in kg or units

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(nullable = false)
    private String location; // e.g., address or coordinates

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by_id", nullable = false)
    private Admin postedBy; // Assuming admins post food items

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodStatus status; // e.g., AVAILABLE, CLAIMED, EXPIRED

    @ElementCollection
    @CollectionTable(name = "food_item_images", joinColumns = @JoinColumn(name = "food_item_id"))
    @Column(name = "image_url")
    private List<String> images; // List of image URLs

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = FoodStatus.AVAILABLE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Default constructor
    public Fooditem() {}

    // Parameterized constructor
    public Fooditem(String title, String description, FoodCategory category, Integer quantity,
                    LocalDate expiryDate, String location, Admin postedBy, List<String> images) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
        this.location = location;
        this.postedBy = postedBy;
        this.images = images;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public FoodCategory getCategory() {
        return category;
    }

    public void setCategory(FoodCategory category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Admin getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Admin postedBy) {
        this.postedBy = postedBy;
    }

    public FoodStatus getStatus() {
        return status;
    }

    public void setStatus(FoodStatus status) {
        this.status = status;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setClaimedBy(Long userId) {
    }
}

