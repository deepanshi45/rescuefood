package com.example.login.repository;

import com.example.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // 🆕 Added List import
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // ✅ 1. Used for Authentication/Login and checking existence (used in UserService.findIdByEmail)
    Optional<User> findByEmail(String email);

    // ✅ 2. Used for the search/explore functionality in UserService
    List<User> findByNameContainingIgnoreCase(String keyword);

    // Optional: If you allow login by username too
    // Optional<User> findByUsername(String username);
}