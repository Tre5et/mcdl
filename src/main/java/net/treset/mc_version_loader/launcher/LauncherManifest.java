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
}
