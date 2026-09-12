package org.game.network.updater;

public class Version {
    private final Integer major;
    private final Integer minor;
    private final Integer release;

    public Version(Integer major, Integer minor, Integer release) {
        this.major = major;
        this.minor = minor;
        this.release = release;
    }

    public Integer getMajor() {
        return major;
    }

    public Integer getMinor() {
        return minor;
    }

    public Integer getRelease() {
        return release;
    }

    @Override
    public String toString() {
        return "Version{" +
                "major=" + major +
                ", minor=" + minor +
                ", release=" + release +
                '}';
    }
}
