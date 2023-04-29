package net.treset.mc_version_loader.format;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
        return LocalDateTime.from(formatter.parse(time));
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
        return formatModComparison(input).replaceAll("[a-zA-Z]", "").replaceAll("-", "").replaceAll("\\+", "");
    }

    public static String curseforgeModLoaderToModLoader(int cfModLoader) {
        switch (cfModLoader) {
            case 0 -> {
                return null;
            }
            case 1 -> {
                return "forge";
            }
            case 2 -> {
                return "cauldron";
            }
            case 3 -> {
                return "liteloader";
            }
            case 4 -> {
                return "fabric";
            }
            case 5 -> {
                return "quilt";
            }
        }
        return null;
    }

    public static int modLoaderToCurseforgeModLoader(String modLoader) {
        if(modLoader == null) {
            return 0;
        }
        switch (modLoader) {
            case "forge" -> {
                return 1;
            }
            case "cauldron" -> {
                return 2;
            }
            case "liteloader" -> {
                return 3;
            }
            case "fabric" -> {
                return 4;
            }
            case "quilt" -> {
                return 5;
            }
        }
        return 0;
    }
}
