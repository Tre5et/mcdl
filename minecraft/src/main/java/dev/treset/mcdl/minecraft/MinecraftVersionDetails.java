package dev.treset.mcdl.minecraft;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.HttpUtil;

import java.io.IOException;
import java.util.List;

@Deprecated
public class MinecraftVersionDetails extends MinecraftProfile {
    /**
     * Deprecated: Use {@link MinecraftProfile#MinecraftProfile(String, String, int, String, int, String, String, String, MinecraftLaunchArguments, MinecraftJavaVersion, MinecraftAssetIndex, MinecraftFileDownloads, List, MinecraftLogging, String)} instead
     */
    @Deprecated
    public MinecraftVersionDetails(String id, String assets, int complianceLevel, String mainClass, int minimumLauncherVersion, String releaseTime, String time, String type, MinecraftLaunchArguments command, MinecraftJavaVersion javaVersion, MinecraftAssetIndex assetIndex, MinecraftFileDownloads downloads, List<MinecraftLibrary> libraries, MinecraftLogging logging, String minecraftArguments) {
        super(id, assets, complianceLevel, mainClass, minimumLauncherVersion, releaseTime, time, type, command, javaVersion, assetIndex, downloads, libraries, logging, minecraftArguments);
    }

    /**
     * Deprecated: Use {@link MinecraftProfile#fromJson(String)} instead
     */
    @Deprecated
    public static MinecraftVersionDetails fromJson(String json) throws SerializationException {
        MinecraftVersionDetails details = GenericJsonParsable.fromJson(json, MinecraftVersionDetails.class);
        modifyFromJson(details, json);
        return details;
    }

    /**
     * Deprecated: Use {@link MinecraftProfile#get(String)} instead
     */
    @Deprecated
    public static MinecraftVersionDetails get(String url) throws FileDownloadException {
        try {
            return MinecraftVersionDetails.fromJson(HttpUtil.getString(url, MinecraftDL.getCaching()));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse version manifest", e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download version manifest", e);
        }
    }

    /**
     * Deprecated: Use {@link MinecraftProfile#getForVersion(String)} instead
     */
    @Deprecated()
    public static MinecraftProfile getForVersion(String version) throws FileDownloadException {
        return get(MinecraftVersion.get(version).getUrl());
    }
}
