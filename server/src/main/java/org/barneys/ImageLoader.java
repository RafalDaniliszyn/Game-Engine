package org.barneys;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class ImageLoader {
    public static Optional<BufferedImage> loadImage(String path) {
        File file = new File(path);
        try {
            BufferedImage img = ImageIO.read(file);
            if (img != null) {
                System.out.println("Wczytano obraz: " + file.getName());
                return Optional.of(img);
            } else {
                System.out.println("nie udało się wczytać: " + file.getName());
            }
        } catch (IOException e) {
            System.err.println("Błąd przy wczytywaniu pliku " + file.getName() + ": " + e.getMessage());
        }
        return Optional.empty();
    }
}
