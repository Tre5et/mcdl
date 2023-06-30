package net.treset.mc_version_loader.mojang;

import net.treset.mc_version_loader.json.GenericJsonParsable;

public class MinecraftUser extends GenericJsonParsable {
    private String uuid;
    private String name;
    private boolean legacy;
    private boolean demo;

    public MinecraftUser(String uuid, String name, boolean legacy, boolean demo) {
        this.uuid = uuid;
        this.name = name;
        this.legacy = legacy;
        this.demo = demo;
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

    public boolean isLegacy() {
        return legacy;
    }

    public void setLegacy(boolean legacy) {
        this.legacy = legacy;
    }

    public boolean isDemo() {
        return demo;
    }

    public void setDemo(boolean demo) {
        this.demo = demo;
    }
}
