package dev.treset.mcdl.mojang;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
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

    /**
     * Gets minecraft profile data for the profile with the specified uuid.
     * @param uuid The uuid of the profile to get data for
     * @return The profile data
     * @throws FileDownloadException If there is an error loading or parsing the profile data
     */
    public static MinecraftProfile get(String uuid) throws FileDownloadException {
        try {
            return MinecraftProfile.fromJson(HttpUtil.getString(MojangDL.getMojangSessionProfileUrl(uuid), MojangDL.getCaching()));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse mojang profile", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to get mojang profile", e);
        }
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
