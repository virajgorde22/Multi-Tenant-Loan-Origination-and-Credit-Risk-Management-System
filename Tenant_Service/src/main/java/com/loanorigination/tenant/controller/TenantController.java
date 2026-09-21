package com.loanorigination.tenant.controller;

import com.loanorigination.tenant.dto.TenantRequest;
import com.loanorigination.tenant.dto.TenantResponse;
import com.loanorigination.tenant.service.TenantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    // Create Tenant
    @PostMapping
    public ResponseEntity<TenantResponse> createTenant(
            @Valid @RequestBody TenantRequest request) {

        TenantResponse response = tenantService.createTenant(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get Tenant By Id
    @GetMapping("/{id}")
    public ResponseEntity<TenantResponse> getTenantById(@PathVariable Long id) {

        TenantResponse response = tenantService.getTenant(id);
        return ResponseEntity.ok(response);
    }

    // Get All Tenants
    @GetMapping
    public ResponseEntity<List<TenantResponse>> getAllTenants() {

        List<TenantResponse> response = tenantService.getAllTenants();
        return ResponseEntity.ok(response);
    }

    // Update Tenant
    @PutMapping("/{id}")
    public ResponseEntity<TenantResponse> updateTenant(
            @PathVariable Long id,
            @Valid @RequestBody TenantRequest request) {

        TenantResponse response = tenantService.updateTenant(id, request);
        return ResponseEntity.ok(response);
    }

    // Soft Delete Tenant
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTenant(@PathVariable Long id) {

        tenantService.deleteTenant(id);
        return ResponseEntity.ok("Tenant deleted successfully.");
    }

}