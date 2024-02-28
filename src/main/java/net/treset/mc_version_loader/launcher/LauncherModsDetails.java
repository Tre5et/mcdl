package net.treset.mc_version_loader.launcher;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.json.SerializationException;

import java.util.List;

public class LauncherModsDetails extends GenericJsonParsable {
    private List<String> types;
    private List<String> versions;
    private List<LauncherMod> mods;

    public LauncherModsDetails(List<String> types, List<String> versions, List<LauncherMod> mods) {
        this.types = types;
        this.versions = versions;
        this.mods = mods;
    }

    public static LauncherModsDetails fromJson(String json) throws SerializationException {
        LauncherModsDetails details = fromJson(json, LauncherModsDetails.class);
        if(details.getTypes() == null) {
            JsonObject obj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
            String type = JsonUtils.getAsString(obj, "mods_type");
            details.setTypes(List.of(type));
        }
        if(details.getVersions() == null) {
            JsonObject obj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));            String version = JsonUtils.getAsString(obj, "mods_version");
            details.setVersions(List.of(version));
        }
        return details;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }

    public List<LauncherMod> getMods() {
        return mods;
    }

    public void setMods(List<LauncherMod> mods) {
        this.mods = mods;
    }
}
