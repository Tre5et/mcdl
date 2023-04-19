package net.treset.mc_version_loader.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.fabric.*;

import java.util.ArrayList;
import java.util.List;

public class FabricJsonParser {
    public static List<FabricVersion> parseFabricManifest(String manifestJson) {
        JsonArray manifestArray = JsonUtils.getAsJsonArray(JsonUtils.parseJson(manifestJson));
        List<FabricVersion> versions = new ArrayList<>();
        if(manifestArray != null) {
            for(JsonElement v : manifestArray) {
                JsonObject vObj = JsonUtils.getAsJsonObject(v);
                versions.add(new FabricVersion(
                        JsonUtils.getAsString(JsonUtils.getAsJsonObject(vObj, "intermediary"), "version"),
                        JsonUtils.getAsString(JsonUtils.getAsJsonObject(vObj, "loader"), "version")
                ));
            }
        }
        return versions;
    }

    public static FabricVersionDetails parseFabricVersion(String versionJson) {
        JsonObject versionObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(versionJson));
        return new FabricVersionDetails(
                parseFabricIntermediary(JsonUtils.getAsJsonObject(versionObj, "intermediary")),
                parseFabricLauncherMeta(JsonUtils.getAsJsonObject(versionObj, "launcherMeta")),
                parseFabricLoader(JsonUtils.getAsJsonObject(versionObj, "loader"))
        );
    }

    private static FabricIntermediaryData parseFabricIntermediary(JsonObject intermediaryObj) {
        return new FabricIntermediaryData(
                JsonUtils.getAsString(intermediaryObj, "maven"),
                JsonUtils.getAsBoolean(intermediaryObj, "stable"),
                JsonUtils.getAsString(intermediaryObj, "version")
        );
    }

    private static FabricLauncherMeta parseFabricLauncherMeta(JsonObject launcherMetaObj) {
        JsonObject librariesObj = JsonUtils.getAsJsonObject(launcherMetaObj, "libraries");
        JsonObject mainClassObj = JsonUtils.getAsJsonObject(launcherMetaObj, "mainClass");

        return new FabricLauncherMeta(
                parseFabricLibraries(JsonUtils.getAsJsonArray(librariesObj, "client")),
                parseFabricLibraries(JsonUtils.getAsJsonArray(librariesObj, "common")),
                parseFabricLibraries(JsonUtils.getAsJsonArray(librariesObj, "server")),
                JsonUtils.getAsString(mainClassObj, "client"),
                JsonUtils.getAsString(mainClassObj, "server"),
                JsonUtils.getAsInt(launcherMetaObj, "version")
        );
    }

    private static FabricLoaderData parseFabricLoader(JsonObject loaderObj) {
        return new FabricLoaderData(
                JsonUtils.getAsInt(loaderObj, "build"),
                JsonUtils.getAsString(loaderObj, "maven"),
                JsonUtils.getAsString(loaderObj, "separator"),
                JsonUtils.getAsBoolean(loaderObj, "stable"),
                JsonUtils.getAsString(loaderObj, "version")
        );
    }

    private static List<FabricLibrary> parseFabricLibraries(JsonArray libraryArray) {
        List<FabricLibrary> libraries = new ArrayList<>();
        if(libraryArray != null) {
            for(JsonElement e : libraryArray) {
                JsonObject eObj = JsonUtils.getAsJsonObject(e);
                libraries.add(parseFabricLibrary(eObj));
            }
        }
        return libraries;
    }

    private static FabricLibrary parseFabricLibrary(JsonObject libraryObj) {
        return new FabricLibrary(
                JsonUtils.getAsString(libraryObj, "name"),
                JsonUtils.getAsString(libraryObj, "url")
        );
    }
}
