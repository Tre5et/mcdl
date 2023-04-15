package net.treset;

import java.util.List;

public class VersionLoader {
    public static void main(String[] args) {
        System.out.println(getVersions());
    }

    public static List<Version> getVersions() {
        return JsonParser.parseVersionManifest(Sources.getVersionManifest());
    }
}