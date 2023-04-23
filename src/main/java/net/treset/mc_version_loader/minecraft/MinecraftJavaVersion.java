package net.treset.mc_version_loader.minecraft;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

public class MinecraftJavaVersion {
    public String component;
    public int majorVersion;

    public MinecraftJavaVersion(String component, int majorVersion) {
        this.component = component;
        this.majorVersion = majorVersion;
    }

    public static MinecraftJavaVersion fromJson(JsonObject javaVersionObj) {
        return new MinecraftJavaVersion(
                JsonUtils.getAsString(javaVersionObj, "component"),
                JsonUtils.getAsInt(javaVersionObj, "majorVersion")
        );
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
