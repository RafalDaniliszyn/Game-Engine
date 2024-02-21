package org.game.settingsWindow;

import javax.swing.*;

public class Frame extends JFrame {

    public Frame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 300);
        Panel panel = new Panel();
        add(panel);
        setVisible(true);
    }
}
