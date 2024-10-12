package dev.treset.mcdl.mojang;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;

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

    /**
     * Gets minecraft user data for the user with the specified username.
     * @param userName The username of the user to get data for
     * @return The user data
     * @throws FileDownloadException If there is an error loading or parsing the user data
     */
    public static MinecraftUser get(String userName) throws FileDownloadException {
        try {
            return MinecraftUser.fromJson(HttpUtil.getString(MojangDL.getMojangUserProfileUrl(userName), MojangDL.getCaching()));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse mojang user", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to get mojang user", e);
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
