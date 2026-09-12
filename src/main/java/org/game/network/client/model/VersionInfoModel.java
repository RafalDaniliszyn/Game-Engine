package org.game.network.client.model;

import org.game.network.model.BaseModel;

/**
 * Represents a message received from the server containing
 * information about the current game version.
 * This model is used to verify whether it is
 * running the latest version of the game or if an update is required.
 */
public class VersionInfoModel extends BaseModel {
    private int major;
    private int minor;
    private int release;
    private int worldSize;

    public VersionInfoModel() {
    }

    public VersionInfoModel(int major, int minor, int release, int worldSize) {
        this.major = major;
        this.minor = minor;
        this.release = release;
        this.worldSize = worldSize;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getRelease() {
        return release;
    }

    public void setRelease(int release) {
        this.release = release;
    }

    public int getWorldSize() {
        return worldSize;
    }

    public void setWorldSize(int worldSize) {
        this.worldSize = worldSize;
    }

    @Override
    public String toString() {
        return "VersionUpdateModel{" +
                ", major=" + major +
                ", minor=" + minor +
                ", release=" + release +
                ", worldSize=" + worldSize +
                "} " + super.toString();
    }
}
