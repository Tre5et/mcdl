package net.treset.mc_version_loader.fabric;

import com.google.gson.reflect.TypeToken;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.json.SerializationException;

import java.util.List;

public class FabricVersionDetails extends GenericJsonParsable {
    private FabricIntermediaryData intermediary;
    private FabricLauncherMeta launcherMeta;
    private FabricLoaderData loader;

    public FabricVersionDetails(FabricIntermediaryData intermediary, FabricLauncherMeta launcherMeta, FabricLoaderData loader) {
        this.intermediary = intermediary;
        this.launcherMeta = launcherMeta;
        this.loader = loader;
    }

    public static FabricVersionDetails fromJson(String versionJson) throws SerializationException {
        return fromJson(versionJson, FabricVersionDetails.class, JsonUtils.getGsonCamelCase());
    }

    public static List<FabricVersionDetails> fromJsonArray(String jsonArray) throws SerializationException {
        return fromJsonArray(jsonArray, new TypeToken<>(){}, JsonUtils.getGsonCamelCase());
    }

    public FabricIntermediaryData getIntermediary() {
        return intermediary;
    }

    public void setIntermediary(FabricIntermediaryData intermediary) {
        this.intermediary = intermediary;
    }

    public FabricLauncherMeta getLauncherMeta() {
        return launcherMeta;
    }

    public void setLauncherMeta(FabricLauncherMeta launcherMeta) {
        this.launcherMeta = launcherMeta;
    }

    public FabricLoaderData getLoader() {
        return loader;
    }

    public void setLoader(FabricLoaderData loader) {
        this.loader = loader;
    }

    @Override
    public String toString() {
        return this.getLoader().getVersion();
    }
}
