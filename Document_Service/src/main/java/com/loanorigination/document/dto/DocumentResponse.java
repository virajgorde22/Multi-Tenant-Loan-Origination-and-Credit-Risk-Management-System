package com.loanorigination.document.dto;

import com.loanorigination.document.entity.DocumentStatus;
import com.loanorigination.document.entity.DocumentType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DocumentResponse {

    private Long id;

    private Long loanId;

    private Long tenantId;

    private DocumentType documentType;

    private String documentName;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private DocumentStatus status;

    private Long verifiedBy;

    private String verificationRemarks;

    private LocalDateTime uploadedAt;

    private LocalDateTime verifiedAt;
}