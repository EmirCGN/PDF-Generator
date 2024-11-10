package de.emir.pdfgenerator.pdf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * PDFGeneratorWorker is a SwingWorker that handles the generation of a PDF file from a list of file paths.
 * It provides progress feedback through a JProgressBar and shows a completion message upon success or failure.
 */
public class PDFGeneratorWorker extends SwingWorker<Void, Void> {

    private static final Logger logger = LoggerFactory.getLogger(PDFGeneratorWorker.class);
    private List<String> filePaths;
    private File outputFile;
    private JProgressBar progressBar;

    public PDFGeneratorWorker(List<String> filePaths, File outputFile, JProgressBar progressBar) {
        this.filePaths = filePaths;
        this.outputFile = outputFile;
        this.progressBar = progressBar;
    }

    @Override
    protected Void doInBackground() {
        progressBar.setIndeterminate(true);
        try {
            PDFGenerator.mergePDFs(filePaths, outputFile.getAbsolutePath());
        } catch (IOException e) {
            String errorMessage = "Fehler beim Erstellen der PDF-Datei: " + e.getMessage();
            logger.error(errorMessage, e);
            JOptionPane.showMessageDialog(null, errorMessage, "Fehler", JOptionPane.ERROR_MESSAGE);
        } finally {
            progressBar.setIndeterminate(false);
        }
        return null;
    }

    @Override
    protected void done() {
        JOptionPane.showMessageDialog(null,
                "PDF erfolgreich erstellt",
                "Information", JOptionPane.INFORMATION_MESSAGE);
    }
}