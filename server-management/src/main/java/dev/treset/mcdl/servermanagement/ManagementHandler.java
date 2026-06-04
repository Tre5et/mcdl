package dev.treset.mcdl.servermanagement;

import dev.treset.mcdl.json.SerializationException;
import dev.treset.mcdl.servermanagement.data.RpcMessage;
import dev.treset.mcdl.servermanagement.data.TriConsumer;
import dev.treset.mcdl.servermanagement.exception.RpcConnectionException;
import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;
import dev.treset.mcdl.servermanagement.incoming.IncomingHandler;
import dev.treset.mcdl.servermanagement.incoming.IncomingMethod;
import dev.treset.mcdl.servermanagement.incoming.IncomingReceiver;
import dev.treset.mcdl.servermanagement.outgoing.OutgoingMethod;
import dev.treset.mcdl.servermanagement.serialization.DataSerializer;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class ManagementHandler {
    private final URI uri;
    private final String secret;

    private Consumer<ServerHandshake> openHandler = s -> {};
    private TriConsumer<Integer,String,Boolean> closeHandler = (c, r, m) -> {};
    private BiConsumer<String, Exception> warningHandler = (s,e) -> {};
    private Consumer<Exception> errorHandler = e -> {};

    private ManagementClient client = null;
    public final IncomingHandler.Request requestHandler = new IncomingHandler.Request();
    public final IncomingHandler.Notification notificationHandler = new IncomingHandler.Notification();
    public final IncomingHandler.Response responseHandler = new IncomingHandler.Response();

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
        this.warningHandler = warningHandler;
    }

    /**
     * Establishes the connection to the management server.
     * @param reconnect Whether an open connection should be closed and reopened.
     * @throws RpcConnectionException If an error occurs while connecting.
     */
    public void connect(boolean reconnect) throws RpcConnectionException {
        if(isConnected()) {
            if(reconnect) {
                disconnect();
            } else {
                throw new RpcConnectionException("Connection is already open");
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
     * @throws RpcConnectionException If an error occurs while connecting.
     */
    public void connect() throws RpcConnectionException {
        connect(false);
    }

    /**
     * Establishes the connection to the management server and closes the previous connection if required.
     * @throws RpcConnectionException If an error occurs while connecting.
     */
    public void reconnect() throws RpcConnectionException {
        connect(true);
    }

    /**
     * Closes the connection to the management server.
     * @throws RpcConnectionException If an error occurs while closing the connection.
     */
    public void disconnect() throws RpcConnectionException {
        try {
            client.closeBlocking();
        } catch (InterruptedException e) {
            throw new RpcConnectionException("Close operation interrupted", e);
        }
    }

    /**
     * Force closes the connection and doesn't wait for success.
     */
    public void forceDisconnect() {
        client.closeConnection(-1, "Force close by client");
    }

    /**
     * Whether a connection to the management server is open.
     * @return {@code true} if the connection is open, otherwise {@code false}.
     */
    public boolean isConnected() {
        return client != null && client.isOpen();
    }

    /**
     * Registers a notification receiver for a specific RPC method.
     * @param receiver The receiver to register.
     */
    public void addNotificationMethod(IncomingReceiver.Notification<?> receiver) {
        receiver.register(notificationHandler);
    }


    /**
     * Registers a notification receiver for a specific RPC method.
     * @param method The notification method to register.
     * @param serializer The serializer to deserialize the notification parameter with.
     * @param resultConsumer A method that is called with the notification parameter when it is received.
     * @param <T> The type of the notification parameter.
     */
    public <T> void addNotificationMethod(String method, DataSerializer<T> serializer, Consumer<T> resultConsumer) {
        IncomingReceiver.Notification<T> receiver = IncomingReceiver.notification(
                method,
                serializer,
                resultConsumer
        );
        receiver.register(notificationHandler);
    }

    /**
     * Registers a parameterless notification receiver for a specific RPC method.
     * @param method The notification method to register.
     * @param onReceived A method that is called when a notification is received.
     */
    public void addNotificationMethod(String method, Runnable onReceived) {
        IncomingReceiver.ParameterlessNotification receiver = IncomingReceiver.notification(
                method,
                onReceived
        );
        receiver.register(notificationHandler);
    }

    public void addIncomingMethod(IncomingMethod<?,?> method) {
        IncomingReceiver.Method<?,?> receiver = method.constructReceiver(this);
        receiver.register(requestHandler);
    }

    /**
     * Sends an RPC request to the management server.
     * @param method The RPC request method to send.
     * @param data The parameter to send
     * @param responseCallback A method that is called when the response to the request is received.
     * @param errorCallback A method that is called if an error occurs sending the request, receiving the response or the server returns an error.
     * @param timeout The maximum time to wait for a response in milliseconds.
     * @return The id of the request.
     */
    public <S,R> int send(OutgoingMethod<S,R> method, S data, Consumer<R> responseCallback, Consumer<RpcCommunicationException> errorCallback, long timeout) {
        return method.send(data, responseCallback, errorCallback, this, timeout);
    }

    /**
     * Sends an RPC request to the management server.
     * @param method The RPC request method to send.
     * @param data The parameter to send
     * @param responseCallback A method that is called when the response to the request is received.
     * @param errorCallback A method that is called if an error occurs sending the request, receiving the response or the server returns an error.
     * @return The id of the request.
     */
    public <S,R> int send(OutgoingMethod<S,R> method, S data, Consumer<R> responseCallback, Consumer<RpcCommunicationException> errorCallback) {
        return method.send(data, responseCallback, errorCallback, this);
    }

    /**
     * Sends a parameterless RPC request to the management server.
     * @param method The RPC request method to send.
     * @param responseCallback A method that is called when the response to the request is received.
     * @param errorCallback A method that is called if an error occurs sending the request, receiving the response or the server returns an error.
     * @param timeout The maximum time to wait for a response in milliseconds.
     * @return The id of the request.
     */
    public <R> int send(OutgoingMethod.Parameterless<R> method, Consumer<R> responseCallback, Consumer<RpcCommunicationException> errorCallback, long timeout) {
        return method.send(responseCallback, errorCallback, this, timeout);
    }

    /**
     * Sends a parameterless RPC request to the management server.
     * @param method The RPC request method to send.
     * @param responseCallback A method that is called when the response to the request is received.
     * @param errorCallback A method that is called if an error occurs.
     * @return The id of the request.
     */
    public <R> int send(OutgoingMethod.Parameterless<R> method, Consumer<R> responseCallback, Consumer<RpcCommunicationException> errorCallback) {
        return method.send(responseCallback, errorCallback, this);
    }

    /**
     * Sends an RPC request and waits for a response.
     * @param method The method to request.
     * @param data The request parameter to send.
     * @param timeout The maximum time to wait for a response in milliseconds.
     * @return The parameter of the response.
     * @param <S> The type of the request parameter.
     * @param <R> The type of the response parameter.
     * @throws RpcCommunicationException If there is an error sending the request, receiving the response or the server returns an error.
     */
    public <S,R> R request(OutgoingMethod<S,R> method, S data, long timeout) throws RpcCommunicationException {
        return method.sendBlocking(data, this, timeout);
    }

    /**
     * Sends an RPC request and waits for a response.
     * @param method The method to request.
     * @param data The request parameter to send.
     * @return The parameter of the response.
     * @param <S> The type of the request parameter.
     * @param <R> The type of the response parameter.
     * @throws RpcCommunicationException If there is an error sending the request, receiving the response or the server returns an error.
     */
    public <S,R> R request(OutgoingMethod<S,R> method, S data) throws RpcCommunicationException {
        return method.sendBlocking(data, this);
    }

    /**
     * Sends a parameterless RPC request and waits for a response.
     * @param method The method to request.
     * @return The parameter of the response.
     * @param <R> The type of the response parameter.
     * @throws RpcCommunicationException If there is an error sending the request, receiving the response or the server returns an error.
     */
    public <R> R request(OutgoingMethod.Parameterless<R> method) throws RpcCommunicationException {
        return method.sendBlocking(this);
    }

    /** Sends a parameterless RPC request and waits for a response.
     * @param method The method to request.
     * @param timeout The maximum time to wait for a response in milliseconds.
     * @return The parameter of the response.
     * @param <R> The type of the response parameter.
     * @throws RpcCommunicationException If there is an error sending the request, receiving the response or the server returns an error.
     */
    public <R> R request(OutgoingMethod.Parameterless<R> method, long timeout) throws RpcCommunicationException {
        return method.sendBlocking(this, timeout);
    }

    /**
     * Waits for the next notification caught by a receiver. <br><br>
     *
     * Example to wait for saving to complete after triggering it, with a maximum wait time of 10 seconds:
     * <pre>
     * {@code
     *  managementHandler.awaitNotification(
     *      RpcNotifications.Server.saved(),
     *      () -> { if(!managementHandler.request(RpcMethods.Server.SAVE, true)) throw new IOException("Failed to trigger save"); },
     *      10_000L
     *  );
     * }
     * </pre>
     *
     * @param notificationReceiver The receiver to wait for.
     * @param triggerAction A function that may be used to execute any events that trigger the notification to wait for. It is guaranteed, that any notifications received by this receiver during and after the execution of {@code triggerAction} will be returned here.
     * @param timeout The maximum time to wait for a notification in milliseconds.
     * @return The notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws RpcCommunicationException If there is an error receiving the notification.
     */
    public <T> T awaitNotification(IncomingReceiver.Notification<T> notificationReceiver, IncomingReceiver.CheckedAction triggerAction, long timeout) throws RpcCommunicationException {
        notificationReceiver.register(notificationHandler);
        return notificationReceiver.waitForNext(triggerAction, timeout);
    }


    /**
     * Waits for the next notification caught by a receiver. <br><br>
     *
     * Example to wait for saving to complete after triggering it:
     * <pre>
     * {@code
     *  managementHandler.awaitNotification(
     *      RpcNotifications.Server.saved(),
     *      () -> { if(!managementHandler.request(RpcMethods.Server.SAVE, true)) throw new IOException("Failed to trigger save"); }
     *  );
     * }
     * </pre>
     *
     * @param notificationReceiver The receiver to wait for.
     * @param triggerAction A function that may be used to execute any events that trigger the notification to wait for. It is guaranteed, that any notifications received by this receiver during and after the execution of {@code triggerAction} will be returned here.
     * @return The notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws RpcCommunicationException If there is an error receiving the notification.
     */
    public <T> T awaitNotification(IncomingReceiver.Notification<T> notificationReceiver, IncomingReceiver.CheckedAction triggerAction) throws RpcCommunicationException {
        notificationHandler.register(notificationReceiver);
        try {
            T res = notificationReceiver.waitForNext(triggerAction);
            notificationHandler.unregister(notificationReceiver);
            return res;
        } catch (RpcCommunicationException e) {
            notificationHandler.unregister(notificationReceiver);
            throw e;
        }
    }

    /**
     * Waits for the next notification on a method.
     * @param method The notification method to wait for.
     * @param serializer The serializer to deserialize the notification parameter with.
     * @param triggerAction A function that may be used to execute any events that trigger the notification to wait for. It is guaranteed, that any notifications received by this receiver during and after the execution of {@code triggerAction} will be returned here.
     * @param timeout The maximum time to wait for a notification in milliseconds.
     * @return The notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws RpcCommunicationException If there is an error receiving the notification.
     */
    public <T> T awaitNotification(String method, DataSerializer<T> serializer, IncomingReceiver.CheckedAction triggerAction, long timeout) throws RpcCommunicationException {
        return awaitNotification(IncomingReceiver.notification(
                method,
                serializer,
                true
        ), triggerAction, timeout);
    }

    /**
     * Waits for the next notification on a method.
     * @param method The notification method to wait for.
     * @param serializer The serializer to deserialize the notification parameter with.
     * @param triggerAction A function that may be used to execute any events that trigger the notification to wait for. It is guaranteed, that any notifications received by this receiver during and after the execution of {@code triggerAction} will be returned here.
     * @return The notification parameter.
     * @param <T> The type of the notification parameter.
     * @throws RpcCommunicationException If there is an error receiving the notification.
     */
    public <T> T awaitNotification(String method, DataSerializer<T> serializer, IncomingReceiver.CheckedAction triggerAction) throws RpcCommunicationException {
        return awaitNotification(IncomingReceiver.notification(
                method,
                serializer
        ), triggerAction);
    }

    public void sendConstructedMessage(String message) throws RpcCommunicationException {
        if(!isConnected()) {
            try {
                connect();
            } catch (RpcConnectionException e) {
                throw new RpcCommunicationException("Failed to connect", e);
            }
        }
        client.send(message);
    }

    private void handleMessage(String content) {
        try {
            RpcMessage message = RpcMessage.fromJson(content);
            if(message.isRequest()) {
                requestHandler.handle(message.asRequest());
                return;
            }
            if(message.isResponse()) {
                responseHandler.handle(message.asResponse());
                return;
            }
            if(message.isNotification()) {
                notificationHandler.handle(message.asNotification());
                return;
            }
            warningHandler.accept(String.format("Unexpected message format: \"%s\"", content), null);
        } catch (SerializationException e) {
            warningHandler.accept(String.format("Deserialization of message failed: \"%s\"", content), e);
        }
    }
}
