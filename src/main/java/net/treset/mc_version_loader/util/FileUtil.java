package net.treset.mc_version_loader.util;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.format.FormatUtils;
import net.treset.mc_version_loader.resoucepacks.PackMcmeta;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtil {
    private static boolean cacheWebRequests = false;
    private static final Map<String, ResponseCache> webRequestCache = new HashMap<>();

    private record ResponseCache(Long validUntil, byte[] data) {}

    /**
     * Downloads a file from url to a file
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
     * Gets a string from a http get request.
     * @param getUrl url to get the string from
     * @param headers headers to send with the request
     * @param params parameters to send with the request
     * @return http get body as string
     * @throws FileDownloadException if the file can not be downloaded
     */
    public static String getStringFromHttpGet(String getUrl, List<Map.Entry<String, String>> headers, List<Map.Entry<String, String>> params) throws FileDownloadException {
        return new String(getFromHttpGet(getUrl, headers, params), StandardCharsets.UTF_8);
    }

    /**
     * Gets a data from a http get request.
     * @param getUrl url to get the data from
     * @param headers headers to send with the request
     * @param params parameters to send with the request
     * @return http get body as byte array
     * @throws FileDownloadException if the file can not be downloaded
     */
    public static byte[] getFromHttpGet(String getUrl, List<Map.Entry<String, String>> headers, List<Map.Entry<String, String>> params) throws FileDownloadException {
        String cacheKey = getUrl + headers.toString() + params.toString();
        if(cacheWebRequests && webRequestCache.containsKey(cacheKey)) {
            ResponseCache cache = webRequestCache.get(cacheKey);
            if(cache.validUntil() == null || System.currentTimeMillis() <= cache.validUntil()) {
                return cache.data();
            }
        }

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request;
        StringBuilder url = new StringBuilder();
        url.append(getUrl).append("?");
        for(Map.Entry<String, String> p : params) {
            url.append(URLEncoder.encode(p.getKey(), StandardCharsets.UTF_8).replaceAll("\\+", "%20"))
                    .append("=").append(URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8).replaceAll("\\+", "%20"))
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
            HttpResponse<byte[]> res = httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
            byte[] body = res.body();
            if(cacheWebRequests) {
                webRequestCache.put(cacheKey, new ResponseCache(getCacheTime(res.headers()), body));
            }
            return body;
        } catch (IOException | InterruptedException e) {
            throw new FileDownloadException("Unable to download file: url=" + getUrl, e);
        }
    }

    private static Long getCacheTime(HttpHeaders headers) {
        long currentTime = System.currentTimeMillis();

        Optional<String> cacheControlHeader = headers.firstValue("Cache-Control");
        if(cacheControlHeader.isPresent()) {
            String cacheControl = cacheControlHeader.get();
            if(cacheControl.contains("max-age=")) {
                String maxAge = FormatUtils.firstGroup(cacheControl, "max-age=(\\d+)");
                if(maxAge != null) {
                    return currentTime + Long.parseLong(maxAge) * 1000;
                }
            }
        }
        return currentTime + 600000; //cache 10 minutes by default
    }

    /**
     * Converts the contents of a file from the specified url to string
     * @param url url to get the file from
     * @return the contents of the file as string
     * @throws FileDownloadException if the file can not be downloaded
     */
    public static String getStringFromUrl(String url) throws FileDownloadException {
        return getStringFromHttpGet(url, List.of(), List.of());
    }

    /**
     * Reads an image file as a {@link BufferedImage}
     * @param file the file to read
     * @return the image as a {@link BufferedImage}
     * @throws IOException if the file can not be read
     */
    public static BufferedImage loadImage(File file) throws IOException {
        byte[] bytes = Files.readAllBytes(file.toPath());
        try (InputStream is = new ByteArrayInputStream(bytes))
        {
            return ImageIO.read(is);
        }
    }

    /**
     * Gets a specific entry from a zip file as byte array..
     * @param file The zip file
     * @param name The name of the entry
     * @return The entry as byte array
     * @throws IOException If the entry can not be found
     */
    public static byte[] getZipEntry(File file, String name) throws IOException {
        try(ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            PackMcmeta mcmeta = null;
            BufferedImage image = null;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().equals(name)) {
                    return zipFile.getInputStream(entry).readAllBytes();
                }
            }
        }
        throw new IOException("Entry not found: " + name);
    }

    /**
     * Reads a file as a string
     * @param file The file to read
     * @return The file as a string
     * @throws IOException If the file can not be read
     */
    public static String readFileAsString(File file) throws IOException {
        return Files.readString(file.toPath());
    }

    /**
     * Writes bytes to a file
     * @param content The bytes to write
     * @param file The file to write to
     * @throws IOException If the file can not be written
     */
    public static void writeToFile(byte[] content, File file) throws IOException {
        Files.write(file.toPath(), content);
    }

    /**
     * Deletes a file or directory.
     * @param file The file or directory to delete
     * @throws IOException If the file or directory can not be deleted
     */
    public static void delete(File file) throws IOException {
        File[] contents = file.listFiles();

        if(contents != null) {
            for(File f : contents) {
                delete(f);
            }
        }

        if(!file.delete()) {
            throw new IOException("Failed to delete file: " + file.getAbsolutePath());
        }
    }

    private static boolean temDirRegistered = false;
    /**
     * Gets a temporary directory for version loader.
     * @return The temporary directory
     */
    public static File getTempDir() {
        File tempDir = new File("versionloader");

        if(!temDirRegistered) {
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    FileUtil.delete(tempDir);
                } catch (IOException ignored) {}
            }));
            temDirRegistered = true;
        }

        return tempDir;
    }

    /**
     * If set to true results from web requests will be cached. If the same request is preformed again, the cached result will be returned again. Default is false.
     * @param useCache if true the results will be cached
     */
    public static void useWebRequestCache(boolean useCache) {
        cacheWebRequests = useCache;
    }
}
