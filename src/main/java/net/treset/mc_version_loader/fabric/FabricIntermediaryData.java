package net.treset.mc_version_loader.fabric;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

public class FabricIntermediaryData {
    private String maven;
    private boolean stable;
    private String version;

    public FabricIntermediaryData(String maven, boolean stable, String version) {
        this.maven = maven;
        this.stable = stable;
        this.version = version;
    }

    public static FabricIntermediaryData fromJson(JsonObject intermediaryObj) {
        return new FabricIntermediaryData(
                JsonUtils.getAsString(intermediaryObj, "maven"),
                JsonUtils.getAsBoolean(intermediaryObj, "stable"),
                JsonUtils.getAsString(intermediaryObj, "version")
        );
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
