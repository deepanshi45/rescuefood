package com.example.login.service;//package com.example.login.service;
//

import com.example.login.model.Admin;
import com.example.login.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    // Get admin panel data (e.g., summary stats)
    public Admin getAdminPanel(Long adminId) {
        Optional<Admin> admin = adminRepository.findById(adminId);
        if (admin.isPresent()) {
            // In a real app, add stats like total food items, users, etc.
            return admin.get();
        }
        throw new RuntimeException("Admin not found with ID: " + adminId);
    }

    // Add food item (delegates to FoodService, but admin-specific)
    // Note: Actual food addition is in FoodService; this could approve/flag
    public void addFoodAsAdmin(Long adminId, Long foodId) {
        // Logic to associate food with admin or approve it
        Optional<Admin> admin = adminRepository.findById(adminId);
        if (admin.isEmpty()) {
            throw new RuntimeException("Admin not found with ID: " + adminId);
        }
        // Integrate with FoodService here if needed
    }

    // View history (e.g., all activities)
    public List<Admin> getAdminHistory() {
        // Assuming history is logged in admin entity or separate log
        return adminRepository.findAll(); // Placeholder; use custom query for history
    }

    // Update admin settings
    public Admin updateSettings(Long adminId, Admin updatedAdmin) {
        Optional<Admin> existingAdmin = adminRepository.findById(adminId);
        if (existingAdmin.isPresent()) {
            Admin admin = existingAdmin.get();
            admin.setName(updatedAdmin.getName());
            admin.setEmail(updatedAdmin.getEmail());
            return adminRepository.save(admin);
        }
        throw new RuntimeException("Admin not found with ID: " + adminId);
    }
}

