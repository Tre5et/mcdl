package net.treset.mc_version_loader.mojang;

import net.treset.mc_version_loader.json.GenericJsonParsable;

public class MinecraftUser extends GenericJsonParsable {
    private String uuid;
    private String name;

    public MinecraftUser(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public static MinecraftUser fromJson(String json) {
        return fromJson(json, MinecraftUser.class);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
