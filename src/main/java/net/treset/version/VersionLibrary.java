package net.treset.version;

import java.util.List;

public class VersionLibrary {
    private String name;
    private String artifactPath;
    private String artifactSha1;
    private int artifactSize;
    private String artifactUrl;
    private List<VersionRule> rules;

    public VersionLibrary(String name, String artifactPath, String artifactSha1, int artifactSize, String artifactUrl, List<VersionRule> rules) {
        this.name = name;
        this.artifactPath = artifactPath;
        this.artifactSha1 = artifactSha1;
        this.artifactSize = artifactSize;
        this.artifactUrl = artifactUrl;
        this.rules = rules;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtifactPath() {
        return artifactPath;
    }

    public void setArtifactPath(String artifactPath) {
        this.artifactPath = artifactPath;
    }

    public String getArtifactSha1() {
        return artifactSha1;
    }

    public void setArtifactSha1(String artifactSha1) {
        this.artifactSha1 = artifactSha1;
    }

    public int getArtifactSize() {
        return artifactSize;
    }

    public void setArtifactSize(int artifactSize) {
        this.artifactSize = artifactSize;
    }

    public String getArtifactUrl() {
        return artifactUrl;
    }

    public void setArtifactUrl(String artifactUrl) {
        this.artifactUrl = artifactUrl;
    }

    public List<VersionRule> getRules() {
        return rules;
    }

    public void setRules(List<VersionRule> rules) {
        this.rules = rules;
    }
}
