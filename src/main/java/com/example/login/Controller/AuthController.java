package com.example.login.Controller;



import com.example.login.dto.UserDTO;
import com.example.login.dto.AdminDTO;
import com.example.login.model.User;
import com.example.login.model.Admin;
import com.example.login.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    // --- ROLE SELECT (entry point) ---
    @GetMapping("/")
    public String roleSelect() {
        return "role-select"; // role-select.html
    }

    // --- ADMIN AUTH PAGES ---
    @GetMapping("/admin/login")
    public String adminLogin() {
        return "admin/admin-login"; // static/admin/admin-login.html
    }



    // --- USER AUTH PAGES ---
    @GetMapping("/user/login")
    public String userLogin() {
        return "user/user-login"; // static/user/user-login.html
    }

    @GetMapping("/user/register")
    public String userRegister() {
        return "user/user-register"; // static/user/user-register.html
    }

    // --- Common login (if role not selected yet) ---
    @GetMapping("/login")
    public String generalLogin() {
        return "login"; // static/login.html
    }
}