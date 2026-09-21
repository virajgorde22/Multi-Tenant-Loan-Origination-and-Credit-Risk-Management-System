package com.loanorigination.loan.repository;

import com.loanorigination.loan.entity.Loan;
import com.loanorigination.loan.entity.LoanStatus;
import com.loanorigination.loan.entity.LoanType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    Optional<Loan> findByLoanNumber(String loanNumber);

    List<Loan> findByCustomerId(Long customerId);

    List<Loan> findByTenantId(Long tenantId);

    boolean existsByCustomerIdAndLoanTypeAndStatus(
            Long customerId,
            LoanType loanType,
            LoanStatus status
    );
}