package net.treset.mc_version_loader.mojang;

import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.SerializationException;

import java.util.List;

public class MinecraftProfile extends GenericJsonParsable {
    private String id;
    private String name;
    private List<MinecraftProfileProperty> properties;

    public MinecraftProfile(String id, String name, List<MinecraftProfileProperty> properties) {
        this.id = id;
        this.name = name;
        this.properties = properties;
    }

    public static MinecraftProfile fromJson(String json) throws SerializationException {
        return fromJson(json, MinecraftProfile.class);
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

    public List<MinecraftProfileProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<MinecraftProfileProperty> properties) {
        this.properties = properties;
    }
}
