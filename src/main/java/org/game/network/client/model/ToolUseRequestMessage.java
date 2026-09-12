package org.game.network.client.model;

public class ToolUseRequestMessage extends ClientMessage {
    private int toolId;

    public ToolUseRequestMessage(int toolId) {
        this.toolId = toolId;
    }

    public ToolUseRequestMessage() {
    }

    public int getToolId() {
        return toolId;
    }

    public void setToolId(int toolId) {
        this.toolId = toolId;
    }
}
