package com.loanorigination.risk.repository;

import com.loanorigination.risk.entity.CreditAssessment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreditAssessmentRepository
        extends JpaRepository<CreditAssessment, Long> {

    List<CreditAssessment> findByTenantId(Long tenantId);

    List<CreditAssessment> findByCustomerId(Long customerId);

    Optional<CreditAssessment> findByLoanId(Long loanId);

    boolean existsByLoanId(Long loanId);
}