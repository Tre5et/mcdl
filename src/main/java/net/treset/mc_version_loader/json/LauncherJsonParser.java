package net.treset.mc_version_loader.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.launcher.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LauncherJsonParser {
    public static LauncherManifest parseLauncherManifest(String json, Map<String, LauncherManifestType> typeConversion) {
        JsonObject manifestObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        return new LauncherManifest(
                JsonUtils.getAsString(manifestObj, "type"),
                typeConversion,
                JsonUtils.getAsString(manifestObj, "id"),
                JsonUtils.getAsString(manifestObj, "details"),
                JsonUtils.getAsString(manifestObj, "prefix"),
                JsonUtils.getAsString(manifestObj, "name"),
                parseJsonStringArray(JsonUtils.getAsJsonArray(manifestObj, "included_files")),
                parseJsonStringArray(JsonUtils.getAsJsonArray(manifestObj, "components"))
        );
    }

    public static LauncherManifest parseLauncherManifest(String json) {
        return parseLauncherManifest(json, ManifestTypeUtils.getDefaultConversion());
    }

    public static LauncherDetails parseLauncherDetails(String json) {
        JsonObject launcherDetailsObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        return new LauncherDetails(
                JsonUtils.getAsString(launcherDetailsObj, "active_instance"),
                JsonUtils.getAsString(launcherDetailsObj, "assets_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "gamedata_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "gamedata_type"),
                JsonUtils.getAsString(launcherDetailsObj, "instance_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "instances_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "instances_type"),
                JsonUtils.getAsString(launcherDetailsObj, "java_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "javas_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "javas_type"),
                JsonUtils.getAsString(launcherDetailsObj, "libraries_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "mods_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "mods_type"),
                JsonUtils.getAsString(launcherDetailsObj, "options_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "options_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "options_type"),
                JsonUtils.getAsString(launcherDetailsObj, "resourcepacks_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "resourcepacks_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "resourcepacks_type"),
                JsonUtils.getAsString(launcherDetailsObj, "saves_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "saves_type"),
                JsonUtils.getAsString(launcherDetailsObj, "version_component_type"),
                JsonUtils.getAsString(launcherDetailsObj, "versions_dir"),
                JsonUtils.getAsString(launcherDetailsObj, "versions_type")
        );
    }

    public static InstanceDetails parseInstanceDetails(String json) {
        JsonObject instanceObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        return new InstanceDetails(
                parseFeatures(JsonUtils.getAsJsonObject(instanceObj, "features")),
                parseJsonStringArray(JsonUtils.getAsJsonArray(instanceObj, "ignored_files")),
                parseArguments(JsonUtils.getAsJsonArray(instanceObj, "jvm_arguments")),
                JsonUtils.getAsString(instanceObj, "mods_component"),
                JsonUtils.getAsString(instanceObj, "options_component"),
                JsonUtils.getAsString(instanceObj, "resourcepacks_component"),
                JsonUtils.getAsString(instanceObj, "saves_component"),
                JsonUtils.getAsString(instanceObj, "version_component")
        );
    }

    public static VersionDetails parseVersionDetails(String json) {
        JsonObject versionObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        return new VersionDetails(
                JsonUtils.getAsString(versionObj, "assets"),
                JsonUtils.getAsString(versionObj, "depends"),
                parseArguments(JsonUtils.getAsJsonArray(versionObj, "game_arguments")),
                parseArguments(JsonUtils.getAsJsonArray(versionObj, "jvm_arguments")),
                JsonUtils.getAsString(versionObj, "java"),
                parseJsonStringArray(JsonUtils.getAsJsonArray(versionObj, "libraries")),
                JsonUtils.getAsString(versionObj, "main_class"),
                JsonUtils.getAsString(versionObj, "main_file")
        );
    }

    public static ModsDetails parseModsDetails(String json) {
        JsonObject modsObj = JsonUtils.getAsJsonObject(JsonUtils.parseJson(json));
        return new ModsDetails(
                JsonUtils.getAsString(modsObj, "mods_type"),
                JsonUtils.getAsString(modsObj, "mods_version"),
                parseMods(JsonUtils.getAsJsonArray(modsObj, "mods"))
        );
    }

    private static List<LauncherMod> parseMods(JsonArray modsArray) {
        if(modsArray == null) {
            return null;
        }
        List<LauncherMod> out = new ArrayList<>();
        for(JsonElement e : modsArray) {
            out.add(parseMod(JsonUtils.getAsJsonObject(e)));
        }
        return out;
    }

    private static LauncherMod parseMod(JsonObject modObj) {
        return new LauncherMod(
            JsonUtils.getAsString(modObj, "current_provider"),
            JsonUtils.getAsBoolean(modObj, "enabled"),
            JsonUtils.getAsString(modObj, "id"),
            JsonUtils.getAsString(modObj, "name"),
            parseDownloads(JsonUtils.getAsJsonArray(modObj, "downloads"))
        );
    }

    private static List<LauncherModDownload> parseDownloads(JsonArray downloadsArray) {
        if(downloadsArray == null) {
            return null;
        }
        List<LauncherModDownload> out = new ArrayList<>();
        for(JsonElement e : downloadsArray) {
            out.add(parseModDownload(JsonUtils.getAsJsonObject(e)));
        }
        return out;
    }

    private static LauncherModDownload parseModDownload(JsonObject downloadObj) {
        return new LauncherModDownload(
                JsonUtils.getAsString(downloadObj, "date"),
                JsonUtils.getAsString(downloadObj, "provider"),
                JsonUtils.getAsString(downloadObj, "url"),
                JsonUtils.getAsString(downloadObj, "version")
        );
    }

    private static List<LauncherLaunchArgument> parseArguments(JsonArray argumentsArray) {
        if(argumentsArray == null) {
            return null;
        }
        List<LauncherLaunchArgument> out = new ArrayList<>();
        for(JsonElement e : argumentsArray) {
            out.add(parseArgument(JsonUtils.getAsJsonObject(e)));
        }
        return out;
    }

    private static LauncherLaunchArgument parseArgument(JsonObject argumentObj) {
        return new LauncherLaunchArgument(
                JsonUtils.getAsString(argumentObj, "argument"),
                JsonUtils.getAsString(argumentObj, "feature"),
                JsonUtils.getAsString(argumentObj, "os_name"),
                JsonUtils.getAsString(argumentObj, "os_version")
        );
    }

    private static List<LauncherFeature> parseFeatures(JsonObject featuresObj) {
        Set<Map.Entry<String, JsonElement>> features = JsonUtils.getMembers(featuresObj);
        if(features == null) {
            return null;
        }
        List<LauncherFeature> out = new ArrayList<>();
        for(Map.Entry<String, JsonElement> e : features) {
            if(JsonUtils.getAsString(e.getValue()) != null) {
                out.add(new LauncherFeature(e.getKey(), JsonUtils.getAsString(e.getValue())));
            }
        }
        return out;
    }

    private static List<String> parseJsonStringArray(JsonArray jsonStringArray) {
        if(jsonStringArray == null) {
            return null;
        }
        List<String> out = new ArrayList<>();
        for(JsonElement e : jsonStringArray) {
            out.add(JsonUtils.getAsString(e));
        }
        return out;
    }
}
