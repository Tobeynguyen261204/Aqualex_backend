package com.aquaLex.ai.contract.application;

import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PdfTextExtractionService {

    public String extractText(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("PDF file must not be empty");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null
                || !fileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are supported");
        }

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            String text = new PDFTextStripper().getText(document).trim();
            if (text.isBlank()) {
                throw new IllegalArgumentException(
                        "The PDF does not contain extractable text. "
                                + "Scanned PDFs require OCR support."
                );
            }
            return text;
        } catch (IOException exception) {
            throw new IllegalArgumentException(
                    "Could not read the PDF file",
                    exception
            );
        }
    }
}
