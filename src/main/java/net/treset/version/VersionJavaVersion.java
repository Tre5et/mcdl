package net.treset.version;

public class VersionJavaVersion {
    public String component;
    public String majorVersion;

    public VersionJavaVersion(String component, String majorVersion) {
        this.component = component;
        this.majorVersion = majorVersion;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getMajorVersion() {
        return majorVersion;
    }

    public void setMajorVersion(String majorVersion) {
        this.majorVersion = majorVersion;
    }
}
