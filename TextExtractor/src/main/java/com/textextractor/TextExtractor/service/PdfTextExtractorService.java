package com.textextractor.TextExtractor.service;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class PdfTextExtractorService {


    @Autowired
    private ITesseract tesseract;

    public List<String> extractText(InputStream inputStream) throws IOException {
        List<String> extractedText = new ArrayList<>();

        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFRenderer renderer = new PDFRenderer(document);

            // Convert each page of the PDF to an image and extract text using Tesseract
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImage(i);

                // Convert BufferedImage to InputStream
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(image, "png", os);
                InputStream imageStream = new ByteArrayInputStream(os.toByteArray());

                // Extract text from the image
                extractedText.add(extractTextFromImage(imageStream));
            }
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }

        return extractedText;
    }

    private String extractTextFromImage(InputStream imageStream) throws IOException, TesseractException {
        String text = tesseract.doOCR(ImageIO.read(imageStream));
        return text;
    }
}



