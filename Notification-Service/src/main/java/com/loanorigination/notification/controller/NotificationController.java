package com.loanorigination.notification.controller;

import com.loanorigination.notification.dto.NotificationRequest;
import com.loanorigination.notification.dto.NotificationResponse;
import com.loanorigination.notification.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;


    // =========================================================
    // CREATE NOTIFICATION
    // =========================================================

    @PostMapping
    public ResponseEntity<NotificationResponse>
    createNotification(
            @Valid @RequestBody NotificationRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        notificationService
                                .createNotification(request)
                );
    }


    // =========================================================
    // SEND EMAIL NOTIFICATION
    // =========================================================

    @PostMapping("/email")
    public ResponseEntity<NotificationResponse>
    sendEmailNotification(
            @Valid @RequestBody NotificationRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        notificationService
                                .sendEmailNotification(request)
                );
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse>
    getNotification(@PathVariable Long id) {

        return ResponseEntity.ok(
                notificationService
                        .getNotification(id)
        );
    }


    // =========================================================
    // GET BY CUSTOMER
    // =========================================================

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<NotificationResponse>>
    getByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                notificationService
                        .getNotificationsByCustomer(customerId)
        );
    }


    // =========================================================
    // GET BY TENANT
    // =========================================================

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<NotificationResponse>>
    getByTenant(
            @PathVariable Long tenantId) {

        return ResponseEntity.ok(
                notificationService
                        .getNotificationsByTenant(tenantId)
        );
    }


    // =========================================================
    // MARK AS READ
    // =========================================================

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse>
    markAsRead(@PathVariable Long id) {

        return ResponseEntity.ok(
                notificationService
                        .markAsRead(id)
        );
    }


    // =========================================================
    // DELETE
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>
    deleteNotification(@PathVariable Long id) {

        notificationService.deleteNotification(id);

        return ResponseEntity.noContent().build();
    }
}

