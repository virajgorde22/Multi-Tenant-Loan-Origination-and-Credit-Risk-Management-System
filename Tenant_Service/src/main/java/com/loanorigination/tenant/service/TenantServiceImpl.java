package com.loanorigination.tenant.service;

import com.loanorigination.tenant.dto.TenantRequest;
import com.loanorigination.tenant.dto.TenantResponse;
import com.loanorigination.tenant.entity.Tenant;
import com.loanorigination.tenant.entity.TenantStatus;
import com.loanorigination.tenant.exception.TenantAlreadyExistsException;
import com.loanorigination.tenant.exception.TenantNotFoundException;
import com.loanorigination.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    @Override
    public TenantResponse createTenant(TenantRequest request) {

        if (tenantRepository.existsByTenantCode(request.getTenantCode())) {
            throw new TenantAlreadyExistsException(
                    "Tenant Code already exists : " + request.getTenantCode());
        }

        Tenant tenant = Tenant.builder()
                .tenantCode(request.getTenantCode())
                .tenantName(request.getTenantName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .website(request.getWebsite())
                .status(TenantStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Tenant savedTenant = tenantRepository.save(tenant);

        return mapToResponse(savedTenant);
    }

    @Override
    public TenantResponse getTenant(Long id) {

        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new TenantNotFoundException("Tenant not found with ID : " + id));

        return mapToResponse(tenant);
    }

    @Override
    public List<TenantResponse> getAllTenants() {

        return tenantRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public TenantResponse updateTenant(Long id, TenantRequest request) {

        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        tenant.setTenantCode(request.getTenantCode());
        tenant.setTenantName(request.getTenantName());
        tenant.setEmail(request.getEmail());
        tenant.setPhone(request.getPhone());
        tenant.setAddress(request.getAddress());
        tenant.setWebsite(request.getWebsite());
        tenant.setUpdatedAt(LocalDateTime.now());

        Tenant updatedTenant = tenantRepository.save(tenant);

        return mapToResponse(updatedTenant);
    }

    @Override
    public void deleteTenant(Long id) {

        Tenant tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found"));

        tenant.setStatus(TenantStatus.INACTIVE);
        tenant.setUpdatedAt(LocalDateTime.now());

        tenantRepository.save(tenant);
    }

    private TenantResponse mapToResponse(Tenant tenant) {

        return TenantResponse.builder()
                .id(tenant.getId())
                .tenantCode(tenant.getTenantCode())
                .tenantName(tenant.getTenantName())
                .email(tenant.getEmail())
                .phone(tenant.getPhone())
                .website(tenant.getWebsite())
                .status(tenant.getStatus())
                .build();
    }
}