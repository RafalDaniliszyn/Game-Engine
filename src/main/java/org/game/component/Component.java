package org.game.component;

import org.game.helper.IdGenerator;
import org.game.isometric.component.ComponentEnum;
import org.game.isometric.component.ComponentSource;

import static org.game.isometric.component.ComponentSource.CLIENT;

public abstract class Component {
    private long id;
    private final ComponentSource source;

    public Component() {
        this.id = IdGenerator.getNextId();
        this.source = CLIENT;
    }

    public Component(ComponentSource source) {
        this.id = IdGenerator.getNextId();
        this.source = source;
    }

    public ComponentEnum getType(){
        return null;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ComponentSource getSource() {
        return source;
    }

    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                "name=" + this.getClass().getName() +
                '}';
    }
}
