package org.game.component;

import org.game.helper.IdGenerator;

public abstract class Component {
    private long id;

    public Component() {
        this.id = IdGenerator.getNextId();
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Component{" +
                "id=" + id +
                "name=" + this.getClass().getName() +
                '}';
    }
}
