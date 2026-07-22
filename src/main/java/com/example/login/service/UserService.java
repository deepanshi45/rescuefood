package com.example.login.service;

import com.example.login.dto.UserDTO;
import com.example.login.model.User;
import com.example.login.model.Admin;
import com.example.login.repository.UserRepository;
import com.example.login.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    // ==========================================================
    // 🔹 Find User/Admin ID by Email — used for Notifications, Feedback, etc.
    // ==========================================================
    public Long findIdByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) return userOpt.get().getId();

        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) return adminOpt.get().getId();

        throw new RuntimeException("Authenticated principal not found in User or Admin database.");
    }

    // ==========================================================
    // 🔹 Fetch Current Authenticated User (Utility)
    // ==========================================================
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("No user found for authenticated email: " + email));
        }
        throw new RuntimeException("User is not authenticated.");
    }

    // ==========================================================
    // 🔹 User Dashboard / Profile Retrieval
    // ==========================================================
    public User getUserDashboard(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    // ==========================================================
    // 🔹 Update User Profile (DTO-driven, safe update)
    // ==========================================================
    public User updateProfile(Long userId, UserDTO updatedUserDTO) {
        return userRepository.findById(userId)
                .map(user -> {
                    if (updatedUserDTO.getName() != null) user.setName(updatedUserDTO.getName());
                    if (updatedUserDTO.getUsername() != null) user.setUsername(updatedUserDTO.getUsername());
                    if (updatedUserDTO.getPhone() != null) user.setPhone(updatedUserDTO.getPhone());
                    if (updatedUserDTO.getAddress() != null) user.setAddress(updatedUserDTO.getAddress());
                    // Optional: update email only if allowed
                    if (updatedUserDTO.getEmail() != null) user.setEmail(updatedUserDTO.getEmail());

                    // Track update timestamp
                    user.setUpdatedAt(LocalDateTime.now());

                    return userRepository.save(user);
                })
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

    // ==========================================================
    // 🔹 Legacy: Save User Profile (from full entity or DTO)
    // ==========================================================
    public void saveUserProfile(UserDTO userDTO) {
        User currentUser = getCurrentUser();
        currentUser.setName(userDTO.getName());
        currentUser.setEmail(userDTO.getEmail());
        currentUser.setPhone(userDTO.getPhone());
        userRepository.save(currentUser);
    }

    // ==========================================================
    // 🔹 Admin / Explorer Utilities
    // ==========================================================
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> exploreUsers(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findByNameContainingIgnoreCase(keyword);
        }
        return userRepository.findAll();
    }
}
