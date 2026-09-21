package com.loanorigination.loan.service;

import com.loanorigination.loan.client.CreditRiskClient;
import com.loanorigination.loan.client.CustomerClient;
import com.loanorigination.loan.client.NotificationClient;
import com.loanorigination.loan.dto.*;
import com.loanorigination.loan.entity.Loan;
import com.loanorigination.loan.entity.LoanStatus;
import com.loanorigination.loan.exception.CustomerNotFoundException;
import com.loanorigination.loan.repository.LoanRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class LoanServiceImpl implements LoanService {

    private static final Logger log =
            LoggerFactory.getLogger(LoanServiceImpl.class);

    private final LoanRepository loanRepository;
    private final CreditRiskClient creditRiskClient;
    private final CustomerClient customerClient;
    private final NotificationClient notificationClient;

    private final AtomicLong loanNumberSequence =
            new AtomicLong(0);


    public LoanServiceImpl(
            LoanRepository loanRepository,
            CreditRiskClient creditRiskClient,
            CustomerClient customerClient,
            NotificationClient notificationClient) {

        this.loanRepository = loanRepository;
        this.creditRiskClient = creditRiskClient;
        this.customerClient = customerClient;
        this.notificationClient = notificationClient;
    }


    // =====================================================
    // CREATE LOAN
    // =====================================================

    @Override
    public LoanResponse createLoan(LoanRequest request) {

        // 1. Get customer
        CustomerResponse customer =
                customerClient.getCustomer(
                        request.getCustomerId()
                );

        if (customer == null) {

            throw new CustomerNotFoundException(
                    "Customer not found with ID: "
                            + request.getCustomerId()
            );
        }

        // 2. Verify tenant
        if (!customer.getTenantId()
                .equals(request.getTenantId())) {

            throw new RuntimeException(
                    "Customer does not belong to tenant ID: "
                            + request.getTenantId()
            );
        }

        // 3. Create loan
        Loan loan = new Loan();

        loan.setTenantId(request.getTenantId());
        loan.setCustomerId(request.getCustomerId());
        loan.setLoanNumber(generateLoanNumber());
        loan.setLoanType(request.getLoanType());
        loan.setLoanAmount(request.getLoanAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setTenureMonths(request.getTenureMonths());
        loan.setMonthlyIncome(request.getMonthlyIncome());
        loan.setMonthlyObligations(request.getMonthlyObligations());
        loan.setCreditScore(request.getCreditScore());
        loan.setEmploymentType(request.getEmploymentType());
        loan.setPurpose(request.getPurpose());

        // New loans start as PENDING
        loan.setStatus(LoanStatus.PENDING);

        // 4. Save loan
        Loan savedLoan =
                loanRepository.save(loan);


        // 5. Send loan-created notification
        sendLoanNotification(
                savedLoan,
                "LOAN_CREATED",
                "Loan Application Created",
                "Your "
                        + savedLoan.getLoanType()
                        + " loan application has been successfully created. "
                        + "Loan Number: "
                        + savedLoan.getLoanNumber()
                        + ". Loan Amount: "
                        + savedLoan.getLoanAmount()
        );


        return mapToResponse(savedLoan);
    }


    // =====================================================
    // GET LOAN BY ID
    // =====================================================

    @Override
    public LoanResponse getLoanById(Long id) {

        Loan loan =
                loanRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Loan not found with ID: "
                                                + id
                                )
                        );

        return mapToResponse(loan);
    }


    // =====================================================
    // GET ALL LOANS
    // =====================================================

    @Override
    public List<LoanResponse> getAllLoans() {

        return loanRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // GET LOANS BY CUSTOMER
    // =====================================================

    @Override
    public List<LoanResponse> getLoansByCustomerId(
            Long customerId) {

        return loanRepository
                .findByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // GET LOANS BY TENANT
    // =====================================================

    @Override
    public List<LoanResponse> getLoansByTenantId(
            Long tenantId) {

        return loanRepository
                .findByTenantId(tenantId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =====================================================
    // UPDATE LOAN
    // =====================================================

    @Override
    public LoanResponse updateLoan(
            Long id,
            LoanRequest request) {

        Loan loan =
                loanRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Loan not found with ID: "
                                                + id
                                )
                        );

        loan.setTenantId(request.getTenantId());
        loan.setCustomerId(request.getCustomerId());
        loan.setLoanType(request.getLoanType());
        loan.setLoanAmount(request.getLoanAmount());
        loan.setInterestRate(request.getInterestRate());
        loan.setTenureMonths(request.getTenureMonths());
        loan.setMonthlyIncome(request.getMonthlyIncome());
        loan.setMonthlyObligations(request.getMonthlyObligations());
        loan.setCreditScore(request.getCreditScore());
        loan.setEmploymentType(request.getEmploymentType());
        loan.setPurpose(request.getPurpose());

        Loan updatedLoan =
                loanRepository.save(loan);

        return mapToResponse(updatedLoan);
    }


    // =====================================================
    // UPDATE LOAN STATUS
    // =====================================================

    @Override
    public LoanResponse updateLoanStatus(
            Long id,
            LoanStatus status) {

        Loan loan =
                loanRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Loan not found with ID: "
                                                + id
                                )
                        );

        // Keep previous status for notification
        LoanStatus previousStatus =
                loan.getStatus();

        // Update status
        loan.setStatus(status);

        Loan updatedLoan =
                loanRepository.save(loan);


        // Send notification only when status changes
        if (previousStatus != status) {

            sendLoanNotification(
                    updatedLoan,
                    "LOAN_STATUS_CHANGED",
                    "Loan Status Updated",
                    "Your loan "
                            + updatedLoan.getLoanNumber()
                            + " status has been updated from "
                            + previousStatus
                            + " to "
                            + status
                            + "."
            );
        }

        return mapToResponse(updatedLoan);
    }


    // =====================================================
    // ASSESS LOAN
    // =====================================================

    @Override
    public CreditAssessmentResponse assessLoan(Long loanId) {

        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Loan not found with ID: " + loanId
                        )
                );

        CreditAssessmentRequest request =
                new CreditAssessmentRequest();

        request.setTenantId(loan.getTenantId());
        request.setCustomerId(loan.getCustomerId());
        request.setLoanId(loan.getId());
        request.setMonthlyIncome(loan.getMonthlyIncome());
        request.setMonthlyObligations(
                loan.getMonthlyObligations()
        );
        request.setLoanAmount(loan.getLoanAmount());
        request.setCreditScore(loan.getCreditScore());

        // -----------------------------------------
        // 1. Mark loan as under credit assessment
        // -----------------------------------------

        LoanStatus previousStatus = loan.getStatus();

        loan.setStatus(LoanStatus.CREDIT_ASSESSMENT);

        loanRepository.save(loan);

        if (previousStatus != LoanStatus.CREDIT_ASSESSMENT) {

            sendLoanNotification(
                    loan,
                    "LOAN_STATUS_CHANGED",
                    "Loan Under Credit Assessment",
                    "Your loan "
                            + loan.getLoanNumber()
                            + " is now under credit assessment."
            );
        }

        // -----------------------------------------
        // 2. Call Credit Risk Service
        // -----------------------------------------

        CreditAssessmentResponse assessment =
                creditRiskClient.assessCredit(request);

        // -----------------------------------------
        // 3. Determine final status
        // -----------------------------------------

        LoanStatus finalStatus =
                determineLoanStatus(assessment);

        loan.setStatus(finalStatus);

        Loan updatedLoan =
                loanRepository.save(loan);

        // -----------------------------------------
        // 4. Notify final status
        // -----------------------------------------

        if (finalStatus != LoanStatus.CREDIT_ASSESSMENT) {

            String subject;
            String message;

            if (finalStatus == LoanStatus.APPROVED) {

                subject = "Loan Approved";

                message =
                        "Congratulations! Your loan "
                                + updatedLoan.getLoanNumber()
                                + " has been approved.";

            } else if (finalStatus == LoanStatus.REJECTED) {

                subject = "Loan Rejected";

                message =
                        "Your loan "
                                + updatedLoan.getLoanNumber()
                                + " has been rejected "
                                + "after credit assessment.";

            } else {

                subject = "Loan Status Updated";

                message =
                        "Your loan "
                                + updatedLoan.getLoanNumber()
                                + " status has been updated to "
                                + finalStatus + ".";
            }

            sendLoanNotification(
                    updatedLoan,
                    "LOAN_STATUS_CHANGED",
                    subject,
                    message
            );
        }

        return assessment;
    }

    // =====================================================
    // DETERMINE LOAN STATUS FROM RISK
    // =====================================================

    private LoanStatus determineLoanStatus(
            CreditAssessmentResponse assessment) {

        if (assessment == null ||
                assessment.getRiskLevel() == null) {

            return LoanStatus.CREDIT_ASSESSMENT;
        }

        String riskLevel =
                assessment.getRiskLevel().toUpperCase();

        switch (riskLevel) {

            case "LOW":
                return LoanStatus.APPROVED;

            case "MEDIUM":
                return LoanStatus.CREDIT_ASSESSMENT;

            case "HIGH":
                return LoanStatus.REJECTED;

            default:
                return LoanStatus.CREDIT_ASSESSMENT;
        }
    }


    // =====================================================
    // SEND NOTIFICATION
    // =====================================================

    private void sendLoanNotification(
            Loan loan,
            String notificationType,
            String subject,
            String message) {

        NotificationRequest notification =
                new NotificationRequest();

        notification.setTenantId(
                loan.getTenantId()
        );

        notification.setCustomerId(
                loan.getCustomerId()
        );

        notification.setLoanId(
                loan.getId()
        );

        notification.setNotificationType(
                notificationType
        );

        notification.setSubject(
                subject
        );

        notification.setMessage(
                message
        );


        try {

            notificationClient.createNotification(
                    notification
            );

        } catch (Exception e) {

            // Notification failure should not
            // rollback the loan operation.

            log.error(
                    "Failed to send notification for loan ID: {}",
                    loan.getId(),
                    e
            );
        }
    }


    // =====================================================
    // DELETE LOAN
    // =====================================================

    @Override
    public void deleteLoan(Long id) {

        Loan loan =
                loanRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Loan not found with ID: "
                                                + id
                                )
                        );

        loanRepository.delete(loan);
    }


    // =====================================================
    // GENERATE LOAN NUMBER
    // =====================================================

    private String generateLoanNumber() {

        long next =
                loanNumberSequence.incrementAndGet();

        return "LN"
                + System.currentTimeMillis()
                + next;
    }


    // =====================================================
    // ENTITY -> RESPONSE
    // =====================================================

    private LoanResponse mapToResponse(Loan loan) {

        LoanResponse response = new LoanResponse();

        response.setId(loan.getId());
        response.setTenantId(loan.getTenantId());
        response.setCustomerId(loan.getCustomerId());
        response.setLoanNumber(loan.getLoanNumber());
        response.setLoanType(loan.getLoanType());
        response.setLoanAmount(loan.getLoanAmount());
        response.setInterestRate(loan.getInterestRate());
        response.setTenureMonths(loan.getTenureMonths());
        response.setMonthlyIncome(loan.getMonthlyIncome());
        response.setMonthlyObligations(loan.getMonthlyObligations());
        response.setCreditScore(loan.getCreditScore());
        response.setEmploymentType(loan.getEmploymentType());
        response.setPurpose(loan.getPurpose());
        response.setStatus(loan.getStatus());
        response.setCreatedAt(loan.getCreatedAt());
        response.setUpdatedAt(loan.getUpdatedAt());

        return response;
    }
}

