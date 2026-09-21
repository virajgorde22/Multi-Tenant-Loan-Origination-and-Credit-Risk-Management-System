package com.loanorigination.customer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId;

    @Column(nullable = false, unique = true, length = 30)
    private String customerCode;

    @Column(nullable = false, length = 100)
    private String firstName;

    @Column(length = 100)
    private String lastName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    private LocalDate dateOfBirth;

    private String gender;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String city;

    private String state;

    private String pincode;

    private String panNumber;

    private String aadhaarNumber;

    private String employmentType;

    private BigDecimal annualIncome;

    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}