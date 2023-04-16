package net.treset.mc_version_loader;

import net.treset.mc_version_loader.fabric.FabricLoaderData;
import net.treset.mc_version_loader.fabric.FabricVersion;
import net.treset.mc_version_loader.fabric.FabricVersionDetails;
import net.treset.mc_version_loader.java.JavaManifest;
import net.treset.mc_version_loader.java.JavaVersion;
import net.treset.mc_version_loader.version.*;

import java.util.ArrayList;
import java.util.List;

public class VersionLoader {
    public static void main(String[] args) {
        List<FabricVersion> versions = JsonParser.parseFabricManifest(Sources.getFabricForMinecraftVersion("1.19.4"));
        FabricVersionDetails details = JsonParser.parseFabricVersion(Sources.getFabricVersion(versions.get(0).getMinecraftVersion(), versions.get(0).getLoaderVersion()));
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