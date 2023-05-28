package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.assets.AssetIndex;
import net.treset.mc_version_loader.assets.AssetObject;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AssetsFileDownloader {
    private static final Logger LOGGER = Logger.getLogger(AssetIndex.class.toString());

    public static boolean downloadAssets(File assetsDir, AssetIndex assetIndex, String indexFileUrl, boolean overwrite) {
        File indexDir = new File(assetsDir, "indexes");
        if(!indexDir.isDirectory() && !indexDir.mkdirs()) {
            LOGGER.log(Level.SEVERE, "Unable to create assets index directory");
            return false;
        }
        String[] indexFileUrlSplit = indexFileUrl.split("/");
        File indexFile = new File(indexDir, indexFileUrlSplit[indexFileUrlSplit.length - 1]);
        if(!indexFile.exists() || overwrite) {
            try {
                if (!FileUtils.downloadFile(new URL(indexFileUrl), indexFile)) {
                    LOGGER.log(Level.SEVERE, "Unable to download assets index");
                    return false;
                }
            } catch (MalformedURLException e) {
                LOGGER.log(Level.SEVERE, "Unable to parse asset index url", e);
                return false;
            }
        } else {
            LOGGER.log(Level.INFO, "Assets index already exists, skipping download");
            return true;
        }

        File objectsDir = new File(assetsDir, "objects");
        if(!objectsDir.isDirectory() && !objectsDir.mkdirs()) {
            LOGGER.log(Level.SEVERE, "Unable to create assets objects directory");
            return false;
        }

        return assetIndex.getObjects().parallelStream()
                .allMatch(o -> downloadAssetsObject(o, objectsDir, overwrite));
    }

    public static boolean downloadAssetsObject(AssetObject object, File objectsDir, boolean overwrite) {
        File dir = new File(objectsDir, object.getHash().substring(0, 2));
        if(!dir.isDirectory() && !dir.mkdirs()) {
            LOGGER.log(Level.SEVERE, "Unable to create assets object directory, id=" + object.getHash());
            return false;
        }
        File objectFile = new File(dir, object.getHash());
        try {
            URL url = new URL(Sources.getAssetsBaseUrl() + object.getHash().substring(0, 2) + "/" + object.getHash());
            if(!objectFile.exists() || overwrite) {
                if(!FileUtils.downloadFile(url, objectFile)) {
                    LOGGER.log(Level.SEVERE, "Unable to download asset object, id=" + object.getHash());
                    return false;
                }
            }
        } catch (MalformedURLException e) {
            LOGGER.log(Level.SEVERE, "Unable to parse asset object url, id=" + object.getHash(), e);
            return false;
        }
        return true;
    }
}
