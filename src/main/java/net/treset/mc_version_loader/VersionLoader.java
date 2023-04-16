package net.treset.mc_version_loader;

import net.treset.mc_version_loader.java.JavaManifest;
import net.treset.mc_version_loader.java.JavaVersion;
import net.treset.mc_version_loader.version.*;

import java.util.ArrayList;
import java.util.List;

public class VersionLoader {
    public static void main(String[] args) {
        List<Version> releases = getReleases();
        for(Version r : releases) {
            if(r.getId().equals("1.19.4")) {
                VersionDetails details = JsonParser.parseVersionDetails(Sources.getFileFromUrl(r.getUrl()));
                List<VersionLibrary> activeLibs = details.getActiveLibraries(new ArrayList<>());
                String command = details.getArguments().getLaunchCommand("java", "file.jar", new ArrayList<>()).getLaunchCommand();
                List<JavaVersion> java = JsonParser.parseJavaVersion(Sources.getJavaRuntimeJson(), details.getJavaVersion().getComponent());
                JavaManifest javaManifest = JsonParser.parseJavaManifest(Sources.getFileFromUrl(java.get(0).getManifestUrl()));
                break;
            }
        }
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