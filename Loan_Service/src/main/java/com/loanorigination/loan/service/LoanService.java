package com.loanorigination.loan.service;

import com.loanorigination.loan.dto.CreditAssessmentResponse;
import com.loanorigination.loan.dto.LoanRequest;
import com.loanorigination.loan.dto.LoanResponse;
import com.loanorigination.loan.entity.LoanStatus;

import java.util.List;

public interface LoanService {

    LoanResponse createLoan(LoanRequest request);

    LoanResponse getLoanById(Long id);

    List<LoanResponse> getAllLoans();

    List<LoanResponse> getLoansByCustomerId(Long customerId);

    List<LoanResponse> getLoansByTenantId(Long tenantId);

    LoanResponse updateLoan(Long id, LoanRequest request);

    LoanResponse updateLoanStatus(Long id, LoanStatus status);

    CreditAssessmentResponse assessLoan(Long loanId);

    void deleteLoan(Long id);
}