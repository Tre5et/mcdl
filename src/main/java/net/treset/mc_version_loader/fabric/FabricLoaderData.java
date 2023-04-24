package net.treset.mc_version_loader.fabric;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

public class FabricLoaderData {
    private int build;
    private String maven;
    private String separator;
    private boolean stable;
    private String version;

    public FabricLoaderData(int build, String maven, String separator, boolean stable, String version) {
        this.build = build;
        this.maven = maven;
        this.separator = separator;
        this.stable = stable;
        this.version = version;
    }

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public String getMaven() {
        return maven;
    }

    public void setMaven(String maven) {
        this.maven = maven;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
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
