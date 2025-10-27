package com.example.login.service;



import com.example.login.dto.UserDTO;
import com.example.login.dto.AdminDTO;
import com.example.login.model.User;
import com.example.login.model.Admin;
import com.example.login.repository.UserRepository;
import com.example.login.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // User signup
    public User signupUser (UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("User  already exists with email: " + userDTO.getEmail());
        }
        User user = new User();
        user.setId(Long.valueOf(userDTO.getName()));
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole("USER");
        return userRepository.save(user);
    }

    // User login (returns user if credentials match)
    public Optional<User> loginUser (String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    // Admin signup
    public Admin signupAdmin(AdminDTO adminDTO) {
        if (adminRepository.findByEmail(adminDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Admin already exists with email: " + adminDTO.getEmail());
        }
        Admin admin = new Admin();
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        admin.setRole("ADMIN");
        return adminRepository.save(admin);
    }

    // Admin login
    public Optional<Admin> loginAdmin(String email, String password) {
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent() && passwordEncoder.matches(password, admin.get().getPassword())) {
            return admin;
        }
        return Optional.empty();
    }
}

