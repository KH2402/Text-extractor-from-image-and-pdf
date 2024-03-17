package com.textextractor.TextExtractor.controller;

import com.textextractor.TextExtractor.service.ImageTextExtractor;
import com.textextractor.TextExtractor.service.PdfTextExtractorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api/extract")
public class TextExtractorController {
    @Autowired
    private ImageTextExtractor imageTextExtractor;

    @Autowired
    private PdfTextExtractorService pdfTextExtractorService;


    @PostMapping("/image")
    public ResponseEntity<String> extractTextFromImage(@RequestBody MultipartFile file) {
        try {
            // Check if the uploaded file is not empty
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image file is empty");
            }

            // Extract text from the image file
            byte[] imageData = file.getBytes();
            String extractedText = imageTextExtractor.extractTextFromImage(imageData);

            // Check if text extraction was successful
            if (extractedText != null && !extractedText.isEmpty()) {
                return ResponseEntity.ok(extractedText);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to extract text from the image");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the image file");
        }
    }

    @PostMapping("/pdf")
    public ResponseEntity<List<String>> extractTextFromPDF(@RequestBody MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        try {
            InputStream inputStream = file.getInputStream();
            List<String> extractedText = pdfTextExtractorService.extractText(inputStream);
            return ResponseEntity.ok().body(extractedText);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

