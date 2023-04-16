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
        JsonObject manifestObj = getAsJsonObject(manifest);
        JsonArray versionArray = getAsJsonArray(manifestObj, "versions");
        if(versionArray != null) {
            for (JsonElement e : versionArray) {
                JsonObject versionObj = getAsJsonObject(e);
                out.add(new Version(
                        getAsInt(versionObj, "complianceLevel"),
                        getAsString(versionObj, "id"),
                        getAsString(versionObj, "releaseTime"),
                        getAsString(versionObj, "sha1"),
                        getAsString(versionObj, "time"),
                        getAsString(versionObj, "type"),
                        getAsString(versionObj, "url")
                ));
            }
        }

        return out;
    }

    public static VersionDetails parseVersionDetails(String jsonData) {
        JsonObject versionObj = getAsJsonObject(parseJson(jsonData));
        return new VersionDetails(
                getAsString(versionObj, "id"),
                getAsString(versionObj, "assets"),
                getAsInt(versionObj, "complianceLevel"),
                getAsString(versionObj, "mainClass"),
                getAsInt(versionObj, "minimumLauncherVersion"),
                getAsString(versionObj, "releaseTime"),
                getAsString(versionObj, "time"),
                getAsString(versionObj, "type"),
                parseVersionArguments(getAsJsonObject(versionObj, "arguments")),
                parseVersionJavaVersion(getAsJsonObject(versionObj, "javaVersion")),
                parseVersionAssetIndex(getAsJsonObject(versionObj, "assetIndex")),
                parseVersionDownloads(getAsJsonObject(versionObj, "downloads")),
                parseLibraries(getAsJsonArray(versionObj, "libraries")),
                parseVersionLogging(getAsJsonObject(versionObj, "logging"))
        );
    }

    private static VersionLogging parseVersionLogging(JsonObject loggingObj) {
        JsonObject clientObj = getAsJsonObject(loggingObj, "client");
        JsonObject fileObj = getAsJsonObject(clientObj, "file");

        return new VersionLogging(
                getAsString(clientObj, "argument"),
                getAsString(fileObj, "id"),
                getAsString(fileObj, "sha1"),
                getAsInt(fileObj, "size"),
                getAsString(fileObj, "url"),
                getAsString(clientObj, "type")
        );
    }

    private static VersionDownloads parseVersionDownloads(JsonObject downloadsObj) {
        JsonObject clientObj = getAsJsonObject(downloadsObj, "client");
        JsonObject clientMappingsObj = getAsJsonObject(downloadsObj, "client_mappings");
        JsonObject serverObj = getAsJsonObject(downloadsObj, "server");
        JsonObject serverMappingsObj = getAsJsonObject(downloadsObj, "server_mappings");



        return new VersionDownloads(
                parseVersionDownload(clientObj),
                parseVersionDownload(clientMappingsObj),
                parseVersionDownload(serverObj),
                parseVersionDownload(serverMappingsObj)
        );
    }

    private static VersionDownloads.Downloads parseVersionDownload(JsonObject downloadObj) {
        return new VersionDownloads.Downloads(
                getAsString(downloadObj, "sha1"),
                getAsInt(downloadObj, "size"),
                getAsString(downloadObj, "url")
        );
    }

    private static List<VersionLibrary> parseLibraries(JsonArray librariesArray) {
        List<VersionLibrary> libraries = new ArrayList<>();
        if(librariesArray != null) {
            for(JsonElement l : librariesArray) {
                libraries.add(parseVersionLibrary(getAsJsonObject(l)));
            }
        }
        return libraries;
    }

    private static VersionLibrary parseVersionLibrary(JsonObject libraryObj) {
        JsonObject artifactObj = getAsJsonObject(getAsJsonObject(libraryObj, "downloads"), "artifact");
        JsonArray rulesArray = getAsJsonArray(libraryObj, "rules");
        List<VersionRule> rules = new ArrayList<>();
        if(rulesArray != null) {
            for(JsonElement e: rulesArray) {
                rules.add(parseRule(getAsJsonObject(e)));
            }
        }

        return new VersionLibrary(
                getAsString(libraryObj, "name"),
                getAsString(artifactObj, "path"),
                getAsString(artifactObj, "sha1"),
                getAsInt(artifactObj, "size"),
                getAsString(artifactObj, "url"),
                rules
        );
    }

    public static VersionJavaVersion parseVersionJavaVersion(JsonObject javaVersionObj) {
        return new VersionJavaVersion(
                getAsString(javaVersionObj, "component"),
                getAsInt(javaVersionObj, "majorVersion")
        );
    }

    public static VersionAssetIndex parseVersionAssetIndex(JsonObject assetIndexObj) {
        return new VersionAssetIndex(
                getAsString(assetIndexObj, "id"),
                getAsString(assetIndexObj, "sha1"),
                getAsInt(assetIndexObj, "size"),
                getAsInt(assetIndexObj, "totalSize"),
                getAsString(assetIndexObj, "url")
        );
    }

    public static VersionArguments parseVersionArguments(JsonObject argumentsObj) {
        JsonArray gameArgumentArray = getAsJsonArray(argumentsObj, "game");
        JsonArray jvmArgumentArray = getAsJsonArray(argumentsObj, "jvm");

        List<VersionArgument> gameArguments = parseArguments(gameArgumentArray);
        List<VersionArgument> jvmArguments = parseArguments(jvmArgumentArray);

        return new VersionArguments(gameArguments, jvmArguments);
    }

    private static List<VersionArgument> parseArguments(JsonArray argumentArray) {
        List<VersionArgument> arguments = new ArrayList<>();
        if(argumentArray != null) {
            for (JsonElement e : argumentArray) {
                String ruleString = getAsString(e);
                if (ruleString != null) {
                    arguments.add(new VersionArgument(ruleString, null));
                } else {
                    JsonObject eObj = getAsJsonObject(e);
                    JsonArray rules = getAsJsonArray(eObj, "rules");
                    List<VersionRule> currentRules = new ArrayList<>();
                    if (rules != null) {
                        for (JsonElement r : rules) {
                            JsonObject rObj = getAsJsonObject(r);
                            currentRules.add(parseRule(rObj));
                        }
                    }
                    JsonArray values = getAsJsonArray(eObj, "value");
                    if (values != null) {
                        for (JsonElement v : values) {
                            arguments.add(new VersionArgument(getAsString(v), currentRules));
                        }
                    }
                    String value = getAsString(eObj, "value");
                    if(value != null) {
                        arguments.add(new VersionArgument(value, currentRules));
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
        JsonObject featuresObj = getAsJsonObject(ruleObj, "features");
        VersionFeature feature = VersionFeature.NONE;
        boolean featureValue = false;
        if(featuresObj != null) {
            if(featuresObj.has("has_custom_resolution")) {
                feature = VersionFeature.HAS_CUSTOM_RESOLUTION;
                featureValue = getAsBool(featuresObj, "has_custom_resolution");
            } else if(featuresObj.has("is_demo_user")) {
                feature = VersionFeature.IS_DEMO_USER;
                featureValue = getAsBool(featuresObj, "is_demo_user");
            }
        }
        JsonObject osObj = getAsJsonObject(ruleObj, "os");
        return new VersionRule(
                getAsString(ruleObj, "action"),
                getAsString(osObj, "name"),
                getAsString(osObj, "arch"),
                getAsString(osObj, "version"),
                feature,
                featureValue
        );
    }

    public static JsonObject getAsJsonObject(JsonElement element) {
        if(element != null && element.isJsonObject()) {
            return element.getAsJsonObject();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json object " + element);
        return null;
    }

    public static JsonArray getAsJsonArray(JsonElement element) {
        if(element != null && element.isJsonArray()) {
            return element.getAsJsonArray();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json array " + element);
        return null;
    }

    public static String getAsString(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isString()) {
            return element.getAsJsonPrimitive().getAsString();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json string " + element);
        return null;
    }

    public static int getAsInt(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
            return element.getAsJsonPrimitive().getAsInt();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json string " + element);
        return -1;
    }

    public static boolean getAsBoolean(JsonElement element) {
        if(element != null && element.isJsonPrimitive() && element.getAsJsonPrimitive().isBoolean()) {
            return element.getAsJsonPrimitive().getAsBoolean();
        }
        LOGGER.log(Level.WARNING, "Unable to convert to json string " + element);
        return false;
    }

    public static JsonObject getAsJsonObject(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonObject()) {
            return obj.getAsJsonObject(memberName);
        }
        LOGGER.log(Level.WARNING, "Unable to read requested json object " + memberName);
        return null;
    }

    public static JsonArray getAsJsonArray(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonArray()) {
            return obj.getAsJsonArray(memberName);
        }
        LOGGER.log(Level.WARNING, "Unable to read requested json array " + memberName);
        return null;
    }

    public static String getAsString(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(memberName).isString()) {
                return obj.getAsJsonPrimitive(memberName).getAsString();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested string property " + memberName);
        return null;
    }

    public static int getAsInt(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(memberName).isNumber()) {
                return obj.getAsJsonPrimitive(memberName).getAsInt();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested int property " + memberName);
        return -1;
    }

    public static boolean getAsBool(JsonObject obj, String memberName) {
        if(obj != null && obj.get(memberName) != null && obj.get(memberName).isJsonPrimitive()) {
            if (obj.getAsJsonPrimitive(memberName).isBoolean()) {
                return obj.getAsJsonPrimitive(memberName).getAsBoolean();
            }
        }
        LOGGER.log(Level.WARNING, "Unable to read requested boolean property " + memberName);
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
