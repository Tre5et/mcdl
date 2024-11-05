package dev.treset.mcdl.neoforge;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
import java.util.List;

public class NeoForgeVersionManifest extends GenericJsonParsable {
    private boolean isSnapshot;
    private List<String> versions;

    public NeoForgeVersionManifest(boolean isSnapshot, List<String> versions) {
        this.isSnapshot = isSnapshot;
        this.versions = versions;
    }

    /**
     * Gets all neoforge versions, lastest last
     * @return The neoforge version manifest containing all versions
     * @throws FileDownloadException If the versions could not be downloaded
     */
    public static NeoForgeVersionManifest getAll() throws FileDownloadException {
        try {
            String content = HttpUtil.getString(NeoForgeDL.getMavenVersionsUrl(), NeoForgeDL.getCaching());
            return NeoForgeVersionManifest.fromJson(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse forge versions", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to read forge versions", e);
        }
    }

    /**
     * Gets all neoforge versions for a specific minecraft version
     * @param mcVersion The minecraft version to get neoforge versions for
     * @return The neoforge version manifest containing all versions for the specified minecraft version
     * @throws FileDownloadException If the versions could not be downloaded
     */
    public static NeoForgeVersionManifest getForVersion(String mcVersion) throws FileDownloadException {
        try {
            String content = HttpUtil.getString(NeoForgeDL.getMavenVersionFilterUrl(mcVersion), NeoForgeDL.getCaching());
            return NeoForgeVersionManifest.fromJson(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse forge versions", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to read forge versions", e);
        }
    }

    public static NeoForgeVersionManifest fromJson(String json) throws SerializationException {
        return fromJson(json, NeoForgeVersionManifest.class);
    }

    public boolean isSnapshot() {
        return isSnapshot;
    }

    public void setSnapshot(boolean snapshot) {
        isSnapshot = snapshot;
    }

    public List<String> getVersions() {
        return versions;
    }

    public void setVersions(List<String> versions) {
        this.versions = versions;
    }
}
