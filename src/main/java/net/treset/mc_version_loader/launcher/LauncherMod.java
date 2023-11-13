package net.treset.mc_version_loader.launcher;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.mods.MinecraftMods;
import net.treset.mc_version_loader.util.FileUtil;
import net.treset.mc_version_loader.util.Sources;
import net.treset.mc_version_loader.mods.CombinedModData;
import net.treset.mc_version_loader.mods.ModData;
import net.treset.mc_version_loader.mods.curseforge.CurseforgeMod;
import net.treset.mc_version_loader.mods.modrinth.ModrinthMod;

import java.util.ArrayList;
import java.util.List;

public class LauncherMod {
    private String currentProvider;
    private String description;
    private boolean enabled;
    private String url;
    private String iconUrl;
    private String name;
    private String fileName;
    private String version;
    private List<LauncherModDownload> downloads;

    public LauncherMod(String currentProvider, String description, boolean enabled, String url, String iconUrl, String name, String fileName, String version, List<LauncherModDownload> downloads) {
        this.currentProvider = currentProvider;
        this.description = description;
        this.enabled = enabled;
        this.url = url;
        this.iconUrl = iconUrl;
        this.name = name;
        this.fileName = fileName;
        this.version = version;
        this.downloads = downloads;
    }

    public ModData getModData() throws FileDownloadException {
        if(MinecraftMods.getModrinthUserAgent() == null || MinecraftMods.getCurseforgeApiKey().isBlank()) {
            throw new FileDownloadException("Modrinth user agent or curseforge api key not set");
        }
        ArrayList<ModData> mods = new ArrayList<>();
        for(LauncherModDownload download : downloads) {
            if(download.getProvider().equals("modrinth")) {
                String json = FileUtil.getStringFromHttpGet(Sources.getModrinthProjectUrl(download.getId()), Sources.getModrinthHeaders(MinecraftMods.getModrinthUserAgent()), List.of());
                if(json == null || json.isBlank()) {
                    continue;
                }
                ModrinthMod modrinthMod;
                try {
                    modrinthMod = ModrinthMod.fromJson(json);
                } catch (SerializationException e) {
                    throw new FileDownloadException("Could not parse modrinth mod data: " + json, e);
                }
                if(modrinthMod.getName() != null && !modrinthMod.getName().isBlank()) {
                    mods.add(modrinthMod);
                }
            }
            if(download.getProvider().equals("curseforge")) {
                String json = FileUtil.getStringFromHttpGet(Sources.getCurseforgeProjectUrl(Integer.parseInt(download.getId())), Sources.getCurseforgeHeaders(MinecraftMods.getCurseforgeApiKey()), List.of());
                if(json == null || json.isBlank()) {
                    continue;
                }
                CurseforgeMod curseforgeMod;
                try {
                    curseforgeMod = CurseforgeMod.fromJson(json);
                } catch (SerializationException e) {
                    throw new FileDownloadException("Could not parse curseforge mod data: " + json, e);
                }
                if(curseforgeMod.getName() != null && !curseforgeMod.getName().isBlank()) {
                    mods.add(curseforgeMod);
                }
            }
        }
        if(mods.isEmpty()) {
            throw new FileDownloadException("No mod data found: mod=" + name);
        }
        if(mods.size() == 1) {
            return mods.get(0);
        }
        return new CombinedModData(mods.get(0), mods.get(1));
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<LauncherModDownload> getDownloads() {
        return downloads;
    }

    public void setDownloads(List<LauncherModDownload> downloads) {
        this.downloads = downloads;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
