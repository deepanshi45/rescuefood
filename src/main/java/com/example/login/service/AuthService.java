package com.example.login.service;

import com.example.login.dto.AdminDTO;
import com.example.login.dto.UserDTO;
import com.example.login.model.Admin;
import com.example.login.model.User;
import com.example.login.repository.AdminRepository;
import com.example.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injected from SecurityConfig

    // ===================== ADMIN AUTH =====================
    public Admin signupAdmin(AdminDTO adminDTO) {
        // 1️⃣ Check for duplicate email
        if (adminRepository.findByEmail(adminDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Admin with this email already exists!");
        }

        // 2️⃣ Map DTO to Entity
        Admin admin = new Admin();
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setUsername(adminDTO.getEmail()); // ✅ Use email as username
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword())); // ✅ Hash password
        admin.setRole("ROLE_ADMIN");
        admin.setAdminRole("DONOR"); // Optional internal role tag

        // 3️⃣ Save to database
        return adminRepository.save(admin);
    }

    // ===================== USER AUTH =====================
    public User signupUser(UserDTO userDTO) {
        // 1️⃣ Check for duplicate email
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User with this email already exists!");
        }

        // 2️⃣ Map DTO to Entity
        User user = new User();
        user.setName(userDTO.getName());
        user.setUsername(userDTO.getUsername()); // Assuming username exists in DTO
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // ✅ Hash password
        user.setRole("ROLE_USER");

        // 3️⃣ Save to database
        return userRepository.save(user);
    }

    // ❌ No manual login methods — Spring Security handles authentication
}
