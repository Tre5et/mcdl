package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.launcher.LauncherMod;
import net.treset.mc_version_loader.launcher.LauncherModDownload;
import net.treset.mc_version_loader.mods.ModProvider;
import net.treset.mc_version_loader.mods.ModVersionData;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ModFileDownloader {
    public static LauncherMod downloadModFile(ModVersionData data, File parentDir, boolean enabled) throws FileDownloadException {
        if(data == null || data.getParentMod() == null) {
            throw new FileDownloadException("Unable to download mod: unmet requirements: mod=" + data);
        }

        List<ModProvider> providers = data.getParentMod().getModProviders();
        List<String> projectIds = data.getParentMod().getProjectIds();
        if(providers.size() != projectIds.size()) {
            throw new FileDownloadException("Unable to download mod, provider count does not match project id count: mod=" + data.getName());
        }

        String[] urlParts = data.getDownloadUrl().split("/");
        String fileName = urlParts[urlParts.length - 1];
        File modFile = new File(parentDir, fileName);
        URL downloadUrl;
        try {
            downloadUrl = new URL(data.getDownloadUrl());
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to download mod, malformed url: mod=" + data.getName(), e);
        }
        FileUtils.downloadFile(downloadUrl, modFile);

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
                fileName,
                data.getVersionNumber(),
                downloads
        );
    }
}
