package net.treset.mc_version_loader.mojang;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.util.FileUtil;
import net.treset.mc_version_loader.util.Sources;

public class MojangData {
    /**
     * Gets minecraft profile data for the profile with the specified uuid.
     * @param uuid The uuid of the profile to get data for
     * @return The profile data
     * @throws FileDownloadException If there is an error loading or parsing the profile data
     */
    public static MinecraftProfile getMinecraftProfile(String uuid) throws FileDownloadException {
        try {
            return MinecraftProfile.fromJson(FileUtil.getStringFromUrl(Sources.getMojangSessionProfileUrl(uuid)));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse mojang profile", e);
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
            return MinecraftUser.fromJson(FileUtil.getStringFromUrl(Sources.getMojangUserProfileUrl(userName)));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse mojang user", e);
        }
    }
}
