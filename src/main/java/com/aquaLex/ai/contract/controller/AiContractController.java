package com.aquaLex.ai.contract.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestPart;

import com.aquaLex.common.response.ApiResponse;
import com.aquaLex.ai.contract.application.ContractPdfExportService;
import com.aquaLex.ai.contract.application.GenerationRequestService;
import com.aquaLex.ai.contract.application.PdfTextExtractionService;
import com.aquaLex.ai.contract.dto.ConfirmGenerationRequest;
import com.aquaLex.ai.contract.dto.CreateGenerationRequest;
import com.aquaLex.ai.contract.dto.GenerationRequestResponse;
import com.aquaLex.ai.contract.dto.ReviseDraftRequest;
import com.aquaLex.ai.contract.dto.UpdateDraftRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/ai-contracts")
public class AiContractController {

    private final GenerationRequestService generationRequestService;
    private final ContractPdfExportService contractPdfExportService;
    private final PdfTextExtractionService pdfTextExtractionService;

    public AiContractController(
            GenerationRequestService generationRequestService,
            ContractPdfExportService contractPdfExportService,
            PdfTextExtractionService pdfTextExtractionService
    ) {
        this.generationRequestService = generationRequestService;
        this.contractPdfExportService = contractPdfExportService;
        this.pdfTextExtractionService = pdfTextExtractionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(
            summary = "Create an AI contract generation request",
            description = "Creates an asynchronous AI contract generation request from a text description."
    )
    @SecurityRequirement(name = "bearerAuth")
    public ApiResponse<GenerationRequestResponse> createGenerationRequest(
            @Valid @RequestBody CreateGenerationRequest request,
            Authentication authentication,
            HttpServletRequest httpRequest
    ) {
        GenerationRequestResponse response = generationRequestService.createRequest(
                request,
                authentication.getName()
        );

        return ApiResponse.success(
                "GENERATION_REQUEST_CREATED",
                "AI contract generation request created successfully",
                response,
                httpRequest.getRequestURI()
        );
    }

    @PostMapping("/{requestId}/analyze")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Analyze an AI contract generation request",
            description = "Analyzes the request description and stores extracted contract data."
    )
    @SecurityRequirement(name = "bearerAuth")
    public ApiResponse<GenerationRequestResponse> analyzeGenerationRequest(
            @PathVariable UUID requestId,
            Authentication authentication,
            HttpServletRequest httpRequest
    ) {
        GenerationRequestResponse response = generationRequestService.analyzeRequest(
                requestId,
                authentication.getName()
        );

        return ApiResponse.success(
                "GENERATION_REQUEST_ANALYZED",
                "AI contract generation request analyzed successfully",
                response,
                httpRequest.getRequestURI()
        );
    }

    @PostMapping(
            value = "/{requestId}/files",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Attach a PDF to an AI contract request",
            description = "Extracts text from a PDF and stores it for AI analysis."
    )
    @SecurityRequirement(name = "bearerAuth")
    public ApiResponse<GenerationRequestResponse> attachPdf(
            @PathVariable UUID requestId,
            @RequestPart("file") MultipartFile file,
            Authentication authentication,
            HttpServletRequest httpRequest
    ) {
        GenerationRequestResponse response =
                generationRequestService.attachPdf(
                        requestId,
                        authentication.getName(),
                        file
                );

        return ApiResponse.success(
                "SOURCE_PDF_ATTACHED",
                "Source PDF attached and text extracted successfully",
                response,
                httpRequest.getRequestURI()
        );
    }

    @GetMapping("/{requestId}")
    @ResponseStatus(HttpStatus.OK)
        @Operation(
                summary = "Get an AI contract generation request by ID",
                description = "Retrieves the details of an AI contract generation request by its ID."
        )
        @SecurityRequirement(name = "bearerAuth")
        public ApiResponse<GenerationRequestResponse> getGenerationRequestById(
                        @PathVariable UUID requestId,
                        Authentication authentication,
                        HttpServletRequest httpRequest
                ) {
                GenerationRequestResponse response = generationRequestService.getRequestById(
                        requestId,
                        authentication.getName()
                );
        
                return ApiResponse.success(
                        "GENERATION_REQUEST_RETRIEVED",
                        "AI contract generation request retrieved successfully",
                        response,
                        httpRequest.getRequestURI()
                );
                }
@PostMapping("/{requestId}/confirm")
@ResponseStatus(HttpStatus.OK)
@Operation(
        summary = "Confirm an AI contract generation request",
        description = "Confirms the analyzed data of an AI contract generation request."
)
@SecurityRequirement (name = "bearerAuth")
public ApiResponse<GenerationRequestResponse> confirmGenerationRequest(
        @PathVariable UUID requestId,
        @Valid @RequestBody ConfirmGenerationRequest confirmRequest,
        Authentication authentication,
        HttpServletRequest httpRequest
) {
    GenerationRequestResponse response = generationRequestService.confirmRequest(
            requestId,
            authentication.getName(),
            confirmRequest.isConfirmed(),
            confirmRequest.additionalData()
    );

    return ApiResponse.success(
            "GENERATION_REQUEST_CONFIRMED",
            "AI contract generation request confirmed successfully",
            response,
            httpRequest.getRequestURI()
    );
}
@PostMapping("/{requestId}/generate")
@ResponseStatus(HttpStatus.OK)
@Operation(
        summary = "Generate a contract draft from an AI contract generation request",
        description = "Generates a contract draft based on the confirmed data of an AI contract generation request."
)
@SecurityRequirement(name = "bearerAuth")
public ApiResponse<GenerationRequestResponse> generateContractDraft(
        @PathVariable UUID requestId,
        Authentication authentication,
        HttpServletRequest httpRequest
) {
    GenerationRequestResponse response = generationRequestService.generateDraft(
            requestId,
            authentication.getName() // đây là get email
    );

    return ApiResponse.success(
            "CONTRACT_DRAFT_GENERATED",
            "Contract draft generated successfully",
            response,
            httpRequest.getRequestURI()
    );
}

@PostMapping("/{requestId}/review")
@ResponseStatus(HttpStatus.OK)
@Operation(
        summary = "Review a generated contract draft",
        description = "Reviews the generated draft and stores AI-generated warnings."
)
@SecurityRequirement(name = "bearerAuth")
public ApiResponse<GenerationRequestResponse> reviewContractDraft(
        @PathVariable UUID requestId,
        Authentication authentication,
        HttpServletRequest httpRequest
) {
    GenerationRequestResponse response = generationRequestService.reviewDraft(
            requestId,
            authentication.getName()
    );

    return ApiResponse.success(
            "CONTRACT_DRAFT_REVIEWED",
            "Contract draft reviewed successfully",
            response,
            httpRequest.getRequestURI()
    );
}

@PatchMapping("/{requestId}/draft")
@ResponseStatus(HttpStatus.OK)
@Operation(
        summary = "Update a generated contract draft",
        description = "Updates the draft content and creates a new draft version."
)
@SecurityRequirement(name = "bearerAuth")
public ApiResponse<GenerationRequestResponse> updateContractDraft(
        @PathVariable UUID requestId,
        @Valid @RequestBody UpdateDraftRequest request,
        Authentication authentication,
        HttpServletRequest httpRequest
) {
    GenerationRequestResponse response = generationRequestService.updateDraft(
            requestId,
            authentication.getName(),
            request
    );

    return ApiResponse.success(
            "CONTRACT_DRAFT_UPDATED",
            "Contract draft updated successfully",
            response,
            httpRequest.getRequestURI()
    );
}

@PostMapping("/{requestId}/revise")
@ResponseStatus(HttpStatus.OK)
@Operation(
        summary = "Revise a contract draft with an instruction",
        description = "Revises the current contract draft and creates a new draft version."
)
@SecurityRequirement(name = "bearerAuth")
public ApiResponse<GenerationRequestResponse> reviseContractDraft(
        @PathVariable UUID requestId,
        @Valid @RequestBody ReviseDraftRequest request,
        Authentication authentication,
        HttpServletRequest httpRequest
) {
    GenerationRequestResponse response = generationRequestService.reviseDraft(
            requestId,
            authentication.getName(),
            request
    );

    return ApiResponse.success(
            "CONTRACT_DRAFT_REVISED",
            "Contract draft revised successfully",
            response,
            httpRequest.getRequestURI()
    );
}

@PostMapping("/{requestId}/finalize")
@ResponseStatus(HttpStatus.OK)
@Operation(
        summary = "Finalize a contract draft",
        description = "Finalizes a completed contract draft for export."
)
@SecurityRequirement(name = "bearerAuth")
public ApiResponse<GenerationRequestResponse> finalizeContractDraft(
        @PathVariable UUID requestId,
        Authentication authentication,
        HttpServletRequest httpRequest
) {
    GenerationRequestResponse response = generationRequestService.finalizeDraft(
            requestId,
            authentication.getName()
    );

    return ApiResponse.success(
            "CONTRACT_DRAFT_FINALIZED",
            "Contract draft finalized successfully",
            response,
            httpRequest.getRequestURI()
    );
}

@GetMapping("/{requestId}/export")
@ResponseStatus(HttpStatus.OK)
@Operation(
        summary = "Export a finalized contract as PDF",
        description = "Downloads the finalized contract draft as a PDF file."
)
@SecurityRequirement(name = "bearerAuth")
public ResponseEntity<byte[]> exportContractPdf(
        @PathVariable UUID requestId,
        @org.springframework.web.bind.annotation.RequestParam String format,
        Authentication authentication
) {
    if (!"pdf".equalsIgnoreCase(format)) {
        throw new IllegalArgumentException("Only PDF export is supported");
    }

    byte[] pdf = contractPdfExportService.export(
            requestId,
            authentication.getName()
    );

    return ResponseEntity.ok()
            .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"contract-" + requestId + ".pdf\""
            )
            .contentType(MediaType.APPLICATION_PDF)
            .contentLength(pdf.length)
            .body(pdf);
}
}
