package net.treset.mc_version_loader.mojang;

import java.util.Base64;

public class MinecraftProfileProperty {
    private String name;
    private String value;
    private String signature;

    public MinecraftProfileProperty(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public MinecraftProfileTextures getTextures() {
        if(!name.equals("textures")) {
            return null;
        }
        byte[] decodedBytes = Base64.getDecoder().decode(value);
        return MinecraftProfileTextures.fromJson(new String(decodedBytes));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
