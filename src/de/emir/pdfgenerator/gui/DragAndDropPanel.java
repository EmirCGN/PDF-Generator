package de.emir.pdfgenerator.gui;

import de.emir.pdfgenerator.util.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

/**
 * The DragAndDropPanel class is a custom Swing component that extends JPanel.
 * It allows users to drag and drop files onto the panel, and the valid files are added to a provided list model.
 */
public class DragAndDropPanel extends JPanel {

    private DefaultListModel<String> listModel;

    public DragAndDropPanel(DefaultListModel<String> listModel) {
        this.listModel = listModel;

        setBorder(BorderFactory.createTitledBorder("Dateien hierher ziehen"));

        DropTarget dropTarget = new DropTarget(this, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> droppedFiles = (List<File>) dtde.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);

                    for (File file : droppedFiles) {
                        String filePath = file.getAbsolutePath();
                        if (FileUtils.isValidFileType(file)) {
                            listModel.addElement(filePath);
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        setDropTarget(dropTarget);

        setLayout(new BorderLayout());
        JLabel instructionLabel = new JLabel("Ziehen Sie die Dateien hierher, um sie zur Liste hinzuzufügen.", SwingConstants.CENTER);
        add(instructionLabel, BorderLayout.CENTER);
    }
}