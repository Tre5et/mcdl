package net.treset.mc_version_loader.mods.curseforge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class CurseforgeModule {
    private int fingerprint;
    private String name;

    public CurseforgeModule(int fingerprint, String name) {
        this.fingerprint = fingerprint;
        this.name = name;
    }

    private static CurseforgeModule fromJson(JsonObject moduleObj) {
        return new CurseforgeModule(
                JsonUtils.getAsInt(moduleObj, "fingerprint"),
                JsonUtils.getAsString(moduleObj, "name")
        );
    }

    public static List<CurseforgeModule> parseCurseforgeModules(JsonArray moduleArray) {
        List<CurseforgeModule> modules = new ArrayList<>();
        if(moduleArray != null) {
            for(JsonElement e : moduleArray) {
                modules.add(fromJson(JsonUtils.getAsJsonObject(e)));
            }
        }
        return modules;
    }

    public int getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(int fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
