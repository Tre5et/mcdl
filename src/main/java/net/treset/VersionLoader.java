package net.treset;

import net.treset.version.Version;

import java.util.ArrayList;
import java.util.List;

public class VersionLoader {
    public static void main(String[] args) {
        System.out.println(getReleases());
    }

    public static List<Version> getVersions() {
        return JsonParser.parseVersionManifest(Sources.getVersionManifest());
    }

    public static List<Version> getReleases() {
        List<Version> releases = new ArrayList<>();
        List<Version> versions = getVersions();
        for(Version v : versions) {
            if(!v.isSnapshot()) {
                releases.add(v);
            }
        }
        return releases;
    }
}