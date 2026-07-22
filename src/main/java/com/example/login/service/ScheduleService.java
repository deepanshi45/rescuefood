package com.example.login.service;

import com.example.login.model.FoodStatus;
import com.example.login.repository.FoodRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    private FoodRepository foodRepository;

    /**
     * Runs every 15 minutes to check for and mark expired food items.
     * Addresses Requirement 6: auto-closing donations past expiry.
     */
    @Scheduled(cron = "0 0/15 * * * ?")
    public void autoExpireFoodItems() {
        logger.info("Starting scheduled job: Auto-expiring food items at {}", LocalDateTime.now());

        LocalDate today = LocalDate.now();

        int count = foodRepository.markExpiredFood(
                FoodStatus.EXPIRED,
                FoodStatus.AVAILABLE,
                today
        );

        if (count > 0) {
            logger.warn("Finished scheduled job. Successfully marked {} food item(s) as EXPIRED.", count);
            // Future step: Use NotificationService to alert admin
        } else {
            logger.info("Finished scheduled job. No food items expired today.");
        }
    }
}