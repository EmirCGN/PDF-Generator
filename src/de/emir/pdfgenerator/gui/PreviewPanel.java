package de.emir.pdfgenerator.gui;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The PreviewPanel class is a custom Swing component that extends JPanel.
 * It is used to display a preview of PDF files by rendering images of their pages.
 * The preview images are cached in memory to improve performance on subsequent accesses.
 */
public class PreviewPanel extends JPanel {

    private JPanel previewContainer;
    private Map<String, BufferedImage[]> imageCache = new HashMap<>();

    public PreviewPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Vorschau"));

        previewContainer = new JPanel();
        previewContainer.setLayout(new BoxLayout(previewContainer, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(previewContainer);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void showPreview(String filePath) {
        previewContainer.removeAll(); // Clear existing previews

        if (imageCache.containsKey(filePath)) {
            for (BufferedImage image : imageCache.get(filePath)) {
                JLabel label = new JLabel(new ImageIcon(image));
                label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                previewContainer.add(label);
            }
        } else {
            File file = new File(filePath);
            if (file.exists() && file.isFile() && filePath.toLowerCase().endsWith(".pdf")) {
                try (PDDocument document = PDDocument.load(file)) {
                    PDFRenderer renderer = new PDFRenderer(document);
                    BufferedImage[] images = new BufferedImage[Math.min(2, document.getNumberOfPages())];
                    for (int i = 0; i < images.length; i++) {
                        images[i] = renderer.renderImageWithDPI(i, 100); // Render pages with 100 DPI
                        JLabel label = new JLabel(new ImageIcon(images[i]));
                        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                        previewContainer.add(label);
                    }
                    imageCache.put(filePath, images);
                } catch (IOException e) {
                    e.printStackTrace();
                    previewContainer.removeAll();
                    previewContainer.add(new JLabel("Fehler beim Anzeigen der Vorschau"));
                }
            } else {
                previewContainer.removeAll();
                previewContainer.add(new JLabel("Keine Vorschau verfügbar"));
            }
        }
        previewContainer.revalidate();
        previewContainer.repaint();
    }
}