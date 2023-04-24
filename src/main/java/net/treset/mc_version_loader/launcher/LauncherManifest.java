package net.treset.mc_version_loader.launcher;

import com.google.gson.*;
import net.treset.mc_version_loader.VersionLoader;
import net.treset.mc_version_loader.json.GenericJsonParsable;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.List;
import java.util.Map;

public class LauncherManifest extends GenericJsonParsable {
    private String type;
    private String id;
    private String details;
    private String prefix;
    private String name;
    private List<String> includedFiles;
    private List<String> components;
    private transient Map<String, LauncherManifestType> typeConversion;

    public LauncherManifest(String type, Map<String, LauncherManifestType> typeConversion, String id, String details, String prefix, String name, List<String> includedFiles, List<String> components) {
        this.type = type;
        this.id = id;
        this.details = details;
        this.prefix = prefix;
        this.name = name;
        this.includedFiles = includedFiles;
        this.components = components;
        this.typeConversion = typeConversion;
    }

    public static LauncherManifest fromJson(String json, Map<String, LauncherManifestType> typeConversion) {
        LauncherManifest launcherManifest = fromJson(json, LauncherManifest.class);
        launcherManifest.setTypeConversion(typeConversion);
        return launcherManifest;
    }

    public static LauncherManifest fromJson(String json) {
        return fromJson(json, LauncherManifestTypeUtils.getDefaultConversion());
    }

    public LauncherManifestType getType() {
        return LauncherManifestTypeUtils.getLauncherManifestType(type, typeConversion);
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getIncludedFiles() {
        return includedFiles;
    }

    public void setIncludedFiles(List<String> includedFiles) {
        this.includedFiles = includedFiles;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }

    public Map<String, LauncherManifestType> getTypeConversion() {
        return typeConversion;
    }

    public void setTypeConversion(Map<String, LauncherManifestType> typeConversion) {
        this.typeConversion = typeConversion;
    }
}
