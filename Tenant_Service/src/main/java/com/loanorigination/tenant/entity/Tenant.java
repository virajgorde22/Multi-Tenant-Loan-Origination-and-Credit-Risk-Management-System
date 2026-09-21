package com.loanorigination.tenant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String tenantCode;

    @Column(nullable = false)
    private String tenantName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    private String address;

    private String logoUrl;

    private String website;

    @Enumerated(EnumType.STRING)
    private TenantStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}