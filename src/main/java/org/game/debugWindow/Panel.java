package org.game.debugWindow;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Panel extends JPanel {

    private static final List<String> descriptionList;
    static {
        descriptionList = new ArrayList<>();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < descriptionList.size(); i++) {
            g.drawString(descriptionList.get(i), 100, (i * 100) + 100);
            Image image = SelectedItem.image;
            if (image != null) {
                g.drawImage(image, 500, 50, null);
            }

        }
        repaint();
    }

    public static void addDescription(String... description) {
        StringBuilder builder = new StringBuilder();
        for (String s : description) {
            builder.append(s).append("\n");
        }
        descriptionList.add(builder.toString());
    }

    public static void clearDescription() {
        descriptionList.clear();
    }
}
