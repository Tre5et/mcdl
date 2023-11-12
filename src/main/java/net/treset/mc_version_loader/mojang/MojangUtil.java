package net.treset.mc_version_loader.mojang;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.util.FileUtil;
import net.treset.mc_version_loader.util.Sources;

public class MojangUtil {
    public static MinecraftProfile getMinecraftProfile(String uuid) throws FileDownloadException {
        try {
            return MinecraftProfile.fromJson(FileUtil.getStringFromUrl(Sources.getMojangSessionProfileUrl(uuid)));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse mojang profile", e);
        }
    }

    public static MinecraftUser getMinecraftUser(String userName) throws FileDownloadException {
        try {
            return MinecraftUser.fromJson(FileUtil.getStringFromUrl(Sources.getMojangUserProfileUrl(userName)));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse mojang user", e);
        }
    }
}
