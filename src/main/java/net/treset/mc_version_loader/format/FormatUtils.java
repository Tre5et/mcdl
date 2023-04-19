package net.treset.mc_version_loader.format;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtils {
    public static boolean matches(String testString, String pattern) {
        final Pattern patter = Pattern.compile(pattern);
        final Matcher matcher = patter.matcher(testString);
        return matcher.find();
    }

    public static String firstGroup(String testString, String pattern) {
        final Pattern patter = Pattern.compile(pattern);
        final Matcher matcher = patter.matcher(testString);
        if(matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
