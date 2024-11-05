package dev.treset.mcdl.forge;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.minecraft.MinecraftProfile;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.cache.Caching;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.function.Consumer;

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
     * Installs the specified forge version and its libraries to the specified directory.
     * @param version The forge version to install
     * @param librariesDir The directory to install the libraries to
     * @param javaExecutable The java executable to run the installer with
     * @param onStatus The consumer to accept download status updates
     * @return The profile of the installed forge version
     * @throws FileDownloadException If there is an error downloading or installing the forge version
     */
    public static MinecraftProfile installVersion(String version, File librariesDir, File javaExecutable, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return new ForgeInstallerExecutor(version)
                .install(librariesDir, javaExecutable, onStatus);
    }

    /**
     * Gets the forge installer for the specified version.
     * @param forgeVersion The forge version to get the installer for
     * @return The forge installer
     * @throws FileDownloadException If there is an error instantiating the forge installer
     */
    @Deprecated
    public static ForgeInstaller getForgeInstaller(String forgeVersion) throws FileDownloadException {
        return ForgeInstaller.getForVersion(forgeVersion);
    }

    private static Caching<HttpResponse<byte[]>> caching = null;

    /**
     * Sets a caching strategy this module
     * @param caching The caching strategy to use
     */
    public static void setCaching(Caching<HttpResponse<byte[]>> caching) {
        ForgeDL.caching = caching;
    }

    public static Caching<HttpResponse<byte[]>> getCaching() {
        return caching;
    }
}
