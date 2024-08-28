package net.treset.mcdl.util;

import net.treset.mcdl.format.FormatUtils;

public class OsUtil {

    /**
     * Checks whether the current os matches the specified os name.
     * @param name The os name to check
     * @return Whether the current os matches the specified os name
     */
    public static boolean isOsName(String name) {
        return FormatUtils.matches(System.getProperty("os.name").toLowerCase().replaceAll("\\s+",""), name);
    }

    /**
     * Checks whether the current os matches the specified os version.
     * @param version The os version to check
     * @return Whether the current os matches the specified os version
     */
    public static boolean isOsVersion(String version) {
        return FormatUtils.matches(System.getProperty("os.version").toLowerCase().replaceAll("\\s+",""), version);
    }

    /**
     * Checks whether the current os matches the specified os architecture.
     * @param arch The os architecture to check
     * @return Whether the current os matches the specified os architecture
     */
    public static boolean isOsArch(String arch) {
        return FormatUtils.matches(System.getProperty("os.arch").toLowerCase().replaceAll("\\s+",""), arch);
    }

    /**
     * Gets the java identifier gor the current os.
     * @return The java identifier for the current os
     * @throws IllegalArgumentException If the os is unknown
     */
    public static String getJavaIdentifier() throws IllegalArgumentException {
        if(isOsName("windows")) {
            if(isOsArch("x86")) {
                return "windows-x86";
            }
            if(isOsArch("amd64")) {
                return "windows-x64";
            }
            return "windows-unknown";
        }
        if(isOsName("osx")) {
            if(isOsArch("arm64")) {
                return "mac-os-arm64";
            }
            return "mac-os";
        }
        if(isOsName("linux")) {
            if(isOsArch("i386")) {
                return "linux-i386";
            }
            return "linux";
        }
        throw new IllegalArgumentException("Unknown OS");
    }

}
