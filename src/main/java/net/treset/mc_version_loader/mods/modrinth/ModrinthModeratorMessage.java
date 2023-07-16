package net.treset.mc_version_loader.mods.modrinth;

public class ModrinthModeratorMessage {
    private String message;
    private String body;

    public ModrinthModeratorMessage(String message, String body) {
        this.message = message;
        this.body = body;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
