package org.game.ui.component;

public interface MouseCallback {
    void mouseButtonCallbackI(long window, int button, int action, int mods);
    void cursorPosCallbackI(long window, double xpos, double ypos);
    void windowSizeCallbackI(long window, int width, int height);

}
