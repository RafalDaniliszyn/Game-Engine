package org.barneys.server.modelHandler;

import org.barneys.model.BaseModel;

public class JsonFileModel extends BaseModel {
    private String jsonFile;

    public JsonFileModel(String jsonFile) {
        this.jsonFile = jsonFile;
    }

    public JsonFileModel() {
    }

    public String getJsonFile() {
        return jsonFile;
    }

    public void setJsonFile(String jsonFile) {
        this.jsonFile = jsonFile;
    }
}
