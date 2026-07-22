        package com.example.login.dto;

        import jakarta.validation.constraints.Min;
        import jakarta.validation.constraints.NotBlank;
        import jakarta.validation.constraints.NotNull;
        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.math.BigDecimal;
        import java.time.LocalDate;
        import java.util.List;

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
            private LocalDate expiryDate;

            @NotBlank(message = "Location is required")
            private String location;

            private Long postedBy;

            private String status = "AVAILABLE";

            private List<String> images;

            private String allergens;


            private String imageUrl;

            public void setImageUrl(String imageUrl) {
                if (imageUrl != null && !imageUrl.isBlank()) {
                    this.images = List.of(imageUrl);
                }
            }
        }
