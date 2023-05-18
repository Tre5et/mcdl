package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.launcher.LauncherMod;
import net.treset.mc_version_loader.launcher.LauncherModDownload;
import net.treset.mc_version_loader.mods.ModProvider;
import net.treset.mc_version_loader.mods.ModVersionData;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ModFileDownloader {
    private static final Logger LOGGER = LogManager.getLogManager().getLogger(ModFileDownloader.class.toString());

    public static LauncherMod downloadModFile(ModVersionData data, File parentDir, boolean enabled) {
        if(data.getParentMod() == null) {
            LOGGER.log(Level.SEVERE, "Unable to download mod, parent mod is null");
            return null;
        }

        List<ModProvider> providers = data.getParentMod().getModProviders();
        List<String> projectIds = data.getParentMod().getProjectIds();
        if(providers.size() != projectIds.size()) {
            LOGGER.log(Level.SEVERE, "Unable to download mod, provider count does not match project id count");
            return null;
        }

        String[] urlParts = data.getDownloadUrl().split("/");
        String fileName = urlParts[urlParts.length - 1];
        File modFile = new File(parentDir, fileName);
        URL downloadUrl;
        try {
            downloadUrl = new URL(data.getDownloadUrl());
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "Unable to download mod, malformed url: " + data.getDownloadUrl(), e);
            return null;
        }
        if(!FileUtils.downloadFile(downloadUrl, modFile)) {
            LOGGER.log(Level.SEVERE, "Unable to download mod, download failed");
            return null;
        }

        ArrayList<LauncherModDownload> downloads = new ArrayList<>();
        for(int i = 0; i < providers.size(); i++) {
            downloads.add(new LauncherModDownload(providers.get(i).toString().toLowerCase(), projectIds.get(i)));
        }

        return new LauncherMod(
                data.getModProviders().get(0).toString().toLowerCase(),
                data.getParentMod().getDescription(),
                enabled,
                data.getParentMod().getIconUrl(),
                data.getParentMod().getName(),
                downloads,
                fileName
        );
    }
}
