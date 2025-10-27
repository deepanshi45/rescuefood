package com.example.login.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

    @Controller
    public class WebController {




            // ----------------- 🌐 COMMON PAGES -----------------
            @GetMapping("/")
            public String showHomePage() {
                return "home/about"; // default page from home section
            }

            @GetMapping("/role-select")
            public String showRoleSelect() {
                return "role-select";
            }

            @GetMapping("/login")
            public String showLoginPage() {
                return "login";
            }

            // ----------------- 👮 ADMIN PAGES -----------------
            @GetMapping("/admin/dashboard")
            public String adminDashboard() {
                return "admin/admin-dashboard";
            }

            @GetMapping("/admin/login")
            public String adminLogin() {
                return "admin/admin-login";
            }

            @GetMapping("/admin/register")
            public String adminRegister() {
                return "admin/admin-register";
            }

            @GetMapping("/admin/about")
            public String adminAbout() {
                return "admin/about";
            }

            @GetMapping("/admin/addfood")
            public String adminAddFood() {
                return "admin/addfood";
            }

            @GetMapping("/admin/explore")
            public String adminExplore() {
                return "admin/explore";
            }

            @GetMapping("/admin/history")
            public String adminHistory() {
                return "admin/history";
            }

            @GetMapping("/admin/notification")
            public String adminNotification() {
                return "admin/notification";
            }

            @GetMapping("/admin/settings")
            public String adminSettings() {
                return "admin/settings";
            }


            // ----------------- 👤 USER PAGES -----------------
            @GetMapping("/user/dashboard")
            public String userDashboard() {
                return "user/user-dashboard";
            }

            @GetMapping("/user/login")
            public String userLogin() {
                return "user/user-login";
            }

            @GetMapping("/user/register")
            public String userRegister() {
                return "user/user-register";
            }

            @GetMapping("/user/about")
            public String userAbout() {
                return "user/about";
            }

            @GetMapping("/user/explore")
            public String userExplore() {
                return "user/explore";
            }

            @GetMapping("/user/profile")
            public String userProfile() {
                return "user/profile";
            }

            @GetMapping("/user/settings")
            public String userSettings() {
                return "user/settings";
            }
        // ----------------- 🏠 HOME PAGES -----------------
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

        }
