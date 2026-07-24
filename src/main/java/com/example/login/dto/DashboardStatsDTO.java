package com.example.login.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Read-only summary of platform statistics shown on the Admin Dashboard.
 * Populated entirely from repository count queries — no business logic.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {

    private long totalUsers;
    private long totalAdmins;

    private long totalFoodItems;
    private long availableFood;
    private long claimedFood;
    private long expiredFood;

    private long pendingClaims;
    private long approvedClaims;
    private long rejectedClaims;
}
