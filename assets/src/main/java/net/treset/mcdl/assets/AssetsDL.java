package net.treset.mcdl.assets;

import net.treset.mcdl.exception.FileDownloadException;
import net.treset.mcdl.util.DownloadStatus;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class AssetsDL {
    private static final String ASSET_URL = "https://resources.download.minecraft.net/%s/%s"; // AssetId[:2], AssetId

    /**
     * Downloads all assets from the specified asset index to the specified directory and writes the asset index.
     * @param assetsDir The directory to download the assets to
     * @param assetIndex The asset index to download all assets from
     * @param overwrite Whether to overwrite a file if it already exists
     * @throws FileDownloadException If there is an error downloading or writing a file
     */
    public static void downloadAssets(AssetIndex assetIndex, File assetsDir, boolean overwrite) throws FileDownloadException {
        assetIndex.downloadAll(assetsDir, overwrite, status -> {});
    }

    /**
     * Downloads all assets from the specified asset index to the specified directory and writes the asset index.
     * @param assetsDir The directory to download the assets to
     * @param assetIndex The asset index to download all assets from
     * @param overwrite Whether to overwrite a file if it already exists
     * @param onStatus A callback to be called when a file is downloaded
     * @throws FileDownloadException If there is an error downloading or writing a file
     */
    public static void downloadAssets(AssetIndex assetIndex, File assetsDir, boolean overwrite, Consumer<DownloadStatus> onStatus) throws FileDownloadException {
        assetIndex.downloadAll(assetsDir, overwrite, onStatus);
    }

    /**
     * Downloads a single asset object to the specified directory.
     * @param object The object to download
     * @param objectsDir The asset directory
     * @param overwrite Whether to overwrite the file if it already exists
     * @throws FileDownloadException If there is an error downloading or writing the file
     */
    public static void downloadAssetsObject(AssetObject object, File objectsDir, boolean overwrite) throws FileDownloadException {
        object.download(objectsDir, overwrite);
    }

    /**
     * Parses an asset index from a specified url.
     * @param url The url to get the asset index from. Typically found in {@code MinecraftVersionDetails::getAssetIndex}.
     * @return The asset index as a object
     * @throws FileDownloadException If there is an error downloading or parsing the file
     */
    public static AssetIndex getAssetIndex(String url) throws FileDownloadException {
        return AssetIndex.get(url);
    }

    /**
     * Gets the file location of a specific asset hash
     * @param assetsDir The assets directory
     * @param hash The hash of the asset
     * @return The file location of the asset
     */
    public static File getAssetFile(File assetsDir, String hash) {
        return AssetIndex.getLocalAsset(assetsDir, hash);
    }

    /**
     * Creates resolved assets from an asset index's downloaded objects
     * @param index The asset index to resolve assets for
     * @param assetsDir The assets directory
     * @return The directory where the assets were extracted to
     * @throws IOException If there is an error extracting the assets
     */
    public static File resolveAssets(AssetIndex index, File assetsDir) throws IOException {
        return index.resolveAll(assetsDir);

    }

    /**
     * Creates resolved assets from an asset index's downloaded objects
     * @param version The version of the asset index
     * @param assetsDir The assets directory
     * @return The directory where the assets were extracted to
     * @throws IOException If there is an error extracting the assets
     */
    public static File resolveAssets(String version, File assetsDir) throws IOException {
        return AssetIndex.getLocal(version, assetsDir).resolveAll(assetsDir);
    }

    public static String getAssetUrl(String assetId) {
        if(assetId == null || assetId.length() < 2) {
            throw new IllegalArgumentException("Invalid assetId=" + assetId);
        }
        return String.format(ASSET_URL, assetId.substring(0,2), assetId);
    }
}
