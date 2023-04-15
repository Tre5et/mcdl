package net.treset;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OsDetails {

    public static boolean isOsName(String name) {
        return matches(System.getProperty("os.name"), (name));
    }

    public static boolean isOsVersion(String version) {
        return matches(System.getProperty("os.version"), (version));
    }

    public static boolean isOsArch(String arch) {
        return matches(System.getProperty("os.arch"), (arch));
    }

    private static boolean matches(String testString, String pattern) {
        final Pattern patter = Pattern.compile(pattern);
        final Matcher matcher = patter.matcher(testString);
        return matcher.find();
    }
}
