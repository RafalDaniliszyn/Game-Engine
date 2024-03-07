package org.game.entity;


import org.game.helper.IdGenerator;
import org.game.component.Component;

import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
    private final long id;
    private List<Component> componentList;


    public Entity() {
        this.componentList = new ArrayList<>();
        this.id = IdGenerator.getNextId();
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
}
