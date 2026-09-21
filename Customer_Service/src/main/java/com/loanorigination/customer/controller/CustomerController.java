package com.loanorigination.customer.controller;

import com.loanorigination.customer.dto.CustomerRequest;
import com.loanorigination.customer.dto.CustomerResponse;
import com.loanorigination.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;


    // Create Customer
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(
            @Valid @RequestBody CustomerRequest request) {

        CustomerResponse response =
                customerService.createCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // Get Customer by ID
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(
            @PathVariable Long id) {

        CustomerResponse response =
                customerService.getCustomer(id);

        return ResponseEntity.ok(response);
    }


    // Get all customers belonging to a tenant
    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<CustomerResponse>> getCustomersByTenant(
            @PathVariable Long tenantId) {

        List<CustomerResponse> customers =
                customerService.getCustomersByTenant(tenantId);

        return ResponseEntity.ok(customers);
    }


    // Update Customer
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(
            @PathVariable Long id,
            @Valid @RequestBody CustomerRequest request) {

        CustomerResponse response =
                customerService.updateCustomer(id, request);

        return ResponseEntity.ok(response);
    }


    // Deactivate Customer
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateCustomer(
            @PathVariable Long id) {

        customerService.deactivateCustomer(id);

        return ResponseEntity.noContent().build();
    }
}