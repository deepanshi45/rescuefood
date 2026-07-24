package com.example.login.service;

import com.example.login.dto.DashboardStatsDTO;
import com.example.login.model.ClaimStatus;
import com.example.login.model.FoodStatus;
import com.example.login.repository.AdminRepository;
import com.example.login.repository.ClaimRepository;
import com.example.login.repository.FoodRepository;
import com.example.login.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Aggregates read-only platform statistics for the Admin Dashboard.
 * Purely additive: only reads via existing repository count queries,
 * never writes and never touches existing business logic.
 */
@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final FoodRepository foodRepository;
    private final ClaimRepository claimRepository;

    public DashboardService(UserRepository userRepository, AdminRepository adminRepository,
                            FoodRepository foodRepository, ClaimRepository claimRepository) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.foodRepository = foodRepository;
        this.claimRepository = claimRepository;
    }

    public DashboardStatsDTO getStats() {
        DashboardStatsDTO stats = new DashboardStatsDTO();

        stats.setTotalUsers(userRepository.count());
        stats.setTotalAdmins(adminRepository.count());

        stats.setTotalFoodItems(foodRepository.count());
        stats.setAvailableFood(foodRepository.countByStatus(FoodStatus.AVAILABLE));
        stats.setClaimedFood(foodRepository.countByStatus(FoodStatus.CLAIMED));
        stats.setExpiredFood(foodRepository.countByStatus(FoodStatus.EXPIRED));

        stats.setPendingClaims(claimRepository.countByStatus(ClaimStatus.PENDING));
        stats.setApprovedClaims(claimRepository.countByStatus(ClaimStatus.APPROVED));
        stats.setRejectedClaims(claimRepository.countByStatus(ClaimStatus.REJECTED));

        return stats;
    }
}