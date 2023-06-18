package net.treset.mc_version_loader.files;

import net.treset.mc_version_loader.assets.AssetIndex;
import net.treset.mc_version_loader.assets.AssetObject;
import net.treset.mc_version_loader.exception.FileDownloadException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AssetsFileDownloader {
    public static void downloadAssets(File assetsDir, AssetIndex assetIndex, String indexFileUrl, boolean overwrite) throws FileDownloadException {
        downloadAssets(assetsDir, assetIndex, indexFileUrl, overwrite, status -> {});
    }

    public static void downloadAssets(File assetsDir, AssetIndex assetIndex, String indexFileUrl, boolean overwrite, Consumer<DownloadStatus> statusCallback) throws FileDownloadException {
        File indexDir = new File(assetsDir, "indexes");
        if(!indexDir.isDirectory() && !indexDir.mkdirs()) {
            throw new FileDownloadException("Unable to create assets indexes directory");
        }
        String[] indexFileUrlSplit = indexFileUrl.split("/");
        File indexFile = new File(indexDir, indexFileUrlSplit[indexFileUrlSplit.length - 1]);
        if(!indexFile.exists() || overwrite) {
            try {
                FileUtils.downloadFile(new URL(indexFileUrl), indexFile);
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
            statusCallback.accept(new DownloadStatus(currentAmount++, totalAmount, o.getHash(), failed));
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

    public static void downloadAssetsObject(AssetObject object, File objectsDir, boolean overwrite) throws FileDownloadException {
        File dir = new File(objectsDir, object.getHash().substring(0, 2));
        if(!dir.isDirectory() && !dir.mkdirs()) {
            throw new FileDownloadException("Unable to create assets object directory, id=" + object.getHash());
        }
        File objectFile = new File(dir, object.getHash());
        try {
            URL url = new URL(Sources.getAssetsBaseUrl() + object.getHash().substring(0, 2) + "/" + object.getHash());
            if(!objectFile.exists() || overwrite) {
                try {
                    FileUtils.downloadFile(url, objectFile);
                } catch (FileDownloadException e) {
                    throw new FileDownloadException("Unable to download assets object, id=" + object.getHash(), e);
                }
            }
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to parse asset object url, id=" + object.getHash(), e);
        }
    }
}
