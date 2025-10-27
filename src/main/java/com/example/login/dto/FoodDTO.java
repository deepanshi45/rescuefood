package com.example.login.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodDTO {

    private Long id; // Optional: for updates/retrieval

    @NotBlank(message = "Food title is required")
    private String title; // e.g., "Fresh Apples - 5kg"

    @NotBlank(message = "Description is required")
    private String description; // e.g., "Surplus from market, no bruises"

    @NotBlank(message = "Category is required")
    private String category; // e.g., "Fruits", "Vegetables", "Baked Goods"

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be positive")
    private BigDecimal quantity; // e.g., 5.0 kg or units

    @NotNull(message = "Unit is required")
    private String unit; // e.g., "kg", "pieces", "liters"

    @NotNull(message = "Expiry date is required")
    @PastOrPresent(message = "Expiry date cannot be in the future")
    private LocalDateTime expiryDate; // When the food expires

    @NotBlank(message = "Location is required")
    private String location; // e.g., "Downtown Market, City Center"

    private Long postedBy; // Reference to User ID who posted it

    private String status = "AVAILABLE"; // e.g., "AVAILABLE", "CLAIMED", "EXPIRED", "DONATED"

    // Additional fields if needed (e.g., images, allergens)
    private String imageUrl;
    private String allergens; // e.g., "Nuts, Dairy"
}




