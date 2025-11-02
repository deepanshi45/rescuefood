package com.example.login.Controller;

import com.example.login.dto.FoodDTO;
import com.example.login.model.Fooditem;
import com.example.login.service.FoodService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class FoodController {

    @Autowired
    private FoodService foodService;

    // Post food (user or admin)
    @GetMapping("/food/post")
    public String showPostFoodForm(Model model) {
        model.addAttribute("foodDTO", new FoodDTO());
        return "fragments/post-food-form"; // Or a dedicated template
    }

    @PostMapping("/food/post")
    public String postFood(@Valid @ModelAttribute FoodDTO foodDTO, Model model, RedirectAttributes redirectAttributes) {
        try {
            // Service layer now handles DTO conversion
            foodService.postFood(foodDTO);
            redirectAttributes.addFlashAttribute("message", "Food posted successfully");
            return "redirect:/user/explore";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("foodDTO", foodDTO); // Send DTO back to form
            return "fragments/post-food-form";
        }
    }

    // View all food (explore) - This path now matches your HTML
    @GetMapping("/user/explore")
    public String exploreFood(@RequestParam(required = false) String location,
                              @RequestParam(required = false) String keyword, Model model) {
        List<Fooditem> foods = foodService.getAllFoodItems(location, keyword);
        model.addAttribute("foods", foods);
        return "user/explore"; // Renders templates/user/explore.html
    }

    // Search food
    @GetMapping("/food/search")
    public String searchFood(@RequestParam String query, Model model) {
        List<Fooditem> results = foodService.searchFood(query);
        model.addAttribute("results", results);
        return "user/search"; // Renders templates/user/search.html
    }

    // View specific food
    @GetMapping("/food/{id}")
    public String viewFood(@PathVariable Long id, Model model) {
        Optional<Fooditem> food = foodService.getFoodById(id);
        if (food.isPresent()) {
            model.addAttribute("food", food.get());
            return "food/view"; // Assume a dedicated template food/view.html
        } else {
            model.addAttribute("error", "Food not found");
            return "error/404"; // Assume an error template
        }
    }

    // Claim food
    @PostMapping("/food/{id}/claim")
    public String claimFood(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        // FIXME: Replace 1L with the ID of the authenticated user
        Long userId = 1L;
        try {
            foodService.claimFood(id, userId);
            redirectAttributes.addFlashAttribute("message", "Food claimed successfully");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/food/" + id;
    }
}