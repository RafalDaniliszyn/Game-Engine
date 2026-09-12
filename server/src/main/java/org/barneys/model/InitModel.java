package org.barneys.model;

import org.barneys.DataType;
import org.barneys.Input;
import java.util.Map;
import java.util.UUID;

public class InitModel extends BaseModel {
    private static final DataType DATA_TYPE = DataType.INIT_MODEL;
    private Map<Integer, Input> inputMap;

    public InitModel() {
        super(DATA_TYPE.getType());
    }

    public InitModel(Map<Integer, Input> inputMap, UUID userUuid) {
        super(userUuid, DATA_TYPE.getType());
        this.inputMap = inputMap;
    }

    public Map<Integer, Input> getInputMap() {
        return inputMap;
    }

    public void setInputMap(Map<Integer, Input> inputMap) {
        this.inputMap = inputMap;
    }


    @Override
    public String toString() {
        return "InitModel{" +
                "inputMap=" + inputMap +
                "} " + super.toString();
    }
}
