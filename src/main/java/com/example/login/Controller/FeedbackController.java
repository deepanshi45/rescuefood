package com.example.login.Controller;

import com.example.login.model.Feedback;
import com.example.login.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    // Show submit feedback form
    @GetMapping("/feedback")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "home/feedback"; // Renders templates/home/feedback.html
    }

    @PostMapping("/feedback")
    public String submitFeedback(@ModelAttribute Feedback feedback, Model model) {
        // FIXME: Replace 1L with ID from authenticated user session
        Long userId = 1L;
        feedback.setUserId(userId);

        try {
            feedbackService.submitFeedback(feedback);
            model.addAttribute("message", "Feedback submitted successfully");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        model.addAttribute("feedback", new Feedback()); // Reset form
        return "home/feedback";
    }

    // View all feedback (admin only)
    @GetMapping("/Templates/admin/feedback")
    public String adminFeedback(Model model) {
        List<Feedback> feedbacks = feedbackService.getAllFeedback();
        model.addAttribute("feedbacks", feedbacks);
        return "Templates/admin/feedback-list"; // Renders templates/admin/feedback-list.html
    }

    // Update feedback status (admin)
    @PostMapping("/Templates/admin/feedback/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        feedbackService.updateStatus(id, status);
        return "redirect:/admin/feedback";
    }

    // User view their feedback
    @GetMapping("/user/my-feedback")
    public String userFeedback(Model model) {
        // FIXME: Replace 1L with ID from authenticated user session
        Long userId = 1L;
        List<Feedback> feedbacks = feedbackService.getUserFeedback(userId);
        model.addAttribute("feedbacks", feedbacks);
        return "user/my-feedback"; // Renders templates/user/my-feedback.html
    }

    // Delete feedback
    @PostMapping("/feedback/{id}/delete")
    public String deleteFeedback(@PathVariable Long id) {
        // FIXME: Replace 1L with ID from authenticated user session
        Long userId = 1L;
        try {
            feedbackService.deleteFeedback(id, userId);
        } catch (RuntimeException e) {
            // Handle error (e.g., flash message)
        }
        return "redirect:/user/my-feedback";
    }
}