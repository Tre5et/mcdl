package dev.treset.mcdl.forge;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.util.cache.Caching;
import dev.treset.mcdl.util.cache.MemoryCaching;

import java.net.http.HttpResponse;
import java.util.List;

public class ForgeDL {
    // TODO: Support pre-1.5.2 installer-less process

    private static final String MAVEN_META_URL = "https://files.minecraftforge.net/net/minecraftforge/forge/maven-metadata.json";
    public static String getMavenMetaUrl() {
        return MAVEN_META_URL;
    }
    private static final String INSTALLER_URL = "https://maven.minecraftforge.net/net/minecraftforge/forge/%s/forge-%s-installer.jar"; // versionNumber
    public static String getInstallerUrl(String versionNumber) {
        return String.format(INSTALLER_URL, versionNumber, versionNumber);
    }


    /**
     * Gets all forge versions.
     * @return List of all forge versions
     * @throws FileDownloadException If there is an error downloading or parsing the forge versions
     */
    public static List<ForgeMetaVersion> getForgeVersions() throws FileDownloadException {
        return ForgeMetaVersion.getAll();
    }

    /**
     * Gets all forge version ids for a specific minecraft version.
     * @param mcVersion The minecraft version to get forge versions for
     * @return List of forge versions for the specified minecraft version
     * @throws FileDownloadException If there is an error downloading or parsing the forge versions
     */
    public static List<String> getForgeVersions(String mcVersion) throws FileDownloadException {
        return ForgeVersion.getAll(mcVersion);
    }

    /**
     * Gets the forge installer for the specified version.
     * @param forgeVersion The forge version to get the installer for
     * @return The forge installer
     * @throws FileDownloadException If there is an error instantiating the forge installer
     */
    public static ForgeInstaller getForgeInstaller(String forgeVersion) throws FileDownloadException {
        return ForgeInstaller.getForVersion(forgeVersion);
    }

    /**
     * Sets the caching strategy for forge versions. Default: {@link MemoryCaching}
     * @param caching The caching strategy to use
     */
    public static void setVersionCaching(Caching<HttpResponse<byte[]>> caching) {
        ForgeMetaVersion.setCaching(caching);
    }
}
