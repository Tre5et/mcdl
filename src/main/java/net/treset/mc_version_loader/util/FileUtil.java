package net.treset.mc_version_loader.util;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.format.FormatUtils;

import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FileUtil {

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

        String pomFile = getStringFromUrl(baseUrl + mavenPath + mavenFile);

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

    /**
     * Converts the contents of a file from the specified http get url to string
     * @param getUrl url to get the file from
     * @param headers headers to send with the request
     * @param params parameters to send with the request
     * @return the contents of the file as string
     * @throws FileDownloadException if the file can not be downloaded
     */
    public static String getStringFromHttpGet(String getUrl, List<Map.Entry<String, String>> headers, List<Map.Entry<String, String>> params) throws FileDownloadException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request;
        StringBuilder url = new StringBuilder();
        url.append(getUrl).append("?");
        for(Map.Entry<String, String> p : params) {
            url.append(URLEncoder.encode(p.getKey(), StandardCharsets.UTF_8))
                    .append("=").append(URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8))
                    .append("&");
        }
        try {
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(new URI(url.substring(0, url.length() - 1)));
            headers.forEach(h -> requestBuilder.header(h.getKey(), h.getValue()));
            request = requestBuilder.build();
        } catch (URISyntaxException e) {
            throw new FileDownloadException("Unable to build download URI: url=" + getUrl, e);
        }

        try {
            return httpClient.send(request, HttpResponse.BodyHandlers.ofString()).body();
        } catch (IOException | InterruptedException e) {
            throw new FileDownloadException("Unable to download file: url=" + getUrl, e);
        }
    }

    /**
     * Converts the contents of a file from the specified url to string
     * @param url url to get the file from
     * @return the contents of the file as string
     * @throws FileDownloadException if the file can not be downloaded
     */
    public static String getStringFromUrl(String url) throws FileDownloadException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            StringBuilder stringBuilder = new StringBuilder();

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(inputLine);
                stringBuilder.append(System.lineSeparator());
            }

            bufferedReader.close();
            return stringBuilder.toString().trim();
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download file: url=" + url, e);
        }
    }
}
