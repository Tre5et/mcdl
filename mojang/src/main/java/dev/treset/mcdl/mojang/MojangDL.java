package dev.treset.mcdl.mojang;

import dev.treset.mcdl.exception.FileDownloadException;

public class MojangDL {
    private static final String MOJANG_USER_PROFILE_URL = "https://api.mojang.com/users/profiles/minecraft/%s"; // Playername
    private static final String MOJANG_SESSION_PROFILE_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s"; // UUID

    /**
     * Gets minecraft profile data for the profile with the specified uuid.
     * @param uuid The uuid of the profile to get data for
     * @return The profile data
     * @throws FileDownloadException If there is an error loading or parsing the profile data
     */
    public static MinecraftProfile getMinecraftProfile(String uuid) throws FileDownloadException {
        return MinecraftProfile.get(uuid);
    }

    /**
     * Gets minecraft user data for the user with the specified username.
     * @param userName The username of the user to get data for
     * @return The user data
     * @throws FileDownloadException If there is an error loading or parsing the user data
     */
    public static MinecraftUser getMinecraftUser(String userName) throws FileDownloadException {
        return MinecraftUser.get(userName);
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
