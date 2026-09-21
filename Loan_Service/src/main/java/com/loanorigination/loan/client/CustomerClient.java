package com.loanorigination.loan.client;

import com.loanorigination.loan.dto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "CUSTOMER-SERVICE")
public interface CustomerClient {

    @GetMapping("/api/customers/{id}")
    CustomerResponse getCustomer(
            @PathVariable("id") Long id
    );
}