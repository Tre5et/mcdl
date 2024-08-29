package net.treset.mcdl.mojang;

import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.SerializationException;

public class MinecraftUser extends GenericJsonParsable {
    private String id;
    private String name;
    private boolean legacy;
    private boolean demo;

    public MinecraftUser(String id, String name, boolean legacy, boolean demo) {
        this.id = id;
        this.name = name;
        this.legacy = legacy;
        this.demo = demo;
    }

    public static MinecraftUser fromJson(String json) throws SerializationException {
        return fromJson(json, MinecraftUser.class);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
