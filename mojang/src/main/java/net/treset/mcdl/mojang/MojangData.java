package net.treset.mcdl.mojang;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.util.HttpUtil;

import java.io.IOException;

public class MojangData {
    private static final String MOJANG_USER_PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/%s"; // Playername
    private static final String MOJANG_SESSION_PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s"; // UUID

    /**
     * Gets minecraft profile data for the profile with the specified uuid.
     * @param uuid The uuid of the profile to get data for
     * @return The profile data
     * @throws FileDownloadException If there is an error loading or parsing the profile data
     */
    public static MinecraftProfile getMinecraftProfile(String uuid) throws FileDownloadException {
        try {
            return MinecraftProfile.fromJson(HttpUtil.getString(getMojangSessionProfileUrl(uuid)));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse mojang profile", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to get mojang profile", e);
        }
    }

    /**
     * Gets minecraft user data for the user with the specified username.
     * @param userName The username of the user to get data for
     * @return The user data
     * @throws FileDownloadException If there is an error loading or parsing the user data
     */
    public static MinecraftUser getMinecraftUser(String userName) throws FileDownloadException {
        try {
            return MinecraftUser.fromJson(HttpUtil.getString(getMojangUserProfileUrl(userName)));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse mojang user", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to get mojang user", e);
        }
    }

    public static String getMojangUserProfileUrl(String playerName) {
        if(playerName == null || playerName.isBlank()) {
            throw new IllegalArgumentException("Invalid playerName=" + playerName);
        }
        return String.format(MOJANG_USER_PROFILE_URL, playerName);
    }

    public static String getMojangSessionProfileUrl(String uuid) {
        if(uuid == null || uuid.isBlank()) {
            throw new IllegalArgumentException("Invalid uuid=" + uuid);
        }
        return String.format(MOJANG_SESSION_PROFILE_URL, uuid);
    }
}
