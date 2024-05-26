package org.game.isometric.component;

import org.game.component.Component;

/**
 * The StateChangedComponent2D class represents a component that marks an entity as having undergone a state change.
 * This component is used to flag entities whose state has been altered,
 * triggering necessary updates and processing by {@link org.game.isometric.system.StateChangedSystem2D}.
 */
public class StateChangedComponent2D extends Component {
    @Override
    public ComponentEnum getType() {
        return ComponentEnum.StateChangedComponent2D;
    }
}
