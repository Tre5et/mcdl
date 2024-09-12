package net.treset.mcdl.forge;

import net.treset.mcdl.minecraft.MinecraftLaunchArguments;
import net.treset.mcdl.minecraft.MinecraftLibrary;

import java.util.List;

public class ForgeInstallData {
    private String id;
    private String inheritsFrom;
    private String type;
    private String mainClass;
    private List<MinecraftLibrary> libraries;
    private MinecraftLaunchArguments arguments;

    public ForgeInstallData(String id, String inheritsFrom, String type, String mainClass, List<MinecraftLibrary> libraries, MinecraftLaunchArguments arguments) {
        this.id = id;
        this.inheritsFrom = inheritsFrom;
        this.type = type;
        this.mainClass = mainClass;
        this.libraries = libraries;
        this.arguments = arguments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInheritsFrom() {
        return inheritsFrom;
    }

    public void setInheritsFrom(String inheritsFrom) {
        this.inheritsFrom = inheritsFrom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMainClass() {
        return mainClass;
    }

    public void setMainClass(String mainClass) {
        this.mainClass = mainClass;
    }

    public List<MinecraftLibrary> getLibraries() {
        return libraries;
    }

    public void setLibraries(List<MinecraftLibrary> libraries) {
        this.libraries = libraries;
    }

    public MinecraftLaunchArguments getArguments() {
        return arguments;
    }

    public void setArguments(MinecraftLaunchArguments arguments) {
        this.arguments = arguments;
    }
}
