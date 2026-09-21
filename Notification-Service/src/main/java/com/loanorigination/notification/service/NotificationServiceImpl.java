package com.loanorigination.notification.service;


import com.loanorigination.notification.dto.NotificationRequest;
import com.loanorigination.notification.dto.NotificationResponse;
import com.loanorigination.notification.entity.Notification;
import com.loanorigination.notification.entity.NotificationStatus;
import com.loanorigination.notification.exception.NotificationNotFoundException;
import com.loanorigination.notification.repository.NotificationRepository;

import com.loanorigination.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    // Used to send email
    private final JavaMailSender mailSender;


    // =========================================================
    // CREATE NOTIFICATION
    // =========================================================

    @Override
    public NotificationResponse createNotification(
            NotificationRequest request) {

        Notification notification =
                Notification.builder()
                        .tenantId(request.getTenantId())
                        .customerId(request.getCustomerId())
                        .loanId(request.getLoanId())
                        .notificationType(
                                request.getNotificationType()
                        )
                        .subject(request.getSubject())
                        .message(request.getMessage())
                        .status(NotificationStatus.SENT)
                        .createdAt(LocalDateTime.now())
                        .sentAt(LocalDateTime.now())
                        .build();

        Notification saved =
                notificationRepository.save(notification);

        return mapToResponse(saved);
    }


    // =========================================================
    // SEND EMAIL NOTIFICATION
    // =========================================================

    @Override
    public NotificationResponse sendEmailNotification(
            NotificationRequest request) {

        /*
         * First create the notification in the database.
         *
         * We initially mark it as PENDING because the email
         * has not been sent yet.
         */

        Notification notification =
                Notification.builder()
                        .tenantId(request.getTenantId())
                        .customerId(request.getCustomerId())
                        .loanId(request.getLoanId())
                        .notificationType(
                                request.getNotificationType()
                        )
                        .subject(request.getSubject())
                        .message(request.getMessage())
                        .status(NotificationStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .build();

        Notification saved =
                notificationRepository.save(notification);


        // =====================================================
        // SEND EMAIL
        // =====================================================

        try {

            SimpleMailMessage mail =
                    new SimpleMailMessage();

            mail.setTo(request.getRecipient());

            mail.setSubject(
                    request.getSubject()
            );

            mail.setText(
                    request.getMessage()
            );

            mailSender.send(mail);


            // =================================================
            // EMAIL SENT SUCCESSFULLY
            // =================================================

            saved.setStatus(
                    NotificationStatus.SENT
            );

            saved.setSentAt(
                    LocalDateTime.now()
            );

        } catch (Exception e) {

            // =================================================
            // EMAIL FAILED
            // =================================================

            saved.setStatus(
                    NotificationStatus.FAILED
            );

            System.out.println(
                    "Email sending failed: "
                            + e.getMessage()
            );
        }


        // Save final status
        Notification updated =
                notificationRepository.save(saved);

        return mapToResponse(updated);
    }


    // =========================================================
    // GET NOTIFICATION
    // =========================================================

    @Override
    public NotificationResponse getNotification(
            Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new NotificationNotFoundException(
                                        "Notification not found with ID: "
                                                + id
                                )
                        );

        return mapToResponse(notification);
    }


    // =========================================================
    // GET CUSTOMER NOTIFICATIONS
    // =========================================================

    @Override
    public List<NotificationResponse>
    getNotificationsByCustomer(Long customerId) {

        return notificationRepository
                .findByCustomerIdOrderByCreatedAtDesc(
                        customerId
                )
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET TENANT NOTIFICATIONS
    // =========================================================

    @Override
    public List<NotificationResponse>
    getNotificationsByTenant(Long tenantId) {

        return notificationRepository
                .findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // MARK AS READ
    // =========================================================

    @Override
    public NotificationResponse markAsRead(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new NotificationNotFoundException(
                                        "Notification not found with ID: "
                                                + id
                                )
                        );

        notification.setStatus(
                NotificationStatus.READ
        );

        notification.setReadAt(
                LocalDateTime.now()
        );

        Notification updated =
                notificationRepository.save(notification);

        return mapToResponse(updated);
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Override
    public void deleteNotification(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new NotificationNotFoundException(
                                        "Notification not found with ID: "
                                                + id
                                )
                        );

        notificationRepository.delete(notification);
    }


    // =========================================================
    // ENTITY → RESPONSE
    // =========================================================

    private NotificationResponse mapToResponse(
            Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .tenantId(notification.getTenantId())
                .customerId(notification.getCustomerId())
                .loanId(notification.getLoanId())
                .notificationType(
                        notification.getNotificationType()
                )
                .subject(notification.getSubject())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .sentAt(notification.getSentAt())
                .readAt(notification.getReadAt())
                .build();
    }
}

