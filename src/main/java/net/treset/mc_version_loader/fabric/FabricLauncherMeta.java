package net.treset.mc_version_loader.fabric;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.List;

public class FabricLauncherMeta {
    private FabricLauncherMetaLibraries libraries;
    private FabricLauncherMetaMainClass mainClass;
    private int version;

    public FabricLauncherMeta(FabricLauncherMetaLibraries libraries, FabricLauncherMetaMainClass mainClass, int version) {
        this.libraries = libraries;
        this.mainClass = mainClass;
        this.version = version;
    }

    public FabricLauncherMetaLibraries getLibraries() {
        return libraries;
    }

    public void setLibraries(FabricLauncherMetaLibraries libraries) {
        this.libraries = libraries;
    }

    public FabricLauncherMetaMainClass getMainClass() {
        return mainClass;
    }

    public void setMainClass(FabricLauncherMetaMainClass mainClass) {
        this.mainClass = mainClass;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
