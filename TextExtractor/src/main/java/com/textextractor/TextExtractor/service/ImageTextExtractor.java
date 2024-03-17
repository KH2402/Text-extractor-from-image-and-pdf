package com.textextractor.TextExtractor.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ImageTextExtractor {

    @Autowired
    private ITesseract tesseract;

    public String extractTextFromImage(byte[] imageData) {
        try {
            // Convert byte array to InputStream
            InputStream inputStream = new ByteArrayInputStream(imageData);
            // Extract text from the InputStream using Tesseract OCR
            return tesseract.doOCR(ImageIO.read(inputStream));
        } catch (IOException | TesseractException e) {
            e.printStackTrace();
            return null;
        }

    }
}
