package com.loanorigination.risk.service;

import com.loanorigination.risk.dto.CreditAssessmentRequest;
import com.loanorigination.risk.dto.CreditAssessmentResponse;

import java.util.List;

public interface CreditRiskService {

    CreditAssessmentResponse assessCredit(
            CreditAssessmentRequest request
    );

    CreditAssessmentResponse getAssessment(Long id);

    CreditAssessmentResponse getAssessmentByLoanId(Long loanId);

    List<CreditAssessmentResponse> getAssessmentsByCustomer(
            Long customerId
    );

    List<CreditAssessmentResponse> getAssessmentsByTenant(
            Long tenantId
    );
}