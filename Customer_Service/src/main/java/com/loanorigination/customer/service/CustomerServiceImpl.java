package com.loanorigination.customer.service;

import com.loanorigination.customer.client.TenantClient;
import com.loanorigination.customer.dto.CustomerRequest;
import com.loanorigination.customer.dto.CustomerResponse;
import com.loanorigination.customer.dto.TenantResponse;
import com.loanorigination.customer.entity.Customer;
import com.loanorigination.customer.entity.CustomerStatus;
import com.loanorigination.customer.exception.CustomerAlreadyExistsException;
import com.loanorigination.customer.exception.CustomerNotFoundException;
import com.loanorigination.customer.repository.CustomerRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.loanorigination.customer.exception.TenantNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final TenantClient tenantClient;


    // =========================================================
    // CREATE CUSTOMER
    // =========================================================

    @Override
    public CustomerResponse createCustomer(CustomerRequest request) {

        // -----------------------------------------------------
        // 1. CHECK WHETHER TENANT EXISTS
        // -----------------------------------------------------

        validateTenant(request.getTenantId());


        // -----------------------------------------------------
        // 2. CHECK DUPLICATE CUSTOMER CODE
        // -----------------------------------------------------

        if (customerRepository.existsByCustomerCode(
                request.getCustomerCode())) {

            throw new CustomerAlreadyExistsException(
                    "Customer code already exists: "
                            + request.getCustomerCode()
            );
        }


        // -----------------------------------------------------
        // 3. CHECK DUPLICATE EMAIL
        // -----------------------------------------------------

        if (customerRepository.existsByEmail(
                request.getEmail())) {

            throw new CustomerAlreadyExistsException(
                    "Customer with email already exists: "
                            + request.getEmail()
            );
        }


        // -----------------------------------------------------
        // 4. CREATE CUSTOMER
        // -----------------------------------------------------

        LocalDateTime now = LocalDateTime.now();

        Customer customer = Customer.builder()
                .tenantId(request.getTenantId())
                .customerCode(request.getCustomerCode())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pincode(request.getPincode())
                .panNumber(request.getPanNumber())
                .aadhaarNumber(request.getAadhaarNumber())
                .employmentType(request.getEmploymentType())
                .annualIncome(request.getAnnualIncome())
                .status(CustomerStatus.ACTIVE)
                .createdAt(now)
                .updatedAt(now)
                .build();


        // -----------------------------------------------------
        // 5. SAVE CUSTOMER
        // -----------------------------------------------------

        Customer savedCustomer =
                customerRepository.save(customer);

        return mapToResponse(savedCustomer);
    }


    // =========================================================
    // VALIDATE TENANT
    // =========================================================

    private void validateTenant(Long tenantId) {

        try {

            TenantResponse tenant =
                    tenantClient.getTenant(tenantId);

            if (tenant == null) {
                throw new TenantNotFoundException(
                        "Tenant not found with ID: " + tenantId
                );
            }

        } catch (FeignException.NotFound e) {

            throw new TenantNotFoundException(
                    "Tenant not found with ID: " + tenantId
            );

        } catch (FeignException e) {

            throw new RuntimeException(
                    "Unable to connect to Tenant Service"
            );
        }
    }

    // =========================================================
    // GET CUSTOMER BY ID
    // =========================================================

    @Override
    public CustomerResponse getCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with ID: " + id
                        )
                );

        return mapToResponse(customer);
    }


    // =========================================================
    // GET CUSTOMERS BY TENANT
    // =========================================================

    @Override
    public List<CustomerResponse> getCustomersByTenant(
            Long tenantId) {

        // Also verify that tenant exists
        validateTenant(tenantId);

        return customerRepository
                .findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // UPDATE CUSTOMER
    // =========================================================

    @Override
    public CustomerResponse updateCustomer(
            Long id,
            CustomerRequest request) {

        // -----------------------------------------------------
        // 1. CHECK CUSTOMER EXISTS
        // -----------------------------------------------------

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with ID: " + id
                        )
                );


        // -----------------------------------------------------
        // 2. CHECK TENANT EXISTS
        // -----------------------------------------------------

        validateTenant(request.getTenantId());


        // -----------------------------------------------------
        // 3. CHECK EMAIL
        // -----------------------------------------------------

        customerRepository.findByEmail(request.getEmail())
                .ifPresent(existingCustomer -> {

                    if (!existingCustomer.getId().equals(id)) {

                        throw new CustomerAlreadyExistsException(
                                "Email already exists: "
                                        + request.getEmail()
                        );
                    }
                });


        // -----------------------------------------------------
        // 4. UPDATE CUSTOMER
        // -----------------------------------------------------

        customer.setTenantId(request.getTenantId());

        customer.setFirstName(
                request.getFirstName()
        );

        customer.setLastName(
                request.getLastName()
        );

        customer.setEmail(
                request.getEmail()
        );

        customer.setPhone(
                request.getPhone()
        );

        customer.setDateOfBirth(
                request.getDateOfBirth()
        );

        customer.setGender(
                request.getGender()
        );

        customer.setAddress(
                request.getAddress()
        );

        customer.setCity(
                request.getCity()
        );

        customer.setState(
                request.getState()
        );

        customer.setPincode(
                request.getPincode()
        );

        customer.setPanNumber(
                request.getPanNumber()
        );

        customer.setAadhaarNumber(
                request.getAadhaarNumber()
        );

        customer.setEmploymentType(
                request.getEmploymentType()
        );

        customer.setAnnualIncome(
                request.getAnnualIncome()
        );

        customer.setUpdatedAt(
                LocalDateTime.now()
        );


        // -----------------------------------------------------
        // 5. SAVE
        // -----------------------------------------------------

        Customer updatedCustomer =
                customerRepository.save(customer);

        return mapToResponse(updatedCustomer);
    }


    // =========================================================
    // DEACTIVATE CUSTOMER
    // =========================================================

    @Override
    public void deactivateCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() ->
                        new CustomerNotFoundException(
                                "Customer not found with ID: " + id
                        )
                );

        customer.setStatus(CustomerStatus.INACTIVE);

        customer.setUpdatedAt(
                LocalDateTime.now()
        );

        customerRepository.save(customer);
    }


    // =========================================================
    // ENTITY -> RESPONSE DTO
    // =========================================================

    private CustomerResponse mapToResponse(
            Customer customer) {

        return CustomerResponse.builder()
                .id(customer.getId())
                .tenantId(customer.getTenantId())
                .customerCode(customer.getCustomerCode())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender())
                .address(customer.getAddress())
                .city(customer.getCity())
                .state(customer.getState())
                .pincode(customer.getPincode())
                .employmentType(customer.getEmploymentType())
                .annualIncome(customer.getAnnualIncome())
                .status(customer.getStatus())
                .build();
    }
}
```
