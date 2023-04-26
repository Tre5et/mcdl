package net.treset.mc_version_loader.mods.modrinth;

public class ModrinthHashes {
    private String sha1;
    private String sha512;

    public ModrinthHashes(String sha1, String sha512) {
        this.sha1 = sha1;
        this.sha512 = sha512;
    }

    public String getSha1() {
        return sha1;
    }

    public void setSha1(String sha1) {
        this.sha1 = sha1;
    }

    public String getSha512() {
        return sha512;
    }

    public void setSha512(String sha512) {
        this.sha512 = sha512;
    }
}
