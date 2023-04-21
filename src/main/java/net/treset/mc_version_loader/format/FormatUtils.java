package net.treset.mc_version_loader.format;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtils {
    public static boolean matches(String testString, String pattern) {
        final Pattern patter = Pattern.compile(pattern);
        final Matcher matcher = patter.matcher(testString);
        return matcher.matches();
    }

    public static String firstGroup(String testString, String pattern) {
        final Pattern patter = Pattern.compile(pattern);
        final Matcher matcher = patter.matcher(testString);
        if(matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public static List<String> findMatches(String testString, String pattern) {
        final Pattern patter = Pattern.compile(pattern);
        final Matcher matcher = patter.matcher(testString);
        List<String> matches = new ArrayList<>();
        while(matcher.find()) {
            matches.add(matcher.group(1));
        }
        return matches;
    }
}
