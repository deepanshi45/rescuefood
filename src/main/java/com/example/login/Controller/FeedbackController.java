package com.example.login.Controller;

import com.example.login.model.Feedback;
import com.example.login.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;




    @GetMapping
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        // Map to a common form page or the user's feedback page
        return "user/feedback";
    }

    // 2. 📤 Submit Feedback
    @PostMapping
    public String submitFeedback(@ModelAttribute Feedback feedback,
                                 RedirectAttributes redirectAttributes) {
        try {
            // Long userId = service.getCurrentUserId(); // Fetch ID here
            // feedback.setUserId(userId);
            // feedbackService.submitFeedback(feedback);
            redirectAttributes.addFlashAttribute("success", "Feedback submitted successfully.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/feedback";
    }


    @GetMapping("/admin/view")
    public String adminFeedback(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedback());
        return "admin/feedback-list"; // Assumes templates/admin/feedback-list.html
    }


    @GetMapping("/user/view")
    public String userFeedbackHistory(Model model) {
        // model.addAttribute("feedbacks", feedbackService.getUserFeedback(currentUserId));
        return "user/my-feedback"; // Assumes templates/user/my-feedback.html
    }
}