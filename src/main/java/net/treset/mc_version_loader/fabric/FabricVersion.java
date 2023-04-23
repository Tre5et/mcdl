package net.treset.mc_version_loader.fabric;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class FabricVersion {
    private String minecraftVersion;
    private String loaderVersion;

    public FabricVersion(String minecraftVersion, String loaderVersion) {
        this.minecraftVersion = minecraftVersion;
        this.loaderVersion = loaderVersion;
    }

    public static FabricVersion fromJson(JsonObject versionObj) {
        return new FabricVersion(
                JsonUtils.getAsString(JsonUtils.getAsJsonObject(versionObj, "intermediary"), "version"),
                JsonUtils.getAsString(JsonUtils.getAsJsonObject(versionObj, "loader"), "version")
        );
    }

    public static List<FabricVersion> parseFabricManifest(String manifestJson) {
        JsonArray manifestArray = JsonUtils.getAsJsonArray(JsonUtils.parseJson(manifestJson));
        List<FabricVersion> versions = new ArrayList<>();
        if(manifestArray != null) {
            for(JsonElement v : manifestArray) {
                versions.add(fromJson(JsonUtils.getAsJsonObject(v)));
            }
        }
        return versions;
    }

    public String getMinecraftVersion() {
        return minecraftVersion;
    }

    public void setMinecraftVersion(String minecraftVersion) {
        this.minecraftVersion = minecraftVersion;
    }

    public String getLoaderVersion() {
        return loaderVersion;
    }

    public void setLoaderVersion(String loaderVersion) {
        this.loaderVersion = loaderVersion;
    }
}
