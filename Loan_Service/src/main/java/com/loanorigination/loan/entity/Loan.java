package com.loanorigination.loan.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "loans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tenant_id", nullable = false)
    private Long tenantId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "loan_number", nullable = false, unique = true, length = 30)
    private String loanNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_type", nullable = false, length = 30)
    private LoanType loanType;

    @Column(
            name = "loan_amount",
            nullable = false,
            precision = 15,
            scale = 2
    )
    private BigDecimal loanAmount;

    @Column(
            name = "interest_rate",
            precision = 5,
            scale = 2
    )
    private BigDecimal interestRate;

    @Column(name = "tenure_months", nullable = false)
    private Integer tenureMonths;

    @Column(
            name = "monthly_income",
            precision = 15,
            scale = 2
    )
    private BigDecimal monthlyIncome;

    @Column(
            name = "monthly_obligations",
            precision = 15,
            scale = 2
    )
    private BigDecimal monthlyObligations;

    @Column(name = "credit_score")
    private Integer creditScore;

    @Column(name = "employment_type", length = 50)
    private String employmentType;

    @Column(name = "purpose", length = 255)
    private String purpose;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private LoanStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    // =========================================================
    // BEFORE INSERT
    // =========================================================

    @PrePersist
    protected void onCreate() {

        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = LoanStatus.PENDING;
        }
    }


    // =========================================================
    // BEFORE UPDATE
    // =========================================================

    @PreUpdate
    protected void onUpdate() {

        updatedAt = LocalDateTime.now();
    }
}