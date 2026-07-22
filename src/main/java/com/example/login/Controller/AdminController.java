package com.example.login.Controller;

import com.example.login.dto.FoodDTO;
import com.example.login.model.FoodStatus;
import com.example.login.service.FoodService;
import com.example.login.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final FoodService foodService;
    private final UserService userService;

    public AdminController(FoodService foodService, UserService userService) {
        this.foodService = foodService;
        this.userService = userService;
    }

    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            return userService.findIdByEmail(email);
        }
        return null;
    }

    @GetMapping({"/homepage", "/dashboard"})
    public String adminDashboard() {
        return "admin/admin-dashboard";
    }

    @GetMapping("/explore")
    public String reviewFoodListings(Model model) {
        model.addAttribute("foods", foodService.getAllFoodItems(null, null));
        return "admin/explore";
    }

    @GetMapping("/history")
    public String viewHistory(Model model) {
        return "admin/history";
    }

    @GetMapping("/notifications")
    public String viewNotifications() {
        return "admin/notification";
    }

    @GetMapping("/reports")
    public String viewReports() {
        return "admin/view-reports";
    }

    @GetMapping("/settings")
    public String adminSettings(Model model) {
        return "admin/settings";
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        return "admin/manage-users";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "admin/about";
    }

    @GetMapping("/addfood")
    public String postFoodPage(Model model) {
        if (!model.containsAttribute("foodDTO")) {
            model.addAttribute("foodDTO", new FoodDTO());
        }
        return "admin/addfood";
    }

    @PostMapping("/addfood")
    public String addFoodItem(@Valid @ModelAttribute("foodDTO") FoodDTO foodDTO,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("error", "Please correct the highlighted errors.");
            return "admin/addfood";
        }

        try {
            Long adminId = getCurrentUserId();
            if (adminId == null) {
                model.addAttribute("error", "User session expired or not authenticated.");
                return "admin/addfood";
            }

            foodDTO.setPostedBy(adminId);
            foodService.postFood(foodDTO);
            redirectAttributes.addFlashAttribute("success", "✅ Food item posted successfully!");
            return "redirect:/admin/explore";

        } catch (RuntimeException e) {
            model.addAttribute("error", "⚠️ Failed to save food item: " + e.getMessage());
            return "admin/addfood";
        }
    }

    // --- UPDATE FOOD STATUS (Accept / Decline) ---
    @PostMapping("/food/status/{foodId}")
    public String updateFoodStatus(@PathVariable Long foodId,
                                   @RequestParam("action") String action,
                                   RedirectAttributes redirectAttributes) {
        try {
            if (action.equalsIgnoreCase("ACCEPT")) {
                foodService.updateStatus(foodId, FoodStatus.DONATED);
                redirectAttributes.addFlashAttribute("success", "✅ Claim accepted and donation finalized.");

            } else if (action.equalsIgnoreCase("DECLINE")) {
                foodService.updateStatus(foodId, FoodStatus.AVAILABLE);
                redirectAttributes.addFlashAttribute("success", "❌ Claim declined. Item relisted successfully.");

            } else {
                throw new RuntimeException("Invalid action specified.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to process action: " + e.getMessage());
        }

        return "redirect:/admin/explore";
    }
}
