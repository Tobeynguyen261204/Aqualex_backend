package com.aquaLex.ai.contract.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import com.aquaLex.common.exception.GenerationRequestNotFoundException;
import com.aquaLex.ai.contract.domain.GenerationRequest;
import com.aquaLex.ai.contract.domain.GenerationRequestStatus;
import com.aquaLex.ai.contract.dto.CreateGenerationRequest;
import com.aquaLex.ai.contract.dto.ContractReviewResult;
import com.aquaLex.ai.contract.dto.GenerationRequestResponse;
import com.aquaLex.ai.contract.dto.ReviseDraftRequest;
import com.aquaLex.ai.contract.dto.UpdateDraftRequest;
import com.aquaLex.ai.contract.infrastructure.GenerationRequestRepository;
import com.aquaLex.auth.domain.User;
import com.aquaLex.auth.infrastructure.UserRepository;

@Service
public class GenerationRequestService {

    private final GenerationRequestRepository generationRequestRepository;
    private final UserRepository userRepository;
    private final ContractAnalysisService contractAnalysisService;
    private final ObjectMapper objectMapper;
    private final ContractDraftGenerator contractDraftGenerator;
    private final ContractReviewService contractReviewService;
    private final ContractRevisionService contractRevisionService;

    public GenerationRequestService(
            GenerationRequestRepository generationRequestRepository,
            UserRepository userRepository,
            ContractAnalysisService contractAnalysisService,
            ObjectMapper objectMapper,
            ContractDraftGenerator contractDraftGenerator,
            ContractReviewService contractReviewService,
            ContractRevisionService contractRevisionService
    ) {
        this.generationRequestRepository = generationRequestRepository;
        this.userRepository = userRepository;
        this.contractAnalysisService = contractAnalysisService;
        this.objectMapper = objectMapper;
        this.contractDraftGenerator = contractDraftGenerator;
        this.contractReviewService = contractReviewService;
        this.contractRevisionService = contractRevisionService;
    }

    public GenerationRequestResponse createRequest(
            CreateGenerationRequest request,
            String userEmail
    ) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new GenerationRequestNotFoundException("User not found"));

        GenerationRequest generationRequest = generationRequestRepository.save(
                new GenerationRequest(request.description().trim(), user)
        );

        return toResponse(generationRequest);
    }
    public GenerationRequestResponse getRequestById(
            UUID requestId,
            String userEmail
    )
    {
        GenerationRequest generationRequest =
                generationRequestRepository.findByIdAndUser_Email(
                        requestId,
                        userEmail
                ).orElseThrow(() -> new GenerationRequestNotFoundException(
                        "Generation request not found"
                ));

        return toResponse(generationRequest);
    }
    public GenerationRequestResponse analyzeRequest(
            UUID requestId,
            String userEmail
    ) {
        GenerationRequest generationRequest =
                generationRequestRepository.findByIdAndUser_Email(
                        requestId,
                        userEmail
                ).orElseThrow(() -> new GenerationRequestNotFoundException(
                        "Generation request not found"
                ));
        if (generationRequest.getStatus() != GenerationRequestStatus.ANALYZING) {
            return toResponse(generationRequest);
        }

        ContractAnalysisResult analysisResult =
                contractAnalysisService.analyze(generationRequest.getDescription());

        generationRequest.updateAnalysisResult(
                analysisResult.extractedData(),
                analysisResult.missingFields(),
                analysisResult.warnings(),
                analysisResult.status()
        );
        generationRequestRepository.save(generationRequest);

        return toResponse(generationRequest);
    }
    public GenerationRequestResponse confirmRequest(
            UUID requestId,
            String userEmail,
            boolean confirmed,
            Map<String, Object> additionalData
    ) {
        GenerationRequest generationRequest =
                generationRequestRepository.findByIdAndUser_Email(
                        requestId,
                        userEmail
                ).orElseThrow(() -> new GenerationRequestNotFoundException(
                        "Generation request not found"
                ));

        if (generationRequest.getStatus() != GenerationRequestStatus.NEEDS_CONFIRMATION) {
            return toResponse(generationRequest);
        }

        if (!confirmed) {
            generationRequest.changeStatus(GenerationRequestStatus.CANCELLED);
        } else {
            String mergedExtractedData = mergeExtractedData(
                    generationRequest.getExtractedData(),
                    additionalData
            );
            generationRequest.updateAnalysisResult(
                    mergedExtractedData,
                    generationRequest.getMissingFields(),
                    generationRequest.getWarnings(),
                    GenerationRequestStatus.GENERATING
            );
        }
        generationRequestRepository.save(generationRequest);

        return toResponse(generationRequest);
    }
    public GenerationRequestResponse generateDraft(
            UUID requestId,
            String userEmail
    ) {
        GenerationRequest generationRequest =
                generationRequestRepository.findByIdAndUser_Email(
                        requestId,
                        userEmail
                ).orElseThrow(() -> new GenerationRequestNotFoundException(
                        "Generation request not found"
                ));

        if (generationRequest.getStatus() != GenerationRequestStatus.GENERATING) {
            return toResponse(generationRequest);
        }

        ContractDraftResult draftResult = contractDraftGenerator.generateDraft(
                generationRequest.getDescription(),
                generationRequest.getExtractedData()
        );

        generationRequest.updateDraft(
                draftResult.content(),
                draftResult.language(),
                draftResult.version()
        );
        generationRequestRepository.save(generationRequest);

        return toResponse(generationRequest);
    }

    public GenerationRequestResponse reviewDraft(
            UUID requestId,
            String userEmail
    ) {
        GenerationRequest generationRequest =
                generationRequestRepository.findByIdAndUser_Email(
                        requestId,
                        userEmail
                ).orElseThrow(() -> new GenerationRequestNotFoundException(
                        "Generation request not found"
                ));

        if (generationRequest.getStatus() != GenerationRequestStatus.COMPLETED
                || generationRequest.getDraftContent() == null
                || generationRequest.getDraftContent().isBlank()) {
            return toResponse(generationRequest);
        }

        ContractReviewResult reviewResult = contractReviewService.review(
                generationRequest.getDraftContent(),
                generationRequest.getExtractedData()
        );

        generationRequest.updateReview(
                reviewResult.reviewResult(),
                reviewResult.reviewAt()
        );
        generationRequestRepository.save(generationRequest);

        return toResponse(generationRequest);
    }

    public GenerationRequestResponse updateDraft(
            UUID requestId,
            String userEmail,
            UpdateDraftRequest request
    ) {
        GenerationRequest generationRequest =
                generationRequestRepository.findByIdAndUser_Email(
                        requestId,
                        userEmail
                ).orElseThrow(() -> new GenerationRequestNotFoundException(
                        "Generation request not found"
                ));

        if (generationRequest.getDraftContent() == null
                || generationRequest.getDraftContent().isBlank()) {
            throw new IllegalStateException(
                    "Contract draft has not been generated"
            );
        }

        generationRequest.updateDraftContent(request.draftContent().trim());
        generationRequestRepository.save(generationRequest);

        return toResponse(generationRequest);
    }

    public GenerationRequestResponse reviseDraft(
            UUID requestId,
            String userEmail,
            ReviseDraftRequest request
    ) {
        GenerationRequest generationRequest =
                generationRequestRepository.findByIdAndUser_Email(
                        requestId,
                        userEmail
                ).orElseThrow(() -> new GenerationRequestNotFoundException(
                        "Generation request not found"
                ));

        if (generationRequest.getDraftContent() == null
                || generationRequest.getDraftContent().isBlank()) {
            throw new IllegalStateException(
                    "Contract draft has not been generated"
            );
        }

        String revisedContent = contractRevisionService.revise(
                generationRequest.getDraftContent(),
                request.instruction()
        );

        generationRequest.updateDraftContent(revisedContent.trim());
        generationRequestRepository.save(generationRequest);

        return toResponse(generationRequest);
    }

    public GenerationRequestResponse finalizeDraft(
            UUID requestId,
            String userEmail
    ) {
        GenerationRequest generationRequest =
                generationRequestRepository.findByIdAndUser_Email(
                        requestId,
                        userEmail
                ).orElseThrow(() -> new GenerationRequestNotFoundException(
                        "Generation request not found"
                ));

        if (generationRequest.getStatus() == GenerationRequestStatus.FINALIZED) {
            return toResponse(generationRequest);
        }

        if (generationRequest.getStatus() != GenerationRequestStatus.COMPLETED
                || generationRequest.getDraftContent() == null
                || generationRequest.getDraftContent().isBlank()) {
            throw new IllegalStateException(
                    "Only a completed contract draft can be finalized"
            );
        }

        generationRequest.finalizeDraft();
        generationRequestRepository.save(generationRequest);

        return toResponse(generationRequest);
    }

    private String mergeExtractedData(
            String extractedData,
            Map<String, Object> additionalData
    ) {
        Map<String, Object> mergedData = new LinkedHashMap<>();

        if (extractedData != null && !extractedData.isBlank()) {
            try {
                mergedData.putAll(objectMapper.readValue(
                        extractedData,
                        new TypeReference<Map<String, Object>>() {}
                ));
            } catch (JsonProcessingException exception) {
                throw new IllegalStateException(
                        "Stored extracted data is not valid JSON",
                        exception
                );
            }
        }

        if (additionalData != null) {
            mergedData.putAll(additionalData);
        }

        try {
            return objectMapper.writeValueAsString(mergedData);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException(
                    "Could not serialize extracted contract data",
                    exception
            );
        }
    }

    private GenerationRequestResponse toResponse(
            GenerationRequest generationRequest
    ) {
        return new GenerationRequestResponse(
                generationRequest.getId(),
                generationRequest.getStatus(),
                generationRequest.getDescription(),
                generationRequest.getCreatedAt(),
                generationRequest.getUpdatedAt(),
                generationRequest.getExtractedData(),
                generationRequest.getMissingFields(),
                generationRequest.getWarnings(), 
                generationRequest.getDraftContent(),
                generationRequest.getDraftLanguage(),
                generationRequest.getDraftVersion(),
                generationRequest.getReviewResult(),
                generationRequest.getReviewAt()
        );
    }

}