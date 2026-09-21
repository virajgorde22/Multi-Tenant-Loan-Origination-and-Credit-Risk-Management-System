package com.loanorigination.loan.dto;

import com.loanorigination.loan.entity.LoanStatus;
import com.loanorigination.loan.entity.LoanType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class LoanResponse {

    private Long id;

    private Long tenantId;

    private Long customerId;

    private String loanNumber;

    private LoanType loanType;

    private BigDecimal loanAmount;

    private BigDecimal interestRate;

    private Integer tenureMonths;

    private BigDecimal monthlyIncome;

    private BigDecimal monthlyObligations;

    private Integer creditScore;

    private String employmentType;

    private String purpose;

    private LoanStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}