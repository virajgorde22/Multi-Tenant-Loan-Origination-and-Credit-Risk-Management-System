package com.loanorigination.document.controller;

import com.loanorigination.document.dto.DocumentResponse;
import com.loanorigination.document.dto.DocumentVerificationRequest;
import com.loanorigination.document.entity.DocumentType;
import com.loanorigination.document.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;


    // =========================================================
    // UPLOAD DOCUMENT
    // =========================================================

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DocumentResponse> uploadDocument(

            @RequestParam Long loanId,

            @RequestParam Long tenantId,

            @RequestParam DocumentType documentType,

            @RequestParam MultipartFile file) {

        DocumentResponse response =
                documentService.uploadDocument(
                        loanId,
                        tenantId,
                        documentType,
                        file
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    // =========================================================
    // GET DOCUMENT BY ID
    // =========================================================

    @GetMapping("/{id}")
    public ResponseEntity<DocumentResponse> getDocument(
            @PathVariable Long id) {

        DocumentResponse response =
                documentService.getDocument(id);

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // GET DOCUMENTS BY LOAN
    // =========================================================

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByLoan(
            @PathVariable Long loanId) {

        List<DocumentResponse> documents =
                documentService.getDocumentsByLoan(loanId);

        return ResponseEntity.ok(documents);
    }


    // =========================================================
    // GET DOCUMENTS BY TENANT
    // =========================================================

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<DocumentResponse>> getDocumentsByTenant(
            @PathVariable Long tenantId) {

        List<DocumentResponse> documents =
                documentService.getDocumentsByTenant(tenantId);

        return ResponseEntity.ok(documents);
    }


    // =========================================================
    // VERIFY DOCUMENT
    // =========================================================

    @PutMapping("/{id}/verify")
    public ResponseEntity<DocumentResponse> verifyDocument(

            @PathVariable Long id,

            @RequestBody DocumentVerificationRequest request) {

        DocumentResponse response =
                documentService.verifyDocument(
                        id,
                        request
                );

        return ResponseEntity.ok(response);
    }


    // =========================================================
    // DELETE DOCUMENT
    // =========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable Long id) {

        documentService.deleteDocument(id);

        return ResponseEntity.noContent().build();
    }

    // =========================================================
// DOWNLOAD DOCUMENT
// =========================================================

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long id) {

        Resource resource =
                (Resource) documentService.downloadDocument(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.getFilename() + "\""
                )
                .body(resource);
    }
}