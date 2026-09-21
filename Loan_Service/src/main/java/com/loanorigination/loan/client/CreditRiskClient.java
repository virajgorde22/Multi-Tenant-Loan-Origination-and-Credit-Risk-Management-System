package com.loanorigination.loan.client;

import com.loanorigination.loan.dto.CreditAssessmentRequest;
import com.loanorigination.loan.dto.CreditAssessmentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "CREDIT-RISK-SERVICE")
public interface CreditRiskClient {

    @PostMapping("/api/risk/assess")
    CreditAssessmentResponse assessCredit(
            @RequestBody CreditAssessmentRequest request
    );
}