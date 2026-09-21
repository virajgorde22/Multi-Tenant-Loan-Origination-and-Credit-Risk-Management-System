package com.loanorigination.customer.repository;

import com.loanorigination.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByCustomerCode(String customerCode);

    boolean existsByEmail(String email);

    Optional<Customer> findByEmail(String email);

    List<Customer> findByTenantId(Long tenantId);
}