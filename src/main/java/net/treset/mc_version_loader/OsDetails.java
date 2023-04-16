package net.treset.mc_version_loader;

public class OsDetails {

    public static boolean isOsName(String name) {
        System.out.println("OS name = " + System.getProperty("os.name"));
        return FormatUtils.matches(System.getProperty("os.name").toLowerCase(), name);
    }

    public static boolean isOsVersion(String version) {
        System.out.println("OS Version = " + System.getProperty("os.version"));
        return FormatUtils.matches(System.getProperty("os.version").toLowerCase(), version);
    }

    public static boolean isOsArch(String arch) {
        System.out.println("OS arch = " + System.getProperty("os.arch"));
        return FormatUtils.matches(System.getProperty("os.arch").toLowerCase(), arch);
    }

}
