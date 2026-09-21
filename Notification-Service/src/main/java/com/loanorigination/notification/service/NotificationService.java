package com.loanorigination.notification.service;

import com.loanorigination.notification.dto.NotificationRequest;
import com.loanorigination.notification.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {

    // =========================================================
    // CREATE NOTIFICATION
    // =========================================================

    NotificationResponse createNotification(
            NotificationRequest request
    );


    // =========================================================
    // SEND EMAIL NOTIFICATION
    // =========================================================

    NotificationResponse sendEmailNotification(
            NotificationRequest request
    );


    // =========================================================
    // GET NOTIFICATION
    // =========================================================

    NotificationResponse getNotification(Long id);


    // =========================================================
    // GET CUSTOMER NOTIFICATIONS
    // =========================================================

    List<NotificationResponse> getNotificationsByCustomer(
            Long customerId
    );


    // =========================================================
    // GET TENANT NOTIFICATIONS
    // =========================================================

    List<NotificationResponse> getNotificationsByTenant(
            Long tenantId
    );


    // =========================================================
    // MARK AS READ
    // =========================================================

    NotificationResponse markAsRead(Long id);


    // =========================================================
    // DELETE
    // =========================================================

    void deleteNotification(Long id);
}

