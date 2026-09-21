package com.loanorigination.document.service;

import com.loanorigination.document.dto.DocumentResponse;
import com.loanorigination.document.dto.DocumentVerificationRequest;
import com.loanorigination.document.entity.DocumentType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {

    DocumentResponse uploadDocument(
            Long loanId,
            Long tenantId,
            DocumentType documentType,
            MultipartFile file
    );

    DocumentResponse getDocument(Long id);

    List<DocumentResponse> getDocumentsByLoan(Long loanId);

    List<DocumentResponse> getDocumentsByTenant(Long tenantId);

    DocumentResponse verifyDocument(
            Long id,
            DocumentVerificationRequest request
    );

    void deleteDocument(Long id);


    // Download document
    Resource downloadDocument(Long id);
}