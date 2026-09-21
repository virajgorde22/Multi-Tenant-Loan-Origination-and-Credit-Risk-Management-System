package com.loanorigination.risk.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreditAssessmentRequest {

    @NotNull
    private Long tenantId;

    @NotNull
    private Long customerId;

    @NotNull
    private Long loanId;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal monthlyIncome;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal monthlyObligations;

    @NotNull
    @DecimalMin(value = "0.0")
    private BigDecimal loanAmount;

    private Integer creditScore;
}