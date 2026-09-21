package com.loanorigination.risk.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "credit_assessments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long tenantId;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false)
    private Long loanId;

    private BigDecimal monthlyIncome;

    private BigDecimal monthlyObligations;

    private BigDecimal loanAmount;

    private Integer creditScore;

    private Double debtToIncomeRatio;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    @Enumerated(EnumType.STRING)
    private AssessmentStatus status;

    private String remarks;

    private LocalDateTime assessedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}