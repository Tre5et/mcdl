package net.treset.mcdl.forge;

public class ForgeInstallDataPath {
    private String client;
    private String server;

    public ForgeInstallDataPath(String name, String client, String server) {
        this.client = client;
        this.server = server;
    }

    public String getClient() {
        return client;
    }

    public String getResolvedClient() {
        return decodeLibraryPath(client);
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getServer() {
        return server;
    }

    public String getResolvedServer() {
        return decodeLibraryPath(server);
    }

    public void setServer(String server) {
        this.server = server;
    }

    public static String decodeLibraryPath(String path) {
        if(path.startsWith("[") && path.endsWith("]")) {
            //Library location
            StringBuilder pathBuilder = new StringBuilder();
            StringBuilder fileBuilder = new StringBuilder();

            int inFilePart = 0;
            boolean inExtensionPart = false;
            for(int i = 1; i < path.length() - 1; i++) {
                char c = path.charAt(i);
                if(c == '.' && inFilePart == 0 && !inExtensionPart) {
                    pathBuilder.append('/');
                } else if(c == ':') {
                    if(inFilePart > 0) {
                        fileBuilder.append('-');
                    }
                    inFilePart++;
                    if(inFilePart <= 2) {
                        pathBuilder.append('/');
                    }
                } else if(c == '@') {
                    fileBuilder.append('.');
                    inExtensionPart = true;
                } else if(!inExtensionPart) {
                    if(inFilePart <= 2) {
                        pathBuilder.append(c);
                    }
                    if(inFilePart > 0) {
                        fileBuilder.append(c);
                    }
                } else {
                    fileBuilder.append(c);
                }
            }
            if(!inExtensionPart) {
                fileBuilder.append(".jar");
            }

            pathBuilder.append('/').append(fileBuilder);
            return pathBuilder.toString();
        }
        return path;
    }
}
