package net.treset.mc_version_loader.fabric;

import com.google.gson.JsonObject;
import net.treset.mc_version_loader.json.JsonUtils;

import java.util.List;

public class FabricLauncherMeta {
    private List<FabricLibrary> librariesClient;
    private List<FabricLibrary> librariesCommon;
    private List<FabricLibrary> librariesServer;
    private String mainClassClient;
    private String mainClassServer;
    private int version;

    public FabricLauncherMeta(List<FabricLibrary> librariesClient, List<FabricLibrary> librariesCommon, List<FabricLibrary> librariesServer, String mainClassClient, String mainClassServer, int version) {
        this.librariesClient = librariesClient;
        this.librariesCommon = librariesCommon;
        this.librariesServer = librariesServer;
        this.mainClassClient = mainClassClient;
        this.mainClassServer = mainClassServer;
        this.version = version;
    }

    public static FabricLauncherMeta fromJson(JsonObject launcherMetaObj) {
        JsonObject librariesObj = JsonUtils.getAsJsonObject(launcherMetaObj, "libraries");
        JsonObject mainClassObj = JsonUtils.getAsJsonObject(launcherMetaObj, "mainClass");

        return new FabricLauncherMeta(
                FabricLibrary.parseFabricLibraries(JsonUtils.getAsJsonArray(librariesObj, "client")),
                FabricLibrary.parseFabricLibraries(JsonUtils.getAsJsonArray(librariesObj, "common")),
                FabricLibrary.parseFabricLibraries(JsonUtils.getAsJsonArray(librariesObj, "server")),
                JsonUtils.getAsString(mainClassObj, "client"),
                JsonUtils.getAsString(mainClassObj, "server"),
                JsonUtils.getAsInt(launcherMetaObj, "version")
        );
    }

    public List<FabricLibrary> getLibrariesClient() {
        return librariesClient;
    }

    public void setLibrariesClient(List<FabricLibrary> librariesClient) {
        this.librariesClient = librariesClient;
    }

    public List<FabricLibrary> getLibrariesCommon() {
        return librariesCommon;
    }

    public void setLibrariesCommon(List<FabricLibrary> librariesCommon) {
        this.librariesCommon = librariesCommon;
    }

    public List<FabricLibrary> getLibrariesServer() {
        return librariesServer;
    }

    public void setLibrariesServer(List<FabricLibrary> librariesServer) {
        this.librariesServer = librariesServer;
    }

    public String getMainClassClient() {
        return mainClassClient;
    }

    public void setMainClassClient(String mainClassClient) {
        this.mainClassClient = mainClassClient;
    }

    public String getMainClassServer() {
        return mainClassServer;
    }

    public void setMainClassServer(String mainClassServer) {
        this.mainClassServer = mainClassServer;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
