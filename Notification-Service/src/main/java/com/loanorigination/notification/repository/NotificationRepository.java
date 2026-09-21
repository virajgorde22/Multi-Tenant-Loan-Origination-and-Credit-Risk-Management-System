package com.loanorigination.notification.repository;

import com.loanorigination.notification.entity.Notification;
import com.loanorigination.notification.entity.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByCustomerId(Long customerId);

    List<Notification> findByTenantId(Long tenantId);

    List<Notification> findByStatus(NotificationStatus status);

    List<Notification> findByCustomerIdOrderByCreatedAtDesc(
            Long customerId
    );
}