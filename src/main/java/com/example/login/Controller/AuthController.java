package com.example.login.Controller;

import com.example.login.dto.UserDTO;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    // --- ROLE SELECT (Entry Point) ---
    @GetMapping("/")
    public String roleSelect() {
        return "role-select"; // Renders templates/role-select.html
    }

    // --- COMMON LOGIN (If needed) ---
    @GetMapping("/login")
    public String generalLogin() {
        return "login"; // Renders templates/login.html
    }

    // --- ADMIN AUTH ---
    @GetMapping("/Templates/admin/login")
    public String adminLoginPage() {
        return "Templates/admin/admin-login"; // Renders templates/admin/admin-login.html
    }

//    @GetMapping("/admin/register")
//    public String adminRegisterPage(Model model) {
//        model.addAttribute("adminDTO", new AdminDTO());
//        return "admin/admin-register"; // Renders templates/admin/admin-register.html
//    }
// In AuthController.java
@GetMapping("/Templates/admin/register-page") // Changed from "/admin/register"
public String adminRegisterPage(Model model) {
    return "Templates/admin/register-page";
}

    @PostMapping("/Templates/admin/register")
    public String registerAdmin(@Valid @ModelAttribute("adminDTO") AdminDTO adminDTO,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "Templates/admin/admin-register";
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

    // --- USER AUTH ---
    @GetMapping("/user/login")
    public String userLoginPage() {
        return "user/user-login"; // Renders templates/user/user-login.html
    }

    @GetMapping("/user/register")
    public String userRegisterPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user/user-register"; // Renders templates/user/user-register.html
    }

    @PostMapping("/user/register")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/user-register";
        }
        try {
            authService.signupUser(userDTO);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/user/login";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/user/register";
        }
    }
}