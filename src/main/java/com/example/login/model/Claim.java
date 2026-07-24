package com.example.login.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Represents a user's claim on a food item.
 *
 * A Claim has its own lifecycle (PENDING -> APPROVED/REJECTED) independent
 * of Fooditem.status, so claim history is preserved permanently even after
 * an admin decision changes the food item's own status.
 */
@Entity
@Table(name = "claims")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"foodItem", "user"}) // Avoid lazy loading issues in logs
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Relationships ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_item_id", nullable = false)
    private Fooditem foodItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // --- Status ---
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status;

    // --- Timestamps ---
    @Column(name = "claimed_at", nullable = false)
    private LocalDateTime claimedAt;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    @PrePersist
    protected void onCreate() {
        if (claimedAt == null) claimedAt = LocalDateTime.now();
        if (status == null) status = ClaimStatus.PENDING;
    }
}
