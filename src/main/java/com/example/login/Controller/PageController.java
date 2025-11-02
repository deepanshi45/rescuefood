package com.example.login.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Consolidates all static page controllers.
 * This removes ambiguity and cleans up the routing.
 */
@Controller
public class PageController {

    // ----------------- 🌐 COMMON/HOME PAGES -----------------
    @GetMapping("/home/about")
    public String homeAbout() {
        return "home/about";
    }

    @GetMapping("/home/explore")
    public String homeExplore() {
        return "home/explore";
    }

    @GetMapping("/home/feedback")
    public String homeFeedback() {
        return "home/feedback";
    }

    @GetMapping("/home/profile")
    public String homeProfile() {
        return "home/profile";
    }

    @GetMapping("/home/search")
    public String homeSearch() {
        return "home/search";
    }

    @GetMapping("/home/settings")
    public String homeSettings() {
        return "home/settings";
    }

    // ----------------- 👮 ADMIN PAGES -----------------
    @GetMapping("/Templates/admin/dashboard")
    public String adminDashboard() {
        // After login, Spring Security will redirect here.
        // We can also just return the template name.
        return "Templates/admin/admin-dashboard";
    }

    @GetMapping("/Templates/admin/about")
    public String adminAbout() {
        return "Templates/admin/about";
    }

    @GetMapping("/Templates/admin/addfood")
    public String adminAddFood() {
        return "Templates/admin/addfood";
    }

    @GetMapping("/Templates/admin/explore")
    public String adminExplore() {
        return "Templates/admin/explore";
    }

    @GetMapping("/Templates/admin/history")
    public String adminHistory() {
        return "Templates/admin/history";
    }

    @GetMapping("/Templates/admin/notification")
    public String adminNotification() {
        return "Templates/admin/notification";
    }

    @GetMapping("/Templates/admin/settings")
    public String adminSettings() {
        return "Templates/admin/settings";
    }


    // ----------------- 👤 USER PAGES -----------------
    @GetMapping("/user/dashboard")
    public String userDashboard() {
        return "user/user-dashboard";
    }

    @GetMapping("/user/about")
    public String userAbout() {
        return "user/about";
    }

    // Note: /user/explore is handled by FoodController to load data
    // If you have a static 'explore' page, you can add it here.
    // @GetMapping("/user/explore")
    // public String userExplore() {
    //     return "user/explore";
    // }

    @GetMapping("/user/profile")
    public String userProfile() {
        return "user/profile";
    }

    @GetMapping("/user/settings")
    public String userSettings() {
        return "user/settings";
    }
}