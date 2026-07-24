package com.example.login.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FeedbackDTO {

    @NotBlank(message = "Feedback cannot be empty")
    @Size(min = 5, max = 500, message = "Feedback must be between 5 and 500 characters")
    private String message;

    public FeedbackDTO() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
