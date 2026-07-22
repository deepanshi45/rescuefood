package com.example.login.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @Column(nullable=false, columnDefinition="TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private NotificationType type;

    @Column(nullable=false)
    private String recipientUsername;

    @Column(nullable=false)
    private boolean isRead = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="related_food_id")
    private Fooditem relatedFood;

    @Column(name="created_at") private LocalDateTime createdAt;
    @Column(name="updated_at") private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Notification() {}
    public Notification(String message, NotificationType type, String recipientUsername, Fooditem relatedFood) {
        this.message = message; this.type = type; this.recipientUsername = recipientUsername; this.relatedFood = relatedFood;
    }

    // --- FIX: Add Missing Getters/Setters for isRead and other fields ---

    // Getter for isRead (standard Java boolean naming: is<Field>)
    public boolean isRead() {
        return isRead;
    }

    // Setter for isRead (This is the method the service needs!)
    public void setRead(boolean read) {
        isRead = read;
    }

    // --- Other getters/setters (omitted for brevity, assume they exist) ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    // ... (include all other necessary getters/setters for JPA)
}