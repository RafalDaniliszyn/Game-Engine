package org.game.settingsWindow;

import javax.swing.*;
import java.awt.*;

public class Panel extends JPanel {

    public static float ROTATION;

    static{
        ROTATION = 0.0f;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Font font = g.getFont();
        g.setFont(new Font(font.getFontName(), font.getStyle(), 25));
        g.drawString("Player rotation Y: " + ROTATION, 100, 100);
        repaint();
    }
}
