package net.treset.mc_version_loader.forge;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.minecraft.MinecraftGame;
import net.treset.mc_version_loader.minecraft.MinecraftLibrary;
import net.treset.mc_version_loader.resoucepacks.PackMcmeta;
import net.treset.mc_version_loader.util.DownloadStatus;
import net.treset.mc_version_loader.util.FileUtil;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MinecraftForge {
    private static final String mavenMetaUrl = "https://files.minecraftforge.net/net/minecraftforge/forge/maven-metadata.json";

    private static final String installerUrl = "https://maven.minecraftforge.net/net/minecraftforge/forge/%s/forge-%s-installer.jar"; // versionNumber
    private static String getInstallerUrl(String versionNumber) {
        return String.format(installerUrl, versionNumber, versionNumber);
    }


    /**
     * Gets all forge versions.
     * @return List of all forge versions
     * @throws FileDownloadException If there is an error downloading or parsing the forge versions
     */
    public static List<ForgeMetaVersion> getForgeVersions() throws FileDownloadException {
        try {
            return ForgeMetaVersion.fromJson(FileUtil.getStringFromUrl(mavenMetaUrl));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse forge versions", e);
        }
    }

    /**
     * Gets a specific forge version. This is a fairly slow operation as it requires downloading the installer jar and extracting the version json.
     * @param versionId The version to get
     * @return The forge version
     * @throws FileDownloadException If there is an error downloading or parsing the forge version
     */
    public static ForgeVersion getForgeVersion(String versionId) throws FileDownloadException {
        File jarFile = tempDownloadForgeInstaller(versionId);

        ForgeVersion version;
        try {
            version = ForgeVersion.fromJson(new String(FileUtil.getZipEntry(jarFile, "version.json")));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse forge version", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to read versions.json", e);
        }

        return version;
    }

    /**
     * Gets the install profile for a specific forge version. This is a fairly slow operation as it requires downloading the installer jar and extracting the install profile json.
     * @param versionId The version to get the install profile for
     * @return The forge install profile
     * @throws FileDownloadException If there is an error downloading or parsing the install profile
     */
    public static ForgeInstallProfile getForgeInstallProfile(String versionId) throws FileDownloadException {
        File jarFile = tempDownloadForgeInstaller(versionId);

        ForgeInstallProfile profile;
        try {
            profile = ForgeInstallProfile.fromJson(new String(FileUtil.getZipEntry(jarFile, "install_profile.json")));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse forge install profile", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to read install_profile.json", e);
        }

        return profile;
    }

    /**
     * Creates a forge client jar from a specified version and install profile.
     * @param outFile The file to write the client jar to
     * @param version The version id to create the client jar for
     * @param profile The install profile to use
     * @param minecraftJar A vanilla minecraft client jar of the correct version
     * @param statusCallback A callback to be called when a step in the process is started
     * @throws FileDownloadException If there is an error processing the client jar
     */
    public static void createForgeClient(File outFile, String version, ForgeInstallProfile profile, File minecraftJar, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        File tempDir = new File(FileUtil.getTempDir(), "forge-" + version + "-installer");
        if(!tempDir.isDirectory() && !tempDir.mkdirs()) {
            throw new FileDownloadException("Failed to create directory for forge installer");
        }

        List<ForgeInstallProcessor> processors = profile.getProcessors().stream().filter(p -> p.getSides() == null || p.getSides().contains("client")).toList();

        statusCallback.accept(new DownloadStatus(1, processors.size() + 3, "Download Installer Libraries", false));
        List<LibraryData> libraries = downloadInstallerLibraries(profile, tempDir);

        statusCallback.accept(new DownloadStatus(2, processors.size() + 3, "Extract Installer Data", false));
        extractData(version, tempDir);

        for (int i = 0; i < processors.size(); i++) {
            statusCallback.accept(new DownloadStatus(i + 3, processors.size() + 3, "Run Processor: " + processors.get(i).getJar(), false));
            startProcessor(processors.get(i), libraries, profile, minecraftJar, tempDir);
        }

        statusCallback.accept(new DownloadStatus(processors.size() + 2, processors.size() + 3, "Copy Client Jar", false));
        ForgeInstallDataPath patched = profile.getData().get("PATCHED");
        if(patched == null) {
            throw new FileDownloadException("Failed to find patched client jar");
        }
        File clientJar = new File(tempDir, patched.getResolvedClient());

        try {
            Files.copy(clientJar.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to copy client jar", e);
        }
    }

    /**
     * Downloads a list of forge libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param libraries The libraries to download
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadForgeLibraries(File baseDir, List<MinecraftLibrary> libraries) throws FileDownloadException {
        return MinecraftGame.downloadVersionLibraries(libraries, baseDir, List.of(), status -> {});
    }

    /**
     * Downloads a list of forge libraries to a specified directory and returns a list of library paths.
     * @param baseDir The directory to download the libraries to
     * @param libraries The libraries to download
     * @param statusCallback A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadForgeLibraries(File baseDir, List<MinecraftLibrary> libraries, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        return MinecraftGame.downloadVersionLibraries(libraries, baseDir, List.of(), statusCallback);
    }

    private static void extractData(String versionId, File tempDir) throws FileDownloadException {
        File jarFile = tempDownloadForgeInstaller(versionId);

        try(ZipFile zipFile = new ZipFile(jarFile)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            PackMcmeta mcmeta = null;
            BufferedImage image = null;
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().startsWith("data/")) {
                    File out = new File(tempDir, entry.getName());
                    if(!out.getParentFile().isDirectory() && !out.getParentFile().mkdirs()) {
                        throw new FileDownloadException("Failed to create directory for forge installer data");
                    }

                    FileUtil.writeToFile(zipFile.getInputStream(entry).readAllBytes(), out);
                }
            }
        } catch (IOException e) {
            throw new FileDownloadException("Failed to extract data from forge installer", e);
        }
    }

    private static void startProcessor(ForgeInstallProcessor processor, List<LibraryData> libraries, ForgeInstallProfile profile, File minecraftJar, File tempDir) throws FileDownloadException {
        LibraryData mainLib;
        try {
            mainLib = libraries.stream().filter(l -> l.getId().equals(processor.getJar())).findFirst().orElseThrow();
        } catch(NoSuchElementException e) {
            throw new FileDownloadException("Failed to find main library for processor: " + processor.getJar());
        }

        List<String> classpath = libraries.stream().filter(l -> processor.getClasspath().contains(l.getId())).map(LibraryData::getPath).toList();

        StringBuilder classpathString = new StringBuilder();
        classpathString.append(mainLib.getPath());
        classpath.forEach(c -> classpathString.append(';').append(c));

        ProcessBuilder builder = new ProcessBuilder(System.getProperty("java.home") + "\\bin\\java", "-cp");
        builder.command().add(classpathString.toString());
        builder.command().add(mainLib.getMainClass());
        for(String arg : processor.getArgs()) {
            try {
                builder.command().add(processArg(arg, profile, minecraftJar, tempDir));
            } catch (NoSuchElementException e) {
                throw new FileDownloadException("Failed to process argument: " + arg, e);
            }
        }

        builder.directory(tempDir);

        Process process;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new FileDownloadException("Failed to start processor: " + processor.getJar(), e);
        }

        try {
            try(BufferedReader reader = process.inputReader()) {
                reader.lines().iterator().forEachRemaining(
                        value -> System.out.println("Processor: " + value)
                );
            }
        } catch (IOException e) {
            System.out.println("Game output forwarding failed: " + processor.getJar());
        }
        while (process.isAlive()) {
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                System.out.println("Processor listening interrupted, restarting");
            }
        }

        if(process.exitValue() != 0) {
            try(BufferedReader reader = process.errorReader()) {
                reader.lines().iterator().forEachRemaining(
                        value -> System.out.println("Processor: ERROR: " + value)
                );
            } catch (IOException e) {
                System.out.println("Game error forwarding failed: " + processor.getJar());
            }
            throw new FileDownloadException("Processor failed: " + processor.getJar());
        }
    }

    private static String processArg(String arg, ForgeInstallProfile profile, File minecraftJar, File tempDir) throws NoSuchElementException {
        String replacement;
        if(arg.startsWith("[") && arg.endsWith("]")) {
            replacement = ForgeInstallDataPath.decodeLibraryPath(arg);
        } else if(arg.startsWith("{") && arg.endsWith("}")) {
            replacement = getReplacement(arg, profile, minecraftJar);
        } else {
            replacement = arg;
        }
        if(replacement.contains("/")) {
            replacement = new File(tempDir, replacement).getAbsolutePath();
        }
        return replacement;
    }

    private static String getReplacement(String arg, ForgeInstallProfile profile, File minecraftJar) throws NoSuchElementException {
        if(arg.startsWith("{") && arg.endsWith("}")) {
            String key = arg.substring(1, arg.length() - 1);

            return switch(key) {
                case "SIDE" -> "client";
                case "MINECRAFT_JAR" -> minecraftJar.getAbsolutePath();
                default -> {
                    ForgeInstallDataPath profileData = profile.getData().get(key);
                    if(profileData == null) {
                        throw new NoSuchElementException("Failed to find replacement for: " + key);
                    }
                    yield profileData.getResolvedClient();  
                }
            };
        }
        return arg;
    }

    private static List<LibraryData> downloadInstallerLibraries(ForgeInstallProfile profile, File tempDir) throws FileDownloadException {
        File libsDir = new File(tempDir,"libs");
        if(!libsDir.exists() && !libsDir.mkdirs()) {
            throw new FileDownloadException("Failed to create directory for forge libraries");
        }

        ArrayList<LibraryData> libraryData = new ArrayList<>();
        for (MinecraftLibrary lib : profile.getLibraries()) {
            File outFile = new File(libsDir, lib.getDownloads().getArtifacts().getPath());
            if(!outFile.getParentFile().isDirectory() && !outFile.getParentFile().mkdirs()) {
                throw new FileDownloadException("Failed to create directory for forge library: " + lib.getName());
            }

            try {
                FileUtil.downloadFile(new URL(lib.getDownloads().getArtifacts().getUrl()), outFile);
            } catch (MalformedURLException e) {
                throw new FileDownloadException("Failed to parse forge library Url: " + lib.getName(), e);
            }

            String mainClass = null;
            try {
                String mainManifestContent = new String(FileUtil.getZipEntry(outFile, "META-INF/MANIFEST.MF"));
                String[] lines = mainManifestContent.split("\n");
                for (String line : lines) {
                    if (line.startsWith("Main-Class:")) {
                        mainClass = line.substring(11).trim();
                        break;
                    }
                }
            } catch (IOException ignored) {}

            libraryData.add(new LibraryData(lib.getName(), outFile.getAbsolutePath(), mainClass));
        }

        return libraryData;
    }

    private static class LibraryData {
        private String id;
        private String path;
        private String mainClass;

        public LibraryData(String id, String path, String mainClass) {
            this.id = id;
            this.path = path;
            this.mainClass = mainClass;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getMainClass() {
            return mainClass;
        }

        public void setMainClass(String mainClass) {
            this.mainClass = mainClass;
        }
    }

    private static File tempDownloadForgeInstaller(String versionId) throws FileDownloadException {
        File jarFile = new File(FileUtil.getTempDir(),"forge-" + versionId + "-installer.jar");

        if(!jarFile.exists()) {
            if (!jarFile.getParentFile().exists() && !jarFile.getParentFile().mkdirs()) {
                throw new FileDownloadException("Failed to create directory for forge installer");
            }

            try {
                FileUtil.downloadFile(new URL(getInstallerUrl(versionId)), jarFile);
            } catch (MalformedURLException e) {
                throw new FileDownloadException("Failed to parse forge installer Url", e);
            }
        }

        jarFile.deleteOnExit();

        return jarFile;
    }
}