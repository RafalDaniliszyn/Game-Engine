package org.barneys;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uuid", referencedColumnName = "uuid")
    private PlayerEntityModel playerEntityModel;

    @Transient
    private KeySettings keySettings;

    @Transient
    private List<Input> pressedKeys;

    public User() {
        this.keySettings = new KeySettings(new HashMap<>());
        this.pressedKeys = new ArrayList<>();

    }

    public User(UUID uuid, KeySettings keySettings, PlayerEntityModel playerEntityModel, String name) {
        this.uuid = uuid;
        this.name = name;
        this.playerEntityModel = playerEntityModel;
        this.keySettings = keySettings;
        this.pressedKeys = new ArrayList<>();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public KeySettings getKeySettings() {
        return keySettings;
    }

    public void setKeySettings(KeySettings keySettings) {
        this.keySettings = keySettings;
    }

    public List<Input> getPressedKeys() {
        return pressedKeys;
    }

    public void setPressedKeys(List<Input> pressedKeys) {
        this.pressedKeys = pressedKeys;
    }

    public PlayerEntityModel getPlayerEntityModel() {
        return playerEntityModel;
    }

    public void setPlayerEntityModel(PlayerEntityModel playerEntityModel) {
        this.playerEntityModel = playerEntityModel;
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", playerEntityModel=" + playerEntityModel +
                ", keySettings=" + keySettings +
                ", pressedKeys=" + pressedKeys +
                '}';
    }
}
