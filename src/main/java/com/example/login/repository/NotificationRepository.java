package com.example.login.repository;

import com.example.login.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // ✅ Find all unread notifications for a recipient
    List<Notification> findByRecipientUsernameAndIsReadFalse(String recipientUsername);

    // ✅ Find all notifications for a recipient, newest first
    List<Notification> findByRecipientUsernameOrderByCreatedAtDesc(String recipientUsername);

    // ✅ Count pending unread notifications for a specific user
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.recipientUsername = :username AND n.isRead = false")
    Long getPendingNotificationsCount(@Param("username") String username);

    // ✅ Fetch user-type notifications
    @Query("SELECT n FROM Notification n WHERE n.recipientUsername = :username AND n.type = 'USER'")
    List<Notification> getUserNotifications(@Param("username") String username);

    // ✅ Fetch admin-type notifications
    @Query("SELECT n FROM Notification n WHERE n.recipientUsername = :username AND n.type = 'ADMIN'")
    List<Notification> getAdminNotifications(@Param("username") String username);

    // ✅ Mark notification as read
    @Transactional
    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.id = :id")
    void markAsRead(@Param("id") Long id);

    // ✅ Fetch notifications by user ID
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    // ✅ Count unread notifications (fixed field name)
    long countByUserIdAndIsReadFalse(Long userId);

    // ✅ Find specific notification for a specific user
    Optional<Notification> findByIdAndUserId(Long notificationId, Long userId);
}
