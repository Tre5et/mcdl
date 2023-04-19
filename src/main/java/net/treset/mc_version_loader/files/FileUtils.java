package net.treset.mc_version_loader.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtils {
    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.toString());

    public static boolean downloadFile(URL downloadUrl, File outFile) {
        try(FileOutputStream fileOutputStream = new FileOutputStream(outFile)) {

            ReadableByteChannel readableByteChannel = Channels.newChannel(downloadUrl.openStream());

            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch(IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to download file", e);
            return false;
        }
        return true;
    }
}
