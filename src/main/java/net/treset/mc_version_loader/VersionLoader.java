package net.treset.mc_version_loader;

import net.treset.mc_version_loader.fabric.FabricLoaderData;
import net.treset.mc_version_loader.fabric.FabricVersion;
import net.treset.mc_version_loader.fabric.FabricVersionDetails;
import net.treset.mc_version_loader.java.JavaFile;
import net.treset.mc_version_loader.java.JavaManifest;
import net.treset.mc_version_loader.java.JavaVersion;
import net.treset.mc_version_loader.version.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VersionLoader {
    public static void main(String[] args) {

        List<Version> mcVersions = getReleases();
        Version version = null;
        for(Version v : mcVersions) {
            if(v.getId().equals("1.19.4")) {
                version = v;
                break;
            }
        }
        VersionDetails mcDetails = JsonParser.parseVersionDetails(Sources.getFileFromUrl(version.getUrl()));
        List<FabricVersion> fabricVersions = JsonParser.parseFabricManifest(Sources.getFabricForMinecraftVersion("1.19.4"));

        JavaVersion javaVersion = JsonParser.parseJavaVersion(Sources.getJavaRuntimeJson(), mcDetails.getJavaVersion().getComponent()).get(0);
        JavaManifest javaManifest = JsonParser.parseJavaManifest(Sources.getFileFromUrl(javaVersion.getManifestUrl()));
        File javaDir = new File("./downloads/java");
        javaDir.mkdirs();
        for(JavaFile f : javaManifest.getFiles()) {
            FileDownloader.downloadJavaFile(f, javaDir);
        }

        File clientDir = new File("./downloads/client");
        clientDir.mkdirs();
        FileDownloader.downloadVersionDownload(mcDetails.getDownloads().getClient(), clientDir);
        File libsDir = new File("./downloads/libraries");
        libsDir.mkdirs();
        for(VersionLibrary l : mcDetails.getLibraries()) {
            FileDownloader.downloadVersionLibrary(l, libsDir);
        }
        VersionLaunchCommand launchCommand = mcDetails.getArguments().getLaunchCommand("java", "client.jar", mcDetails.getMainClass(), "../libraries" , mcDetails.getLibraries(), new ArrayList<>());
        System.out.println(launchCommand.getLaunchCommand());
        FabricVersionDetails details = JsonParser.parseFabricVersion(Sources.getFabricVersion(fabricVersions.get(0).getMinecraftVersion(), fabricVersions.get(0).getLoaderVersion()));
        return;
    }

    public static List<Version> getVersions() {
        return JsonParser.parseVersionManifest(Sources.getVersionManifestJson());
    }

    public static List<Version> getReleases() {
        List<Version> releases = new ArrayList<>();
        List<Version> versions = getVersions();
        for(Version v : versions) {
            if(v.isRelease()) {
                releases.add(v);
            }
        }
        return releases;
    }
}