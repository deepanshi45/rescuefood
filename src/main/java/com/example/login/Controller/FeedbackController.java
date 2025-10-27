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

    // Submit feedback form
    @GetMapping("/feedback")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "user/feedback"; // Renders user/feedback.html
    }

    @PostMapping("/feedback")
    public String submitFeedback(@ModelAttribute Feedback feedback, Model model) {
        Long userId = 1L; // From session; set in entity


        feedback.setUserId (userId);
        try {
            Feedback saved = feedbackService.submitFeedback(feedback);
            model.addAttribute("message", "Feedback submitted successfully");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "user/feedback";
    }

    // View all feedback (admin only)
    @GetMapping("/admin/feedback")
    public String adminFeedback(Model model) {
        List<Feedback> feedbacks = feedbackService.getAllFeedback();
        model.addAttribute("feedbacks", feedbacks);
        return "admin/feedback-list"; // Assume dedicated template
    }

    // Update feedback status (admin)
    @PostMapping("/admin/feedback/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam String status) {
        feedbackService.updateStatus(id, status);
        return "redirect:/admin/feedback";
    }

    // User view their feedback
    @GetMapping("/user/my-feedback")
    public String userFeedback(Model model) {
        Long userId = 1L; // From session
        List<Feedback> feedbacks = feedbackService.getUserFeedback(userId);
        model.addAttribute("feedbacks", feedbacks);
        return "user/my-feedback"; // Or integrate into profile
    }

    // Delete feedback
    @PostMapping("/feedback/{id}/delete")
    public String deleteFeedback(@PathVariable Long id) {
        Long userId = 1L; // From session
        try {
            feedbackService.deleteFeedback(id, userId);
        } catch (RuntimeException e) {
            // Handle error
        }
        return "redirect:/user/my-feedback";
    }
}
