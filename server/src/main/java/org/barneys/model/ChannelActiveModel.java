package org.barneys.model;

import org.barneys.DataType;
import org.barneys.blockLoader.EntityDto;
import java.util.List;

public class ChannelActiveModel extends BaseModel {
    private static final DataType DATA_TYPE = DataType.CHANNEL_ACTIVE_MODEL;
    private List<EntityDto> entityDto;

    public ChannelActiveModel() {
        super(DATA_TYPE.getType());
    }

    public ChannelActiveModel(List<EntityDto> entityDto) {
        super(DATA_TYPE.getType());
        this.entityDto = entityDto;
    }

    public List<EntityDto> getEntityDto() {
        return entityDto;
    }

    public void setEntityDto(List<EntityDto> entityDto) {
        this.entityDto = entityDto;
    }

    @Override
    public String toString() {
        return "ChannelActiveModel{" +
                "entityDto=" + entityDto +
                "} " + super.toString();
    }
}