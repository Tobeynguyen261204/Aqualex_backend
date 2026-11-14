package com.aquaLex.ai.contract.domain;

import com.aquaLex.auth.domain.User;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "generation_requests")
public class GenerationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "source_file_name", length = 255)
    private String sourceFileName;

    @Column(name = "source_text", columnDefinition = "TEXT")
    private String sourceText;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GenerationRequestStatus status;

    @Column(name = "extracted_data", columnDefinition = "TEXT")
    private String extractedData;

    @Column(name = "missing_fields", columnDefinition = "TEXT")
    private String missingFields;

    @Column(name = "warnings", columnDefinition = "TEXT")
    private String warnings;

    @Column(name="draft_content", columnDefinition = "TEXT")
    private String draftContent;
    @Column(name="draft_language", length = 10)
    private String draftLanguage;
    @Column(name = "draft_version")
    private Integer draftVersion;
    @Column(name ="review_result", columnDefinition = "TEXT")
    private String reviewResult;
    @Column(name = "review_at")
    private Instant reviewAt;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected GenerationRequest() {
    }

    public GenerationRequest(String description, User user) {
        this.description = description;
        this.user = user;
        this.status = GenerationRequestStatus.ANALYZING;
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public UUID getId() {
        return id;
    }

    public GenerationRequestStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public String getSourceText() {
        return sourceText;
    }

    public User getUser() {
        return user;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public String getExtractedData() {
        return extractedData;
    }
    public String getMissingFields() {
        return missingFields;
    }
    public String getWarnings() {
        return warnings;
    }
    public String getDraftContent() {
        return draftContent;
    }
    public String getDraftLanguage() {
        return draftLanguage;
    }
    public Integer getDraftVersion() {
        return draftVersion;
    }
    public String getReviewResult() {
        return reviewResult;
    }
    public Instant getReviewAt() {
        return reviewAt;
    }
    public void changeStatus(GenerationRequestStatus status) {
        this.status = status;
        this.updatedAt = Instant.now();
    }

    public void updateAnalysisResult(
            String extractedData,
            String missingFields,
            String warnings,
            GenerationRequestStatus status
    ) {
        this.extractedData = extractedData;
        this.missingFields = missingFields;
        this.warnings = warnings;
        changeStatus(status);
    }
    public void updateDraft(
            String draftContent,
            String draftLanguage,
            Integer draftVersion
    ) {
        this.draftContent = draftContent;
        this.draftLanguage = draftLanguage;
        this.draftVersion = draftVersion;
        this.status = GenerationRequestStatus.COMPLETED;
        this.updatedAt = Instant.now();
    }

    public void updateDraftContent(String draftContent) {
        this.draftContent = draftContent;
        this.draftVersion = this.draftVersion == null
                ? 1
                : this.draftVersion + 1;
        this.reviewResult = null;
        this.reviewAt = null;
        this.status = GenerationRequestStatus.COMPLETED;
        this.updatedAt = Instant.now();
    }

    public void finalizeDraft() {
        this.status = GenerationRequestStatus.FINALIZED;
        this.updatedAt = Instant.now();
    }

    public void updateReview(String reviewResult, Instant reviewAt) {
        this.reviewResult = reviewResult;
        this.reviewAt = reviewAt;
        this.updatedAt = reviewAt;
    }

    public void attachSourceDocument(String fileName, String text) {
        this.sourceFileName = fileName;
        this.sourceText = text;
        this.extractedData = null;
        this.missingFields = null;
        this.warnings = null;
        this.status = GenerationRequestStatus.ANALYZING;
        this.updatedAt = Instant.now();
    }
}
