package dev.treset.mcdl.assets;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.GenericJsonParsable;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.DownloadStatus;
import dev.treset.mcdl.util.FileUtil;
import dev.treset.mcdl.util.HttpUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.function.Consumer;

public class AssetIndex extends GenericJsonParsable {
    private boolean mapToResources;
    private Map<String, AssetObject> objects;

    private transient String version = null;

    public AssetIndex(Map<String, AssetObject> objects) {
        this.objects = objects;
    }

    public static AssetIndex fromJson(String json, String version) throws SerializationException {
        AssetIndex index = fromJson(json, AssetIndex.class);
        index.version = version;
        return index;
    }

    /**
     * Parses an asset index from a specified url.
     * @param url The url to get the asset index from. Typically found in {@code MinecraftVersionDetails::getAssetIndex}.
     * @return The asset index as a object
     * @throws FileDownloadException If there is an error downloading or parsing the file
     */
    public static AssetIndex get(String url) throws FileDownloadException {
        try {
            String[] indexFileUrlSplit = url.split("/");
            String fileName = indexFileUrlSplit[indexFileUrlSplit.length - 1];
            String version = fileName.substring(0, fileName.length() - 5);
            String content = HttpUtil.getString(url, AssetsDL.getCaching());
            return AssetIndex.fromJson(content, version);
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse asset index", e);
        } catch (IOException e) {
            throw new FileDownloadException("Unable to download asset index", e);
        }
    }

    /**
     * Gets the local asset index from the specified version and assets directory
     * @param version The version of the asset index
     * @param assetsDir The assets directory
     * @return The asset index
     * @throws IOException If there is an error reading the file
     */
    public static AssetIndex getLocal(String version, File assetsDir) throws IOException {
        String indexContent = FileUtil.readFileAsString(new File(assetsDir, "indexes/" + version + ".json"));
        AssetIndex index;
        try {
            return AssetIndex.fromJson(indexContent, version);
        } catch (SerializationException e) {
            throw new IOException("Unable to parse asset index", e);
        }
    }

    /**
     * Gets the file location of a specific asset hash
     * @param assetsDir The assets directory
     * @param hash The hash of the asset
     * @return The file location of the asset
     */
    public static File getLocalAsset(File assetsDir, String hash) {
        return new File(assetsDir, "objects/" + hash.substring(0, 2) + "/" + hash);
    }

    /**
     * Downloads all assets from the asset index to the specified directory and writes the asset index.
     * @param assetsDir The directory to download the assets to
     * @param overwrite Whether to overwrite a file if it already exists
     * @param onStatus A callback to be called when a file is downloaded
     * @throws FileDownloadException If there is an error downloading or writing a file
     */
    public void downloadAll(File assetsDir, boolean overwrite, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        File indexDir = new File(assetsDir, "indexes");
        if(!indexDir.isDirectory() && !indexDir.mkdirs()) {
            throw new FileDownloadException("Unable to create assets indexes directory");
        }
        File indexFile = new File(indexDir, getVersion() + ".json");
        if(!indexFile.exists() || overwrite) {
            try {
                FileUtil.writeToFile(toJson().getBytes(), indexFile);
            } catch (IOException e) {
                throw new FileDownloadException("Unable to write assets index file", e);
            }
        }

        File objectsDir = new File(assetsDir, "objects");
        if(!objectsDir.isDirectory() && !objectsDir.mkdirs()) {
            throw new FileDownloadException("Unable to create assets objects directory");
        }

        int totalAmount = getObjects().size();
        int currentAmount = 0;
        for(AssetObject o : getObjects().values()) {
            if (onStatus != null) {
                onStatus.accept(new DownloadStatus(++currentAmount, totalAmount, o.getHash()));
            }
            try {
                o.download(objectsDir, overwrite);
            } catch (FileDownloadException e) {
                throw new FileDownloadException("Unable to download assets object, id=" + o.getHash(), e);
            }
        }
    }

    /**
     * Creates resolved assets from the asset index's downloaded assets
     * @param assetsDir The assets directory
     * @return The directory where the assets were extracted to
     * @throws IOException If there is an error extracting the assets
     */
    public File resolveAll(File assetsDir) throws IOException {
        File outDir = new File(assetsDir, "virtual/" + getVersion());
        if(!outDir.isDirectory() && !outDir.mkdirs()) {
            throw new IOException("Unable to create virtual assets directory");
        }
        for(Map.Entry<String, AssetObject> o : getObjects().entrySet()) {
            File srcFile = getLocalAsset(assetsDir, o.getValue().getHash());
            File outFile = new File(outDir, o.getKey());
            try {
                FileUtil.copyFileContent(srcFile, outFile, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new IOException("Unable to copy virtual asset, id=" + o.getKey(), e);
            }
        }
        return outDir;
    }

    public Map<String, AssetObject> getObjects() {
        return objects;
    }

    public void setObjects(Map<String, AssetObject> objects) {
        this.objects = objects;
    }

    public boolean isMapToResources() {
        return mapToResources;
    }

    public void setMapToResources(boolean mapToResources) {
        this.mapToResources = mapToResources;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
