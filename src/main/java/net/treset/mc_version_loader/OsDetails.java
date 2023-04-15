package net.treset.mc_version_loader;

public class OsDetails {

    public static boolean isOsName(String name) {
        return FormatUtils.matches(System.getProperty("os.name").toLowerCase(), name);
    }

    public static boolean isOsVersion(String version) {
        return FormatUtils.matches(System.getProperty("os.version").toLowerCase(), version);
    }

    public static boolean isOsArch(String arch) {
        return FormatUtils.matches(System.getProperty("os.arch").toLowerCase(), arch);
    }

}
