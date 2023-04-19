package net.treset.mc_version_loader.os;

import net.treset.mc_version_loader.format.FormatUtils;

public class OsDetails {

    public static boolean isOsName(String name) {
        return FormatUtils.matches(System.getProperty("os.name").toLowerCase().replaceAll("\\s+",""), name);
    }

    public static boolean isOsVersion(String version) {
        return FormatUtils.matches(System.getProperty("os.version").toLowerCase().replaceAll("\\s+",""), version);
    }

    public static boolean isOsArch(String arch) {
        return FormatUtils.matches(System.getProperty("os.arch").toLowerCase().replaceAll("\\s+",""), arch);
    }

    public static String getJavaIdentifier() {
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
        return null;
    }

}
