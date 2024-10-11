package dev.treset.mcdl.assets;

import com.google.gson.JsonObject;
import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.json.JsonUtils;
import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.util.FileUtil;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class AssetObject {
    private String hash;
    private int size;

    public AssetObject(String hash, int size) {
        this.hash = hash;
        this.size = size;
    }

    public static AssetObject fromJson(JsonObject jsonObject) throws SerializationException {
        return new AssetObject(
                JsonUtils.getAsString(jsonObject, "hash"),
                JsonUtils.getAsInt(jsonObject, "size")
        );
    }

    /**
     * Downloads the asset object to the specified directory.
     * @param objectsDir The asset directory
     * @param overwrite Whether to overwrite the file if it already exists
     * @throws FileDownloadException If there is an error downloading or writing the file
     */
    public void download(File objectsDir, boolean overwrite) throws FileDownloadException {
        if(getHash() == null || getHash().isBlank()) {
            throw new FileDownloadException("Unable to download assert object, hash is empty");
        }
        File dir = new File(objectsDir, getHash().substring(0, 2));
        if(!dir.isDirectory() && !dir.mkdirs()) {
            throw new FileDownloadException("Unable to create assets object directory, id=" + getHash());
        }
        File objectFile = new File(dir, getHash());
        try {
            URL url = new URL(AssetsDL.getAssetUrl(getHash()));
            if(!objectFile.exists() || overwrite) {
                try {
                    FileUtil.downloadFile(url, objectFile);
                } catch (FileDownloadException e) {
                    throw new FileDownloadException("Unable to download assets object, id=" + getHash(), e);
                }
            }
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to parse asset object url, id=" + getHash(), e);
        }
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
