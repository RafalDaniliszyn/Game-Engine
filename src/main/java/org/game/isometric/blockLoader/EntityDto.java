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
    private String afterDestroyTexturePath;
    private String afterDestroyLabel;
    private boolean isDestroyable;
    private double destructionDifficulty;
    private Map<String, Integer> drop;


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

    public String getAfterDestroyTexturePath() {
        return afterDestroyTexturePath;
    }

    public void setAfterDestroyTexturePath(String afterDestroyTexturePath) {
        this.afterDestroyTexturePath = afterDestroyTexturePath;
    }

    public String getAfterDestroyLabel() {
        return afterDestroyLabel;
    }

    public void setAfterDestroyLabel(String afterDestroyLabel) {
        this.afterDestroyLabel = afterDestroyLabel;
    }

    public boolean isDestroyable() {
        return isDestroyable;
    }

    public void setIsDestroyable(boolean destroyable) {
        isDestroyable = destroyable;
    }

    public double getDestructionDifficulty() {
        return destructionDifficulty;
    }

    public void setDestructionDifficulty(double destructionDifficulty) {
        this.destructionDifficulty = destructionDifficulty;
    }

    public Map<String, Integer> getDrop() {
        return drop;
    }

    public void setDrop(Map<String, Integer> drop) {
        this.drop = drop;
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
                ", replaceableTexture=" + replaceableTexture +
                ", components=" + components +
                ", afterDestroyTexturePath='" + afterDestroyTexturePath + '\'' +
                ", afterDestroyLabel='" + afterDestroyLabel + '\'' +
                ", isDestroyable=" + isDestroyable +
                '}';
    }
}
