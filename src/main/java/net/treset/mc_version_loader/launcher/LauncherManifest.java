package net.treset.mc_version_loader.launcher;

import java.util.List;
import java.util.Map;

public class LauncherManifest {
    private LauncherManifestType type;
    private String id;
    private String details;
    private String prefix;
    private String name;
    private List<String> includedFiles;
    private List<String> components;

    public LauncherManifest(String type, String id, String details, String prefix, String name, List<String> includedFiles, List<String> components) {
        this(type, ManifestTypeUtils.getDefaultConversion(), id, details, prefix, name, includedFiles, components);
    }

    public LauncherManifest(String type, Map<String, LauncherManifestType> typeConversion, String id, String details, String prefix, String name, List<String> includedFiles, List<String> components) {
        this.type = ManifestTypeUtils.getLauncherManifestType(type, typeConversion);
        this.id = id;
        this.details = details;
        this.prefix = prefix;
        this.name = name;
        this.includedFiles = includedFiles;
        this.components = components;
    }

    public LauncherManifestType getType() {
        return type;
    }

    public void setType(LauncherManifestType type) {
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
}
