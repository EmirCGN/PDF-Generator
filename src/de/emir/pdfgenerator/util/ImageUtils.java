package de.emir.pdfgenerator.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    // Beispielmethode zum Laden eines Bildes
    public static BufferedImage loadImage(File file) throws IOException {
        return ImageIO.read(file);
    }
}