package de.emir.pdfgenerator.util;

import java.io.File;
import java.util.Set;

public class FileUtils {

    private static final Set<String> VALID_FILE_EXTENSIONS = Set.of("pdf", "jpg", "jpeg", "png");

    public static boolean isValidFileType(File file) {
        String fileName = file.getName().toLowerCase();
        return VALID_FILE_EXTENSIONS.stream().anyMatch(fileName::endsWith);
    }
}