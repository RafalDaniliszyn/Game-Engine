package org.game;

import org.game.isometric.GameState;
import java.util.UUID;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        for (String arg : args) {
            if (arg.startsWith("host:")) {
                String[] split = arg.split(":");
                GraphicsDisplay.host = split[1];
            }
            if (arg.startsWith("port:")) {
                String[] split = arg.split(":");
                GraphicsDisplay.port = Integer.parseInt(split[1]);
            }

            String[] split1 = arg.split("=");
            switch (split1[0]) {
                case "uuid" -> GameState.userUuid = UUID.fromString(split1[1]);
            }
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
