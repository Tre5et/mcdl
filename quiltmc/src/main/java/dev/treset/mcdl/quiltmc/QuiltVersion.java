package dev.treset.mcdl.quiltmc;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
import java.util.List;

public class QuiltVersion extends GenericJsonParsable {
    private QuiltLoader loader;
    private QuiltHashed hashed;
    private QuiltIntermediary intermediary;
    @SerializedName("launcherMeta")
    private QuiltLauncherMeta launcherMeta;

    public QuiltVersion(QuiltLoader loader, QuiltHashed hashed, QuiltIntermediary intermediary, QuiltLauncherMeta launcherMeta) {
        this.loader = loader;
        this.hashed = hashed;
        this.intermediary = intermediary;
        this.launcherMeta = launcherMeta;
    }

    public static QuiltVersion fromJson(String json) throws SerializationException {
        return fromJson(json, QuiltVersion.class);
    }

    public static List<QuiltVersion> fromJsonArray(String json) throws SerializationException {
        return fromJsonArray(json, new TypeToken<>() {});
    }

    /**
     * Get all QuiltMC versions for a specific Minecraft version
     * @param mcVersion The Minecraft version to get QuiltMC versions for
     * @return A list of QuiltMC versions
     * @throws FileDownloadException If the versions could not be fetched
     */
    public static List<QuiltVersion> getAll(String mcVersion) throws FileDownloadException {
        try {
            String content = HttpUtil.getString(QuiltDL.getQuiltMetaUrl() + "loader/" + mcVersion, QuiltDL.getCaching());
            return QuiltVersion.fromJsonArray(content);
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse QuiltMC version JSON", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to get QuiltMC versions JSON", e);
        }
    }

    public QuiltLoader getLoader() {
        return loader;
    }

    public void setLoader(QuiltLoader loader) {
        this.loader = loader;
    }

    public QuiltHashed getHashed() {
        return hashed;
    }

    public void setHashed(QuiltHashed hashed) {
        this.hashed = hashed;
    }

    public QuiltIntermediary getIntermediary() {
        return intermediary;
    }

    public void setIntermediary(QuiltIntermediary intermediary) {
        this.intermediary = intermediary;
    }

    public QuiltLauncherMeta getLauncherMeta() {
        return launcherMeta;
    }

    public void setLauncherMeta(QuiltLauncherMeta launcherMeta) {
        this.launcherMeta = launcherMeta;
    }
}
