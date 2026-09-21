package com.loanorigination.tenant.service;

import com.loanorigination.tenant.dto.TenantRequest;
import com.loanorigination.tenant.dto.TenantResponse;

import java.util.List;

public interface TenantService {

    TenantResponse createTenant(TenantRequest request);

    TenantResponse getTenant(Long id);

    List<TenantResponse> getAllTenants();

    TenantResponse updateTenant(Long id, TenantRequest request);

    void deleteTenant(Long id);
}