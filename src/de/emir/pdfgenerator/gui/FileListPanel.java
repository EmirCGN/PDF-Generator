package de.emir.pdfgenerator.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The FileListPanel class is a custom Swing component that extends JPanel.
 * It displays a list of files and provides functionality to preview selected files
 * using a linked PreviewPanel.
 */
public class FileListPanel extends JPanel {

    private JList<String> fileList;
    private PreviewPanel previewPanel;

    public FileListPanel(DefaultListModel<String> listModel, PreviewPanel previewPanel) {
        this.previewPanel = previewPanel;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Dateiliste"));

        fileList = new JList<>(listModel);
        fileList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedFile = fileList.getSelectedValue();
                if (selectedFile != null) {
                    previewPanel.showPreview(selectedFile);
                }
            }
        });

        add(new JScrollPane(fileList), BorderLayout.CENTER);
    }

    public java.util.List<String> getFiles() {
        List<String> files = new ArrayList<>();
        for (int i = 0; i < fileList.getModel().getSize(); i++) {
            files.add(fileList.getModel().getElementAt(i));
        }
        return files;
    }

    public JList<String> getFileList() {
        return fileList;
    }
}