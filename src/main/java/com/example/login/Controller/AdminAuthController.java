package com.example.login.Controller;

import com.example.login.dto.AdminDTO;
import com.example.login.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminAuthController {

    @Autowired
    private AuthService authService;

    // --- Admin login page ---
    @GetMapping("/login")
    public String adminLoginPage() {
        return "admin/admin-login";
    }

    // --- Admin registration page ---
    @GetMapping("/register")
    public String adminRegisterPage(Model model) {
        model.addAttribute("adminDTO", new AdminDTO()); // Consistent DTO naming
        return "admin/admin-register";
    }

    // --- Handle admin registration form submission ---
    @PostMapping("/register")
    public String registerAdmin(@Valid @ModelAttribute("adminDTO") AdminDTO adminDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/admin-register";
        }
        try {
            authService.signupAdmin(adminDTO);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/admin/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/register";
        }
    }
}
