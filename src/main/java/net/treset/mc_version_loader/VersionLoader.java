package net.treset.mc_version_loader;

import net.treset.mc_version_loader.fabric.FabricVersion;
import net.treset.mc_version_loader.fabric.FabricVersionDetails;
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

        JavaVersion javaVersion = JavaJsonParser.parseJavaVersion(Sources.getJavaRuntimeJson(), mcDetails.getJavaVersion().getComponent()).get(0);
        JavaManifest javaManifest = JavaJsonParser.parseJavaManifest(Sources.getFileFromUrl(javaVersion.getManifestUrl()));
        File javaDir = new File("./downloads/java");
        javaDir.mkdirs();
        for(JavaFile f : javaManifest.getFiles()) {
            JavaFileDownloader.downloadJavaFile(f, javaDir);
        }

        File clientDir = new File("./downloads/client");
        clientDir.mkdirs();
        MinecraftVersionFileDownloader.downloadVersionDownload(mcDetails.getDownloads().getClient(), clientDir);
        File libsDir = new File("./downloads/libraries");
        libsDir.mkdirs();
        for(MinecraftLibrary l : mcDetails.getLibraries()) {
            MinecraftVersionFileDownloader.downloadVersionLibrary(l, libsDir);
        }
        MinecraftLaunchCommand launchCommand = mcDetails.getArguments().getLaunchCommand("java", "client.jar", mcDetails.getMainClass(), "../libraries" , mcDetails.getLibraries(), new ArrayList<>());
        System.out.println(launchCommand.getLaunchCommand());
        FabricVersionDetails details = FabricJsonParser.parseFabricVersion(Sources.getFabricVersion(fabricVersions.get(0).getMinecraftVersion(), fabricVersions.get(0).getLoaderVersion()));
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