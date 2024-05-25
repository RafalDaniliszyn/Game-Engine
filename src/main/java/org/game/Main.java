package org.game;

import org.game.editWindow.Frame;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        if (Arrays.asList(args).contains("debugWindow=true")) {
            Frame frame = new Frame();
        }
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
