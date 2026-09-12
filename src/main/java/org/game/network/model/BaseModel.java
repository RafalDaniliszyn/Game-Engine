package org.game.network.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.game.isometric.GameState;
import org.game.network.client.GameClient;
import org.game.network.client.incomingDataHandler.ServerMessage;
import org.game.network.client.incomingDataHandler.fileTransfer.FileTransferRequestModel;
import org.game.network.client.incomingDataHandler.fileTransfer.PngTransferModel;
import org.game.network.client.model.*;

import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = GameStateModel.class, name = "GameStateModel"),
        @JsonSubTypes.Type(value = GameClient.ShutdownModel.class, name = "ShutdownModel"),
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
        this.userUuid = GameState.getUserUuid();
    }

    public BaseModel(String dataType) {
        this.userUuid = GameState.getUserUuid();
        this.dataType = dataType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    @Override
    public String toString() {
        return "BaseModel{" +
                "userUuid=" + userUuid +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}
