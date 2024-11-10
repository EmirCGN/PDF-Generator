package de.emir.pdfgenerator.pdf;

import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.List;

/**
 * Utility class for generating PDF documents from various sources.
 */
public class PDFGenerator {

    public static void mergePDFs(List<String> filePaths, String outputFilePath) throws IOException {
        PDFMergerUtility pdfMerger = new PDFMergerUtility();
        pdfMerger.setDestinationFileName(outputFilePath);

        for (String filePath : filePaths) {
            pdfMerger.addSource(new File(filePath));
        }

        pdfMerger.mergeDocuments(null);
    }

    public static void convertImagesToPDF(List<String> filePaths, String outputFilePath) throws IOException {
        PDDocument document = new PDDocument();

        try {
            for (String filePath : filePaths) {
                File imageFile = new File(filePath);
                BufferedImage bufferedImage = ImageIO.read(imageFile);

                if (bufferedImage == null) {
                    throw new IOException("Datei " + filePath + " ist kein unterstütztes Bildformat.");
                }

                PDPage page = new PDPage();
                document.addPage(page);

                PDImageXObject pdImage = PDImageXObject.createFromFileByContent(imageFile, document);
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                // Bildgröße an die Seitengröße anpassen
                float scale = Math.min(page.getMediaBox().getWidth() / pdImage.getWidth(),
                        page.getMediaBox().getHeight() / pdImage.getHeight());

                float width = pdImage.getWidth() * scale;
                float height = pdImage.getHeight() * scale;
                float xPosition = (page.getMediaBox().getWidth() - width) / 2;
                float yPosition = (page.getMediaBox().getHeight() - height) / 2;

                contentStream.drawImage(pdImage, xPosition, yPosition, width, height);
                contentStream.close();
            }

            document.save(outputFilePath);
        } finally {
            document.close();
        }
    }
}