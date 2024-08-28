package net.treset.mcdl.java;

public class JavaVersion {
    private int availabilityGroup;
    private int availabilityProgress;
    private String manifestSha1;
    private int manifestSize;
    private String manifestUrl;
    private String versionName;
    private String versionReleased;

    public JavaVersion(int availabilityGroup, int availabilityProgress, String manifestSha1, int manifestSize, String manifestUrl, String versionName, String versionReleased) {
        this.availabilityGroup = availabilityGroup;
        this.availabilityProgress = availabilityProgress;
        this.manifestSha1 = manifestSha1;
        this.manifestSize = manifestSize;
        this.manifestUrl = manifestUrl;
        this.versionName = versionName;
        this.versionReleased = versionReleased;
    }

    public int getAvailabilityGroup() {
        return availabilityGroup;
    }

    public void setAvailabilityGroup(int availabilityGroup) {
        this.availabilityGroup = availabilityGroup;
    }

    public int getAvailabilityProgress() {
        return availabilityProgress;
    }

    public void setAvailabilityProgress(int availabilityProgress) {
        this.availabilityProgress = availabilityProgress;
    }

    public String getManifestSha1() {
        return manifestSha1;
    }

    public void setManifestSha1(String manifestSha1) {
        this.manifestSha1 = manifestSha1;
    }

    public int getManifestSize() {
        return manifestSize;
    }

    public void setManifestSize(int manifestSize) {
        this.manifestSize = manifestSize;
    }

    public String getManifestUrl() {
        return manifestUrl;
    }

    public void setManifestUrl(String manifestUrl) {
        this.manifestUrl = manifestUrl;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionReleased() {
        return versionReleased;
    }

    public void setVersionReleased(String versionReleased) {
        this.versionReleased = versionReleased;
    }
}
