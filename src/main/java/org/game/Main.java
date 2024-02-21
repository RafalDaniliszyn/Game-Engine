package org.game;

import org.game.settingsWindow.Frame;

public class Main {
    public static void main(String[] args) {
        //Frame frame = new Frame();
        Runnable window = () -> {
            try {
                GraphicsDisplay.get().createDisplay();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        new Thread(window).start();
    }

}
