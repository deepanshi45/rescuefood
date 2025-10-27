package com.example.login.service;

import com.example.login.model.Notification;
import com.example.login.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    // 1️⃣ Send notification to user (default unread)
    public Notification sendNotification(Notification notification) {
        notification.setRead(false);
        return notificationRepository.save(notification);
    }

    // 2️⃣ Get all notifications for a user (sorted by created date desc)
    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // 3️⃣ Mark notification as read
    public void markAsRead(Long notificationId) {
        Optional<Notification> notification = notificationRepository.findById(notificationId);
        if (notification.isPresent()) {
            Notification notif = notification.get();
            notif.setRead(true);
            notificationRepository.save(notif);
        } else {
            throw new RuntimeException("Notification not found with ID: " + notificationId);
        }
    }

    // 4️⃣ Get unread count for user
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }

    // 5️⃣ Delete notification (only if it belongs to user)
    public void deleteNotification(Long notificationId, Long userId) {
        Optional<Notification> notification = notificationRepository.findByIdAndUserId(notificationId, userId);
        if (notification.isPresent()) {
            notificationRepository.delete(notification.get());
        } else {
            throw new RuntimeException("Notification not found or access denied");
        }
    }
}
