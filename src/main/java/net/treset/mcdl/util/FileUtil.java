package net.treset.mcdl.util;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.format.FormatUtils;

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
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

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
     * Copies the content of a file to another file while creating that file.
     * @param src The file to copy
     * @param dst The file to copy to
     * @throws IOException If the file can not be copied
     */
    public static void copyFileContent(File src, File dst, CopyOption... options) throws IOException {
        Files.createDirectories(dst.getParentFile().toPath());
        Files.copy(src.toPath(), dst.toPath(), options);
    }

    /**
     * Copies the content of a directory to another directory while creating that directory.
     * @param src The directory to copy
     * @param dst The directory to copy to
     * @throws IOException If the directory can not be copied
     */
    public static void copyDirectory(File src, File dst, CopyOption... options) throws IOException {
        if(src.isDirectory()) {
            if(!dst.exists()) {
                Files.createDirectories(dst.toPath());
            }
            String[] files = src.list();
            if(files == null) {
                return;
            }
            for(String file : files) {
                copyDirectory(new File(src, file), new File(dst, file), options);
            }
        } else {
            Files.copy(src.toPath(), dst.toPath(), options);
        }
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


    /**
     * Extracts a zip file to a directory
     * @param srcFile The file to extract
     * @param dstDir The directory to extract the contents of the zip file into
     * @throws IOException If there is an error extracting the file
     */
    public static void exctractFile(File srcFile, File dstDir) throws IOException {
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(srcFile));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(dstDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();
    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    /**
     * Compresses a file or the contents of a directory to a zip file
     * @param src The file or directory to compress
     * @param dstFile The output zip file
     * @throws IOException If there is an error compressing the file
     */
    public static void compressContents(File src, File dstFile) throws IOException {
        FileOutputStream fos = new FileOutputStream(dstFile);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        zipFile(src, src.getName(), zipOut, true);
        zipOut.close();
        fos.close();
    }

    private static void zipFile(File fileToZip, String fileName, ZipOutputStream zipOut, boolean isStart) throws IOException {
        if (fileToZip.isHidden()) {
            return;
        }
        if (fileToZip.isDirectory()) {
            if(!isStart) {
                zipOut.putNextEntry(new ZipEntry(fileName + (fileName.endsWith("/") ? "" : "/")));
                zipOut.closeEntry();
            }
            File[] children = fileToZip.listFiles();
            if(children == null) {
                return;
            }
            for (File childFile : children) {
                zipFile(childFile, (isStart ? "" : (fileName + "/")) + childFile.getName(), zipOut, false);
            }
            return;
        }
        FileInputStream fis = new FileInputStream(fileToZip);
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);
        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zipOut.write(bytes, 0, length);
        }
        fis.close();
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
