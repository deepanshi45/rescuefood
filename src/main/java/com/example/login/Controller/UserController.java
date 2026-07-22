package com.example.login.Controller;

import com.example.login.dto.UserDTO;
import org.springframework.http.ResponseEntity;
import com.example.login.model.User;
import com.example.login.service.UserService;
import com.example.login.service.FoodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

    private final FoodService foodService;
    private final UserService userService;

    public UserController(FoodService foodService, UserService userService) {
        this.foodService = foodService;
        this.userService = userService;
    }

    // --- Homepage ---
    @GetMapping({"/homepage", "/"})
    public String userHomepage() {
        return "user/homepage";
    }

    // --- Explore page ---
    @GetMapping("/explore")
    public String explore(Model model) {
        model.addAttribute("foods", foodService.getAvailable());
        return "user/explore";
    }

    // --- Profile page ---
    @GetMapping("/profile")
    public String userProfile(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("user", currentUser);
        return "user/profile";
    }

    // --- Settings page ---
    @GetMapping("/settings")
    public String userSettings(Model model) {
        User currentUser = userService.getCurrentUser();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(currentUser.getId());
        userDTO.setName(currentUser.getName());
        userDTO.setUsername(currentUser.getUsername());
        userDTO.setEmail(currentUser.getEmail());
        userDTO.setPhone(currentUser.getPhone());
        userDTO.setAddress(currentUser.getAddress());
        model.addAttribute("user", userDTO);
        return "user/settings";
    }

    // --- Update user info ---
    @PostMapping("/settings/update-info")
    public String updateUserInfo(@ModelAttribute("user") UserDTO userDTO,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "user/settings";
        }

        try {
            userService.updateProfile(userDTO.getId(), userDTO);
            redirectAttributes.addFlashAttribute("success", "Profile details updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update profile: " + e.getMessage());
        }

        return "redirect:/user/settings";
    }

    // --- Feedback page ---
    @GetMapping("/feedback")
    public String userFeedback() {
        return "user/feedback";
    }

    // --- About page ---
    @GetMapping("/about")
    public String userAbout() {
        return "user/about";
    }

    // --- Cart page ---
    @GetMapping("/cart")
    public String userCart() {
        return "user/cart";
    }



    // --- Order page (user's claimed items) ---
    @GetMapping("/order")
    public String userOrder(Model model) {
        Long userId = getCurrentUserId();
        if (userId != null) {
            model.addAttribute("orders", foodService.getClaimsByUserId(userId));
        } else {
            model.addAttribute("orders", java.util.Collections.emptyList());
        }
        return "user/order";
    }

    // --- Files page ---
    @GetMapping("/files")
    public String userFiles() {
        return "user/files";
    }

    // --- Notifications page ---
    @GetMapping("/notifications")
    public String userNotifications() {
        return "user/notifications";
    }

    // --- CLAIM FOOD ITEM ---
    @GetMapping("/claim/{foodId}")
    public String claimFoodItem(@PathVariable Long foodId, RedirectAttributes redirectAttributes) {
        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                redirectAttributes.addFlashAttribute("error", "Your session expired. Please log in.");
                return "redirect:/user/login";
            }

            foodService.claimFood(foodId, userId);

            redirectAttributes.addFlashAttribute("success", "✅ Food claimed! Awaiting Admin approval.");
            return "redirect:/user/order";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Claim failed: " + e.getMessage());
            return "redirect:/user/explore";
        }
    }

    // --- Helper: get current logged-in user ID ---
    private Long getCurrentUserId() {
        User currentUser = userService.getCurrentUser();
        return currentUser != null ? currentUser.getId() : null;
    }
}
