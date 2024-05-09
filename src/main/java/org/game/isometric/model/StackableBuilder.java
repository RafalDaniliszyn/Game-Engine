package org.game.isometric.model;

public class StackableBuilder {
    public static Stackable getStackable(String label) {
        return switch (label) {
            case "blueCoin" -> new BlueCoin();
            case "goldCoin" -> new GoldCoin();
            default -> null;
        };

    }
}
