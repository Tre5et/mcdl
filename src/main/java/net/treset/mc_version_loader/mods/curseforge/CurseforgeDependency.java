package net.treset.mc_version_loader.mods.curseforge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class CurseforgeDependency {
    private int modId;
    private int relationType;

    public CurseforgeDependency(int modId, int relationType) {
        this.modId = modId;
        this.relationType = relationType;
    }

    public static CurseforgeDependency fromJson(JsonObject dependencyObj) {
        return new CurseforgeDependency(
                JsonUtils.getAsInt(dependencyObj, "modId"),
                JsonUtils.getAsInt(dependencyObj, "relationType")
        );
    }

    public static List<CurseforgeDependency> parseCurseforgeDependencies(JsonArray dependencyArray) {
        List<CurseforgeDependency> dependencies = new ArrayList<>();
        if(dependencyArray != null) {
            for(JsonElement e : dependencyArray) {
                dependencies.add(fromJson(JsonUtils.getAsJsonObject(e)));
            }
        }
        return dependencies;
    }

    public int getModId() {
        return modId;
    }

    public void setModId(int modId) {
        this.modId = modId;
    }

    public int getRelationType() {
        return relationType;
    }

    public void setRelationType(int relationType) {
        this.relationType = relationType;
    }
}
