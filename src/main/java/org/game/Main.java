package org.game;

public class Main {
    public static void main(String[] args) {
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
