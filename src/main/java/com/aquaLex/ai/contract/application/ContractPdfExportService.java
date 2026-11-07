package com.aquaLex.ai.contract.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.stereotype.Service;

import com.aquaLex.ai.contract.domain.GenerationRequest;
import com.aquaLex.ai.contract.domain.GenerationRequestStatus;
import com.aquaLex.ai.contract.infrastructure.GenerationRequestRepository;
import com.aquaLex.common.exception.GenerationRequestNotFoundException;

@Service
public class ContractPdfExportService {

    private static final float MARGIN = 50;
    private static final float FONT_SIZE = 13;
    private static final float LINE_HEIGHT = 19;
    private static final float MAX_LINE_WIDTH = 495;
    private static final String DEFAULT_FONT_PATH =
            "C:\\Windows\\Fonts\\times.ttf";
    private static final String FONT_PATH_ENV =
            "AQUALEX_TIMES_NEW_ROMAN_FONT";

    private final GenerationRequestRepository generationRequestRepository;

    public ContractPdfExportService(
            GenerationRequestRepository generationRequestRepository
    ) {
        this.generationRequestRepository = generationRequestRepository;
    }

    public byte[] export(
            UUID requestId,
            String userEmail
    ) {
        GenerationRequest request =
                generationRequestRepository.findByIdAndUser_Email(
                        requestId,
                        userEmail
                ).orElseThrow(() -> new GenerationRequestNotFoundException(
                        "Generation request not found"
                ));

        if (request.getStatus() != GenerationRequestStatus.FINALIZED
                || request.getDraftContent() == null
                || request.getDraftContent().isBlank()) {
            throw new IllegalStateException(
                    "Only a finalized contract draft can be exported"
            );
        }

        try {
            return createPdf(request.getDraftContent());
        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not generate contract PDF",
                    exception
            );
        }
    }

    private byte[] createPdf(String draftContent) throws IOException {
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDFont font = loadTimesNewRoman(document);
            float y = page.getMediaBox().getHeight() - MARGIN;
            PDPageContentStream contentStream =
                    new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(font, FONT_SIZE);
            contentStream.newLineAtOffset(MARGIN, y);

            for (String line : wrapLines(draftContent, font)) {
                if (y < MARGIN) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage(PDRectangle.A4);
                    document.addPage(page);
                    y = page.getMediaBox().getHeight() - MARGIN;
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.beginText();
                    contentStream.setFont(font, FONT_SIZE);
                    contentStream.newLineAtOffset(MARGIN, y);
                }

                contentStream.showText(line);
                contentStream.newLineAtOffset(0, -LINE_HEIGHT);
                y -= LINE_HEIGHT;
            }

            contentStream.endText();
            contentStream.close();
            document.save(output);
            return output.toByteArray();
        }
    }

    private List<String> wrapLines(String text, PDFont font)
            throws IOException {
        List<String> result = new ArrayList<>();

        for (String originalLine : text.split("\\R", -1)) {
            String remaining = originalLine;
            while (font.getStringWidth(remaining) * FONT_SIZE / 1000
                    > MAX_LINE_WIDTH) {
                int splitAt = remaining.length() - 1;
                while (splitAt > 0) {
                    String candidate = remaining.substring(0, splitAt);
                    if (candidate.endsWith(" ")
                            && font.getStringWidth(candidate) * FONT_SIZE / 1000
                            <= MAX_LINE_WIDTH) {
                        break;
                    }
                    splitAt--;
                }

                if (splitAt == 0) {
                    splitAt = remaining.length();
                }

                result.add(remaining.substring(0, splitAt).trim());
                remaining = remaining.substring(splitAt).trim();
            }
            result.add(remaining);
        }

        return result;
    }

    private PDFont loadTimesNewRoman(PDDocument document) throws IOException {
        String configuredPath = System.getenv(FONT_PATH_ENV);
        Path fontPath = Path.of(
                configuredPath == null || configuredPath.isBlank()
                        ? DEFAULT_FONT_PATH
                        : configuredPath
        );

        if (!Files.isRegularFile(fontPath)) {
            throw new IOException(
                    "Times New Roman font was not found at " + fontPath
                            + ". Set " + FONT_PATH_ENV
                            + " to the path of times.ttf."
            );
        }

        try (InputStream fontStream = Files.newInputStream(fontPath)) {
            return PDType0Font.load(document, fontStream);
        }
    }
}
