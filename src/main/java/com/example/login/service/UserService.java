package com.example.login.service;

import com.example.login.model.User;
import com.example.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Get user dashboard (e.g., user's food claims or profile summary)
    public User getUseDashboard(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            // In a real app, enrich with joined food items or notifications
            return user.get();
        }
        throw new RuntimeException("User  not found with ID: " + userId);
    }

    // Update user profile
    public User updateProfile(Long userId, User updatedUser ) {
        Optional<User> existingUser  = userRepository.findById(userId);
        if (existingUser .isPresent()) {
            User user = existingUser .get();
            user.setName(updatedUser .getName());
            user.setEmail(updatedUser .getEmail());
            // Password update would require hashing; omitted for brevity
            user.setPhone(updatedUser .getPhone()); // Assuming phone field exists
            return userRepository.save(user);
        }
        throw new RuntimeException("User  not found with ID: " + userId);
    }

    // Get all users (for admin view, but accessible via user service for now)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Explore users or search (basic implementation)
    public List<User> exploreUsers(String keyword) {
        if (keyword != null && !keyword.isEmpty()) {
            return userRepository.findByNameContainingIgnoreCase(keyword);
        }
        return userRepository.findAll();
    }
}


