package org.game.component;

import org.game.settingsWindow.Clickable;

public class ButtonComponent extends Component {
    private boolean clicked;
    private Clickable clickable;

    public ButtonComponent(Clickable clickable) {
        this.clicked = false;
        this.clickable = clickable;
    }

    public Clickable getClickable() {
        return clickable;
    }

    public void setClickable(Clickable clickable) {
        this.clickable = clickable;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }
}
