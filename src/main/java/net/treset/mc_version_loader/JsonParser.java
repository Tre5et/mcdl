package net.treset.mc_version_loader;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.version.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonParser {
    private static final Logger LOGGER = Logger.getLogger(JsonParser.class.toString());

    public static List<Version> parseVersionManifest(String manifestJson) {
        List<Version> out = new ArrayList<>();

        JsonElement manifest = parseJson(manifestJson);
        JsonObject manifestObj = toJsonObject(manifest);
        JsonArray versionArray = resolvePropertyJsonArray(manifestObj, "versions");
        if(versionArray != null) {
            for (JsonElement e : versionArray) {
                JsonObject versionObj = toJsonObject(e);
                out.add(new Version(
                        resolvePropertyInt(versionObj, "complianceLevel"),
                        resolvePropertyString(versionObj, "id"),
                        resolvePropertyString(versionObj, "releaseTime"),
                        resolvePropertyString(versionObj, "sha1"),
                        resolvePropertyString(versionObj, "time"),
                        resolvePropertyString(versionObj, "type"),
                        resolvePropertyString(versionObj, "url")
                ));
            }
        }

        return out;
    }

    public static VersionDetails parseVersionDetails(String jsonData) {
        JsonObject versionObj = toJsonObject(parseJson(jsonData));
        return new VersionDetails(
                resolvePropertyString(versionObj, "id"),
                resolvePropertyString(versionObj, "assets"),
                resolvePropertyInt(versionObj, "complianceLevel"),
                resolvePropertyString(versionObj, "mainClass"),
                resolvePropertyInt(versionObj, "minimumLauncherVersion"),
                resolvePropertyString(versionObj, "releaseTime"),
                resolvePropertyString(versionObj, "time"),
                resolvePropertyString(versionObj, "type"),
                parseVersionArguments(resolvePropertyJsonObject(versionObj, "arguments")),
                parseVersionJavaVersion(resolvePropertyJsonObject(versionObj, "javaVersion")),
                parseVersionAssetIndex(resolvePropertyJsonObject(versionObj, "assetIndex")),
                parseVersionDownloads(resolvePropertyJsonObject(versionObj, "downloads")),
                parseLibraries(resolvePropertyJsonArray(versionObj, "libraries")),
                parseVersionLogging(resolvePropertyJsonObject(versionObj, "logging"))
        );
    }

    private static VersionLogging parseVersionLogging(JsonObject loggingObj) {
        JsonObject clientObj = resolvePropertyJsonObject(loggingObj, "client");
        JsonObject fileObj = resolvePropertyJsonObject(clientObj, "file");

        return new VersionLogging(
                resolvePropertyString(clientObj, "argument"),
                resolvePropertyString(fileObj, "id"),
                resolvePropertyString(fileObj, "sha1"),
                resolvePropertyInt(fileObj, "size"),
                resolvePropertyString(fileObj, "url"),
                resolvePropertyString(clientObj, "type")
        );
    }

    private static VersionDownloads parseVersionDownloads(JsonObject downloadsObj) {
        JsonObject clientObj = resolvePropertyJsonObject(downloadsObj, "client");
        JsonObject clientMappingsObj = resolvePropertyJsonObject(downloadsObj, "client_mappings");
        JsonObject serverObj = resolvePropertyJsonObject(downloadsObj, "server");
        JsonObject serverMappingsObj = resolvePropertyJsonObject(downloadsObj, "server_mappings");



        return new VersionDownloads(
                parseVersionDownload(clientObj),
                parseVersionDownload(clientMappingsObj),
                parseVersionDownload(serverObj),
                parseVersionDownload(serverMappingsObj)
        );
    }

    private static VersionDownloads.Downloads parseVersionDownload(JsonObject downloadObj) {
        return new VersionDownloads.Downloads(
                resolvePropertyString(downloadObj, "sha1"),
                resolvePropertyInt(downloadObj, "size"),
                resolvePropertyString(downloadObj, "url")
        );
    }

    private static List<VersionLibrary> parseLibraries(JsonArray librariesArray) {
        List<VersionLibrary> libraries = new ArrayList<>();
        if(librariesArray != null) {
            for(JsonElement l : librariesArray) {
                libraries.add(parseVersionLibrary(toJsonObject(l)));
            }
        }
        return libraries;
    }

    private static VersionLibrary parseVersionLibrary(JsonObject libraryObj) {
        JsonObject artifactObj = resolvePropertyJsonObject(resolvePropertyJsonObject(libraryObj, "downloads"), "artifact");
        JsonArray rulesArray = resolvePropertyJsonArray(libraryObj, "rules");
        List<VersionRule> rules = new ArrayList<>();
        if(rulesArray != null) {
            for(JsonElement e: rulesArray) {
                rules.add(parseRule(toJsonObject(e)));
            }
        }

        return new VersionLibrary(
                resolvePropertyString(libraryObj, "name"),
                resolvePropertyString(artifactObj, "path"),
                resolvePropertyString(artifactObj, "sha1"),
                resolvePropertyInt(artifactObj, "size"),
                resolvePropertyString(artifactObj, "url"),
                rules
        );
    }

    public static VersionJavaVersion parseVersionJavaVersion(JsonObject javaVersionObj) {
        return new VersionJavaVersion(
                resolvePropertyString(javaVersionObj, "component"),
                resolvePropertyInt(javaVersionObj, "majorVersion")
        );
    }

    public static VersionAssetIndex parseVersionAssetIndex(JsonObject assetIndexObj) {
        return new VersionAssetIndex(
                resolvePropertyString(assetIndexObj, "id"),
                resolvePropertyString(assetIndexObj, "sha1"),
                resolvePropertyInt(assetIndexObj, "size"),
                resolvePropertyInt(assetIndexObj, "totalSize"),
                resolvePropertyString(assetIndexObj, "url")
        );
    }

    public static VersionArguments parseVersionArguments(JsonObject argumentsObj) {
        JsonArray gameArgumentArray = resolvePropertyJsonArray(argumentsObj, "game");
        JsonArray jvmArgumentArray = resolvePropertyJsonArray(argumentsObj, "jvm");

        List<VersionArgument> gameArguments = parseArguments(gameArgumentArray);
        List<VersionArgument> jvmArguments = parseArguments(jvmArgumentArray);

        return new VersionArguments(gameArguments, jvmArguments);
    }

    private static List<VersionArgument> parseArguments(JsonArray argumentArray) {
        List<VersionArgument> arguments = new ArrayList<>();
        if(argumentArray != null) {
            for (JsonElement e : argumentArray) {
                String ruleString = toString(e);
                if (ruleString != null) {
                    arguments.add(new VersionArgument(ruleString, null));
                } else {
                    JsonObject eObj = toJsonObject(e);
                    JsonArray rules = resolvePropertyJsonArray(eObj, "rules");
                    List<VersionRule> currentRules = new ArrayList<>();
                    if (rules != null) {
                        for (JsonElement r : rules) {
                            JsonObject rObj = toJsonObject(r);
                            currentRules.add(parseRule(rObj));
                        }
                    }
                    JsonArray values = resolvePropertyJsonArray(eObj, "value");
                    if (values != null) {
                        for (JsonElement v : values) {
                            arguments.add(new VersionArgument(toString(v), currentRules));
                        }
                    }
                }

            }
        }
        return arguments;
    }

    private static VersionRule parseRule(JsonObject ruleObj) {
        if(ruleObj == null) {
            return new VersionRule(null, null, null, null, null, false);
        }
        JsonObject featuresObj = resolvePropertyJsonObject(ruleObj, "features");
        VersionFeature feature = VersionFeature.NONE;
        boolean featureValue = false;
        if(featuresObj != null) {
            if(featuresObj.has("has_custom_resolution")) {
                feature = VersionFeature.HAS_CUSTOM_RESOLUTION;
                featureValue = resolvePropertyBool(featuresObj, "has_custom_resolution");
            } else if(featuresObj.has("is_demo_user")) {
                feature = VersionFeature.IS_DEMO_USER;
                featureValue = resolvePropertyBool(featuresObj, "is_demo_user");
            }
        }
        JsonObject osObj = resolvePropertyJsonObject(ruleObj, "os");
        return new VersionRule(
                resolvePropertyString(ruleObj, "action"),
                resolvePropertyString(osObj, "name"),
                resolvePropertyString(osObj, "arch"),
                resolvePropertyString(osObj, "version"),
                feature,
                featureValue
        );
    }

    public static JsonObject toJsonObject(JsonElement element) {
        if(element != null && element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json object " + element);
        return null;
    }

    public static JsonArray toJsonArray(JsonElement element) {
        if(element != null && element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json array " + element);
        return null;
    }

    public static String toString(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsJsonPrimitive().getAsString();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json string " + element);
        return null;
    }

    public static int toInt(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsJsonPrimitive().getAsInt();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json string " + element);
        return -1;
    }

    public static boolean toBoolean(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsJsonPrimitive().getAsBoolean();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json string " + element);
        return false;
    }

    public static JsonObject resolvePropertyJsonObject(JsonObject obj, String propertyName) {
        if(obj != null && obj.get(propertyName) != null && obj.get(propertyName).isJsonObject()) {
            return obj.getAsJsonObject(propertyName);
        }
        LOGGER.log(Level.WARNING, "Unable to read requested json object " + propertyName);
        return null;
    }

    public static JsonArray resolvePropertyJsonArray(JsonObject obj, String propertyName) {
        if(obj != null && obj.get(propertyName) != null && obj.get(propertyName).isJsonArray()) {
            return obj.getAsJsonArray(propertyName);
        }
        LOGGER.log(Level.WARNING, "Unable to read requested json array " + propertyName);
        return null;
    }

    public static String resolvePropertyString(JsonObject obj, String propertyName) {
        if(obj != null && obj.get(propertyName) != null && obj.get(propertyName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(propertyName).isString()) {
                return obj.getAsJsonPrimitive(propertyName).getAsString();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested string property " + propertyName);
        return null;
    }

    public static int resolvePropertyInt(JsonObject obj, String propertyName) {
        if(obj != null && obj.get(propertyName) != null && obj.get(propertyName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(propertyName).isNumber()) {
                return obj.getAsJsonPrimitive(propertyName).getAsInt();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested int property " + propertyName);
        return -1;
    }

    public static boolean resolvePropertyBool(JsonObject obj, String propertyName) {
        if(obj != null && obj.get(propertyName) != null && obj.get(propertyName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(propertyName).isBoolean()) {
                return obj.getAsJsonPrimitive(propertyName).getAsBoolean();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested boolean property " + propertyName);
        return false;
    }


    public static JsonElement parseJson(String json) {
        try {
            return com.google.gson.JsonParser.parseString(json);
        } catch(Exception e) {
            LOGGER.log(Level.WARNING, "Unable to parse json", e);
        }
        return null;
    }
}
