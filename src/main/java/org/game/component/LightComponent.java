package org.game.component;

import org.game.isometric.component.ComponentEnum;
import org.joml.Vector3f;

public class LightComponent extends Component {
    private Vector3f lightColor;
    private boolean down = true;

    public LightComponent(Vector3f lightColor) {
        this.lightColor = lightColor;
    }

    public Vector3f getLightColor() {
        return lightColor;
    }

    public void setLightColor(Vector3f lightColor) {
        this.lightColor = lightColor;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    @Override
    public ComponentEnum getType() {
        return null;
    }
}
