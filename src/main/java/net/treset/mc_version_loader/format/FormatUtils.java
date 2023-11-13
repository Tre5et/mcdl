package net.treset.mc_version_loader.format;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatUtils {
    /**
     * Checks whether the testString matches the pattern one or more times
     * @param testString string to find pattern in
     * @param pattern a regex pattern
     * @return {@code true} if the testString matches the pattern one or more times
     */
    public static boolean matches(String testString, String pattern) {
        final Pattern patter = Pattern.compile(pattern);
        final Matcher matcher = patter.matcher(testString);
        return matcher.find();
    }

    /**
     * Gets the first group of the first match of the pattern in the testString
     * @param testString string to find pattern in
     * @param pattern a regex pattern
     * @return the first group of the first match of the pattern in the testString
     * @throws IllegalArgumentException if no match is found
     */
    public static String firstGroup(String testString, String pattern) throws IllegalArgumentException {
        final Pattern patter = Pattern.compile(pattern);
        final Matcher matcher = patter.matcher(testString);
        if(matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("No match found for pattern: " + pattern + " in string: " + testString);
    }

    /**
     * Gets all the first groups of all the matches of the pattern in the testString
     * @param testString string to find pattern in
     * @param pattern a regex pattern
     * @return list of all the first groups of all the matches of the pattern in the testString
     */
    public static List<String> findMatches(String testString, String pattern) {
        final Pattern patter = Pattern.compile(pattern);
        final Matcher matcher = patter.matcher(testString);
        List<String> matches = new ArrayList<>();
        while(matcher.find()) {
            matches.add(matcher.group(1));
        }
        return matches;
    }

    /**
     * Parses a LocalDateTime from a string
     * @param time string representation of a LocalDateTime
     * @return LocalDateTime parsed from the string, null if input is null
     * @throws IllegalArgumentException if the time string is invalid
     */
    public static LocalDateTime parseLocalDateTime(String time) throws IllegalArgumentException {
        if(time == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        try {
            return LocalDateTime.from(formatter.parse(time));
        }
        catch (DateTimeException e) {
            throw new IllegalArgumentException("Invalid time: " + time);
        }
    }

    /**
     * Formats a LocalDateTime to a string
     * @param time LocalDateTime to format
     * @return string representation of the LocalDateTime
     * @throws IllegalArgumentException if the time fails to parse
     */
    public static String formatLocalDateTime(LocalDateTime time) throws IllegalArgumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        try {
            return formatter.format(time);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Invalid time: " + time);
        }
    }

    /**
     * Formats a mod name for comparison with other names. Improves accuracy determining whether mods from different sources are the same.
     * @param input mod name to format
     * @return formatted mod name
     */
    public static String formatModComparison(String input) {
        return input == null ? "" : input.toLowerCase().replaceAll("\\s", "").replace("_", "");
    }

    /**
     * Formats a mod version id for comparison with other version ids. Improves accuracy determining whether mod versions from different sources are the same.
     * @param input mod version id to format
     * @return formatted mod version id
     */
    public static String formatVersionComparison(String input) {
        return formatModComparison(input).replaceAll("[a-zA-Z]", "").replaceAll("-", "").replaceAll("\\+", "");
    }

    /**
     * Converts a curseforge mod loader id to a (modrinth) mod loader string
     * @param cfModLoader curseforge mod loader id
     * @return mod loader string
     * @throws IllegalArgumentException if the curseforge mod loader id is invalid
     */
    public static String curseforgeModLoaderToModLoader(int cfModLoader) throws IllegalArgumentException {
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
        throw new IllegalArgumentException("Invalid curseforge mod loader: " + cfModLoader);
    }

    /**
     * Converts a (modrith) mod loader string to a curseforge mod loader id.
     * @param modLoader mod loader string
     * @return curseforge mod loader id
     * @throws IllegalArgumentException if the mod loader string is invalid
     */
    public static int modLoaderToCurseforgeModLoader(String modLoader) throws IllegalArgumentException {
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
        throw new IllegalArgumentException("Invalid mod loader: " + modLoader);
    }
}
