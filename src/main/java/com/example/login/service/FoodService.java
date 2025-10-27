package com.example.login.service;




import com.example.login.model.FoodStatus;
import com.example.login.model.Fooditem;
import com.example.login.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FoodService {

    @Autowired
    private FoodRepository foodRepository;

    // Post a new food item
    public Fooditem postFood(Fooditem foodItem) {
        // Validate: ensure expiry date is in the future
        if (foodItem.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Food item expiry date must be in the future");
        }
        foodItem.setStatus(FoodStatus.AVAILABLE); // Default status
        return foodRepository.save(foodItem);
    }

    // View all food items (explore/search)
    public List<Fooditem> getAllFoodItems(String location, String keyword) {
        if (location != null && !location.isEmpty()) {
            return foodRepository.findByLocationContainingIgnoreCase(location);
        }
        if (keyword != null && !keyword.isEmpty()) {
            return foodRepository.findByDescriptionContainingIgnoreCase(keyword);
        }
        return foodRepository.findAll();
    }

    // Search food by criteria
    public List<Fooditem> searchFood(String query) {
        return foodRepository.findByDescriptionContainingIgnoreCaseOrLocationContainingIgnoreCase(query, query);
    }

    // Claim or update food status (e.g., mark as claimed)
    public Fooditem claimFood(Long foodId, Long userId) {
        Optional<Fooditem> food = foodRepository.findById(foodId);
        if (food.isPresent()) {
            Fooditem item = food.get();
            if (item.getStatus() == FoodStatus.AVAILABLE) {
                item.setStatus(FoodStatus.CLAIMED);
                item.setClaimedBy(userId); // Assuming claimedBy field exists
                return foodRepository.save(item);
            } else {
                throw new RuntimeException("Food item is no longer available");
            }
        }
        throw new RuntimeException("Food item not found with ID: " + foodId);
    }

    // Get food by ID
    public Optional<Fooditem> getFoodById(Long id) {
        return foodRepository.findById(id);
    }
}

