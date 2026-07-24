package com.example.login.Controller;

import com.example.login.dto.FoodDTO;
import com.example.login.model.Claim;
import com.example.login.model.FoodStatus;
import com.example.login.model.Fooditem;
import com.example.login.service.ClaimService;
import com.example.login.service.DashboardService;
import com.example.login.service.FileStorageException;
import com.example.login.service.FileStorageService;
import com.example.login.service.FoodService;
import com.example.login.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final FoodService foodService;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final ClaimService claimService;
    private final DashboardService dashboardService;

    public AdminController(FoodService foodService, UserService userService,
                           FileStorageService fileStorageService, ClaimService claimService,
                           DashboardService dashboardService) {
        this.foodService = foodService;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
        this.claimService = claimService;
        this.dashboardService = dashboardService;
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
    public String adminDashboard(Model model) {
        model.addAttribute("stats", dashboardService.getStats());
        return "admin/admin-dashboard";
    }

    @GetMapping("/explore")
    public String reviewFoodListings(Model model) {
        List<Fooditem> foods = foodService.getAllFoodItems(null, null);
        model.addAttribute("foods", foods);

        // For each claimed item, resolve its pending Claim so the template can
        // show who claimed it and the claim's current status alongside Accept/Decline.
        Map<Long, Claim> pendingClaims = new HashMap<>();
        for (Fooditem food : foods) {
            if (food.getStatus() == FoodStatus.CLAIMED) {
                claimService.getPendingClaimForFood(food.getId())
                        .ifPresent(claim -> pendingClaims.put(food.getId(), claim));
            }
        }
        model.addAttribute("pendingClaims", pendingClaims);

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
                              @RequestParam(value = "image", required = false) MultipartFile imageFile,
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

            // Image upload is optional; only store it if the admin actually selected a file
            if (imageFile != null && !imageFile.isEmpty()) {
                String storedFilename = fileStorageService.store(imageFile);
                foodDTO.setImageUrl("/images/" + storedFilename);
            }

            foodDTO.setPostedBy(adminId);
            foodService.postFood(foodDTO);
            redirectAttributes.addFlashAttribute("success", "✅ Food item posted successfully!");
            return "redirect:/admin/explore";

        } catch (FileStorageException e) {
            model.addAttribute("error", "⚠️ Image upload failed: " + e.getMessage());
            return "admin/addfood";
        } catch (RuntimeException e) {
            model.addAttribute("error", "⚠️ Failed to save food item: " + e.getMessage());
            return "admin/addfood";
        }
    }

    // Thrown by Spring during multipart parsing (before the method body runs)
    // when the uploaded file exceeds spring.servlet.multipart.max-file-size.
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceeded(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "⚠️ Image is too large. Maximum allowed size is 5MB.");
        return "redirect:/admin/addfood";
    }

    // --- ADMIN DECISION ON A CLAIM (Accept / Decline) ---
    @PostMapping("/food/status/{foodId}")
    public String updateFoodStatus(@PathVariable Long foodId,
                                   @RequestParam("action") String action,
                                   RedirectAttributes redirectAttributes) {
        try {
            Claim pendingClaim = claimService.getPendingClaimForFood(foodId)
                    .orElseThrow(() -> new RuntimeException("No pending claim found for this item."));

            if (action.equalsIgnoreCase("ACCEPT")) {
                claimService.decideClaim(pendingClaim.getId(), true);
                redirectAttributes.addFlashAttribute("success", "✅ Claim accepted and donation finalized.");

            } else if (action.equalsIgnoreCase("DECLINE")) {
                claimService.decideClaim(pendingClaim.getId(), false);
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