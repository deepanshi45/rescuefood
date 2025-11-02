package com.example.login.service;

import com.example.login.model.Feedback;
import com.example.login.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    // Submit feedback
    public Feedback submitFeedback(Feedback feedback) {
        if (feedback.getMessage() == null || feedback.getMessage().trim().isEmpty()) {
            throw new RuntimeException("Feedback message cannot be empty");
        }
        feedback.setStatus("PENDING");
        return feedbackRepository.save(feedback);
    }

    // Get all feedback (for admin review)
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAllByOrderByCreatedAtDesc();
    }

    // Get feedback by user
    public List<Feedback> getUserFeedback(Long userId) {
        // This was the incomplete method
        return feedbackRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Update feedback status
    public Feedback updateStatus(Long feedbackId, String status) {
        Optional<Feedback> feedback = feedbackRepository.findById(feedbackId);
        if (feedback.isPresent()) {
            Feedback fb = feedback.get();
            fb.setStatus(status); // e.g., "RESOLVED"
            return feedbackRepository.save(fb);
        }
        throw new RuntimeException("Feedback not found with ID: " + feedbackId);
    }

    // Delete feedback
    public void deleteFeedback(Long feedbackId, Long userId) {
        Optional<Feedback> feedback = feedbackRepository.findByIdAndUserId(feedbackId, userId);
        if (feedback.isPresent()) {
            feedbackRepository.delete(feedback.get());
        } else {
            throw new RuntimeException("Feedback not found or access denied");
        }
    }
}
