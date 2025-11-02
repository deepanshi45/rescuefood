package com.example.login.Controller;

import com.example.login.model.Notification;
import com.example.login.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Send notification (e.g., from admin or system)
    @PostMapping("/notifications/send")
    public String sendNotification(@ModelAttribute Notification notification, Model model) {
        try {
            notificationService.sendNotification(notification);
            model.addAttribute("message", "Notification sent");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/admin/notification"; // Renders templates/admin/notification.html
    }

    // View user notifications
    @GetMapping("/user/notifications")
    public String userNotifications(Model model) {
        // FIXME: Replace 1L with ID from authenticated user session
        Long userId = 1L;
        List<Notification> notifications = notificationService.getUserNotifications(userId);
        long unreadCount = notificationService.getUnreadCount(userId);
        model.addAttribute("notifications", notifications);
        model.addAttribute("unreadCount", unreadCount);
        return "user/notifications"; // Renders templates/user/notifications.html
    }

    // Mark as read
    @PostMapping("/notifications/{id}/read")
    public String markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return "redirect:/user/notifications";
    }

    // Delete notification
    @PostMapping("/notifications/{id}/delete")
    public String deleteNotification(@PathVariable Long id) {
        // FIXME: Replace 1L with ID from authenticated user session
        Long userId = 1L;
        try {
            notificationService.deleteNotification(id, userId);
        } catch (RuntimeException e) {
            // Handle error (e.g., flash message)
        }
        return "redirect:/user/notifications";
    }
}