package org.game.ui.component;

import org.game.component.Component;
import org.game.event.Event;
import org.game.mouse.MouseInput;
import org.game.mouse.Clickable;
import org.game.event.EventManager;
import org.game.mouse.MouseState;
import org.joml.Vector2f;
import org.joml.Vector3f;

import static org.game.helper.PositionHelper.getClipCoordinates;
import static org.game.helper.PositionHelper.getScreenCoordinates;

public class UiComponent extends Component implements Clickable {

    private double x;
    private double y;
    private final double width;
    private final double height;
    private final Event event;
    private final EventManager eventManager;
    private RawUiModel rawUiModel;
    private boolean activeDrag;

    public UiComponent(double x, double y, double width, double height, Event event, EventManager eventManager, RawUiModel rawUiModel) {
        Vector2f screenCoordinates = getScreenCoordinates(x, y, height, width);
        this.x = screenCoordinates.x;
        this.y = screenCoordinates.y;
        this.width = width;
        this.height = height;
        this.event = event;
        this.eventManager = eventManager;
        this.rawUiModel = rawUiModel;
        this.activeDrag = false;
    }



    @Override
    public void update(double x, double y) {
        if (activeDrag) {

            Vector2f setMouseCoordinatesToModel = getClipCoordinates(x, y + (this.height/2));
            this.rawUiModel.getPosition().x = setMouseCoordinatesToModel.x;
            this.rawUiModel.getPosition().y = setMouseCoordinatesToModel.y;

            Vector3f modelPosition = this.rawUiModel.getPosition();
            Vector2f updateComponentCoordinates = getScreenCoordinates(modelPosition.x, modelPosition.y, this.height, this.width);
            this.x = updateComponentCoordinates.x;
            this.y = updateComponentCoordinates.y;
        }

        if (overlap(x, y, this.x, this.y, width, 25.0) && MouseInput.LEFT_CLICK && !MouseState.drag.isInUse()) {
            this.activeDrag = true;
            MouseState.drag.setInUse(true);
            if (!eventManager.hasEvent(event)) {
                eventManager.publishEvent(event);
            }
        }
        if (MouseInput.RELEASE) {
            this.activeDrag = false;
            MouseState.drag.setInUse(false);
        }
    }

    private boolean overlap(double x1, double y1, double x2, double y2, double width2, double height2) {
       return x1 > x2 && x1 < x2 + width2 && y1 > y2 && y1 < y2 + height2;
    }
}
