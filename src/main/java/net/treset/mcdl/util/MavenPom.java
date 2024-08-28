package net.treset.mcdl.util;

public class MavenPom {
    private String modelVersion;
    private String groupId;
    private String artifactId;
    private String version;
    private String packaging;

    public MavenPom(String modelVersion, String groupId, String artifactId, String version, String packaging) {
        this.modelVersion = modelVersion;
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.packaging = packaging;
    }

    public String getMavenUrl() {
        String mavenDir = getMavenDir();
        String mavenFileName = getMavenFileName();
        if(mavenDir == null || mavenDir.isBlank() || mavenFileName == null || mavenFileName.isBlank()) {
            return null;
        }
        return mavenDir + mavenFileName;
    }

    public String getMavenDir() {
        if(getGroupId() == null || getGroupId().isBlank() || getArtifactId() == null || getArtifactId().isBlank() || getVersion() == null || getVersion().isBlank()) {
            return null;
        }
        String[] parts = getGroupId().split("\\.");
        StringBuilder mavenPath = new StringBuilder();
        for(String p : parts) {
            mavenPath.append(p).append("/");
        }
        mavenPath.append(getArtifactId()).append("/").append(getVersion()).append("/");
        return mavenPath.toString();
    }

    public String getMavenFileName() {
        if(getArtifactId() == null || getArtifactId().isBlank() || getVersion() == null || getVersion().isBlank()) {
            return null;
        }
        return getArtifactId() + "-" + getVersion() + ".jar";
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPackaging() {
        return packaging;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }
}
