package com.loanorigination.document.repository;

import com.loanorigination.document.entity.Document;
import com.loanorigination.document.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository
        extends JpaRepository<Document, Long> {

    List<Document> findByLoanId(Long loanId);

    List<Document> findByTenantId(Long tenantId);

    List<Document> findByLoanIdAndDocumentType(
            Long loanId,
            DocumentType documentType
    );
}