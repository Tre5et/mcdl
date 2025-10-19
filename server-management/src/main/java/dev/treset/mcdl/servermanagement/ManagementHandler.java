package dev.treset.mcdl.servermanagement;

import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.servermanagement.data.TriConsumer;
import dev.treset.mcdl.servermanagement.notification.NotificationHandler;
import dev.treset.mcdl.servermanagement.notification.ParameterlessNotificationHandler;
import dev.treset.mcdl.servermanagement.notification.ParametrizedNotificationHandler;
import dev.treset.mcdl.servermanagement.request.RpcResponse;
import org.java_websocket.handshake.ServerHandshake;

import java.io.IOException;
import java.net.URI;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ManagementHandler {
    private final URI uri;
    private final String secret;

    private Consumer<ServerHandshake> openHandler = s -> {};
    private TriConsumer<Integer,String,Boolean> closeHandler = (c, r, m) -> {};
    private Consumer<Exception> errorHandler = e -> {};

    private ManagementClient client = null;
    private final MessageHandler messageHandler = new MessageHandler(this::sendConstructedMessage);

    public ManagementHandler(String host, int port, boolean ssl, String secret) {
        this.uri = URI.create((ssl ? "wss" : "ws") + "://" + host + ":" + port);
        this.secret = secret;
    }

    /**
     * Sets an action that is executed after the connection was opened.
     * @param openHandler The function that is executed.
     */
    public void onOpen(Consumer<ServerHandshake> openHandler) {
        this.openHandler = openHandler;
        if(client != null) {
            client.setOpenHandler(openHandler);
        }
    }

    /**
     * Sets an action that is executed after the connection was closed.
     * @param closeHandler The function that is executed.
     */
    public void onClose(TriConsumer<Integer,String,Boolean> closeHandler) {
        this.closeHandler = closeHandler;
        if(client != null) {
            client.setCloseHandler(closeHandler);
        }
    }

    /**
     * Sets an action that is executed when a connection error occurred.
     * @param errorHandler The function that is executed.
     */
    public void onError(Consumer<Exception> errorHandler) {
        this.errorHandler = errorHandler;
        if(client != null) {
            client.setErrorHandler(errorHandler);
        }
    }

    /**
     * Sets an action that is executed when a unexpected event occurs. E.g. unexpected message type or content.
     * @param warningHandler The function that is executed.
     */
    public void onWarning(BiConsumer<String, Exception> warningHandler) {
        messageHandler.setWarningHandler(warningHandler);
    }

    /**
     * Establishes the connection to the management server.
     * @param reconnect Whether an open connection should be closed and reopened.
     * @throws IOException If an error occurs while connecting.
     */
    public void connect(boolean reconnect) throws IOException {
        if(isConnected()) {
            if(reconnect) {
                disconnect();
            } else {
                throw new IOException("Connection is already open");
            }
        }
        this.client = new ManagementClient(
                uri,
                secret,
                this::handleMessage,
                openHandler,
                closeHandler,
                errorHandler
        );
    }

    /**
     * Establishes the connection to the management server.
     * @throws IOException If an error occurs while connecting.
     */
    public void connect() throws IOException {
        connect(false);
    }

    /**
     * Establishes the connection to the management server and closes the previous connection if required.
     * @throws IOException If an error occurs while connecting.
     */
    public void reconnect() throws IOException {
        connect(true);
    }

    /**
     * Closes the connection to the management server.
     * @throws IOException If an error occurs while closing the connection.
     */
    public void disconnect() throws IOException {
        try {
            client.closeBlocking();
        } catch (InterruptedException e) {
            throw new IOException("Close operation interrupted", e);
        }
    }

    /**
     * Whether a connection to the management server is open.
     * @return {@code true} if the connection is open, otherwise {@code false}.
     */
    public boolean isConnected() {
        return client != null && client.isOpen();
    }

    /**
     * Adds a notification handler for a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param handler The handler called with the notification content.
     */
    public void addNotificationHandler(String method, NotificationHandler handler) {
        messageHandler.addNotificationHandler(method, handler);
    }

    /**
     * Adds a notification handler for a specific RPC method without notification parameters.
     * @param method The RPC method to listen for notifications on.
     * @param handler The function called when the notification is received.
     */
    public void addNotificationHandler(String method, Runnable handler) {
        addNotificationHandler(method, new ParameterlessNotificationHandler(method, handler));
    }

    /**
     * Adds a notification handler for a specific RPC method with a notification parameter.
     * @param method The RPC method to listen for notifications on.
     * @param token A type token representing the type of the notification parameter.
     * @param handler The method called with the parameter data when a notification is received.
     * @param <T> The type of the notification parameter.
     */
    public <T> void addNotificationHandler(String method, TypeToken<T> token, Consumer<T> handler) {
        addNotificationHandler(method, new ParametrizedNotificationHandler<>(method, token, handler));
    }

    /**
     * Adds a notification handler for a specific RPC method with a notification parameter.
     * @param method The RPC method to listen for notifications on.
     * @param parameterType A type class of the notification parameter.
     * @param handler The method called with the parameter data when a notification is received.
     * @param <T> The type of the notification parameter.
     */
    public <T> void addNotificationHandler(String method, Class<T> parameterType, Consumer<T> handler) {
        addNotificationHandler(method, TypeToken.get(parameterType), handler);
    }

    /**
     * Sends an RPC request to the management server.
     * @param method The RPC request method to send.
     * @param responseCallback A method that is called when the response to the request is received.
     * @param params The parameters to send with the request.
     * @return The id of the request.
     * @throws IOException If there is an error sending the request.
     */
    public int send(String method, Consumer<RpcResponse> responseCallback, Object... params) throws IOException {
        return messageHandler.send(method, responseCallback, params);
    }

    /**
     * Sends an RPC request to the management server.
     * @param method The RPC request method to send.
     * @param params The parameters to send with the request.
     * @return The id of the request.
     * @throws IOException If there is an error sending the request.
     */
    public int send(String method, Object... params) throws IOException {
        return send(method, r -> {}, params);
    }

    /**
     * Sends an RPC request and waits for the response.
     * @param timeoutMs The maximum time in milliseconds to wait for a response.
     * @param method The RPC request method to send.
     * @param params The parameters to send with the request.
     * @return The response to the request.
     * @throws IOException If there is an error sending the request or receiving the response.
     */
    public RpcResponse request(long timeoutMs, String method, Object... params) throws IOException {
        return messageHandler.request(timeoutMs, method, params);
    }

    /**
     * Sends an RPC request and waits at most 10 seconds for the response.
     * @param method The RPC request method to send.
     * @param params The parameters to send with the request.
     * @return The response to the request.
     * @throws IOException If there is an error sending the request or receiving the response.
     */
    public RpcResponse request(String method, Object... params) throws IOException {
        return request(10_000, method, params);
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param type A type token representing the type of the notification parameter.
     * @param timeoutMs The maximum time in milliseconds to wait for the notification.
     * @param actionBefore A function to run immediately before waiting for the notification, ensuring no delay.
     * @return The content of the notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws IOException If there is an error waiting for the notification or the timeout expired.
     */
    public <T> T awaitNotification(String method, TypeToken<T> type, long timeoutMs, Runnable actionBefore) throws IOException {
        return messageHandler.awaitNotification(actionBefore, method, type, timeoutMs);
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param type A type token representing the type of the notification parameter.
     * @param timeoutMs The maximum time in milliseconds to wait for the notification.
     * @return The content of the notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws IOException If there is an error waiting for the notification or the timeout expired.
     */
    public <T> T awaitNotification(String method, TypeToken<T> type, long timeoutMs) throws IOException {
        return awaitNotification(method, type, timeoutMs, () -> {});
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param type A type token representing the type of the notification parameter.
     * @param actionBefore A function to run immediately before waiting for the notification, ensuring no delay.
     * @return The content of the notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws IOException If there is an error waiting for the notification or 10 seconds expired.
     */
    public <T> T awaitNotification(String method, TypeToken<T> type, Runnable actionBefore) throws IOException {
        return awaitNotification(method, type, 10_000, actionBefore);
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param type A type token representing the type of the notification parameter.
     * @return The content of the notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws IOException If there is an error waiting for the notification or 10 seconds expired.
     */
    public <T> T awaitNotification(String method, TypeToken<T> type) throws IOException {
        return awaitNotification(method, type, 10_000, () -> {});
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param type The class of the notification parameter.
     * @param timeoutMs The maximum time in milliseconds to wait for the notification.
     * @param actionBefore A function to run immediately before waiting for the notification, ensuring no delay.
     * @return The content of the notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws IOException If there is an error waiting for the notification or the timeout expired.
     */
    public <T> T awaitNotification(String method, Class<T> type, long timeoutMs, Runnable actionBefore) throws IOException {
        return awaitNotification(method, TypeToken.get(type), timeoutMs, actionBefore);
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param type The class of the notification parameter.
     * @param timeoutMs The maximum time in milliseconds to wait for the notification.
     * @return The content of the notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws IOException If there is an error waiting for the notification or the timeout expired.
     */
    public <T> T awaitNotification(String method, Class<T> type, long timeoutMs) throws IOException {
        return awaitNotification(method, type, timeoutMs, () -> {});
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param type The class of the notification parameter.
     * @param actionBefore A function to run immediately before waiting for the notification, ensuring no delay.
     * @return The content of the notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws IOException If there is an error waiting for the notification or 10 seconds expired.
     */
    public <T> T awaitNotification(String method, Class<T> type, Runnable actionBefore) throws IOException {
        return awaitNotification(method, type, 10_000, actionBefore);
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param type The class of the notification parameter.
     * @return The content of the notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws IOException If there is an error waiting for the notification or 10 seconds expired.
     */
    public <T> T awaitNotification(String method, Class<T> type) throws IOException {
        return awaitNotification(method, type, 10_000, () -> {});
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param timeoutMs The maximum time in milliseconds to wait for the notification.
     * @param actionBefore A function to run immediately before waiting for the notification, ensuring no delay.
     * @throws IOException If there is an error waiting for the notification or the timeout expired.
     */
    public void awaitNotification(String method, long timeoutMs, Runnable actionBefore) throws IOException {
        awaitNotification(method, (TypeToken<Object>) null, timeoutMs, actionBefore);
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param timeoutMs The maximum time in milliseconds to wait for the notification.
     * @throws IOException If there is an error waiting for the notification or the timeout expired.
     */
    public void awaitNotification(String method, long timeoutMs) throws IOException {
        awaitNotification(method, timeoutMs, () -> {});
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @param actionBefore A function to run immediately before waiting for the notification, ensuring no delay.
     * @throws IOException If there is an error waiting for the notification or 10 seconds expired.
     */
    public void awaitNotification(String method, Runnable actionBefore) throws IOException {
        awaitNotification(method, 10_000, actionBefore);
    }

    /**
     * Waits for to receive a notification on a specific RPC method.
     * @param method The RPC method to listen for notifications on.
     * @throws IOException If there is an error waiting for the notification or 10 seconds expired.
     */
    public void awaitNotification(String method) throws IOException {
        awaitNotification(method, 10_000, () -> {});
    }

    private void sendConstructedMessage(String message) throws IOException {
        if(!isConnected()) connect();
        client.send(message);
    }

    private void handleMessage(String message) {
        messageHandler.handleMessage(message);
    }
}
