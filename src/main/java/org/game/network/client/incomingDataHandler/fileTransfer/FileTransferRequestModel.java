package org.game.network.client.incomingDataHandler.fileTransfer;

import org.game.network.model.BaseModel;

public class FileTransferRequestModel extends BaseModel {
    private String label;

    public FileTransferRequestModel() {
    }

    public FileTransferRequestModel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
