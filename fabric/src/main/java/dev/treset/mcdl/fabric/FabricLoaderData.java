package dev.treset.mcdl.fabric;

import dev.treset.mcdl.exception.FileDownloadException;
import dev.treset.mcdl.util.FileUtil;
import dev.treset.mcdl.util.MavenPom;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class FabricLoaderData {
    private int build;
    private String maven;
    private String separator;
    private boolean stable;
    private String version;

    public FabricLoaderData(int build, String maven, String separator, boolean stable, String version) {
        this.build = build;
        this.maven = maven;
        this.separator = separator;
        this.stable = stable;
        this.version = version;
    }

    /**
     * Downloads the fabric client jar to a specified directory.
     * @param outFile The file to download the fabric client as
     * @throws FileDownloadException If there is an error downloading or writing the loader
     */
    public void downloadClient(File outFile) throws FileDownloadException {
        if(getMaven() == null || getMaven().isBlank() || outFile == null || !outFile.getParentFile().isDirectory()) {
            throw new FileDownloadException("Unmet requirements for fabric download, version=" + getVersion());
        }

        MavenPom mavenPom;
        try {
            mavenPom = FileUtil.parseMavenUrl(FabricDL.getFabricMavenUrl(), getMaven());
        } catch (MalformedURLException | FileDownloadException e) {
            throw new FileDownloadException("Unable to parse fabric version maven file Url", e);
        }

        String mavenUrl = mavenPom.getMavenUrl();
        if(mavenUrl == null || mavenUrl.isBlank()) {
            throw new FileDownloadException("Unable to parse fabric version maven file");
        }

        URL downloadUrl;
        try {
            downloadUrl = new URL(FabricDL.getFabricMavenUrl() + mavenUrl);
        } catch (MalformedURLException e) {
            throw new FileDownloadException("Unable to parse fabric download Url", e);
        }

        FileUtil.downloadFile(downloadUrl, outFile);
    }

    public int getBuild() {
        return build;
    }

    public void setBuild(int build) {
        this.build = build;
    }

    public String getMaven() {
        return maven;
    }

    public void setMaven(String maven) {
        this.maven = maven;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public boolean isStable() {
        return stable;
    }

    public void setStable(boolean stable) {
        this.stable = stable;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
