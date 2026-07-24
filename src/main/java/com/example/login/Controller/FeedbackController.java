package com.example.login.Controller;

import com.example.login.dto.FeedbackDTO;
import com.example.login.model.Feedback;
import com.example.login.model.User;
import com.example.login.service.FeedbackService;
import com.example.login.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final UserService userService;

    public FeedbackController(FeedbackService feedbackService, UserService userService) {
        this.feedbackService = feedbackService;
        this.userService = userService;
    }

    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            String email = userDetails.getUsername();
            return userService.findIdByEmail(email);
        }
        return null;
    }

    // 1. Show the feedback form
    @GetMapping
    public String showFeedbackForm(Model model) {
        if (!model.containsAttribute("feedbackDTO")) {
            model.addAttribute("feedbackDTO", new FeedbackDTO());
        }
        return "user/feedback";
    }

    // 2. Submit Feedback
    @PostMapping
    public String submitFeedback(@Valid @ModelAttribute("feedbackDTO") FeedbackDTO feedbackDTO,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("error", "Please correct the highlighted errors.");
            return "user/feedback";
        }

        try {
            Long userId = getCurrentUserId();
            if (userId == null) {
                redirectAttributes.addFlashAttribute("error", "Your session expired. Please log in again.");
                return "redirect:/user/login";
            }

            Feedback feedback = new Feedback();
            feedback.setUserId(userId);
            feedback.setMessage(feedbackDTO.getMessage());
            feedbackService.submitFeedback(feedback);

            redirectAttributes.addFlashAttribute("success", "✅ Thank you! Your feedback has been submitted.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", "⚠️ Failed to submit feedback: " + e.getMessage());
        }
        return "redirect:/feedback";
    }

    // 3. Admin: view all feedback
    @GetMapping("/admin/view")
    public String adminFeedback(Model model) {
        var feedbacks = feedbackService.getAllFeedback();

        // Feedback only stores a raw userId (no FK/join), so resolve display names
        // in one query rather than looking up each user individually.
        Map<Long, String> userNames = userService.getAllUsers().stream()
                .collect(Collectors.toMap(User::getId, User::getName));

        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("userNames", userNames);
        return "admin/feedback-list"; // templates/admin/feedback-list.html
    }

    // 4. User: view their own feedback history
    @GetMapping("/user/view")
    public String userFeedbackHistory(Model model) {
        Long userId = getCurrentUserId();
        if (userId != null) {
            model.addAttribute("feedbacks", feedbackService.getUserFeedback(userId));
        } else {
            model.addAttribute("feedbacks", Collections.emptyList());
        }
        return "user/my-feedback"; // templates/user/my-feedback.html
    }
}