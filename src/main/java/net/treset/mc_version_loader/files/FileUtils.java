package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.format.FormatUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class FileUtils {

    /**
     * Downloads a file from a url to a file
     * @param downloadUrl url to download from
     * @param outFile file to download to
     * @throws FileDownloadException if the file can not be downloaded
     */
    public static void downloadFile(URL downloadUrl, File outFile) throws FileDownloadException {
        try(FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {
            ReadableByteChannel readableByteChannel = Channels.newChannel(downloadUrl.openStream());
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch(IOException e) {
            throw new FileDownloadException("Unable to download file: url=" + downloadUrl + ", outPath=" + outFile.getAbsolutePath(), e);
        }
    }
    public static MavenPom parseMavenUrl(String baseUrl, String mavenFileName) throws MalformedURLException, FileDownloadException {
        String[] parts = mavenFileName.split(":");
        if(parts.length < 2) {
            throw new MalformedURLException("Invalid maven file name");
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
        return FormatUtils.matches(pomFile, "<"+property+">(.*)</"+property+">") ? FormatUtils.firstGroup(pomFile, "<"+property+">(.*)</"+property+">") : null;
    }
}
