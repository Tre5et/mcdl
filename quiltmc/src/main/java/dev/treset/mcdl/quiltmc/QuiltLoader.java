package dev.treset.mcdl.quiltmc;

public class QuiltLoader {
    private String separator;
    private int build;
    private String maven;
    private String version;

    public QuiltLoader(String separator, int build, String maven, String version) {
        this.separator = separator;
        this.build = build;
        this.maven = maven;
        this.version = version;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
