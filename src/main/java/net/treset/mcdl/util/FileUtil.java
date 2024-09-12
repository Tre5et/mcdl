package net.treset.mcdl.util;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.format.FormatUtils;
import net.treset.mcdl.util.cache.Caching;
import net.treset.mcdl.util.cache.NoCaching;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.net.http.HttpResponse;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtil {
    private static Caching<HttpResponse<byte[]>> defaultCaching = new NoCaching<>();

    /**
     * Downloads a file from a URL to a specified file using the default caching strategy
     * @param downloadUrl The URL to download the file from
     * @param outFile The file to download the file to
     * @throws FileDownloadException If there is an error downloading the file
     */
    public static void downloadFile(URL downloadUrl, File outFile) throws FileDownloadException {
        downloadFile(downloadUrl, outFile, defaultCaching);
    }

    /**
     * Downloads a file from a URL to a specified file
     * @param downloadUrl The URL to download the file from
     * @param outFile The file to download the file to
     * @param caching The caching strategy to use
     * @throws FileDownloadException If there is an error downloading the file
     */
    public static void downloadFile(URL downloadUrl, File outFile, Caching<HttpResponse<byte[]>> caching) throws FileDownloadException {
        try {
            HttpResponse<byte[]> response = HttpUtil.get(downloadUrl, Map.of(), Map.of(), caching);
            Files.write(outFile.toPath(), response.body());
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download file: url=" + downloadUrl, e);
        }
    }

    /**
     * Gets maven data from a maven path
     * @param baseUrl The maven url
     * @param mavenFileName The maven library name
     * @return The maven data
     * @throws MalformedURLException if the url is invalid
     * @throws FileDownloadException if the file can not be downloaded
     */
    public static MavenPom parseMavenUrl(String baseUrl, String mavenFileName) throws MalformedURLException, FileDownloadException {
        String mavenPath = toMavenPath(mavenFileName, ".pom");

        String pomFile;
        try {
            pomFile = HttpUtil.getString(baseUrl + mavenPath);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download pom file: url=" + baseUrl + mavenPath, e);
        }

        return new MavenPom(
                parseMavenProperty(pomFile, "modelVersion"),
                parseMavenProperty(pomFile, "groupId"),
                parseMavenProperty(pomFile, "artifactId"),
                parseMavenProperty(pomFile, "version"),
                parseMavenProperty(pomFile, "packaging")
        );
    }

    /**
     * Converts a maven file name to a relative file path
     * @param mavenFileName The maven file name
     * @param fileExtension The file extension of the resulting file
     * @return The relative file path
     * @throws MalformedURLException if the maven file name is invalid
     */
    public static String toMavenPath(String mavenFileName, String fileExtension) throws MalformedURLException {
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
        mavenFile.append(fileExtension);
        return mavenPath.toString() + mavenFile;
    }

    private static String parseMavenProperty(String pomFile, String property) {
        return FormatUtils.matches(pomFile, "<"+property+">(.*)</"+property+">") ? FormatUtils.firstGroup(pomFile, "<"+property+">(.*)</"+property+">") : null;
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
     * Sets the default caching strategy for file downloads. Default: {@link NoCaching}
     * @param defaultCaching The caching strategy to use
     */
    public static void setDefaultCaching(Caching<HttpResponse<byte[]>> defaultCaching) {
        FileUtil.defaultCaching = defaultCaching;
    }
}
