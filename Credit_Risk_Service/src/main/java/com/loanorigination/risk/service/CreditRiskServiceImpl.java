package com.loanorigination.risk.service;

import com.loanorigination.risk.client.NotificationClient;
import com.loanorigination.risk.dto.CreditAssessmentRequest;
import com.loanorigination.risk.dto.CreditAssessmentResponse;
import com.loanorigination.risk.dto.NotificationRequest;
import com.loanorigination.risk.entity.AssessmentStatus;
import com.loanorigination.risk.entity.CreditAssessment;
import com.loanorigination.risk.entity.RiskLevel;
import com.loanorigination.risk.exception.AssessmentAlreadyExistsException;
import com.loanorigination.risk.exception.CreditAssessmentNotFoundException;
import com.loanorigination.risk.repository.CreditAssessmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditRiskServiceImpl implements CreditRiskService {

    private final CreditAssessmentRepository repository;
    private final NotificationClient notificationClient;


    @Override
    public CreditAssessmentResponse assessCredit(
            CreditAssessmentRequest request) {

        // 1. Validate input
        validateAssessmentRequest(request);

        // 2. Check duplicate assessment
        if (repository.existsByLoanId(request.getLoanId())) {

            throw new AssessmentAlreadyExistsException(
                    "Credit assessment already exists for loan ID: "
                            + request.getLoanId()
            );
        }

        // 3. Calculate Debt-to-Income Ratio
        double dti = calculateDebtToIncomeRatio(
                request.getMonthlyIncome(),
                request.getMonthlyObligations()
        );

        // 4. Determine risk level
        RiskLevel riskLevel = determineRiskLevel(
                request.getCreditScore(),
                dti
        );

        // 5. Generate remarks
        String remarks = generateRemarks(riskLevel);

        LocalDateTime now = LocalDateTime.now();

        // 6. Create assessment
        CreditAssessment assessment = CreditAssessment.builder()
                .tenantId(request.getTenantId())
                .customerId(request.getCustomerId())
                .loanId(request.getLoanId())
                .monthlyIncome(request.getMonthlyIncome())
                .monthlyObligations(request.getMonthlyObligations())
                .loanAmount(request.getLoanAmount())
                .creditScore(request.getCreditScore())
                .debtToIncomeRatio(dti)
                .riskLevel(riskLevel)
                .status(AssessmentStatus.COMPLETED)
                .remarks(remarks)
                .assessedAt(now)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // 7. Save assessment
        CreditAssessment saved = repository.save(assessment);


        // 8. Send notification
        NotificationRequest notification =
                new NotificationRequest();

        notification.setTenantId(saved.getTenantId());
        notification.setCustomerId(saved.getCustomerId());
        notification.setLoanId(saved.getLoanId());

        notification.setNotificationType(
                "CREDIT_ASSESSMENT_COMPLETED"
        );

        notification.setSubject(
                "Credit Assessment Completed"
        );

        notification.setMessage(
                "Your credit assessment has been completed. "
                        + "Risk Level: "
                        + saved.getRiskLevel()
                        + ". "
                        + saved.getRemarks()
        );

        try {

            notificationClient.createNotification(notification);

        } catch (Exception e) {

            // Notification failure should not undo
            // the successfully completed credit assessment.

            log.error(
                    "Failed to send notification for loan ID: {}",
                    saved.getLoanId(),
                    e
            );
        }


        // 9. Return assessment response
        return mapToResponse(saved);
    }


    // =====================================================
    // INPUT VALIDATION
    // =====================================================

    private void validateAssessmentRequest(
            CreditAssessmentRequest request) {

        // Request validation
        if (request == null) {

            throw new IllegalArgumentException(
                    "Assessment request cannot be null"
            );
        }

        // Tenant ID
        if (request.getTenantId() == null ||
                request.getTenantId() <= 0) {

            throw new IllegalArgumentException(
                    "Tenant ID must be greater than 0"
            );
        }

        // Customer ID
        if (request.getCustomerId() == null ||
                request.getCustomerId() <= 0) {

            throw new IllegalArgumentException(
                    "Customer ID must be greater than 0"
            );
        }

        // Loan ID
        if (request.getLoanId() == null ||
                request.getLoanId() <= 0) {

            throw new IllegalArgumentException(
                    "Loan ID must be greater than 0"
            );
        }

        // Monthly Income
        if (request.getMonthlyIncome() == null) {

            throw new IllegalArgumentException(
                    "Monthly income is required"
            );
        }

        if (request.getMonthlyIncome()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Monthly income must be greater than 0"
            );
        }

        // Monthly Obligations
        if (request.getMonthlyObligations() == null) {

            throw new IllegalArgumentException(
                    "Monthly obligations are required"
            );
        }

        if (request.getMonthlyObligations()
                .compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "Monthly obligations cannot be negative"
            );
        }

        // Loan Amount
        if (request.getLoanAmount() == null) {

            throw new IllegalArgumentException(
                    "Loan amount is required"
            );
        }

        if (request.getLoanAmount()
                .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Loan amount must be greater than 0"
            );
        }

        // Credit Score
        if (request.getCreditScore() != null) {

            if (request.getCreditScore() < 300 ||
                    request.getCreditScore() > 900) {

                throw new IllegalArgumentException(
                        "Credit score must be between 300 and 900"
                );
            }
        }

        // Monthly obligations should not exceed income
        if (request.getMonthlyObligations()
                .compareTo(request.getMonthlyIncome()) > 0) {

            throw new IllegalArgumentException(
                    "Monthly obligations cannot exceed monthly income"
            );
        }
    }


    // =====================================================
    // GET ASSESSMENT BY ID
    // =====================================================

    @Override
    public CreditAssessmentResponse getAssessment(Long id) {

        CreditAssessment assessment =
                repository.findById(id)
                        .orElseThrow(() ->
                                new CreditAssessmentNotFoundException(
                                        "Credit assessment not found: "
                                                + id
                                )
                        );

        return mapToResponse(assessment);
    }


    // =====================================================
    // GET ASSESSMENT BY LOAN ID
    // =====================================================

    @Override
    public CreditAssessmentResponse getAssessmentByLoanId(
            Long loanId) {

        CreditAssessment assessment =
                repository.findByLoanId(loanId)
                        .orElseThrow(() ->
                                new CreditAssessmentNotFoundException(
                                        "Assessment not found for loan: "
                                                + loanId
                                )
                        );

        return mapToResponse(assessment);
    }


    // =====================================================
    // GET ASSESSMENTS BY CUSTOMER
    // =====================================================

    @Override
    public List<CreditAssessmentResponse> getAssessmentsByCustomer(
            Long customerId) {

        return repository.findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // GET ASSESSMENTS BY TENANT
    // =====================================================

    @Override
    public List<CreditAssessmentResponse> getAssessmentsByTenant(
            Long tenantId) {

        return repository.findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // CALCULATE DTI
    // =====================================================

    private double calculateDebtToIncomeRatio(
            BigDecimal income,
            BigDecimal obligations) {

        if (income == null ||
                income.compareTo(BigDecimal.ZERO) <= 0) {

            return 1.0;
        }

        if (obligations == null ||
                obligations.compareTo(BigDecimal.ZERO) < 0) {

            return 0.0;
        }

        return obligations
                .divide(
                        income,
                        4,
                        RoundingMode.HALF_UP
                )
                .doubleValue();
    }


    // =====================================================
    // DETERMINE RISK LEVEL
    // =====================================================

    private RiskLevel determineRiskLevel(
            Integer creditScore,
            double dti) {

        if (creditScore != null) {

            if (creditScore >= 750 &&
                    dti < 0.40) {

                return RiskLevel.LOW;
            }

            if (creditScore >= 650 &&
                    dti < 0.50) {

                return RiskLevel.MEDIUM;
            }

            return RiskLevel.HIGH;
        }

        // If credit score is not available,
        // determine risk based only on DTI.

        if (dti < 0.40) {

            return RiskLevel.LOW;
        }

        if (dti < 0.50) {

            return RiskLevel.MEDIUM;
        }

        return RiskLevel.HIGH;
    }


    // =====================================================
    // GENERATE REMARKS
    // =====================================================

    private String generateRemarks(
            RiskLevel riskLevel) {

        return switch (riskLevel) {

            case LOW ->
                    "Low credit risk. Customer has a healthy debt-to-income ratio.";

            case MEDIUM ->
                    "Medium credit risk. Further verification is recommended.";

            case HIGH ->
                    "High credit risk. Additional credit assessment is required.";
        };
    }


    // =====================================================
    // MAP ENTITY TO RESPONSE
    // =====================================================

    private CreditAssessmentResponse mapToResponse(
            CreditAssessment assessment) {

        return CreditAssessmentResponse.builder()
                .id(assessment.getId())
                .tenantId(assessment.getTenantId())
                .customerId(assessment.getCustomerId())
                .loanId(assessment.getLoanId())
                .monthlyIncome(
                        assessment.getMonthlyIncome()
                )
                .monthlyObligations(
                        assessment.getMonthlyObligations()
                )
                .loanAmount(
                        assessment.getLoanAmount()
                )
                .creditScore(
                        assessment.getCreditScore()
                )
                .debtToIncomeRatio(
                        assessment.getDebtToIncomeRatio()
                )
                .riskLevel(
                        assessment.getRiskLevel()
                )
                .status(
                        assessment.getStatus()
                )
                .remarks(
                        assessment.getRemarks()
                )
                .assessedAt(
                        assessment.getAssessedAt()
                )
                .build();
    }
}
