package net.treset.mcdl.mods.curseforge;

public class CurseforgeModLinks {
    private String issuesUrl;
    private String sourceUrl;
    private String websiteUrl;
    private String wikiUrl;

    public CurseforgeModLinks(String issuesUrl, String sourceUrl, String websiteUrl, String wikiUrl) {
        this.issuesUrl = issuesUrl;
        this.sourceUrl = sourceUrl;
        this.websiteUrl = websiteUrl;
        this.wikiUrl = wikiUrl;
    }

    public String getIssuesUrl() {
        return issuesUrl;
    }

    public void setIssuesUrl(String issuesUrl) {
        this.issuesUrl = issuesUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getWikiUrl() {
        return wikiUrl;
    }

    public void setWikiUrl(String wikiUrl) {
        this.wikiUrl = wikiUrl;
    }
}
