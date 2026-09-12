package org.game.entity;

import org.game.helper.IdGenerator;
import org.game.component.Component;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.StateChangedComponent2D;
import org.game.system.shader.ShaderEnum;

import java.util.*;

public abstract class Entity {
    private long id;
    private State state;
    private final List<Component> componentList;
    private final Set<ComponentEnum> componentEnumSet;
    private EntityProperties properties;
    private EntityType entityType;
    private Map<State, Long> entityByState;

    public Entity() {
        this.componentList = Collections.synchronizedList(new ArrayList<>());
        //this.id = IdGenerator.getNextId();
        this.id = -1;
        this.state = State.NEW;
        this.properties = new EntityProperties(ShaderEnum.DEFAULT);
        this.componentEnumSet = new HashSet<>();
        this.entityByState = new HashMap<>();
    }

    public Entity(EntityProperties properties, EntityType entityType) {
        this.componentList = Collections.synchronizedList(new ArrayList<>());
        //this.id = IdGenerator.getNextId();
        this.id = -1;
        this.state = State.NEW;
        this.properties = properties;
        this.componentEnumSet = new HashSet<>();
        this.entityType = entityType;
        this.entityByState = new HashMap<>();
    }

    public Entity(long sessionEntityId, EntityProperties properties, EntityType entityType) {
        this.componentList = Collections.synchronizedList(new ArrayList<>());
        this.id = sessionEntityId;
        this.state = State.NEW;
        this.properties = properties;
        this.componentEnumSet = new HashSet<>();
        this.entityType = entityType;
        this.entityByState = new HashMap<>();
    }

    public void addComponents(List<? extends Component> component) {
        componentList.addAll(component);
        component.forEach(comp -> {
            componentEnumSet.add(comp.getType());
        });
        markStateChangedComponent();
    }

    public void addComponent(Component component) {
        componentList.add(component);
        componentEnumSet.add(component.getType());
        markStateChangedComponent();
    }

    public <T extends Component> void removeComponent(Class<T> toRemove) {
        synchronized (componentList) {
            for (int i = 0; i < componentList.size(); i++) {
                if (toRemove.isAssignableFrom(componentList.get(i).getClass())) {
                    Component removed = componentList.remove(i);
                    componentEnumSet.remove(removed.getType());
                    markStateChangedComponent();
                    return;
                }
            }
        }
    }

    public <T extends Component> void removeComponents(Class<T> toRemove) {
        synchronized (componentList) {
            List<Component> removed = new ArrayList<>();
            for (int i = 0; i < componentList.size(); i++) {
                if (toRemove.isAssignableFrom(componentList.get(i).getClass())) {
                    Component removedComponent = componentList.remove(i);
                    if (removedComponent != null) {
                        removed.add(removedComponent);
                    }
                }
            }
            if (removed.size() != 0) {
                componentEnumSet.remove(removed.get(0).getType());
                markStateChangedComponent();
            }
        }
    }

    /**
     * This method is to prevent infinite loops and StackOverflowException.
     * Note: it should not be used outside the StateChangedSystem2D class.
     */
    public synchronized void removeStateChangedComponent() {
        synchronized (componentList) {
            for (Iterator<Component> iterator = componentList.iterator(); iterator.hasNext(); ) {
                Component component = iterator.next();
                if (StateChangedComponent2D.class.isAssignableFrom(component.getClass())) {
                    iterator.remove();
                    componentEnumSet.remove(ComponentEnum.StateChangedComponent2D);
                }
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

    public void addToEntityByState(long id, State state) {
        this.entityByState.put(state, id);
    }

    public Long getEntityIdByState(State state) {
        if (!entityByState.containsKey(state)) {
            return null;
        }
        return entityByState.get(state);
    }

    public Map<State, Long> getEntityByState() {
        return entityByState;
    }

    public void setEntityByState(Map<State, Long> entityByState) {
        this.entityByState = entityByState;
    }

    private void markStateChangedComponent() {
        StateChangedComponent2D stateChangedComponent = new StateChangedComponent2D();
        this.componentList.add(stateChangedComponent);
        this.componentEnumSet.add(ComponentEnum.StateChangedComponent2D);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    public EntityProperties getProperties() {
        return properties;
    }

    public void setProperties(EntityProperties properties) {
        this.properties = properties;
    }

    public Set<ComponentEnum> getComponentEnumSet() {
        return componentEnumSet;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }


    @Override
    public String toString() {
        return "Entity{" +
                "id=" + id +
                ", state=" + state +
                ", componentList=" + componentList +
                ", componentEnumList=" + componentEnumSet +
                ", properties=" + properties +
                ", entityType=" + entityType +
                '}';
    }

    public enum State {
        NEW,
        ACTIVE,
        DESTROYED,
        REPLACED_EDGE
    }
}
