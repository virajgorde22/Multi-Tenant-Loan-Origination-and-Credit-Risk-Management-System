package com.loanorigination.document.dto;

import com.loanorigination.document.entity.DocumentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DocumentVerificationRequest {

    @NotNull
    private DocumentStatus status;

    private String remarks;
}