package com.example.login.Controller;



import com.example.login.model.Admin;
import com.example.login.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;



        @GetMapping("/admin/dashboard")
        public String adminDashboard() {
            return "admin/admin-dashboard";
        }

        @GetMapping("/admin/about")
        public String adminAbout() {
            return "admin/about";
        }

        @GetMapping("/admin/addfood")
        public String addFood() {
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

        @GetMapping("/admin/register")
        public String adminRegister() {
            return "admin/admin-register";
        }

        @GetMapping("/admin/login")
        public String adminLogin() {
            return "admin/admin-login";
        }
    }
