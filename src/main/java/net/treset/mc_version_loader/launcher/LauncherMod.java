package net.treset.mc_version_loader.launcher;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;
import net.treset.mc_version_loader.mods.ModData;

import java.util.ArrayList;
import java.util.List;

public class LauncherMod {
    private String currentProvider;
    private String description;
    private boolean enabled;
    private String iconUrl;
    private String id;
    private String name;
    private List<LauncherModDownload> downloads;

    public LauncherMod(String currentProvider, String description, boolean enabled, String iconUrl, String id, String name, List<LauncherModDownload> downloads) {
        this.currentProvider = currentProvider;
        this.description = description;
        this.enabled = enabled;
        this.iconUrl = iconUrl;
        this.id = id;
        this.name = name;
        this.downloads = downloads;
    }

    public String getCurrentProvider() {
        return currentProvider;
    }

    public void setCurrentProvider(String currentProvider) {
        this.currentProvider = currentProvider;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LauncherModDownload> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<LauncherModDownload> downloads) {
        this.downloads = downloads;
    }
}
