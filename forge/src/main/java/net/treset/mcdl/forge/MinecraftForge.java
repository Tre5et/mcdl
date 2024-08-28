package net.treset.mcdl.forge;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.util.DownloadStatus;
import net.treset.mcdl.util.FileUtil;
import net.treset.mcdl.minecraft.MinecraftGame;
import net.treset.mcdl.minecraft.MinecraftLibrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

    private static boolean cacheVersions = false;
    private static List<ForgeMetaVersion> versionCache = null;


    /**
     * Gets all forge versions.
     * @return List of all forge versions
     * @throws FileDownloadException If there is an error downloading or parsing the forge versions
     */
    public static List<ForgeMetaVersion> getForgeVersions() throws FileDownloadException {
        if(cacheVersions && versionCache != null) {
            return versionCache;
        }
        try {
            List<ForgeMetaVersion> v = ForgeMetaVersion.fromJson(FileUtil.getStringFromUrl(mavenMetaUrl));
            if(cacheVersions) {
                versionCache = v;
            }
            return v;
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
            version = ForgeVersion.fromJson(new String(FileUtil.getZipEntry(jarFile, "version.json"), StandardCharsets.UTF_8));
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
            profile = ForgeInstallProfile.fromJson(new String(FileUtil.getZipEntry(jarFile, "install_profile.json"), StandardCharsets.UTF_8));
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse forge install profile", e);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to read install_profile.json", e);
        }

        return profile;
    }

    /**
     * Creates a forge client libraries from a specified version and install profile.
     * @param version The version id to create the client libraries for
     * @param profile The install profile to use
     * @param minecraftJar A vanilla minecraft client jar of the correct version
     * @throws FileDownloadException If there is an error processing the client jar
     */
    public static void createForgeClient(String version, File librariesDir, ForgeInstallProfile profile, File minecraftJar, File javaFile) throws FileDownloadException {
        createForgeClient(version, librariesDir, profile, minecraftJar, javaFile, status -> {});
    }

    /**
     * Creates a forge client libraries from a specified version and install profile.
     * @param version The version id to create the client libraries for
     * @param profile The install profile to use
     * @param minecraftJar A vanilla minecraft client jar of the correct version
     * @param statusCallback A callback to be called when a step in the process is started
     * @throws FileDownloadException If there is an error processing the client jar
     */
    public static void createForgeClient(String version, File librariesDir, ForgeInstallProfile profile, File minecraftJar, File javaFile, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        File tempDir = new File(FileUtil.getTempDir(), "forge-" + version + "-installer");
        if(!tempDir.isDirectory() && !tempDir.mkdirs()) {
            throw new FileDownloadException("Failed to create directory for forge installer");
        }

        extractMaven(version, tempDir);

        File localLibrariesDir = new File(tempDir, "maven");

        List<ForgeInstallProcessor> processors = profile.getProcessors().stream().filter(p -> p.getSides() == null || p.getSides().contains("client")).toList();

        statusCallback.accept(new DownloadStatus(1, processors.size() + 3, "Download Installer Libraries", false));
        List<LibraryData> libraries = downloadInstallerLibraries(profile, librariesDir, localLibrariesDir);

        statusCallback.accept(new DownloadStatus(2, processors.size() + 3, "Extract Installer Data", false));
        extractData(version, tempDir);

        for (int i = 0; i < processors.size(); i++) {
            statusCallback.accept(new DownloadStatus(i + 3, processors.size() + 3, "Run Processor: " + processors.get(i).getJar(), false));
            startProcessor(processors.get(i), libraries, profile, minecraftJar, tempDir, librariesDir, javaFile);
        }

        statusCallback.accept(new DownloadStatus(processors.size() + 2, processors.size() + 3, "Copy Processed Libraries", false));
    }

    /**
     * Downloads a list of forge libraries to a specified directory and returns a list of library paths.
     * @param targetDir The directory to download the libraries to
     * @param libraries The libraries to download
     * @param nativesDir The directory to download natives to
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadForgeLibraries(File targetDir, String version, List<MinecraftLibrary> libraries, File nativesDir) throws FileDownloadException {
        return downloadForgeLibraries(targetDir, version, libraries, nativesDir, status -> {});
    }

    /**
     * Downloads a list of forge libraries to a specified directory and returns a list of library paths.
     * @param targetDir The directory to download the libraries to
     * @param libraries The libraries to download
     * @param nativesDir The directory to download natives to
     * @param statusCallback A callback to be called when a library is downloaded
     * @return A list of library paths
     * @throws FileDownloadException If there is an error downloading or writing a library
     */
    public static List<String> downloadForgeLibraries(File targetDir, String version, List<MinecraftLibrary> libraries, File nativesDir, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        extractMaven(version, targetDir);
        File localLibsDir = new File(targetDir, "maven");
        return MinecraftGame.downloadVersionLibraries(libraries, targetDir, localLibsDir, List.of(), nativesDir, statusCallback);
    }

    /**
     * If set to true a list of forge versions will be cached when @code{getFabricVersions} is first called and reused on subsequent. Else the versions will be fetched every time. Default is false.
     * @param useCache if true the versions will be cached
     */
    public static void useVersionCache(boolean useCache) {
        cacheVersions = useCache;
    }

    private static void extractData(String versionId, File tempDir) throws FileDownloadException {
        File jarFile = tempDownloadForgeInstaller(versionId);

        try(ZipFile zipFile = new ZipFile(jarFile)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

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

    private static List<String> getAllEmptyLibraryPaths(ForgeInstallProfile profile, File baseDir) {
        return profile.getData().values().stream()
                .filter(p -> p.getClient().startsWith("[") && p.getClient().endsWith("]"))
                .map(ForgeInstallDataPath::getResolvedClient)
                .filter(p -> !new File(baseDir, "libs/" + p).isFile())
                .toList();
    }

    private static List<String> copyAddedLibs(List<String> emptyLibs, File baseDir, File librariesDir) throws FileDownloadException {
        ArrayList<String> copied = new ArrayList<>();
        for (String l : emptyLibs) {
            File lib = new File(baseDir, "libs/" + l);
            if(lib.isFile()) {
                File newFile = new File(librariesDir, l);

                if(!newFile.getParentFile().isDirectory() && !newFile.getParentFile().mkdirs()) {
                    throw new FileDownloadException("Failed to create directory for forge library: " + l);
                }
                try {
                    Files.copy(lib.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                   throw new FileDownloadException("Failed to copy library: " + l, e);
                }
                copied.add(l);
            }
        }
        return copied;
    }

    private static void startProcessor(ForgeInstallProcessor processor, List<LibraryData> libraries, ForgeInstallProfile profile, File minecraftJar, File tempDir, File libsDir, File javaFile) throws FileDownloadException {
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

        ProcessBuilder builder = new ProcessBuilder(javaFile.getAbsolutePath(), "-cp");
        builder.command().add(classpathString.toString());
        builder.command().add(mainLib.getMainClass());
        for(String arg : processor.getArgs()) {
            try {
                builder.command().add(processArg(arg, profile, minecraftJar, tempDir, libsDir));
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

    private static String processArg(String arg, ForgeInstallProfile profile, File minecraftJar, File tempDir, File libsDir) throws NoSuchElementException {
        String replacement;
        if(arg.startsWith("[") && arg.endsWith("]")) {
            replacement = libsDir.getAbsolutePath() + "/" + ForgeInstallDataPath.decodeLibraryPath(arg);
        } else if(arg.startsWith("{") && arg.endsWith("}")) {
            replacement = getReplacement(arg, profile, minecraftJar, libsDir);
        } else {
            replacement = arg;
        }
        if(replacement.startsWith("/")) {
            replacement = tempDir.getAbsolutePath() + replacement;
        }
        return replacement;
    }

    private static String getReplacement(String arg, ForgeInstallProfile profile, File minecraftJar, File librariesDir) throws NoSuchElementException {
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
                    if(profileData.getClient().startsWith("[") && profileData.getClient().endsWith("]")) {
                        yield librariesDir.getAbsolutePath() + "/" + profileData.getResolvedClient();
                    }
                    yield profileData.getClient();
                }
            };
        }
        return arg;
    }

    private static List<LibraryData> downloadInstallerLibraries(ForgeInstallProfile profile, File libsDir, File localLibsDir) throws FileDownloadException {
        if(!libsDir.exists() && !libsDir.mkdirs()) {
            throw new FileDownloadException("Failed to create directory for forge libraries");
        }

        ArrayList<LibraryData> libraryData = new ArrayList<>();
        for (MinecraftLibrary lib : profile.getLibraries()) {
            File outFile = new File(libsDir, lib.getDownloads().getArtifact().getPath());
            if(!outFile.getParentFile().isDirectory() && !outFile.getParentFile().mkdirs()) {
                throw new FileDownloadException("Failed to create directory for forge library: " + lib.getName());
            }

            boolean found = false;
            try {
                FileUtil.downloadFile(new URL(lib.getDownloads().getArtifact().getUrl()), outFile);
                found = true;
            } catch (MalformedURLException e) {
                if(localLibsDir != null) {
                    File localFile = new File(localLibsDir, lib.getDownloads().getArtifact().getPath());
                    if (localFile.isFile()) {
                        try {
                            Files.copy(localFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException ex) {
                            throw new FileDownloadException("Unable to copy local library: library=" + lib.getName(), ex);
                        }
                        found = true;
                    }
                }
            }

            String mainClass = null;
            if(found) {
                try {
                    String mainManifestContent = new String(FileUtil.getZipEntry(outFile, "META-INF/MANIFEST.MF"), StandardCharsets.UTF_8);
                    String[] lines = mainManifestContent.split("\n");
                    for (String line : lines) {
                        if (line.startsWith("Main-Class:")) {
                            mainClass = line.substring(11).trim();
                            break;
                        }
                    }
                } catch (IOException ignored) {
                }
            }

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

    private static void extractMaven(String versionId, File tempDir) throws FileDownloadException {
        File jarFile = tempDownloadForgeInstaller(versionId);

        try(ZipFile zipFile = new ZipFile(jarFile)) {
            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory() && entry.getName().startsWith("maven/")) {
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
}
