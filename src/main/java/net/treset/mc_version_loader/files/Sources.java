package net.treset.mc_version_loader.files;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sources {
    private static final Logger LOGGER = Logger.getLogger(Sources.class.toString());

    private static final String VERSION_MANIFEST_URL = "https://piston-meta.mojang.com/mc/game/version_manifest_v2.json";
    private static final String JAVA_RUNTIME_URL = "https://launchermeta.mojang.com/v1/products/java-runtime/2ec0cc96c44e5a76b9c8b7c39df7210883d12871/all.json";
    private static final String FABRIC_LOADER_MANIFEST_URL = "https://meta.fabricmc.net/v2/versions/loader/%s";
    private static final String FABRIC_LOADER_VERSION_URL = "https://meta.fabricmc.net/v2/versions/loader/%s/%s";

    public static String getVersionManifestJson() {
        return getFileFromUrl(VERSION_MANIFEST_URL);
    }

    public static String getJavaRuntimeJson() {
        return getFileFromUrl(JAVA_RUNTIME_URL);
    }

    public static String getFabricForMinecraftVersion(String version) {
        return getFileFromHttpGet(String.format(FABRIC_LOADER_MANIFEST_URL, version));
    }

    public static String getFabricVersion(String mcVersion, String fabricVersion) {
        return getFileFromHttpGet(String.format(FABRIC_LOADER_VERSION_URL, mcVersion, fabricVersion));
    }

    public static String getFileFromHttpGet(String getUrl) {
        StringBuilder result = new StringBuilder();
        try {
            String urlEncodedString = URLEncoder.encode(getUrl, StandardCharsets.UTF_8);
            URL url = new URL(getUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        } catch(Exception e) {
            LOGGER.log(Level.WARNING, "Unable to perform get request", e);
        }
        return result.toString();
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
