package org.barneys.server.modelHandler;

import org.barneys.model.BaseModel;

public class DownloadUpdateModel extends BaseModel {
    private int major;
    private int minor;
    private int release;

    public DownloadUpdateModel() {
    }

    public DownloadUpdateModel(int major, int minor, int release) {
        this.major = major;
        this.minor = minor;
        this.release = release;
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

    @Override
    public String toString() {
        return "CheckForUpdatesModel{" +
                "major=" + major +
                ", minor=" + minor +
                ", release=" + release +
                "} ";
    }
}
