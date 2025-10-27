package com.example.login.Controller;




import com.example.login.dto.FoodDTO;
import com.example.login.model.Fooditem;
import com.example.login.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
        return "fragments/post-food-form"; // Or a dedicated template; assume fragment for reuse
    }

    @PostMapping("/food/post")
    public String postFood(@ModelAttribute FoodDTO foodDTO, Model model) {
        try {
            Fooditem foodItem = foodService.postFood(/* Convert DTO to Entity */ new Fooditem(/* fields from DTO */));
            model.addAttribute("message", "Food posted successfully");
            return "redirect:/user/explore";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "fragments/post-food-form";
        }
    }

    // View all food (explore)
    @GetMapping("/food/explore")
    public String exploreFood(@RequestParam(required = false) String location,
                              @RequestParam(required = false) String keyword, Model model) {
        List<Fooditem> foods = foodService.getAllFoodItems(location, keyword);
        model.addAttribute("foods", foods);
        return "user/explore"; // Reuse user/explore.html or dedicated
    }

    // Search food
    @GetMapping("/food/search")
    public String searchFood(@RequestParam String query, Model model) {
        List<Fooditem> results = foodService.searchFood(query);
        model.addAttribute("results", results);
        return "user/search"; // Renders user/search.html
    }

    // View specific food
    @GetMapping("/food/{id}")
    public String viewFood(@PathVariable Long id, Model model) {
        Optional<Fooditem> food = foodService.getFoodById(id);
        if (food.isPresent()) {
            model.addAttribute("food", food.get());
            return "food/view"; // Assume a dedicated template or fragment
        } else {
            model.addAttribute("error", "Food not found");
            return "error/404";
        }
    }

    // Claim food
    @PostMapping("/food/{id}/claim")
    public String claimFood(@PathVariable Long id, Model model) {
        Long userId = 1L; // From session
        try {
            Fooditem food = foodService.claimFood(id, userId);
            model.addAttribute("message", "Food claimed successfully");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/food/" + id;
    }
}
