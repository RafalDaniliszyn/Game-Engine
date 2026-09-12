package org.barneys.model;

import org.barneys.DataType;
import java.util.UUID;

public class ShutdownModel extends BaseModel {
    private static final DataType DATA_TYPE = DataType.SHUTDOWN_MODEL;

    public ShutdownModel() {
        super(DATA_TYPE.getType());
    }
    public ShutdownModel(UUID userUuid) {
        super(userUuid, DATA_TYPE.getType());
    }

    @Override
    public String toString() {
        return "ShutdownModel{} " + super.toString();
    }
}
