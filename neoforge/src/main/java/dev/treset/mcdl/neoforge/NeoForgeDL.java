package dev.treset.mcdl.neoforge;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.minecraft.MinecraftProfile;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.cache.Caching;

import java.io.File;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class NeoForgeDL {
    private static final String MAVEN_VERSIONS_URL = "https://maven.neoforged.net/api/maven/versions/releases/net/neoforged/neoforge";
    public static String getMavenVersionsUrl() {
        return MAVEN_VERSIONS_URL;
    }
    private static final String MAVEN_VERSION_FILTER_URL = "https://maven.neoforged.net/api/maven/versions/releases/net/neoforged/neoforge?filter=%s";
    public static String getMavenVersionFilterUrl(String mcVersion) {
        String[] parts = mcVersion.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid Minecraft version: " + mcVersion);
        }
        String version;
        if(parts.length == 2) {
            version = parts[1] + ".0";
        } else {
            version = parts[1] + "." + parts[2];
        }
        return String.format(MAVEN_VERSION_FILTER_URL, version);
    }
    private static final String INSTALLER_URL = "https://maven.neoforged.net/releases/net/neoforged/neoforge/%s/neoforge-%s-installer.jar";
    public static String getInstallerUrl(String versionNumber) {
        return String.format(INSTALLER_URL, versionNumber, versionNumber);
    }

    /**
     * Gets all neoforge versions, latest last
     * @return The neoforge version manifest containing all versions
     * @throws FileDownloadException If the versions could not be downloaded
     */
    public static NeoForgeVersionManifest getNeoForgeVersions() throws FileDownloadException {
        return NeoForgeVersionManifest.getAll();
    }

    /**
     * Gets all neoforge versions for a specific minecraft version, latest last
     * @param mcVersion The minecraft version to get neoforge versions for
     * @return The neoforge version manifest containing all versions for the specified minecraft version
     * @throws FileDownloadException If the versions could not be downloaded
     */
    public static NeoForgeVersionManifest getNeoForgeVersions(String mcVersion) throws FileDownloadException {
        return NeoForgeVersionManifest.getForVersion(mcVersion);
    }

    /**
     * Gets all neoforge versions, latest first
     * @return A list of all neoforge versions
     * @throws FileDownloadException If the versions could not be downloaded
     */
    public static List<String> getNeoForgeVersionsList() throws FileDownloadException {
        ArrayList<String> versions = new ArrayList<>(getNeoForgeVersions().getVersions());
        Collections.reverse(versions);
        return versions;
    }

    /**
     * Gets all neoforge versions for a specific minecraft version, latest first
     * @param mcVersion The minecraft version to get neoforge versions for
     * @return A list of neoforge versions for the specified minecraft version
     * @throws FileDownloadException If the versions could not be downloaded
     */
    public static List<String> getNeoForgeVersionsList(String mcVersion) throws FileDownloadException {
        ArrayList<String> versions = new ArrayList<>(getNeoForgeVersions(mcVersion).getVersions());
        Collections.reverse(versions);
        return versions;
    }

    /**
     * Installs the specified neoforge version and its libraries to the specified directory.
     * @param neoForgeVersion The neoforge version to install
     * @param librariesDir The directory to install the libraries to
     * @param javaExecutable The java executable to run the installer with
     * @param onStatus The consumer to accept download status updates
     * @return The profile of the installed neoforge version
     * @throws FileDownloadException If there is an error downloading or installing the neoforge version
     */
    public static MinecraftProfile install(String neoForgeVersion, File librariesDir, File javaExecutable, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        return new NeoForgeInstallerExecutor(neoForgeVersion)
                .install(librariesDir, javaExecutable, onStatus);
    }

    private static Caching<HttpResponse<byte[]>> caching = null;

    /**
     * Sets a caching strategy this module
     * @param caching The caching strategy to use
     */
    public static void setCaching(Caching<HttpResponse<byte[]>> caching) {
        NeoForgeDL.caching = caching;
    }

    public static Caching<HttpResponse<byte[]>> getCaching() {
        return caching;
    }
}
