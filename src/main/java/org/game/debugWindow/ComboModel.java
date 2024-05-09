package org.game.debugWindow;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class ComboModel {
    String label;
    int id;
    String path;
    Image image;

    public ComboModel(String label, int id, String path) {
        this.label = label;
        this.id = id;
        this.path = path;
        try {
            this.image = ImageIO.read(new File(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return label;
    }
}
