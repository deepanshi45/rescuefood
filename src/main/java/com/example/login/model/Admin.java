//package com.example.login.model;
//
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "admins")
//public class Admin {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String name; // Added missing field
//
//    @Column(nullable = false, unique = true)
//    private String username;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false)
//    private String password; // Hashed password
//
//    private String phone;
//
//    private String role; // Added field to store role
//
//    @Column(name = "admin_role")
//    private String adminRole;
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
//    public Admin() {}
//
//    // Parameterized constructor
//    public Admin(String name, String username, String email, String password, String phone, String adminRole) {
//        this.name = name;
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.phone = phone;
//        this.adminRole = adminRole;
//        this.role = "ADMIN"; // Default role
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
//    // Fixed getter
//    public String getName() {
//        return name;
//    }
//
//    // Fixed setter
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getAdminRole() {
//        return adminRole;
//    }
//
//    public void setAdminRole(String adminRole) {
//        this.adminRole = adminRole;
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
//    // Fixed setter
//    public void setRole(String role) {
//        this.role = role;
//    }
//
//    // Fixed getter
//    public String getRole() {
//        return role;
//    }
//}
package com.example.login.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "admins")
public class Admin {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false) private String name;
    @Column(nullable=false, unique=true) private String username;
    @Column(nullable=false, unique=true) private String email;
    @Column(nullable=false) private String password; // hashed

    @Column(nullable=false)
    private String role = "ROLE_ADMIN";

    @Column
    private String adminRole = "ADMIN"; // internal level (e.g., SUPER_ADMIN)

    @Column(name="created_at") private LocalDateTime createdAt;
    @Column(name="updated_at") private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); updatedAt = LocalDateTime.now(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = LocalDateTime.now(); }

    public Admin() {}
    public Admin(String name, String username, String email, String password) {
        this.name = name; this.username = username; this.email = email; this.password = password;
    }

    // getters/setters...
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getUsername() { return username; } public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; } public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; } public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; } public void setRole(String role) { this.role = role; }
    public String getAdminRole() { return adminRole; } public void setAdminRole(String adminRole) { this.adminRole = adminRole; }
}