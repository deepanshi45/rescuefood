////package com.example.login.Controller;
////
////import com.example.login.dto.UserDTO;
////import com.example.login.service.AuthService;
////import jakarta.validation.Valid;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Controller;
////import org.springframework.ui.Model;
////import org.springframework.validation.BindingResult;
////import org.springframework.web.bind.annotation.GetMapping;
////import org.springframework.web.bind.annotation.ModelAttribute;
////import org.springframework.web.bind.annotation.PostMapping;
////import org.springframework.web.servlet.mvc.support.RedirectAttributes;
////
////@Controller
////// NOTE: We keep this controller without a class-level @RequestMapping
////// so it can host the public entry points.
////public class UserAuthController {
////
////    @Autowired
////    private AuthService authService;
////
////    // --- 🌐 PUBLIC ENTRY POINTS (Keep these here, remove PublicController) ---
////
////    @GetMapping("/")
////    public String landingPageRedirect() {
////        return "redirect:/role-select";
////    }
////
////    @GetMapping("/role-select")
////    public String roleSelectPage() {
////        return "role-select"; // Only one mapping for this URL now
////    }
////
////    // --- USER AUTHENTICATION & REGISTRATION ---
////    @GetMapping("/user/login")
////    public String userLoginPage() {
////        return "user/user-login";
////    }
////
////    @GetMapping("/user/register")
////    public String userRegisterPage(Model model) {
////        model.addAttribute("userDTO", new UserDTO());
////        return "user/user-register";
////    }
////
////    // ... (Rest of UserAuthController methods) ...
////    // ... (The /user/register POST method is fine) ...
////}
//
package com.example.login.Controller;

import com.example.login.dto.UserDTO;
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
public class UserAuthController {

    @Autowired
    private AuthService authService;



    @GetMapping("/")
    public String landingPageRedirect() {
        return "redirect:/role-select";
    }

    @GetMapping("/role-select")
    public String roleSelectPage() {
        return "role-select";
    }

    // --- 👤 USER AUTHENTICATION & REGISTRATION ---

    @GetMapping("/user/login")
    public String userLoginPage() {
        return "user/user-login";
    }

    @GetMapping("/user/register")
    public String userRegisterPage(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "user/user-register";
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


