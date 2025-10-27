package com.example.login.Controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


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

