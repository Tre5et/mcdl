package net.treset.mc_version_loader.format;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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

    public static LocalDateTime parseLocalDateTime(String time) {
        if(time == null || time.isBlank()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return (LocalDateTime)formatter.parse(time);
    }

    public static String fromLocalDateTime(LocalDateTime time) {
        if(time == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return formatter.format(time);
    }

    public static String formatModComparison(String input) {
        return input == null ? "" : input.toLowerCase().replaceAll("\\s", "").replace("_", "");
    }

    public static String formatVersionComparison(String input) {
        return formatModComparison(input).replaceAll("[a-zA-Z]", "");
    }
}
