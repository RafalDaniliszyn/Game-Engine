package org.barneys.game;

public class ToolUseRequestMessage extends ClientMessage {
    private int toolId;

    public ToolUseRequestMessage() {
    }

    public ToolUseRequestMessage(int toolId) {
        this.toolId = toolId;
    }

    public int getToolId() {
        return toolId;
    }

    public void setToolId(int toolId) {
        this.toolId = toolId;
    }
}
