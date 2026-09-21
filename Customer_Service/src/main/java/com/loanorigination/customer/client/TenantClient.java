package com.loanorigination.customer.client;

import com.loanorigination.customer.dto.TenantResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "TENANT-SERVICE")
public interface TenantClient {

    @GetMapping("/api/tenants/{id}")
    TenantResponse getTenant(@PathVariable Long id);
}