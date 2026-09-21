package com.loanorigination.customer.service;

import com.loanorigination.customer.dto.CustomerRequest;
import com.loanorigination.customer.dto.CustomerResponse;

import java.util.List;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest request);

    CustomerResponse getCustomer(Long id);

    List<CustomerResponse> getCustomersByTenant(Long tenantId);

    CustomerResponse updateCustomer(Long id, CustomerRequest request);

    void deactivateCustomer(Long id);
}