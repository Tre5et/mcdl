package net.treset.mcdl.fabric;

public class FabricIntermediaryData {
    private String maven;
    private boolean stable;
    private String version;

    public FabricIntermediaryData(String maven, boolean stable, String version) {
        this.maven = maven;
        this.stable = stable;
        this.version = version;
    }

    public String getMaven() {
        return maven;
    }

    public void setMaven(String maven) {
        this.maven = maven;
    }

    public boolean isStable() {
        return stable;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
