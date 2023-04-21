package net.treset.mc_version_loader.launcher;

public class LauncherModDownload {
    String date;
    String provider;
    String url;
    String version;

    public LauncherModDownload(String date, String provider, String url, String version) {
        this.date = date;
        this.provider = provider;
        this.url = url;
        this.version = version;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
