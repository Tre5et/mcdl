package dev.treset.mcdl.servermanagement;

import java.util.logging.Logger;

public class ServerManagementDL {
    public static final Logger LOGGER = Logger.getLogger("ServerManagementDL");

    /**
     * Creates a management handler representing a connection to a minecraft server.
     * @param host The hostname of the management server.
     * @param port The port of the management server.
     * @param ssl Whether SSL is enabled.
     * @param secret The secret of the management server.
     * @return A management handler.
     */
    public static ManagementHandler createHandler(String host, int port, boolean ssl, String secret) {
        return new ManagementHandler(host, port, ssl, secret);
    }

    /**
     * Creates a management handler with SSL disabled representing a connection to a minecraft server.
     * @param host The hostname of the management server.
     * @param port The port of the management server.
     * @param secret The secret of the management server.
     * @return A management handler.
     */
    public static ManagementHandler createHandler(String host, int port, String secret) {
        return createHandler(host, port, false, secret);
    }
}
