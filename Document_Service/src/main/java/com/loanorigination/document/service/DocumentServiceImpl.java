package com.loanorigination.document.service;

import com.loanorigination.document.client.NotificationClient;
import com.loanorigination.document.dto.DocumentResponse;
import com.loanorigination.document.dto.DocumentVerificationRequest;
import com.loanorigination.document.dto.NotificationRequest;
import com.loanorigination.document.entity.Document;
import com.loanorigination.document.entity.DocumentStatus;
import com.loanorigination.document.entity.DocumentType;
import com.loanorigination.document.exception.DocumentNotFoundException;
import com.loanorigination.document.exception.FileStorageException;
import com.loanorigination.document.repository.DocumentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final NotificationClient notificationClient;

    @Value("${file.upload-dir}")
    private String uploadDir;


    // =========================================================
    // UPLOAD DOCUMENT
    // =========================================================

    @Override
    public DocumentResponse uploadDocument(
            Long loanId,
            Long tenantId,
            DocumentType documentType,
            MultipartFile file) {

        // 1. Check if file is empty
        if (file == null || file.isEmpty()) {
            throw new FileStorageException("File is empty");
        }

        // 2. Check MIME type
        if (!"application/pdf".equalsIgnoreCase(file.getContentType())) {
            throw new FileStorageException("Only PDF files are allowed");
        }

        // 3. Check file extension
        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null ||
                !originalFileName.toLowerCase().endsWith(".pdf")) {

            throw new FileStorageException("Only PDF files are allowed");
        }

        try {

            // Generate unique file name
            String fileName =
                    UUID.randomUUID() + "_" + originalFileName;

            // Create storage directory
            Path storagePath = Paths
                    .get(uploadDir)
                    .toAbsolutePath()
                    .normalize();

            Files.createDirectories(storagePath);

            // Create loan-specific directory
            Path loanDirectory =
                    storagePath.resolve("loan-" + loanId);

            Files.createDirectories(loanDirectory);

            // Final file location
            Path targetLocation =
                    loanDirectory.resolve(fileName);

            // Copy file
            Files.copy(
                    file.getInputStream(),
                    targetLocation,
                    StandardCopyOption.REPLACE_EXISTING
            );

            // Create Document entity
            Document document = Document.builder()
                    .loanId(loanId)
                    .tenantId(tenantId)
                    .documentType(documentType)
                    .documentName(originalFileName)
                    .fileName(fileName)
                    .filePath(targetLocation.toString())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .status(DocumentStatus.UPLOADED)
                    .build();

            Document savedDocument =
                    documentRepository.save(document);


            // =====================================================
            // SEND DOCUMENT UPLOAD NOTIFICATION
            // =====================================================

            sendDocumentNotification(
                    savedDocument,
                    "DOCUMENT_UPLOADED",
                    "Document Uploaded Successfully",
                    "Your "
                            + savedDocument.getDocumentType()
                            + " document has been uploaded successfully "
                            + "for loan ID "
                            + savedDocument.getLoanId()
                            + "."
            );


            return mapToResponse(savedDocument);

        } catch (IOException e) {

            throw new FileStorageException(
                    "Failed to store file",
                    e
            );
        }
    }


    // =========================================================
    // GET DOCUMENT BY ID
    // =========================================================

    @Override
    public DocumentResponse getDocument(Long id) {

        Document document =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new DocumentNotFoundException(
                                        "Document not found with id: " + id
                                )
                        );

        return mapToResponse(document);
    }


    // =========================================================
    // GET DOCUMENTS BY LOAN
    // =========================================================

    @Override
    public List<DocumentResponse> getDocumentsByLoan(Long loanId) {

        List<Document> documents =
                documentRepository.findByLoanId(loanId);

        return documents.stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // GET DOCUMENTS BY TENANT
    // =========================================================

    @Override
    public List<DocumentResponse> getDocumentsByTenant(Long tenantId) {

        List<Document> documents =
                documentRepository.findByTenantId(tenantId);

        return documents.stream()
                .map(this::mapToResponse)
                .toList();
    }


    // =========================================================
    // VERIFY DOCUMENT
    // =========================================================

    @Override
    public DocumentResponse verifyDocument(
            Long id,
            DocumentVerificationRequest request) {

        Document document =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new DocumentNotFoundException(
                                        "Document not found with id: " + id
                                )
                        );

        // Only VERIFIED or REJECTED are allowed
        if (request.getStatus() != DocumentStatus.VERIFIED &&
                request.getStatus() != DocumentStatus.REJECTED) {

            throw new IllegalArgumentException(
                    "Document can only be VERIFIED or REJECTED"
            );
        }

        document.setStatus(request.getStatus());

        document.setVerificationRemarks(
                request.getRemarks()
        );

        document.setVerifiedAt(
                LocalDateTime.now()
        );

        /*
         * Later, when Spring Security/JWT is integrated,
         * get the logged-in employee/user ID here.
         *
         * document.setVerifiedBy(userId);
         */

        Document updatedDocument =
                documentRepository.save(document);


        // =====================================================
        // SEND DOCUMENT VERIFICATION NOTIFICATION
        // =====================================================

        String notificationType;
        String subject;
        String message;

        if (request.getStatus() == DocumentStatus.VERIFIED) {

            notificationType = "DOCUMENT_VERIFIED";
            subject = "Document Verified Successfully";

            message =
                    "Your "
                            + document.getDocumentType()
                            + " document for loan ID "
                            + document.getLoanId()
                            + " has been verified successfully.";

        } else {

            notificationType = "DOCUMENT_REJECTED";
            subject = "Document Verification Failed";

            message =
                    "Your "
                            + document.getDocumentType()
                            + " document for loan ID "
                            + document.getLoanId()
                            + " has been rejected.";

            if (request.getRemarks() != null &&
                    !request.getRemarks().isBlank()) {

                message +=
                        " Remarks: "
                                + request.getRemarks();
            }
        }

        sendDocumentNotification(
                updatedDocument,
                notificationType,
                subject,
                message
        );


        return mapToResponse(updatedDocument);
    }


    // =========================================================
    // DELETE DOCUMENT
    // =========================================================

    @Override
    public void deleteDocument(Long id) {

        Document document =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new DocumentNotFoundException(
                                        "Document not found with id: " + id
                                )
                        );

        // Delete physical file
        if (document.getFilePath() != null) {

            try {

                Files.deleteIfExists(
                        Paths.get(document.getFilePath())
                );

            } catch (IOException e) {

                throw new FileStorageException(
                        "Failed to delete file: "
                                + document.getFileName(),
                        e
                );
            }
        }

        // Delete database record
        documentRepository.delete(document);
    }


    // =========================================================
    // SEND DOCUMENT NOTIFICATION
    // =========================================================

    private void sendDocumentNotification(
            Document document,
            String notificationType,
            String subject,
            String message) {

        NotificationRequest notification =
                new NotificationRequest();

        notification.setTenantId(
                document.getTenantId()
        );

        /*
         * Your current Document entity contains
         * tenantId and loanId, but not customerId.
         *
         * Therefore customerId is not set here.
         */
        notification.setLoanId(
                document.getLoanId()
        );

        notification.setNotificationType(
                notificationType
        );

        notification.setSubject(subject);

        notification.setMessage(message);

        try {

            notificationClient.createNotification(
                    notification
            );

        } catch (Exception e) {

            /*
             * Notification failure should not make
             * document upload/verification fail.
             */
            System.out.println(
                    "Failed to send document notification "
                            + "for document ID "
                            + document.getId()
                            + ": "
                            + e.getMessage()
            );
        }
    }


    // =========================================================
    // ENTITY -> DTO
    // =========================================================

    private DocumentResponse mapToResponse(
            Document document) {

        return DocumentResponse.builder()
                .id(document.getId())
                .loanId(document.getLoanId())
                .tenantId(document.getTenantId())
                .documentType(document.getDocumentType())
                .documentName(document.getDocumentName())
                .fileName(document.getFileName())
                .fileType(document.getFileType())
                .fileSize(document.getFileSize())
                .status(document.getStatus())
                .verifiedBy(document.getVerifiedBy())
                .verificationRemarks(
                        document.getVerificationRemarks()
                )
                .uploadedAt(document.getUploadedAt())
                .verifiedAt(document.getVerifiedAt())
                .build();
    }


    // =========================================================
    // DOWNLOAD DOCUMENT
    // =========================================================

    @Override
    public Resource downloadDocument(Long id) {

        Document document =
                documentRepository.findById(id)
                        .orElseThrow(() ->
                                new DocumentNotFoundException(
                                        "Document not found with id: " + id
                                )
                        );

        if (document.getFilePath() == null ||
                document.getFilePath().isBlank()) {

            throw new FileStorageException(
                    "File path is not available for document id: "
                            + id
            );
        }

        Path filePath =
                Paths.get(document.getFilePath())
                        .toAbsolutePath()
                        .normalize();

        Resource resource =
                new FileSystemResource(filePath);

        if (!resource.exists() || !resource.isReadable()) {

            throw new FileStorageException(
                    "File not found or cannot be read: "
                            + document.getFileName()
            );
        }

        return resource;
    }
}