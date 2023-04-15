package net.treset.mc_version_loader.version;

public class VersionJavaVersion {
    public String component;
    public int majorVersion;

    public VersionJavaVersion(String component, int majorVersion) {
        this.component = component;
        this.majorVersion = majorVersion;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }
}
