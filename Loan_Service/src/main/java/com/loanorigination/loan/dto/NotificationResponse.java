package com.loanorigination.loan.dto;

import lombok.Data;

@Data
public class NotificationResponse {

    private Long id;

    private Long tenantId;

    private Long customerId;

    private Long loanId;

    private String notificationType;

    private String subject;

    private String message;

    private String status;
}