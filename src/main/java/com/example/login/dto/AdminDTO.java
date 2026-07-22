    package com.example.login.dto;



    import jakarta.validation.constraints.Email;
    import jakarta.validation.constraints.NotBlank;
    import jakarta.validation.constraints.Pattern;
    import jakarta.validation.constraints.Size;
    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class AdminDTO {

        private Long id;

        @NotBlank(message = "Full name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        private String email;

        @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be valid (10-15 digits)")
        private String phone; // Optional for some operations

        @Size(min = 6, message = "Password must be at least 6 characters")
        private String password; // For signup/login/settings; can be null to keep current

        private String role = "ADMIN"; // Fixed role for admins

        // Admin-specific fields (e.g., for extended permissions)
        private boolean canManageUsers;
        private boolean canApproveFoodPosts;
    }

