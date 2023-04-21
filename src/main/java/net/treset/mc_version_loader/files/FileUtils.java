package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.format.FormatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils {
    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.toString());

    public static boolean downloadFile(URL downloadUrl, File outFile) {
        try(FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {

            ReadableByteChannel readableByteChannel = Channels.newChannel(downloadUrl.openStream());

            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to download file", e);
            return false;
        }
        return true;
    }

    public static MavenPom parseMavenUrl(String baseUrl, String mavenFileName) {
        String[] parts = mavenFileName.split(":");
            if(parts.length < 2) {
            LOGGER.log(Level.WARNING, "Unable to parse fabric loader url");
            return null;
        }
        StringBuilder mavenFile = new StringBuilder();
        StringBuilder mavenPath = new StringBuilder();
        mavenPath.append(parts[0].replaceAll("\\.", "/")).append("/");
        for(int i = 1; i < parts.length; i++) {
            mavenFile.append(parts[i]).append("-");
            mavenPath.append(parts[i]).append("/");
        }
        mavenFile.deleteCharAt(mavenFile.lastIndexOf("-"));
        mavenFile.append(".pom");

        String pomFile = Sources.getFileFromUrl(baseUrl + mavenPath + mavenFile);

        return new MavenPom(
                parseMavenProperty(pomFile, "modelVersion"),
                parseMavenProperty(pomFile, "groupId"),
                parseMavenProperty(pomFile, "artifactId"),
                parseMavenProperty(pomFile, "version"),
                parseMavenProperty(pomFile, "packaging")
        );
    }

    private static String parseMavenProperty(String pomFile, String property) {
        return FormatUtils.firstGroup(pomFile, "<"+property+">(.*)</"+property+">");
    }
}
