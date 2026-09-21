package com.loanorigination.loan.dto;

import java.math.BigDecimal;

public class CreditAssessmentRequest {

    private Long tenantId;
    private Long customerId;
    private Long loanId;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyObligations;
    private BigDecimal loanAmount;
    private Integer creditScore;

    public CreditAssessmentRequest() {
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlyObligations() {
        return monthlyObligations;
    }

    public void setMonthlyObligations(BigDecimal monthlyObligations) {
        this.monthlyObligations = monthlyObligations;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }
}