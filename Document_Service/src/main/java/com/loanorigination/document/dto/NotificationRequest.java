package com.loanorigination.document.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {

    private Long tenantId;

    private Long customerId;

    private Long loanId;

    private String notificationType;

    private String subject;

    private String message;
}