package net.treset.mc_version_loader.launcher;

import java.util.HashMap;
import java.util.Map;

public class ManifestTypeUtils {
    public static LauncherManifestType getLauncherManifestType(String type, Map<String, LauncherManifestType> conversion) {
        if(type != null && conversion != null && conversion.containsKey(type)) {
            return conversion.get(type);
        }
        return LauncherManifestType.UNKNOWN;
    }

    public static LauncherManifestType getLauncherManifestType(String type) {
        return getLauncherManifestType(type, getDefaultConversion());
    }

    public static Map<String, LauncherManifestType> getDefaultConversion() {
        return Map.of(
                "launcher", LauncherManifestType.LAUNCHER
        );
    }
}
