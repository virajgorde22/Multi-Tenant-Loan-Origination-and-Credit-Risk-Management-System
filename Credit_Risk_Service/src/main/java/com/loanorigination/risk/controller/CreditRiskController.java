package com.loanorigination.risk.controller;

import com.loanorigination.risk.dto.CreditAssessmentRequest;
import com.loanorigination.risk.dto.CreditAssessmentResponse;
import com.loanorigination.risk.service.CreditRiskService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/risk")
@RequiredArgsConstructor
public class CreditRiskController {

    private final CreditRiskService creditRiskService;

    @PostMapping("/assess")
    @ResponseStatus(HttpStatus.CREATED)
    public CreditAssessmentResponse assessCredit(
            @Valid @RequestBody CreditAssessmentRequest request) {

        return creditRiskService.assessCredit(request);
    }

    @GetMapping("/{id}")
    public CreditAssessmentResponse getAssessment(
            @PathVariable Long id) {

        return creditRiskService.getAssessment(id);
    }

    @GetMapping("/loan/{loanId}")
    public CreditAssessmentResponse getAssessmentByLoan(
            @PathVariable Long loanId) {

        return creditRiskService
                .getAssessmentByLoanId(loanId);
    }

    @GetMapping("/customer/{customerId}")
    public List<CreditAssessmentResponse>
    getByCustomer(@PathVariable Long customerId) {

        return creditRiskService
                .getAssessmentsByCustomer(customerId);
    }

    @GetMapping("/tenant/{tenantId}")
    public List<CreditAssessmentResponse>
    getByTenant(@PathVariable Long tenantId) {

        return creditRiskService
                .getAssessmentsByTenant(tenantId);
    }
}