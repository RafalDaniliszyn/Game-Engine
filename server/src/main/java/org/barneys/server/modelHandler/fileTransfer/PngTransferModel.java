package org.barneys.server.modelHandler.fileTransfer;

import org.barneys.model.BaseModel;

public class PngTransferModel extends BaseModel {
    private String imageBase64;
    private String label;

    public PngTransferModel() {
    }

    public PngTransferModel(String imageBase64, String label) {
        this.imageBase64 = imageBase64;
        this.label = label;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
