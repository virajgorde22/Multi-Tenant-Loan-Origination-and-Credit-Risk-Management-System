package com.loanorigination.loan.dto;

public class CreditAssessmentResponse {

    private Long id;
    private Long tenantId;
    private Long customerId;
    private Long loanId;
    private Integer creditScore;
    private Double debtToIncomeRatio;
    private String riskLevel;
    private String status;
    private String remarks;

    public CreditAssessmentResponse() {
    }

    public CreditAssessmentResponse(
            Long id,
            Long tenantId,
            Long customerId,
            Long loanId,
            Integer creditScore,
            Double debtToIncomeRatio,
            String riskLevel,
            String status,
            String remarks) {

        this.id = id;
        this.tenantId = tenantId;
        this.customerId = customerId;
        this.loanId = loanId;
        this.creditScore = creditScore;
        this.debtToIncomeRatio = debtToIncomeRatio;
        this.riskLevel = riskLevel;
        this.status = status;
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getCreditScore() {
        return creditScore;
    }

    public void setCreditScore(Integer creditScore) {
        this.creditScore = creditScore;
    }

    public Double getDebtToIncomeRatio() {
        return debtToIncomeRatio;
    }

    public void setDebtToIncomeRatio(Double debtToIncomeRatio) {
        this.debtToIncomeRatio = debtToIncomeRatio;
    }

    // CORRECTED
    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}