package net.treset.mc_version_loader.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.minecraft.*;

import java.util.ArrayList;
import java.util.List;

public class MinecraftVersionJsonParser {
    public static List<MinecraftVersion> parseVersionManifest(String manifestJson) {
        List<MinecraftVersion> out = new ArrayList<>();

        JsonElement manifest = JsonUtils.parseJson(manifestJson);
        JsonObject manifestObj = JsonUtils.getAsJsonObject(manifest);
        JsonArray versionArray = JsonUtils.getAsJsonArray(manifestObj, "versions");
        if(versionArray != null) {
            for (JsonElement e : versionArray) {
                JsonObject versionObj = JsonUtils.getAsJsonObject(e);
                out.add(new MinecraftVersion(
                        JsonUtils.getAsInt(versionObj, "complianceLevel"),
                        JsonUtils.getAsString(versionObj, "id"),
                        JsonUtils.getAsString(versionObj, "releaseTime"),
                        JsonUtils.getAsString(versionObj, "sha1"),
                        JsonUtils.getAsString(versionObj, "time"),
                        JsonUtils.getAsString(versionObj, "type"),
                        JsonUtils.getAsString(versionObj, "url")
                ));
            }
        }

        return out;
    }

    public static MinecraftVersionDetails parseVersionDetails(String jsonData) {
        JsonObject versionObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(jsonData));
        return new MinecraftVersionDetails(
                JsonUtils.getAsString(versionObj, "id"),
                JsonUtils.getAsString(versionObj, "assets"),
                JsonUtils.getAsInt(versionObj, "complianceLevel"),
                JsonUtils.getAsString(versionObj, "mainClass"),
                JsonUtils.getAsInt(versionObj, "minimumLauncherVersion"),
                JsonUtils.getAsString(versionObj, "releaseTime"),
                JsonUtils.getAsString(versionObj, "time"),
                JsonUtils.getAsString(versionObj, "type"),
                parseVersionArguments(JsonUtils.getAsJsonObject(versionObj, "arguments")),
                parseVersionJavaVersion(JsonUtils.getAsJsonObject(versionObj, "javaVersion")),
                parseVersionAssetIndex(JsonUtils.getAsJsonObject(versionObj, "assetIndex")),
                parseVersionDownloads(JsonUtils.getAsJsonObject(versionObj, "downloads")),
                parseLibraries(JsonUtils.getAsJsonArray(versionObj, "libraries")),
                parseVersionLogging(JsonUtils.getAsJsonObject(versionObj, "logging"))
        );
    }

    private static MinecraftLogging parseVersionLogging(JsonObject loggingObj) {
        JsonObject clientObj = JsonUtils.getAsJsonObject(loggingObj, "client");
        JsonObject fileObj = JsonUtils.getAsJsonObject(clientObj, "file");

        return new MinecraftLogging(
                JsonUtils.getAsString(clientObj, "argument"),
                JsonUtils.getAsString(fileObj, "id"),
                JsonUtils.getAsString(fileObj, "sha1"),
                JsonUtils.getAsInt(fileObj, "size"),
                JsonUtils.getAsString(fileObj, "url"),
                JsonUtils.getAsString(clientObj, "type")
        );
    }

    private static MinecraftFileDownloads parseVersionDownloads(JsonObject downloadsObj) {
        JsonObject clientObj = JsonUtils.getAsJsonObject(downloadsObj, "client");
        JsonObject clientMappingsObj = JsonUtils.getAsJsonObject(downloadsObj, "client_mappings");
        JsonObject serverObj = JsonUtils.getAsJsonObject(downloadsObj, "server");
        JsonObject serverMappingsObj = JsonUtils.getAsJsonObject(downloadsObj, "server_mappings");



        return new MinecraftFileDownloads(
                parseVersionDownload(clientObj),
                parseVersionDownload(clientMappingsObj),
                parseVersionDownload(serverObj),
                parseVersionDownload(serverMappingsObj)
        );
    }

    private static MinecraftFileDownloads.Downloads parseVersionDownload(JsonObject downloadObj) {
        return new MinecraftFileDownloads.Downloads(
                JsonUtils.getAsString(downloadObj, "sha1"),
                JsonUtils.getAsInt(downloadObj, "size"),
                JsonUtils.getAsString(downloadObj, "url")
        );
    }

    private static List<MinecraftLibrary> parseLibraries(JsonArray librariesArray) {
        List<MinecraftLibrary> libraries = new ArrayList<>();
        if(librariesArray != null) {
            for(JsonElement l : librariesArray) {
                libraries.add(parseVersionLibrary(JsonUtils.getAsJsonObject(l)));
            }
        }
        return libraries;
    }

    private static MinecraftLibrary parseVersionLibrary(JsonObject libraryObj) {
        JsonObject artifactObj = JsonUtils.getAsJsonObject(JsonUtils.getAsJsonObject(libraryObj, "downloads"), "artifact");
        JsonArray rulesArray = JsonUtils.getAsJsonArray(libraryObj, "rules");
        List<MinecraftRule> rules = new ArrayList<>();
        if(rulesArray != null) {
            for(JsonElement e: rulesArray) {
                rules.add(parseRule(JsonUtils.getAsJsonObject(e)));
            }
        }

        return new MinecraftLibrary(
                JsonUtils.getAsString(libraryObj, "name"),
                JsonUtils.getAsString(artifactObj, "path"),
                JsonUtils.getAsString(artifactObj, "sha1"),
                JsonUtils.getAsInt(artifactObj, "size"),
                JsonUtils.getAsString(artifactObj, "url"),
                rules
        );
    }

    public static MinecraftJavaVersion parseVersionJavaVersion(JsonObject javaVersionObj) {
        return new MinecraftJavaVersion(
                JsonUtils.getAsString(javaVersionObj, "component"),
                JsonUtils.getAsInt(javaVersionObj, "majorVersion")
        );
    }

    public static MinecraftAssetIndex parseVersionAssetIndex(JsonObject assetIndexObj) {
        return new MinecraftAssetIndex(
                JsonUtils.getAsString(assetIndexObj, "id"),
                JsonUtils.getAsString(assetIndexObj, "sha1"),
                JsonUtils.getAsInt(assetIndexObj, "size"),
                JsonUtils.getAsInt(assetIndexObj, "totalSize"),
                JsonUtils.getAsString(assetIndexObj, "url")
        );
    }

    public static MinecraftLaunchArguments parseVersionArguments(JsonObject argumentsObj) {
        JsonArray gameArgumentArray = JsonUtils.getAsJsonArray(argumentsObj, "game");
        JsonArray jvmArgumentArray = JsonUtils.getAsJsonArray(argumentsObj, "jvm");

        List<MinecraftLaunchArgument> gameArguments = parseArguments(gameArgumentArray);
        List<MinecraftLaunchArgument> jvmArguments = parseArguments(jvmArgumentArray);

        return new MinecraftLaunchArguments(gameArguments, jvmArguments);
    }

    private static List<MinecraftLaunchArgument> parseArguments(JsonArray argumentArray) {
        List<MinecraftLaunchArgument> arguments = new ArrayList<>();
        if(argumentArray != null) {
            for (JsonElement e : argumentArray) {
                String ruleString = JsonUtils.getAsString(e);
                if (ruleString != null) {
                    arguments.add(new MinecraftLaunchArgument(ruleString, null));
                } else {
                    JsonObject eObj = JsonUtils.getAsJsonObject(e);
                    JsonArray rules = JsonUtils.getAsJsonArray(eObj, "rules");
                    List<MinecraftRule> currentRules = new ArrayList<>();
                    if (rules != null) {
                        for (JsonElement r : rules) {
                            JsonObject rObj = JsonUtils.getAsJsonObject(r);
                            currentRules.add(parseRule(rObj));
                        }
                    }
                    JsonArray values = JsonUtils.getAsJsonArray(eObj, "value");
                    if (values != null) {
                        for (JsonElement v : values) {
                            arguments.add(new MinecraftLaunchArgument(JsonUtils.getAsString(v), currentRules));
                        }
                    }
                    String value = JsonUtils.getAsString(eObj, "value");
                    if(value != null) {
                        arguments.add(new MinecraftLaunchArgument(value, currentRules));
                    }
                }

            }
        }
        return arguments;
    }

    private static MinecraftRule parseRule(JsonObject ruleObj) {
        if(ruleObj == null) {
            return new MinecraftRule(null, null, null, null, null, false);
        }
        JsonObject featuresObj = JsonUtils.getAsJsonObject(ruleObj, "features");
        MinecraftLaunchFeature feature = MinecraftLaunchFeature.NONE;
        boolean featureValue = false;
        if(featuresObj != null) {
            if(featuresObj.has("has_custom_resolution")) {
                feature = MinecraftLaunchFeature.HAS_CUSTOM_RESOLUTION;
                featureValue = JsonUtils.getAsBoolean(featuresObj, "has_custom_resolution");
            } else if(featuresObj.has("is_demo_user")) {
                feature = MinecraftLaunchFeature.IS_DEMO_USER;
                featureValue = JsonUtils.getAsBoolean(featuresObj, "is_demo_user");
            }
        }
        JsonObject osObj = JsonUtils.getAsJsonObject(ruleObj, "os");
        return new MinecraftRule(
                JsonUtils.getAsString(ruleObj, "action"),
                JsonUtils.getAsString(osObj, "name"),
                JsonUtils.getAsString(osObj, "arch"),
                JsonUtils.getAsString(osObj, "version"),
                feature,
                featureValue
        );
    }
}
