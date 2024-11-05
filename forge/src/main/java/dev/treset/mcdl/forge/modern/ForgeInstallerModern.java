package dev.treset.mcdl.forge.modern;

import dev.treset.mcdl.forge.*;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.minecraft.MinecraftDL;
import dev.treset.mcdl.minecraft.MinecraftLibrary;
import dev.treset.mcdl.minecraft.MinecraftVersion;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@Deprecated
public class ForgeInstallerModern extends ForgeInstaller {
    private ForgeInstallProfile profile;

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

    public ForgeInstallerModern(String version) {
        super(version);
    }

    @Override
    protected ForgeInstallData loadInstallData() throws FileDownloadException {
        File installer = downloadInstaller();
        File extracted = getExtractedDir();
        try {
            FileUtil.exctractFile(installer, extracted);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to extract forge installer", e);
        }

        try {
            String profileJson = FileUtil.readFileAsString(new File(extracted, "install_profile.json"));
            profile = ForgeInstallProfile.fromJson(profileJson);
            String versionJson = FileUtil.readFileAsString(new File(extracted, profile.getJson()));
            ForgeVersion version = ForgeVersion.fromJson(versionJson);
            return version.toInstallData();
        } catch (IOException | SerializationException e) {
            throw new FileDownloadException("Failed to read install profile", e);
        }
    }

    @Override
    protected void createNewClient(ForgeInstallData installData, File librariesDir, File minecraftClient, File javaExecutable, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        File tempDir = new File(FileUtil.getTempDir(), "forge-" + getVersion() + "-installer");
        if(!tempDir.isDirectory() && !tempDir.mkdirs()) {
            throw new FileDownloadException("Failed to create directory for forge installer");
        }

        File mavenDir = new File(getExtractedDir(), "maven");
        if(!mavenDir.isDirectory()) {
            throw new FileDownloadException("Maven directory does not exist");
        }

        List<ForgeInstallProcessor> processors = profile.getProcessors().stream().filter(p ->
                p.getSides() == null || p.getSides().contains("client")
        ).toList();

        if (onStatus != null) {
            onStatus.accept(new DownloadStatus(1, processors.size() + 3, "Download Installer Libraries"));
        }
        List<LibraryData> libraries = downloadInstallerLibraries(librariesDir, mavenDir);

        if (onStatus != null) {
            onStatus.accept(new DownloadStatus(2, processors.size() + 3, "Extract Installer Data"));
        }

        for (int i = 0; i < processors.size(); i++) {
            if (onStatus != null) {
                onStatus.accept(new DownloadStatus(i + 3, processors.size() + 3, "Run Processor: " + processors.get(i).getJar()));
            }
            startProcessor(processors.get(i), libraries, minecraftClient, librariesDir, javaExecutable);
        }

        if (onStatus != null) {
            onStatus.accept(new DownloadStatus(processors.size() + 3, processors.size() + 3, "Copy Processed Libraries"));
        }
    }

    @Override
    protected List<String> downloadNewLibraries(ForgeInstallData installData, File librariesDir, File nativesDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        File mavenDir = new File(getExtractedDir(), "maven");
        if(!mavenDir.isDirectory()) {
            throw new FileDownloadException("Maven directory does not exist");
        }
        return MinecraftDL.downloadVersionLibraries(installData.getLibraries(), librariesDir, mavenDir, List.of(), nativesDir, onStatus);
    }

    private List<LibraryData> downloadInstallerLibraries(File librariesDir, File mavenDir) throws FileDownloadException {
        if(!librariesDir.exists() && !librariesDir.mkdirs()) {
            throw new FileDownloadException("Failed to create directory for forge libraries");
        }

        ArrayList<LibraryData> libraryData = new ArrayList<>();
        for (MinecraftLibrary lib : profile.getLibraries()) {
            File outFile = new File(librariesDir, lib.getDownloads().getArtifact().getPath());
            if(!outFile.getParentFile().isDirectory() && !outFile.getParentFile().mkdirs()) {
                throw new FileDownloadException("Failed to create directory for forge library: " + lib.getName());
            }

            boolean found = false;
            try {
                FileUtil.downloadFile(new URL(lib.getDownloads().getArtifact().getUrl()), outFile, ForgeDL.getCaching());
                found = true;
            } catch (MalformedURLException e) {
                if(mavenDir != null) {
                    File localFile = new File(mavenDir, lib.getDownloads().getArtifact().getPath());
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
            } else {
                System.out.println("Failed to download library: " + lib.getName());
            }

            libraryData.add(new LibraryData(lib.getName(), outFile.getAbsolutePath(), mainClass));
        }

        return libraryData;
    }

    private void startProcessor(ForgeInstallProcessor processor, List<LibraryData> libraries, File minecraftClient, File librariesDir, File javaExecutable) throws FileDownloadException {
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

        boolean isDeobfRealms = false;

        ProcessBuilder builder = new ProcessBuilder(javaExecutable.getAbsolutePath(), "-cp");
        builder.command().add(classpathString.toString());
        builder.command().add(mainLib.getMainClass());
        for(String arg : processor.getArgs()) {
            try {
                builder.command().add(processArg(arg, profile, minecraftClient, librariesDir));
                if(arg.equals("DEOBF_REALMS")) {
                    isDeobfRealms = true;
                }
            } catch (NoSuchElementException e) {
                throw new FileDownloadException("Failed to process argument: " + arg, e);
            }
        }
        if(isDeobfRealms) {
            File versionJson = new File(getExtractedDir(), "mc-version.json");
            provideMinecraftVersionJson(versionJson, minecraftClient);

            builder.command().add("--libs");
            builder.command().add(librariesDir.getAbsolutePath());
            builder.command().add("--json");
            builder.command().add(versionJson.getAbsolutePath());
        }

        builder.directory(getExtractedDir());

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
                System.out.println("Processor error forwarding failed: " + processor.getJar());
            }
            throw new FileDownloadException("Processor failed: " + processor.getJar());
        }
    }

    private void provideMinecraftVersionJson(File target, File minecraftClient) throws FileDownloadException {
        MinecraftVersion version = MinecraftVersion.get(profile.getMinecraft());
        try {
            FileUtil.writeToFile(version.toJson().getBytes(), target);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to write version.json", e);
        }
    }

    private String processArg(String arg, ForgeInstallProfile profile, File minecraftClient, File librariesDir) throws NoSuchElementException {
        File extractedDir = getExtractedDir();

        String replacement;
        if(arg.startsWith("[") && arg.endsWith("]")) {
            replacement = librariesDir.getAbsolutePath() + "/" + ForgeInstallDataPath.decodeLibraryPath(arg);
        } else if(arg.startsWith("{") && arg.endsWith("}")) {
            replacement = getReplacement(arg, profile, minecraftClient, librariesDir);
        } else {
            replacement = arg;
        }
        if(replacement.startsWith("/")) {
            replacement = extractedDir.getAbsolutePath() + replacement;
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

    private File getExtractedDir()  {
        return new File(FileUtil.getTempDir(), "forge-" + getVersion() + "-installer");
    }
}
