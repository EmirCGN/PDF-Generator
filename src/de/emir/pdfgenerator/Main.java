package de.emir.pdfgenerator;

import de.emir.pdfgenerator.gui.DragAndDropPanel;
import de.emir.pdfgenerator.gui.FileListPanel;
import de.emir.pdfgenerator.gui.PreviewPanel;
import de.emir.pdfgenerator.pdf.PDFGeneratorWorker;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * The Main class is the entry point for the PDF Generator application.
 * It extends JFrame and is responsible for creating and managing the main user interface,
 * which includes drag-and-drop functionality for adding files, a file list, a preview panel,
 * and buttons for generating and deleting PDFs.
 */
public class Main extends JFrame {

    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JProgressBar progressBar = new JProgressBar();

    public Main() {
        setTitle("PDF Generator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;

        ImageIcon icon = new ImageIcon("src/resources/logo/img.png");
        setIconImage(icon.getImage());

        PreviewPanel previewPanel = new PreviewPanel();
        DragAndDropPanel dragAndDropPanel = new DragAndDropPanel(listModel);
        FileListPanel fileListPanel = new FileListPanel(listModel, previewPanel);

        add(dragAndDropPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0.3;
        gbc.weighty = 1;
        add(fileListPanel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        add(previewPanel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton generatePdfButton = new JButton("PDF erstellen");
        generatePdfButton.setEnabled(false);
        generatePdfButton.addActionListener(e -> generatePdf(fileListPanel));
        buttonPanel.add(generatePdfButton);

        listModel.addListDataListener(new javax.swing.event.ListDataListener() {
            @Override
            public void intervalAdded(javax.swing.event.ListDataEvent e) {
                updateGeneratePdfButton();
            }

            @Override
            public void intervalRemoved(javax.swing.event.ListDataEvent e) {
                updateGeneratePdfButton();
            }

            @Override
            public void contentsChanged(javax.swing.event.ListDataEvent e) {
                updateGeneratePdfButton();
            }

            private void updateGeneratePdfButton() {
                generatePdfButton.setEnabled(!listModel.isEmpty());
            }
        });

        JButton deleteButton = new JButton("Ausgewählte löschen");
        deleteButton.addActionListener(e -> deleteSelectedFiles(fileListPanel));
        buttonPanel.add(deleteButton);

        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        add(buttonPanel, gbc);

        progressBar.setVisible(false);
        gbc.gridy = 3;
        add(progressBar, gbc);
    }

    private void generatePdf(FileListPanel fileListPanel) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Speichern unter");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PDF Dateien", "pdf"));

        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File outputFile = fileChooser.getSelectedFile();
            if (!outputFile.getAbsolutePath().endsWith(".pdf")) {
                outputFile = new File(outputFile + ".pdf");
            }

            progressBar.setVisible(true);

            PDFGeneratorWorker pdfGeneratorWorker = new PDFGeneratorWorker(fileListPanel.getFiles(), outputFile, progressBar);
            pdfGeneratorWorker.execute();
        }
    }

    private void deleteSelectedFiles(FileListPanel fileListPanel) {
        int[] selectedIndices = fileListPanel.getFileList().getSelectedIndices();
        if (selectedIndices == null || selectedIndices.length == 0) {
            JOptionPane.showMessageDialog(null, "Keine Dateien ausgewählt", "Information", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        for (int i = selectedIndices.length - 1; i >= 0; i--) {
            listModel.remove(selectedIndices[i]);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}