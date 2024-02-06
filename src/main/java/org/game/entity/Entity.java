package org.game.entity;


import org.game.IdGenerator;
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
