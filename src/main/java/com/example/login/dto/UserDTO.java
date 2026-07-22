package com.example.login.dto;

public class UserDTO {

    private Long id;          // ✅ For identifying the user in updates
    private String name;
    private String username;  // Optional if you use email as username
    private String email;     // Read‑only in form
    private String phone;     // ✅ For profile updates
    private String address;   // ✅ New field
    private String password;  // For registration/change‑password forms

    // =====================================================
    // 🔹 Constructors
    // =====================================================
    public UserDTO() {}

    public UserDTO(Long id, String name, String username, String email,
                   String phone, String address, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    // =====================================================
    // 🔹 Getters & Setters
    // =====================================================
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

