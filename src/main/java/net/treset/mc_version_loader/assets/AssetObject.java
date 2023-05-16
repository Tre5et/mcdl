package net.treset.mc_version_loader.assets;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

public class AssetObject {
    private String hash;
    private int size;

    public AssetObject(String hash, int size) {
        this.hash = hash;
        this.size = size;
    }

    public static AssetObject fromJson(JsonObject jsonObject) {
        return new AssetObject(
                JsonUtils.getAsString(jsonObject, "hash"),
                JsonUtils.getAsInt(jsonObject, "size")
        );
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
