package org.game.ui.component;

import org.game.GraphicsDisplay;
import org.game.component.Component;
import org.game.event.Event;
import org.game.mouse.MouseInput;
import org.game.mouse.Clickable;
import org.game.event.EventManager;

public class UiComponent extends Component implements Clickable {

    private double x;
    private double y;
    private double width;
    private double height;
    private Event event;
    private EventManager eventManager;

    public UiComponent(double x, double y, double width, double height, Event event, EventManager eventManager) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.event = event;
        this.eventManager = eventManager;
    }

    @Override
    public void update(double x, double y) {
        if (x > this.x && x < this.x + width && y < (GraphicsDisplay.HEIGHT - this.y) + height && y > GraphicsDisplay.HEIGHT - this.y
                && MouseInput.LEFT_CLICK) {
            eventManager.publishEvent(event);
        }
    }
}
