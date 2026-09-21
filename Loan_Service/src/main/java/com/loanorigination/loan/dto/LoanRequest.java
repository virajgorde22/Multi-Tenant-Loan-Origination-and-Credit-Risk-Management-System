package com.loanorigination.loan.dto;

import com.loanorigination.loan.entity.LoanType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LoanRequest {

    private Long tenantId;

    private Long customerId;

    private LoanType loanType;

    private BigDecimal loanAmount;

    private BigDecimal interestRate;

    private Integer tenureMonths;

    private BigDecimal monthlyIncome;

    private BigDecimal monthlyObligations;

    private Integer creditScore;

    private String employmentType;

    private String purpose;
}