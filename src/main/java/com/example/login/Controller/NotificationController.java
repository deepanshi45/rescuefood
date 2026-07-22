package com.example.login.Controller;

import com.example.login.model.Notification;
import com.example.login.service.NotificationService;
import com.example.login.service.UserService; // Required to fetch the authenticated user's ID
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @Autowired
    private UserService userService;

    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            String email = ((UserDetails) auth.getPrincipal()).getUsername(); // This is the user's email
            // Use the service method created earlier to look up the ID
            return userService.findIdByEmail(email);
        }
        throw new SecurityException("User not authenticated or ID could not be determined.");
    }

    // ============================
//    / 1. 📢 Admin: Send Notification (Endpoint for Admin actions or background jobs)
    // Map: POST /notifications/send
    // Requires: ROLE_ADMIN (access handled by SecurityConfig)
    // ============================
    @PostMapping("/send")
    public String sendNotification(@ModelAttribute Notification notification,
                                   RedirectAttributes redirectAttributes) {
        try {
            notificationService.sendNotification(notification);
            redirectAttributes.addFlashAttribute("message", "Notification sent successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/notifications";
    }

    // ============================
    // 2. 👮 Admin: View All Notifications (List view)
    // Map: GET /notifications/admin
    // Requires: ROLE_ADMIN
    // ============================
    @GetMapping("/admin")
    public String adminNotifications(Model model) {
        // Admins might view system alerts or unread notifications for all users.
        // For simplicity, we just return the template name, assuming Service handles data loading.
        return "admin/notification"; // templates/admin/notification.html
    }

    // ============================
    // 3. 👤 User: View Notifications (List view)
    // Map: GET /notifications/user
    // Requires: ROLE_USER
    // ============================
    @GetMapping("/user")
    public String userNotifications(Model model) {
        try {
            Long userId = getCurrentUserId();
            List<Notification> notifications = notificationService.getUserNotifications(userId);
            long unreadCount = notificationService.getUnreadCount(userId);
            model.addAttribute("notifications", notifications);
            model.addAttribute("unreadCount", unreadCount);
            return "user/notification"; // templates/user/notification.html
        } catch (SecurityException e) {
            // If user ID fails lookup, security should have already caught it, but good practice.
            return "fragments/access-denied";
        }
    }

    // ============================
    // 4. ✅ Mark Notification as Read
    // Map: POST /notifications/{id}/read
    // Requires: ROLE_USER (or whoever owns the notification)
    // ============================
    @PostMapping("/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "redirect:/notifications/user";
    }

    // ============================



    // ============================
    @PostMapping("/{id}/delete")
    public String deleteNotification(@PathVariable Long id,
                                     RedirectAttributes redirectAttributes) {
        try {
            Long userId = getCurrentUserId();
            notificationService.deleteNotification(id, userId);
            redirectAttributes.addFlashAttribute("message", "Notification deleted.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete notification: " + e.getMessage());
        }
        return "redirect:/notifications/user";
    }
}