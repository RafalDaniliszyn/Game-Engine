package org.game.isometric.blockLoader;

import org.game.isometric.model.Stackable;
import java.util.List;
import java.util.Map;

public class EntityDto {
    private String type;
    private String textureLabel;
    private String texturePath;
    private float depth;
    private boolean stackable;
    private String label;
    private Integer quantity;
    private Stackable stack;
    private boolean replaceableEdges;
    private Map<Side, String> replaceableTexture;
    private List<String> components;


    public EntityDto() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTextureLabel() {
        return textureLabel;
    }

    public void setTextureLabel(String textureLabel) {
        this.textureLabel = textureLabel;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTexturePath(String texturePath) {
        this.texturePath = texturePath;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public boolean isStackable() {
        return stackable;
    }

    public void setStackable(boolean stackable) {
        this.stackable = stackable;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public boolean hasReplaceableEdges() {
        return replaceableEdges;
    }

    public void setReplaceableEdges(boolean replaceableEdges) {
        this.replaceableEdges = replaceableEdges;
    }


    public Map<Side, String> getReplaceableTexture() {
        return replaceableTexture;
    }

    public void setReplaceableTexture(Map<Side, String> replaceableTexture) {
        this.replaceableTexture = replaceableTexture;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    @Override
    public String toString() {
        return "EntityDto{" +
                "type='" + type + '\'' +
                ", textureLabel='" + textureLabel + '\'' +
                ", texturePath='" + texturePath + '\'' +
                ", depth=" + depth +
                ", stackable=" + stackable +
                ", label='" + label + '\'' +
                ", quantity=" + quantity +
                ", stack=" + stack +
                ", replaceableEdges=" + replaceableEdges +
                ", components=" + components +
                '}';
    }
}
