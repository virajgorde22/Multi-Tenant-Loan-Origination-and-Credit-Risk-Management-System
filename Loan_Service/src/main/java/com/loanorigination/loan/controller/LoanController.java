package com.loanorigination.loan.controller;

import com.loanorigination.loan.dto.CreditAssessmentResponse;
import com.loanorigination.loan.dto.LoanRequest;
import com.loanorigination.loan.dto.LoanResponse;
import com.loanorigination.loan.entity.LoanStatus;
import com.loanorigination.loan.service.LoanService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    // CREATE LOAN
    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(
            @Valid @RequestBody LoanRequest request
    ) {

        LoanResponse response = loanService.createLoan(request);

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    // GET LOAN BY ID
    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoanById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                loanService.getLoanById(id)
        );
    }

    // GET ALL LOANS
    @GetMapping
    public ResponseEntity<List<LoanResponse>> getAllLoans() {

        return ResponseEntity.ok(
                loanService.getAllLoans()
        );
    }

    // GET LOANS BY CUSTOMER
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<LoanResponse>> getLoansByCustomerId(
            @PathVariable Long customerId
    ) {

        return ResponseEntity.ok(
                loanService.getLoansByCustomerId(customerId)
        );
    }

    // GET LOANS BY TENANT
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<LoanResponse>> getLoansByTenantId(
            @PathVariable Long tenantId
    ) {

        return ResponseEntity.ok(
                loanService.getLoansByTenantId(tenantId)
        );
    }

    // UPDATE LOAN
    @PutMapping("/{id}")
    public ResponseEntity<LoanResponse> updateLoan(
            @PathVariable Long id,
            @Valid @RequestBody LoanRequest request
    ) {

        return ResponseEntity.ok(
                loanService.updateLoan(id, request)
        );
    }

    // UPDATE LOAN STATUS
    @PutMapping("/{id}/status")
    public ResponseEntity<LoanResponse> updateLoanStatus(
            @PathVariable Long id,
            @RequestParam LoanStatus status
    ) {

        return ResponseEntity.ok(
                loanService.updateLoanStatus(id, status)
        );
    }

    // DELETE LOAN
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(
            @PathVariable Long id
    ) {

        loanService.deleteLoan(id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/assess")
    public CreditAssessmentResponse assessLoan(
            @PathVariable Long id) {

        return loanService.assessLoan(id);
    }

}