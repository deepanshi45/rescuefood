package com.example.login.service;

import com.example.login.dto.FoodDTO;
import com.example.login.model.FoodCategory;
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

    // Post a new food item (Refined to accept DTO)
    public Fooditem postFood(FoodDTO foodDTO) {
        if (foodDTO.getExpiryDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Food item expiry date must be in the future");
        }

        Fooditem foodItem = new Fooditem();
        foodItem.setTitle(foodDTO.getTitle());
        foodItem.setDescription(foodDTO.getDescription());
        foodItem.setLocation(foodDTO.getLocation());

        // Convert BigDecimal to Integer for quantity
        if (foodDTO.getQuantity() != null) {
            foodItem.setQuantity(foodDTO.getQuantity().intValue());
        }

        // Convert String category to Enum
        try {
            foodItem.setCategory(FoodCategory.valueOf(foodDTO.getCategory().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid food category: " + foodDTO.getCategory());
        }

        foodItem.setExpiryDate(foodDTO.getExpiryDate());
        foodItem.setStatus(FoodStatus.AVAILABLE); // Default status

        // FIXME: Set the user/admin who posted this
        // foodItem.setPostedBy(adminRepository.findById(1L).get());

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

    // Claim or update food status
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