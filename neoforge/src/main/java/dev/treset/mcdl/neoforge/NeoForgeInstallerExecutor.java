package dev.treset.mcdl.neoforge;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.minecraft.MinecraftProfile;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class NeoForgeInstallerExecutor {
    private final String version;

    private final File directory;
    private final File installer;

    public NeoForgeInstallerExecutor(String version) {
        this.version = version;
        this.directory = new File(FileUtil.getTempDir(), "neoforge-" + version + "-install");
        this.installer = new File(directory, "neoforge-" + version + "-installer.jar");
    }

    /**
     * Installs the neo forge version and its libraries to the specified directory.
     * @param librariesDir The directory to install the libraries to
     * @param javaExecutable The java executable to run the installer with
     * @param onStatus The consumer to accept download status updates
     * @return The profile of the installed forge version
     * @throws FileDownloadException If there is an error downloading or installing the forge version
     */
    public MinecraftProfile install(File librariesDir, File javaExecutable, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        onStatus.accept(new DownloadStatus(1, -1, "Install directory"));
        prepareDirectory();
        onStatus.accept(new DownloadStatus(2, -1, "NeoForge Installer"));
        downloadInstaller();
        try {
            onStatus.accept(new DownloadStatus(3, -1, "NeoForge Version"));
            executeInstaller(javaExecutable, onStatus);
            onStatus.accept(new DownloadStatus(4, -1, "Libraries"));
            copyLibraries(librariesDir);
            return parseInstallProfile();
        } catch (IOException e) {
            throw new FileDownloadException("Failed to install neoforge", e);
        } catch (SerializationException e) {
            throw new FileDownloadException("Failed to parse neoforge version profile", e);
        }
    }

    public MinecraftProfile parseInstallProfile() throws SerializationException {
        File versionsDir = new File(directory, "versions");
        if(!versionsDir.exists()) {
            throw new SerializationException("NeoForge installer versions directory does not exist");
        }
        File[] versions = versionsDir.listFiles();
        if(versions == null || versions.length == 0) {
            throw new SerializationException("NeoForge installer versions directory is empty");
        }
        File version = Arrays.stream(versions).filter(f -> f.isDirectory() && f.getName().contains("neoforge")).findFirst().orElse(null);
        if(version == null) {
            throw new SerializationException("NeoForge installer versions directory does not contain a neoforge version");
        }
        File profile = new File(version, version.getName() + ".json");
        try {
            String content = FileUtil.readFileAsString(profile);
            return MinecraftProfile.fromJson(content);
        } catch (IOException e) {
            throw new SerializationException("Failed to read neoforge installer version file", e);
        }
    }

    public void copyLibraries(File target) throws IOException {
        FileUtil.copyDirectory(new File(directory, "libraries"), target, StandardCopyOption.REPLACE_EXISTING);
    }

    public void executeInstaller(File javaExecutable, Consumer<DownloadStatus> onStatus) throws IOException {
        Process process = getInstallerProcess(javaExecutable);

        AtomicInteger step = new AtomicInteger(3);

        try(BufferedReader reader = process.inputReader()) {
            reader.lines().iterator().forEachRemaining(value -> {
                if(value.trim().startsWith("Considering library ")) {
                    onStatus.accept(new DownloadStatus(step.incrementAndGet(), -1, value.substring(20)));
                } else if(value.trim().startsWith("MainClass: ")) {
                    onStatus.accept(new DownloadStatus(step.incrementAndGet(), -1, value.substring(13)));
                }
                System.out.println(value);
            });
        } catch (IOException e) {
            System.err.println("Failed to read neoforge installer output:\n" + e);
        }

        while(process.isAlive()) {
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                System.err.println("Processor listening interrupted, restarting");
            }
        }

        if(process.exitValue() != 0) {
            ArrayList<String> error = new ArrayList<>();
            try(BufferedReader reader = process.errorReader()) {
                reader.lines().iterator().forEachRemaining(error::add);
            } catch (IOException e) {
                System.err.println("NeoForge installer error forwarding failed:\n" + e);
            }
            throw new FileDownloadException("NeoForge installer failed: \n" + String.join("\n", error));
        }
    }

    private Process getInstallerProcess(File javaExecutable) throws IOException {
        if(!installer.isFile()) {
            throw new IOException("NeoForge installer jar does not exist");
        }

        ProcessBuilder pb = new ProcessBuilder(
                javaExecutable.getAbsolutePath(),
                "-jar",
                installer.getAbsolutePath(),
                "--installClient",
                directory.getAbsolutePath()
        );
        pb.directory(directory);

        Process process;
        try {
            process = pb.start();
        } catch (IOException e) {
            throw new IOException("Failed to start neoforge installer", e);
        }
        return process;
    }

    public void prepareDirectory() throws FileDownloadException {
        try {
            if (directory.exists()) {
                FileUtil.delete(directory);
            }

            if (!directory.mkdirs()) {
                throw new FileDownloadException("Failed to create directory for neoforge installer");
            }

            File launcherProfilesFile = new File(directory, "launcher_profiles.json");
            FileUtil.writeToFile("{\"profiles\":{}}".getBytes(), launcherProfilesFile);
        } catch (IOException e) {
            throw new FileDownloadException("Failed to create directory for neoforge installer", e);
        }
    }

    public File downloadInstaller() throws FileDownloadException {
        if(!installer.exists()) {
            if (!installer.getParentFile().exists() && !installer.getParentFile().mkdirs()) {
                throw new FileDownloadException("Failed to create directory for neoforge installer");
            }

            try {
                FileUtil.downloadFile(new URL(NeoForgeDL.getInstallerUrl(version)), installer, NeoForgeDL.getCaching());
            } catch (MalformedURLException e) {
                throw new FileDownloadException("Failed to parse neoforge installer Url", e);
            }
        }
        installer.deleteOnExit();
        return installer;
    }
}
