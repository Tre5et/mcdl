package net.treset.mc_version_loader.assets;

import net.treset.mc_version_loader.exception.FileDownloadException;
import net.treset.mc_version_loader.json.SerializationException;
import net.treset.mc_version_loader.minecraft.MinecraftVersionDetails;
import net.treset.mc_version_loader.util.DownloadStatus;
import net.treset.mc_version_loader.util.FileUtil;
import net.treset.mc_version_loader.util.Sources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MinecraftAssets {
    /**
     * Downloads all assets from the specified asset index to the specified directory.
     * @param assetsDir The directory to download the assets to
     * @param assetIndex The asset index to download all assets from
     * @param indexFileUrl The url to the asset index file. Needed to download the index file itself
     * @param overwrite Whether to overwrite a file if it already exists
     * @throws FileDownloadException If there is an error downloading or writing a file
     */
    public static void downloadAssets(File assetsDir, AssetIndex assetIndex, String indexFileUrl, boolean overwrite) throws FileDownloadException {
        downloadAssets(assetsDir, assetIndex, indexFileUrl, overwrite, status -> {});
    }

    /**
     * Downloads all assets from the specified asset index to the specified directory.
     * @param assetsDir The directory to download the assets to
     * @param assetIndex The asset index to download all assets from
     * @param indexFileUrl The url to the asset index file. Needed to download the index file itself
     * @param overwrite Whether to overwrite a file if it already exists
     * @param statusCallback A callback to be called when a file is downloaded
     * @throws FileDownloadException If there is an error downloading or writing a file
     */
    public static void downloadAssets(File assetsDir, AssetIndex assetIndex, String indexFileUrl, boolean overwrite, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        File indexDir = new File(assetsDir, "indexes");
        if(!indexDir.isDirectory() && !indexDir.mkdirs()) {
            throw new FileDownloadException("Unable to create assets indexes directory");
        }
        String[] indexFileUrlSplit = indexFileUrl.split("/");
        File indexFile = new File(indexDir, indexFileUrlSplit[indexFileUrlSplit.length - 1]);
        if(!indexFile.exists() || overwrite) {
            try {
                FileUtil.downloadFile(new URL(indexFileUrl), indexFile);
            } catch (MalformedURLException e) {
                throw new FileDownloadException("Unable to parse assets index url", e);
            } catch (FileDownloadException e) {
                throw new FileDownloadException("Unable to download assets index", e);
            }
        }

        File objectsDir = new File(assetsDir, "objects");
        if(!objectsDir.isDirectory() && !objectsDir.mkdirs()) {
            throw new FileDownloadException("Unable to create assets objects directory");
        }

        List<Exception> exceptionQueue = new ArrayList<>();
        int totalAmount = assetIndex.getObjects().size();
        int currentAmount = 0;
        boolean failed = false;
        for(AssetObject o : assetIndex.getObjects()) {
            statusCallback.accept(new DownloadStatus(++currentAmount, totalAmount, o.getHash(), failed));
            try {
                downloadAssetsObject(o, objectsDir, overwrite);
            } catch (FileDownloadException e) {
                exceptionQueue.add(e);
                failed = true;
            }
        }
        if(!exceptionQueue.isEmpty()) {
            throw new FileDownloadException("Unable to download " + exceptionQueue.size() + " assets objects", exceptionQueue.get(0));
        }
    }

    /**
     * Downloads a singe asset object to the specified directory.
     * @param object The object to download
     * @param objectsDir The asset directory
     * @param overwrite Whether to overwrite the file if it already exists
     * @throws FileDownloadException If there is an error downloading or writing the file
     */
    public static void downloadAssetsObject(AssetObject object, File objectsDir, boolean overwrite) throws FileDownloadException {
        File dir = new File(objectsDir, object.getHash().substring(0, 2));
        if(!dir.isDirectory() && !dir.mkdirs()) {
            throw new FileDownloadException("Unable to create assets object directory, id=" + object.getHash());
        }
        File objectFile = new File(dir, object.getHash());
        try {
            URL url = new URL(Sources.getAssetUrl(object.getHash()));
            if(!objectFile.exists() || overwrite) {
                try {
                    FileUtil.downloadFile(url, objectFile);
                } catch (FileDownloadException e) {
                    throw new FileDownloadException("Unable to download assets object, id=" + object.getHash(), e);
                }
            }
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to parse asset object url, id=" + object.getHash(), e);
        }
    }

    /**
     * Parses a asset index from a specified url.
     * @param url The url to get the asset index from. Typically found in {@link MinecraftVersionDetails#getAssetIndex()}.
     * @return The asset index as a object
     * @throws FileDownloadException If there is an error downloading or parsing the file
     */
    public static AssetIndex getAssetIndex(String url) throws FileDownloadException {
        try {
            return AssetIndex.fromJson(FileUtil.getStringFromUrl(url));
        } catch (SerializationException e) {
            throw new FileDownloadException("Unable to parse asset index", e);
        }
    }
}
