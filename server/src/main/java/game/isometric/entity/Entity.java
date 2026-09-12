package game.isometric.entity;

import game.isometric.component.Component;
import game.isometric.helper.IdGenerator;
import game.isometric.component.ComponentEnum;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    private final long id;
    private State state;
    private List<Component> componentList;
    private final List<ComponentEnum> componentEnumList;
    private EntityProperties properties;

    public Entity() {
        this.id = IdGenerator.getNextId();
        this.componentList = new ArrayList<>();
        this.state = State.NEW;
        this.componentEnumList = new ArrayList<>();
    }

    public Entity(EntityProperties properties) {
        this.id = IdGenerator.getNextId();
        this.componentList = new ArrayList<>();
        this.state = State.NEW;
        this.properties = properties;
        this.componentEnumList = new ArrayList<>();
    }

    public void addComponent(Component component) {
        componentList.add(component);
        componentEnumList.add(component.getType());
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : componentList) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                return componentClass.cast(component);
            }
        }
        return null;
    }

    public long getId() {
        return id;
    }

    public void setState(State state) {
        this.state = state;
    }
    public EntityProperties getProperties() {
        return properties;
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
