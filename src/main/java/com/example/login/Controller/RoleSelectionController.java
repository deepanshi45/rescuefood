package com.example.login.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class  RoleSelectionController {



        @GetMapping("/role-select")
        public String roleSelectPage() {
            return "role-select";
        }

        @GetMapping("/role-select/admin")
        public String adminLoginRedirect() {
            return "admin/admin-login";
        }

        @GetMapping("/role-select/user")
        public String userLoginRedirect() {
            return "user/user-login";
        }
    }
