package com.example.login.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "food_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"postedBy"}) // Avoid lazy loading issues in logs
public class Fooditem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Basic Info ---
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodCategory category;

    private Integer quantity;

    @Column(nullable = false)
    private String unit; // e.g., "kg", "pieces"

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    // --- Location ---
    @Column(nullable = false)
    private String location;

    @Column(nullable = true)
    private Double locationLat;

    @Column(nullable = true)
    private Double locationLng;

    // --- Relationships (Poster) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posted_by_id", nullable = false)
    private Admin postedBy; // Expects an Admin object

    // --- Status ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodStatus status;

    // --- Media ---
    @ElementCollection
    @CollectionTable(name = "food_item_images", joinColumns = @JoinColumn(name = "food_item_id"))
    @Column(name = "image_url")
    private List<String> images;

    // --- Claim Info ---
    @Column(name = "claimed_by_id")
    private Long claimedBy;

    // --- Timestamps ---
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "TEXT")
    private String allergens; // optional


    // --- Lifecycle Hooks ---
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = FoodStatus.AVAILABLE;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
