package net.treset.mc_version_loader.mojang;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.util.FileUtil;
import net.treset.mc_version_loader.util.Sources;

public class MojangUtil {
    public static MinecraftProfile getMinecraftProfile(String uuid) throws FileDownloadException {
        return MinecraftProfile.fromJson(FileUtil.getStringFromUrl(Sources.getMojangSessionProfileUrl(uuid)));
    }

    public static MinecraftUser getMinecraftUser(String userName) throws FileDownloadException {
        return MinecraftUser.fromJson(FileUtil.getStringFromUrl(Sources.getMojangUserProfileUrl(userName)));
    }
}
