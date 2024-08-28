package net.treset.mcdl.quiltmc;

public class QuiltHashed {
    private String maven;
    private String version;

    public QuiltHashed(String maven, String version) {
        this.maven = maven;
        this.version = version;
    }

    public String getMaven() {
        return maven;
    }

    public void setMaven(String maven) {
        this.maven = maven;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
