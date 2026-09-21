package com.loanorigination.risk.dto;

import com.loanorigination.risk.entity.AssessmentStatus;
import com.loanorigination.risk.entity.RiskLevel;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditAssessmentResponse {

    private Long id;

    private Long tenantId;

    private Long customerId;

    private Long loanId;

    private BigDecimal monthlyIncome;

    private BigDecimal monthlyObligations;

    private BigDecimal loanAmount;

    private Integer creditScore;

    private Double debtToIncomeRatio;

    private RiskLevel riskLevel;

    private AssessmentStatus status;

    private String remarks;

    private LocalDateTime assessedAt;
}