package org.barneys.blockLoader.action.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.barneys.blockLoader.action.ActionEnum;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ExplosionActionDto.class, name = "explosionActionDto"),
        @JsonSubTypes.Type(value = MoveUpActionDto.class, name = "moveUpActionDto"),
        @JsonSubTypes.Type(value = MoveDownActionDto.class, name = "moveDownActionDto")
})
public abstract class ActionDto {
    private ActionEnum action;

    public ActionEnum getAction() {
        return action;
    }

    public void setAction(ActionEnum action) {
        this.action = action;
    }
}
