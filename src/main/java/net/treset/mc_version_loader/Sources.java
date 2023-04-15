package net.treset.mc_version_loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sources {
    private static final Logger LOGGER = Logger.getLogger(Sources.class.toString());

    private static final String VERSION_MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";

    public static String getVersionManifest() {
        return getFileFromUrl(VERSION_MANIFEST_URL);
    }

    public static String getFileFromUrl(String url) {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(url).openStream()))) {
            StringBuilder stringBuilder = new StringBuilder();

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(inputLine);
                stringBuilder.append(System.lineSeparator());
            }

            bufferedReader.close();
            return stringBuilder.toString().trim();

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Unable to load file from web", e);
        }
        return "";
    }

}
