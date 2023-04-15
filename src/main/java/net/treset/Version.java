package net.treset;

public class Version {
    private String versionNumber;
    private String versionManifestUrl;
    private boolean snapshot;

    public Version(String versionNumber, String versionManifestUrl, boolean snapshot) {
        this.versionNumber = versionNumber;
        this.versionManifestUrl = versionManifestUrl;
        this.snapshot = snapshot;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionManifestUrl() {
        return versionManifestUrl;
    }

    public void setVersionManifestUrl(String versionManifestUrl) {
        this.versionManifestUrl = versionManifestUrl;
    }

    public boolean isSnapshot() {
        return snapshot;
    }

    public void setSnapshot(boolean snapshot) {
        this.snapshot = snapshot;
    }

    @Override
    public String toString() {
        return "\nVersion: " + getVersionNumber() + ", snapshot: "+ isSnapshot() + ", url: " + getVersionManifestUrl();
    }
}
