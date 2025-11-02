package com.example.login.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate; // Changed from LocalDateTime

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodDTO {

    private Long id;

    @NotBlank(message = "Food title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Category is required")
    private String category; // e.g., "FRUITS", "VEGETABLES"

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be positive")
    private BigDecimal quantity; // e.g., 5.0 kg or units

    @NotNull(message = "Unit is required")
    private String unit; // e.g., "kg", "pieces", "liters"

    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate; // When the food expires

    @NotBlank(message = "Location is required")
    private String location;

    private Long postedBy; // Reference to User ID who posted it

    private String status = "AVAILABLE";

    private String imageUrl;
    private String allergens;
}