package com.loanorigination.risk.client;

import com.loanorigination.creditrisk.dto.NotificationRequest;
import com.loanorigination.creditrisk.dto.NotificationResponse;
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