package com.loanorigination.loan.dto;

import lombok.Data;

@Data
public class NotificationRequest {

    private Long tenantId;

    private Long customerId;

    private Long loanId;

    private String notificationType;

    private String subject;

    private String message;
}