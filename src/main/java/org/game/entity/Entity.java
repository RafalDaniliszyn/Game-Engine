package org.game.entity;

import org.game.helper.IdGenerator;
import org.game.component.Component;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.StateChangedComponent2D;
import org.game.system.shader.ShaderEnum;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    private final long id;
    private State state;
    private List<Component> componentList;
    private final List<ComponentEnum> componentEnumList;
    private EntityProperties properties;

    public Entity() {
        this.componentList = new ArrayList<>();
        this.id = IdGenerator.getNextId();
        this.state = State.NEW;
        this.properties = new EntityProperties(ShaderEnum.DEFAULT);
        this.componentEnumList = new ArrayList<>();
    }

    public Entity(EntityProperties properties) {
        this.componentList = new ArrayList<>();
        this.id = IdGenerator.getNextId();
        this.state = State.NEW;
        this.properties = properties;
        this.componentEnumList = new ArrayList<>();
    }

    public void addComponents(List<? extends Component> component) {
        componentList.addAll(component);
        component.forEach(comp -> {
            componentEnumList.add(comp.getType());
        });
        markStateChangedComponent();
    }

    public void addComponent(Component component) {
        componentList.add(component);
        componentEnumList.add(component.getType());
        markStateChangedComponent();
    }

    public <T extends Component> void changeComponent(Component newComponent, Class<T> toChange) {
        for (int i = 0; i < componentList.size(); i++) {
            if (toChange.isAssignableFrom(componentList.get(i).getClass())) {
                Component removed = componentList.remove(i);
                componentEnumList.remove(removed.getType());
                addComponent(newComponent);
                markStateChangedComponent();
                return;
            }
        }
    }

    public <T extends Component> void removeComponent(Class<T> toRemove) {
        for (int i = 0; i < componentList.size(); i++) {
            if (toRemove.isAssignableFrom(componentList.get(i).getClass())) {
                Component removed = componentList.remove(i);
                componentEnumList.remove(removed.getType());
                markStateChangedComponent();
                return;
            }
        }
    }

    /**
     * This method is to prevent infinite loops and StackOverflowException.
     * Note: it should not be used outside the StateChangedSystem2D class.
     */
    public void removeStateChangedComponent() {
        for (int i = 0; i < componentList.size(); i++) {
            if (StateChangedComponent2D.class.isAssignableFrom(componentList.get(i).getClass())) {
                componentList.remove(i);
                componentEnumList.remove(ComponentEnum.StateChangedComponent2D);
                return;
            }
        }
    }

    public <T extends Component> List<T> getComponents(Class<T> componentClass) {
        List<T> components = new ArrayList<>();
        for (Component component : componentList) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                components.add(componentClass.cast(component));
            }
        }
        return components;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : componentList) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                return componentClass.cast(component);
            }
        }
        return null;
    }

    private void markStateChangedComponent() {
        StateChangedComponent2D stateChangedComponent = new StateChangedComponent2D();
        this.componentList.add(stateChangedComponent);
        this.componentEnumList.add(ComponentEnum.StateChangedComponent2D);
    }

    public long getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<Component> getComponentList() {
        return componentList;
    }

    public void setComponentList(List<Component> componentList) {
        this.componentList = componentList;
    }

    public EntityProperties getProperties() {
        return properties;
    }

    public void setProperties(EntityProperties properties) {
        this.properties = properties;
    }

    public List<ComponentEnum> getComponentEnumList() {
        return componentEnumList;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", componentList=" + componentList +
                ", properties=" + properties +
                '}';
    }

    public enum State {
        NEW,
        ACTIVE,
        DESTROYED
    }
}
