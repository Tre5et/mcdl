package net.treset.mcdl.minecraft;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.json.GenericJsonParsable;
import net.treset.mcdl.json.JsonUtils;
import net.treset.mcdl.json.SerializationException;
import net.treset.mcdl.util.DownloadStatus;
import net.treset.mcdl.util.FileUtil;
import net.treset.mcdl.util.OsUtil;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class MinecraftLibrary {
    private String name;
    private Downloads downloads;
    private Extract extract;
    private List<MinecraftRule> rules;
    private Natives natives;

    public static class Natives {
        private String windows;
        private String linux;
        private String osx;

        public Natives(String windows, String linux, String osx) {
            this.windows = windows;
            this.linux = linux;
            this.osx = osx;
        }

        public String getWindows() {
            return windows;
        }

        public void setWindows(String windows) {
            this.windows = windows;
        }

        public String getLinux() {
            return linux;
        }

        public void setLinux(String linux) {
            this.linux = linux;
        }

        public String getOsx() {
            return osx;
        }

        public void setOsx(String osx) {
            this.osx = osx;
        }
    }

    public static class Downloads {
        private Artifact artifact;
        private Classifiers classifiers;

        public static class Artifact {
            private String path;
            private String sha1;
            private int size;
            private String url;

            public Artifact(String path, String sha1, int size, String url) {
                this.path = path;
                this.sha1 = sha1;
                this.size = size;
                this.url = url;
            }

            /**
             * Downloads the artifact and returns its path
             * @param baseDir The base directory to download the artifact to
             * @param localDir The local directory to download the artifact to
             * @return The path of the downloaded artifact
             * @throws FileDownloadException if there is an error downloading the artifact
             */
            public String download(File baseDir, File localDir) throws FileDownloadException {
                if(baseDir == null || !baseDir.isDirectory() || getPath() == null || getPath().isBlank()) {
                    throw new FileDownloadException("Unmet requirements for artifact download: artifact=" + getPath());
                }

                if(getUrl() == null || getUrl().isBlank()) {
                    return copyLocal(getPath(), baseDir, localDir);
                } else {
                    URL downloadUrl;
                    try {
                        downloadUrl = new URL(getUrl());
                    } catch (MalformedURLException e) {
                        throw new FileDownloadException("Unable to convert artifact download url: artifact=" + getUrl(), e);
                    }

                    File outDir = new File(baseDir, getPath().substring(0, getPath().lastIndexOf('/')));
                    if (!outDir.isDirectory() && !outDir.mkdirs()) {
                        throw new FileDownloadException("Unable to make required dirs: artifact=" + getPath());
                    }
                    File outFile = new File(outDir, getPath().substring(getPath().lastIndexOf('/')));

                    FileUtil.downloadFile(downloadUrl, outFile);

                    return getPath();
                }
            }

            /**
             * Copies a local artifact to the base directory
             * @param path The path of the artifact
             * @param baseDir The base directory to copy the artifact to
             * @param localDir The local directory to copy the artifact from
             * @return The path of the copied artifact
             * @throws FileDownloadException if there is an error copying the artifact
             */
            public static String copyLocal(String path, File baseDir, File localDir) throws FileDownloadException {
                if(path == null || path.isBlank() || baseDir == null || !baseDir.isDirectory() || localDir == null || !localDir.isDirectory()) {
                    return null;
                }

                File outDir = new File(baseDir, path.substring(0, path.lastIndexOf('/')));
                if (!outDir.isDirectory() && !outDir.mkdirs()) {
                    return null;
                }
                File outFile = new File(outDir, path.substring(path.lastIndexOf('/')));

                File localFile = new File(localDir, path);
                if (localFile.isFile()) {
                    try {
                        Files.copy(localFile.toPath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        return path;
                    } catch (IOException e) {
                        throw new FileDownloadException("Unable to copy local artifact: artifact=" + path, e);
                    }
                }
                System.out.println("Local artifact not found: artifact=" + path);
                return null;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public String getSha1() {
                return sha1;
            }

            public void setSha1(String sha1) {
                this.sha1 = sha1;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public static class Classifiers {
            List<Native> natives;

            public static class Native {
                private String name;
                private Downloads.Artifact artifact;

                public Native(String name, Downloads.Artifact artifact) {
                    this.name = name;
                    this.artifact = artifact;
                }

                public static Native from(String name, JsonObject nativeObj) throws SerializationException {
                    if(nativeObj == null) {
                        return null;
                    }
                    return new Native(
                            name,
                            GenericJsonParsable.fromJson(nativeObj.toString(), Downloads.Artifact.class)
                    );
                }

                /**
                 * Applies a native and moves it to the correct location
                 * @param nativeFile The native file to apply the extract to
                 * @param nativeDir The directory to extract the native file to
                 * @param extract The extract to apply
                 * @return true if the extract was applied, false if the extract was not applied
                 * @throws FileDownloadException if there is an error applying the extract
                 */
                public static boolean apply(File nativeFile, File nativeDir, MinecraftLibrary.Extract extract) throws FileDownloadException {
                    if(nativeFile == null || !nativeFile.isFile() || nativeDir == null) {
                        throw new FileDownloadException("Unmet requirements for native application: file=" + nativeFile);
                    }

                    try {
                        Files.createDirectories(nativeDir.toPath());
                    } catch (IOException e) {
                        throw new FileDownloadException("Unable to create native directory", e);
                    }

                    try {
                        applyExtract(nativeFile, nativeDir, extract);
                        return true;
                    } catch(IOException e) {
                        File target = new File(nativeDir, nativeFile.getName());
                        try {
                            Files.copy(nativeFile.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        } catch (IOException e1) {
                            throw new FileDownloadException("Unable to extract or copy native file: file=" + nativeFile, e1);
                        }
                        return false;
                    }
                }

                /**
                 * Applies the extract to the file
                 * @param file The file to apply the extract to
                 * @param nativesDir The directory to extract the file to
                 * @param extract The extract to apply
                 * @throws IOException if there is an error applying the extract
                 */
                public static void applyExtract(File file, File nativesDir, MinecraftLibrary.Extract extract) throws IOException {
                    if(file == null || !file.isFile() || nativesDir == null || !nativesDir.isDirectory()) {
                        throw new IOException("Unmet requirements for extract: file=" + file);
                    }

                    File tempDir = new File(FileUtil.getTempDir(), file.getName());
                    Files.createDirectories(tempDir.toPath());

                    FileUtil.exctractFile(file, tempDir);

                    if(extract != null && extract.getExclude() != null) {
                        for(String exclude : extract.getExclude()) {
                            File toRemove = new File(tempDir, exclude);

                            if(toRemove.exists()) {
                                try {
                                    FileUtil.delete(toRemove);
                                } catch (IOException e) {
                                    throw new IOException("Unable to delete extracted file: file=" + toRemove, e);
                                }
                            }
                        }
                    }

                    FileUtil.copyDirectory(tempDir, nativesDir, StandardCopyOption.REPLACE_EXISTING);
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public Downloads.Artifact getArtifact() {
                    return artifact;
                }

                public void setArtifact(Downloads.Artifact artifact) {
                    this.artifact = artifact;
                }
            }

            public static Classifiers from(JsonObject classifiers) throws SerializationException {
                Set<Map.Entry<String, JsonElement>> entries = JsonUtils.getMembers(classifiers);
                if(entries == null) {
                    return null;
                }
                List<Native> natives = new ArrayList<>();
                for(Map.Entry<String, JsonElement> e : entries) {
                    natives.add(Native.from(e.getKey(), JsonUtils.getAsJsonObject(e.getValue())));
                }
                return new Classifiers(natives);
            }

            public Classifiers(List<Native> natives) {
                this.natives = natives;
            }

            public List<Native> getNatives() {
                return natives;
            }

            public void setNatives(List<Native> natives) {
                this.natives = natives;
            }
        }

        public Downloads(Artifact artifact, Classifiers classifiers) {
            this.artifact = artifact;
            this.classifiers = classifiers;
        }

        public Artifact getArtifact() {
            return artifact;
        }

        public void setArtifact(Artifact artifact) {
            this.artifact = artifact;
        }

        public Classifiers getClassifiers() {
            return classifiers;
        }

        public void setClassifiers(Classifiers classifiers) {
            this.classifiers = classifiers;
        }
    }

    public static class Extract {
        private List<String> exclude;

        public Extract(List<String> exclude) {
            this.exclude = exclude;
        }

        public List<String> getExclude() {
            return exclude;
        }

        public void setExclude(List<String> exclude) {
            this.exclude = exclude;
        }
    }

    public MinecraftLibrary(String name, Downloads downloads, Extract extract, List<MinecraftRule> rules, Natives natives) {
        this.name = name;
        this.downloads = downloads;
        this.extract = extract;
        this.rules = rules;
        this.natives = natives;
    }

    /**
     * Downloads all libraries in a list
     * @param libraries The libraries to download
     * @param librariesDir The directory to download the libraries to
     * @param localLibraryDir The directory to download the libraries to
     * @param features The features to check for rule applicability
     * @param nativesDir The directory to download the natives to
     * @param onStatus The callback to call with download status
     * @return a list of the downloaded libraries
     * @throws FileDownloadException if there is an error downloading the libraries
     */
    public static List<String> downloadAll(List<MinecraftLibrary> libraries, File librariesDir, File localLibraryDir, List<String> features, File nativesDir, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        ArrayList<String> result = new ArrayList<>();
        List<Exception> exceptionQueue = new ArrayList<>();
        int size = libraries.size();
        int current = 0;
        for(MinecraftLibrary l : libraries) {
            onStatus.accept(new DownloadStatus(++current, size, l.getName()));
            try {
                l.download(librariesDir, localLibraryDir, nativesDir, features, result);
            } catch (FileDownloadException e) {
                exceptionQueue.add(e);
            }
        }
        if(!exceptionQueue.isEmpty()) {
            throw new FileDownloadException("Unable to download " + exceptionQueue.size() + " libraries", exceptionQueue.get(0));
        }
        return result;
    }

    /**
     * Downloads the library
     * @param librariesDir The directory to download the library to
     * @param localLibrariesDir The directory to download the library to
     * @param nativesDir The directory to download the natives to
     * @param features The features to check for rule applicability
     * @throws FileDownloadException if there is an error downloading the library
     */
    public void download(File librariesDir, File localLibrariesDir, File nativesDir, List<String> features, ArrayList<String> currentLibraries) throws FileDownloadException {
        if(getRules() != null && !getRules().stream().allMatch(r -> r.isApplicable(features))) {
            return;
        }

        if(getDownloads() == null || librariesDir == null || !librariesDir.isDirectory()) {
            throw new FileDownloadException("Unmet requirements for library download: library=" + getName());
        }

        if (getNatives() != null && getDownloads().getClassifiers() != null && getDownloads().getClassifiers().getNatives() != null) {
            String applicableNative = null;
            if (OsUtil.isOsName("windows") && getNatives().getWindows() != null) {
                applicableNative = getNatives().getWindows();
            } else if (OsUtil.isOsName("linux") && getNatives().getLinux() != null) {
                applicableNative = getNatives().getLinux();
            } else if (OsUtil.isOsName("osx") && getNatives().getOsx() != null) {
                applicableNative = getNatives().getOsx();
            }

            for (MinecraftLibrary.Downloads.Classifiers.Native na : getDownloads().getClassifiers().getNatives()) {
                if (na.getName().equals(applicableNative)) {
                    String libPath = na.getArtifact().download(librariesDir, localLibrariesDir);
                    Downloads.Classifiers.Native.apply(new File(librariesDir, libPath), nativesDir, getExtract());
                }
            }
        }

        if(getDownloads().getArtifact() != null) {
            String path = getDownloads().getArtifact().download(librariesDir, localLibrariesDir);
            if(path != null) {
                currentLibraries.add(path);
            }
        }
    }

    public boolean isApplicable(List<String> activeFeatures) {
        for(MinecraftRule r : getRules()) {
            if(!r.isApplicable(activeFeatures)) {
                return false;
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Downloads getDownloads() {
        return downloads;
    }

    public void setDownloads(Downloads downloads) {
        this.downloads = downloads;
    }

    public Extract getExtract() {
        return extract;
    }

    public void setExtract(Extract extract) {
        this.extract = extract;
    }

    public List<MinecraftRule> getRules() {
        return rules;
    }

    public void setRules(List<MinecraftRule> rules) {
        this.rules = rules;
    }

    public Natives getNatives() {
        return natives;
    }

    public void setNatives(Natives natives) {
        this.natives = natives;
    }
}
