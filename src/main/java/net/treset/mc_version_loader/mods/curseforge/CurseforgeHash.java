package net.treset.mc_version_loader.mods.curseforge;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class CurseforgeHash {
    private int algo;
    private String value;

    public CurseforgeHash(int algo, String value) {
        this.algo = algo;
        this.value = value;
    }

    public static CurseforgeHash fromJson(JsonObject hashObj) {
        return new CurseforgeHash(
                JsonUtils.getAsInt(hashObj, "algo"),
                JsonUtils.getAsString(hashObj, "value")
        );
    }

    public static List<CurseforgeHash> parseCursefprgeHashes(JsonArray hashArray) {
        List<CurseforgeHash> hashes = new ArrayList<>();
        if(hashArray != null) {
            for(JsonElement e : hashArray) {
                hashes.add(fromJson(JsonUtils.getAsJsonObject(e)));
            }
        }
        return hashes;
    }

    public int getAlgo() {
        return algo;
    }

    public void setAlgo(int algo) {
        this.algo = algo;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
