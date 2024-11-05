package dev.treset.mcdl.forge.pre1_13;

import dev.treset.mcdl.minecraft.MinecraftLibrary;
import dev.treset.mcdl.minecraft.MinecraftRule;
import dev.treset.mcdl.util.FileUtil;

import java.net.MalformedURLException;
import java.util.List;

@Deprecated
public class ForgeLibrary {
    private String comment;
    private String name;
    private String url;
    private List<String> checksums;
    private List<MinecraftRule> rules;
    private String serverreq;
    private String clientreq;

    public ForgeLibrary(String comment, String name, String url, List<String> checksums, List<MinecraftRule> rules, String serverreq, String clientreq) {
        this.comment = comment;
        this.name = name;
        this.url = url;
        this.checksums = checksums;
        this.rules = rules;
        this.serverreq = serverreq;
        this.clientreq = clientreq;
    }

    public MinecraftLibrary toMinecraftLibrary(String mirrorUrl, boolean removeDownloads) {
        String path;
        try {
            path = FileUtil.toMavenPath(name, ".jar");
        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        }

        String downloadUrl = url == null ? mirrorUrl : url;
        MinecraftLibrary.Downloads downloads = new MinecraftLibrary.Downloads(
                new MinecraftLibrary.Downloads.Artifact(
                        path,
                        (checksums != null && !checksums.isEmpty()) ? checksums.get(0): null,
                        -1,
                         removeDownloads ? null : downloadUrl + (downloadUrl.endsWith("/") ? "" : "/") + path
                ),
                null
        );

        return new MinecraftLibrary(
                name,
                downloads,
                null,
                rules,
                null
        );
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getChecksums() {
        return checksums;
    }

    public void setChecksums(List<String> checksums) {
        this.checksums = checksums;
    }

    public List<MinecraftRule> getRules() {
        return rules;
    }

    public void setRules(List<MinecraftRule> rules) {
        this.rules = rules;
    }

    public String getServerreq() {
        return serverreq;
    }

    public void setServerreq(String serverreq) {
        this.serverreq = serverreq;
    }

    public String getClientreq() {
        return clientreq;
    }

    public void setClientreq(String clientreq) {
        this.clientreq = clientreq;
    }
}
