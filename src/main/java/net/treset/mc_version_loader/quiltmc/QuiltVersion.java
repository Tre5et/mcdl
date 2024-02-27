package net.treset.mc_version_loader.quiltmc;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.SerializationException;

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
