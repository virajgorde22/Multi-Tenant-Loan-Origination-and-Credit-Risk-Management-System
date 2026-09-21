package com.loanorigination.notification.dto;

import com.loanorigination.notification.entity.NotificationStatus;
import com.loanorigination.notification.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    private Long id;

    private Long tenantId;

    private Long customerId;

    private Long loanId;

    private NotificationType notificationType;

    private String recipient;

    private String subject;

    private String message;

    private NotificationStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    private LocalDateTime readAt;
}

