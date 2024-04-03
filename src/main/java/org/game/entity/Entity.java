package org.game.entity;

import org.game.helper.IdGenerator;
import org.game.component.Component;
import org.game.system.shader.ShaderEnum;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    private final long id;
    private List<Component> componentList;
    private EntityProperties properties;

    public Entity() {
        this.componentList = new ArrayList<>();
        this.id = IdGenerator.getNextId();
        this.properties = new EntityProperties(ShaderEnum.DEFAULT);
    }

    public Entity(EntityProperties properties) {
        this.componentList = new ArrayList<>();
        this.id = IdGenerator.getNextId();
        this.properties = properties;
    }

    public void addComponent(List<? extends Component> component) {
        componentList.addAll(component);
    }

    public void addComponent(Component component) {
        componentList.add(component);
    }

    public <T extends Component> void changeComponent(Component newComponent, Class<T> toChange) {
        for (int i = 0; i < componentList.size(); i++) {
            if (toChange.isAssignableFrom(componentList.get(i).getClass())) {
                componentList.remove(i);
                addComponent(newComponent);
                return;
            }
        }
    }

    public <T extends Component> void removeComponent(Class<T> toRemove) {
        for (int i = 0; i < componentList.size(); i++) {
            if (toRemove.isAssignableFrom(componentList.get(i).getClass())) {
                componentList.remove(i);
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

    public long getId() {
        return id;
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
}
