package com.loanorigination.loan.client;

import com.loanorigination.loan.dto.NotificationRequest;
import com.loanorigination.loan.dto.NotificationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationClient {

    @PostMapping("/api/notifications")
    NotificationResponse createNotification(
            @RequestBody NotificationRequest request
    );
}