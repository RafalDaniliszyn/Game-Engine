package org.game.entity;

import org.game.isometric.blockLoader.Side;
import org.game.isometric.action.Action;
import org.game.isometric.model.Stackable;
import org.game.system.shader.ShaderEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityProperties {

    private ShaderEnum shaderType;

    private boolean collidable;
    private boolean draggable;
    private boolean stackable;
    private String label;
    private Integer quantity;
    private Stackable stack;
    private String type;
    private float depth;
    private boolean replaceableEdges;
    private Map<Side, Integer> replaceableTextureIdMap;
    private List<Action> actionListToDo;
    private List<Action> actionList;

    public EntityProperties() {
        this.shaderType = ShaderEnum.DEFAULT;
        this.actionListToDo = new ArrayList<>();
        this.actionList = new ArrayList<>();
    }

    public EntityProperties(boolean collidable, boolean draggable, boolean stackable, String label,
                            Integer quantity, Stackable stack, boolean replaceableEdges, Map<Side, Integer> replaceableTextureIdMap) {
        this.collidable = collidable;
        this.draggable = draggable;
        this.stackable = stackable;
        this.label = label;
        this.quantity = quantity;
        this.stack = stack;
        this.replaceableEdges = replaceableEdges;
        this.replaceableTextureIdMap = replaceableTextureIdMap;
        this.actionListToDo = new ArrayList<>();
        this.actionList = new ArrayList<>();
    }

    public EntityProperties(ShaderEnum shaderType) {
        this.shaderType = shaderType;
    }

    public ShaderEnum getShaderType() {
        return shaderType;
    }

    public void setShaderType(ShaderEnum shaderType) {
        this.shaderType = shaderType;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Stackable getStack() {
        return stack;
    }

    public void setStack(Stackable stack) {
        this.stack = stack;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public boolean hasReplaceableEdges() {
        return replaceableEdges;
    }

    public void setReplaceableEdges(boolean replaceableEdges) {
        this.replaceableEdges = replaceableEdges;
    }

    public Map<Side, Integer> getReplaceableTextureIdMap() {
        return replaceableTextureIdMap;
    }

    public void setReplaceableTextureIdMap(Map<Side, Integer> replaceableTextureIdMap) {
        this.replaceableTextureIdMap = replaceableTextureIdMap;
    }

    public void setActionComponentList(List<Action> actionList) {
        this.actionListToDo = actionList;
    }

    public List<Action> getActionListToDo() {
        return actionListToDo;
    }
    public void setActionListToDo(List<Action> actionListToDo) {
        this.actionListToDo = actionListToDo;
    }

    public List<Action> getActionList() {
        return actionList;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }

    @Override
    public String toString() {
        return "EntityProperties{" +
                "shaderType=" + shaderType +
                ", collidable=" + collidable +
                ", draggable=" + draggable +
                ", stackable=" + stackable +
                ", label='" + label + '\'' +
                ", quantity=" + quantity +
                ", stack=" + stack +
                '}';
    }

    public static class EntityPropertiesBuilder {

        private final EntityProperties entityProperties = new EntityProperties();


        public EntityProperties build() {
            return entityProperties;
        }

        public EntityPropertiesBuilder setCollidable(boolean collidable) {
            entityProperties.setCollidable(collidable);
            return this;
        }

        public EntityPropertiesBuilder setDraggable(boolean draggable) {
            this.entityProperties.setDraggable(draggable);
            return this;
        }

        public EntityPropertiesBuilder setLabel(String label) {
            this.entityProperties.setLabel(label);
            return this;
        }

        public EntityPropertiesBuilder setStackable(boolean stackable) {
            this.entityProperties.setStackable(stackable);
            return this;
        }

        public EntityPropertiesBuilder setQuantity(Integer quantity) {
            this.entityProperties.setQuantity(quantity);
            return this;
        }

        public EntityPropertiesBuilder setStack(Stackable stack) {
            this.entityProperties.setStack(stack);
            return this;
        }

        public EntityPropertiesBuilder setType(String type) {
            this.entityProperties.setType(type);
            return this;
        }

        public EntityPropertiesBuilder setDepth(float depth) {
            this.entityProperties.setDepth(depth);
            return this;
        }

        public EntityPropertiesBuilder setReplaceableEdges(boolean replaceableEdges) {
            this.entityProperties.setReplaceableEdges(replaceableEdges);
            return this;
        }

        public EntityPropertiesBuilder setReplaceableTextureIdMap(Map<Side, Integer> replaceableTextureIdMap) {
            this.entityProperties.setReplaceableTextureIdMap(replaceableTextureIdMap);
            return this;
        }

        public EntityPropertiesBuilder setActionList(List<Action> actionList) {
            if (actionList == null) {
                this.setActionList(new ArrayList<>());
                return this;
            }
            this.entityProperties.setActionList(actionList);
            return this;
        }

        public EntityPropertiesBuilder setActionListToDo(List<Action> actionListToDo) {
            if (actionListToDo == null) {
                this.entityProperties.setActionListToDo(new ArrayList<>());
                return this;
            }
            this.entityProperties.setActionListToDo(actionListToDo);
            return this;
        }
    }
}
