package org.barneys.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.barneys.game.ClientMessage;
import org.barneys.game.ServerMessage;
import org.barneys.server.modelHandler.*;
import org.barneys.server.modelHandler.fileTransfer.FileTransferRequestModel;
import org.barneys.server.modelHandler.fileTransfer.PngTransferModel;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GameStateModel.class, name = "GameStateModel"),
        @JsonSubTypes.Type(value = ShutdownModel.class, name = "ShutdownModel"),
        @JsonSubTypes.Type(value = InitModel.class, name = "InitModel"),
        @JsonSubTypes.Type(value = PlayerStateModel.class, name = "PlayerStateModel"),
        @JsonSubTypes.Type(value = ChannelActiveModel.class, name = "ChannelActiveModel"),
        @JsonSubTypes.Type(value = WorldMapModel.class, name = "WorldMapModel"),
        @JsonSubTypes.Type(value = PutModel.class, name = "PutModel"),
        @JsonSubTypes.Type(value = DestroyModel.class, name = "DestroyModel"),
        @JsonSubTypes.Type(value = DropModel.class, name = "DropModel"),
        @JsonSubTypes.Type(value = PlayerInitDataModel.class, name = "PlayerInitDataModel"),
        @JsonSubTypes.Type(value = FileTransferRequestModel.class, name = "FileTransferRequestModel"),
        @JsonSubTypes.Type(value = PngTransferModel.class, name = "PngTransferModel"),
        @JsonSubTypes.Type(value = DownloadUpdateModel.class, name = "DownloadUpdateModel"),
        @JsonSubTypes.Type(value = VersionInfoModel.class, name = "VersionInfoModel"),
        @JsonSubTypes.Type(value = JsonFileModel.class, name = "JsonFileModel"),
        @JsonSubTypes.Type(value = ClientMessage.class, name = "ClientMessage"),
        @JsonSubTypes.Type(value = ServerMessage.class, name = "ServerMessage")
})
public abstract class BaseModel {
    private UUID userUuid;
    private String dataType;

    public BaseModel() {
    }

    public BaseModel(String dataType) {
        this.dataType = dataType;
    }

    public BaseModel(UUID userUuid, String dataType) {
        this.userUuid = userUuid;
        this.dataType = dataType;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "userUuid=" + userUuid +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}
