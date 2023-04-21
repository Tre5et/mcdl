package net.treset.mc_version_loader;

import net.treset.mc_version_loader.fabric.FabricLibrary;
import net.treset.mc_version_loader.fabric.FabricVersion;
import net.treset.mc_version_loader.fabric.FabricVersionDetails;
import net.treset.mc_version_loader.files.FabricFileDownloader;
import net.treset.mc_version_loader.files.JavaFileDownloader;
import net.treset.mc_version_loader.files.MinecraftVersionFileDownloader;
import net.treset.mc_version_loader.files.Sources;
import net.treset.mc_version_loader.java.JavaFile;
import net.treset.mc_version_loader.java.JavaManifest;
import net.treset.mc_version_loader.java.JavaVersion;
import net.treset.mc_version_loader.json.FabricJsonParser;
import net.treset.mc_version_loader.json.JavaJsonParser;
import net.treset.mc_version_loader.json.MinecraftVersionJsonParser;
import net.treset.mc_version_loader.minecraft.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VersionLoader {
    public static void main(String[] args) {

        List<MinecraftVersion> mcVersions = getReleases();
        MinecraftVersion minecraftVersion = null;
        for(MinecraftVersion v : mcVersions) {
            if(v.getId().equals("1.19.4")) {
                minecraftVersion = v;
                break;
            }
        }
        MinecraftVersionDetails mcDetails = MinecraftVersionJsonParser.parseVersionDetails(Sources.getFileFromUrl(minecraftVersion.getUrl()));
        List<FabricVersion> fabricVersions = FabricJsonParser.parseFabricManifest(Sources.getFabricForMinecraftVersion("1.19.4"));

        FabricVersionDetails details = FabricJsonParser.parseFabricVersion(Sources.getFabricVersion(fabricVersions.get(0).getMinecraftVersion(), fabricVersions.get(0).getLoaderVersion()));
        File fabricPath = new File("./downloads/fabric-client");
        fabricPath.mkdirs();
        FabricFileDownloader.downloadFabricLoader(fabricPath, details.getLoader());

        File fabricLibraryPath = new File("./downloads/fabric-libraries");
        fabricLibraryPath.mkdirs();
        for(FabricLibrary l : details.getLauncherMeta().getLibrariesCommon()) {
            FabricFileDownloader.downloadFabricLibrary(fabricLibraryPath, l);
        }

        return;
    }

    public static List<MinecraftVersion> getVersions() {
        return MinecraftVersionJsonParser.parseVersionManifest(Sources.getVersionManifestJson());
    }

    public static List<MinecraftVersion> getReleases() {
        List<MinecraftVersion> releases = new ArrayList<>();
        List<MinecraftVersion> minecraftVersions = getVersions();
        for(MinecraftVersion v : minecraftVersions) {
            if(v.isRelease()) {
                releases.add(v);
            }
        }
        return releases;
    }
}