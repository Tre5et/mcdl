package net.treset.mc_version_loader.launcher;

import java.util.List;

public class LauncherMod {
    private String currentProvider;
    private boolean enabled;
    private String id;
    private String name;
    private List<LauncherModDownload> downloads;

    public LauncherMod(String currentProvider, boolean enabled, String id, String name, List<LauncherModDownload> downloads) {
        this.currentProvider = currentProvider;
        this.enabled = enabled;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
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
