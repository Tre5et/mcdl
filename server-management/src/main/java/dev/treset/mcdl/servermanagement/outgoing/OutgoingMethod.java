package dev.treset.mcdl.servermanagement.outgoing;

import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.servermanagement.ManagementHandler;
import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;
import dev.treset.mcdl.servermanagement.incoming.IncomingReceiver;
import dev.treset.mcdl.servermanagement.data.RpcRequest;
import dev.treset.mcdl.servermanagement.serialization.DataSerializer;

import java.util.function.Consumer;

public class OutgoingMethod<S,R> {
    private static final long DEFAULT_RESPONSE_TIMEOUT = 10_000;

    final String method;
    final DataSerializer<S> sendingSerializer;
    final DataSerializer<R> receivingSerializer;

    public OutgoingMethod(String method, DataSerializer<S> sendingSerializer, DataSerializer<R> receivingSerializer) {
        this.method = method;
        this.sendingSerializer = sendingSerializer;
        this.receivingSerializer = receivingSerializer;
    }

    /**
     * Sends a message on the method.
     * @param data The data to send as the request parameter.
     * @param resultConsumer A method that is called with the response parameter when a response is received.
     * @param errorConsumer A method that is called if there is an error sending the request, receiving the response of the server returns an error.
     * @param handler The management handler to send the message with.
     * @param timeout The maximum time to wait for a response in milliseconds.
     * @return The ID of the message.
     */
    public int send(S data, Consumer<R> resultConsumer, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler, long timeout) {
        IncomingReceiver.Response<R> receiver = IncomingReceiver.response(
                handler.responseHandler.newId(),
                receivingSerializer,
                resultConsumer,
                errorConsumer
        );
        receiver.register(handler.responseHandler, timeout);

        try {
            handler.sendConstructedMessage(constructRequest(receiver.identification(), data));
        } catch (RpcCommunicationException e) {
            receiver.unregister(handler.responseHandler);
            errorConsumer.accept(e);
        }

        return receiver.identification();
    }

    /**
     * Sends a message on the method.
     * @param data The data to send as the request parameter.
     * @param resultConsumer A method that is called with the response parameter when a response is received.
     * @param errorConsumer A method that is called if there is an error sending the request, receiving the response of the server returns an error.
     * @param handler The management handler to send the message with.
     * @return The ID of the message.
     */
    public int send(S data, Consumer<R> resultConsumer, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler) {
        return send(data, resultConsumer, errorConsumer, handler, DEFAULT_RESPONSE_TIMEOUT);
    }

    /**
     * Sends a message on the method and waits for a response.
     * @param data The data to send as the request parameter.
     * @param handler The management handler to send the message with.
     * @param timeout The maximum time to wait for a response in milliseconds.
     * @return The response parameter of the response.
     * @throws RpcCommunicationException If there is an error sending the request, receiving the response of the server returns an error.
     */
    public R sendBlocking(S data, ManagementHandler handler, long timeout) throws RpcCommunicationException {
        IncomingReceiver.Response<R> receiver = IncomingReceiver.response(
                handler.responseHandler.newId(),
                receivingSerializer
        );
        receiver.register(handler.responseHandler, timeout);

        return receiver.waitForNext(
                () -> handler.sendConstructedMessage(constructRequest(receiver.identification(), data)),
                timeout
        );
    }

    /**
     * Sends a message on the method and waits for a response.
     * @param data The data to send as the request parameter.
     * @param handler The management handler to send the message with.
     * @return The response parameter of the response.
     * @throws RpcCommunicationException If there is an error sending the request, receiving the response of the server returns an error.
     */
    public R sendBlocking(S data, ManagementHandler handler) throws RpcCommunicationException {
        return sendBlocking(data, handler, DEFAULT_RESPONSE_TIMEOUT);
    }

    private String constructRequest(int id, S data) {
        JsonArray array = new JsonArray();
        if(data != null) {
            array.add(sendingSerializer.serialize(data));
        }

        return new RpcRequest(
                id,
                method,
                array
        ).serialize();
    }

    public static class Parameterless<R> extends OutgoingMethod<Void, R> {
        public Parameterless(String method, DataSerializer<R> receivingSerializer) {
            super(method, DataSerializer.VOID, receivingSerializer);
        }

        /**
         * Sends a message on the method.
         * @param resultConsumer A method that is called with the response parameter when a response is received.
         * @param errorConsumer A method that is called if there is an error sending the request, receiving the response of the server returns an error.
         * @param handler The management handler to send the message with.
         * @param timeout The maximum time to wait for a response in milliseconds.
         * @return The ID of the message.
         */
        public int send(Consumer<R> resultConsumer, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler, long timeout) {
            return send(null, resultConsumer, errorConsumer, handler, timeout);
        }

        /**
         * Sends a message on the method.
         * @param resultConsumer A method that is called with the response parameter when a response is received.
         * @param errorConsumer A method that is called if there is an error sending the request, receiving the response of the server returns an error.
         * @param handler The management handler to send the message with.
         * @return The ID of the message.
         */
        public int send(Consumer<R> resultConsumer, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler) {
            return send(null, resultConsumer, errorConsumer, handler);
        }

        /**
         * Sends a message on the method and waits for a response.
         * @param handler The management handler to send the message with.
         * @param timeout The maximum time to wait for a response in milliseconds.
         * @return The response parameter of the response.
         * @throws RpcCommunicationException If there is an error sending the request, receiving the response of the server returns an error.
         */
        public R sendBlocking(ManagementHandler handler, long timeout) throws RpcCommunicationException {
            return sendBlocking(null, handler, timeout);
        }

        /**
         * Sends a message on the method and waits for a response.
         * @param handler The management handler to send the message with.
         * @return The response parameter of the response.
         * @throws RpcCommunicationException If there is an error sending the request, receiving the response of the server returns an error.
         */
        public R sendBlocking(ManagementHandler handler) throws RpcCommunicationException {
            return sendBlocking(null, handler);
        }

        /**
         * Adds a request parameter to this method.
         * @param sendingSerializer The serializer to serialize the parameter with.
         * @return The request method.
         * @param <S> The type of the parameter.
         */
        public <S> OutgoingMethod<S,R> withParameter(DataSerializer<S> sendingSerializer) {
            return new OutgoingMethod<>(this.method, sendingSerializer, this.receivingSerializer);
        }

        /**
         * Adds a request parameter to this method.
         * @param sendType A TypeToken which is used to serialize the parameter.
         * @return The request method.
         * @param <S> The type of the parameter.
         */
        public <S> OutgoingMethod<S,R> withParameter(TypeToken<S> sendType) {
            return withParameter(DataSerializer.forType(sendType));
        }
    }

    public static class Responseless<S> extends OutgoingMethod<S, Void> {
        public Responseless(String method, DataSerializer<S> sendingSerializer) {
            super(method, sendingSerializer, DataSerializer.VOID);
        }

        /**
         * Sends a message on the method.
         * @param data The data to send as the request parameter.
         * @param errorConsumer A method that is called if there is an error sending the request, receiving the response of the server returns an error.
         * @param handler The management handler to send the message with.
         * @param timeout The maximum time to wait for a response in milliseconds.
         * @return The ID of the message.
         */
        public int send(S data, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler, long timeout) {
            return send(data, r -> {}, errorConsumer, handler, timeout);
        }

        /**
         * Sends a message on the method.
         * @param data The data to send as the request parameter.
         * @param errorConsumer A method that is called if there is an error sending the request, receiving the response of the server returns an error.
         * @param handler The management handler to send the message with.
         * @return The ID of the message.
         */
        public int send(S data, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler) {
            return send(data, r -> {}, errorConsumer, handler);
        }

        /**
         * Adds a response parameter to this method.
         * @param receivingSerializer The serializer to deserialize the parameter with.
         * @return The request method.
         * @param <R> The type of the parameter.
         */
        public <R> OutgoingMethod<S,R> withResponse(DataSerializer<R> receivingSerializer) {
            return new OutgoingMethod<>(this.method, this.sendingSerializer, receivingSerializer);
        }

        /**
         * Adds a response parameter to this method.
         * @param receiveType A TypeToken which is used to deserialize the parameter.
         * @return The request method.
         * @param <R> The type of the parameter.
         */
        public <R> OutgoingMethod<S,R> withResponse(TypeToken<R> receiveType) {
            return withResponse(DataSerializer.forType(receiveType));
        }
    }

    public static class ParameterlessResponseless extends Parameterless<Void> {
        public ParameterlessResponseless(String method) {
            super(method, DataSerializer.VOID);
        }

        /**
         * Sends a message on the method.
         * @param errorConsumer A method that is called if there is an error sending the request, receiving the response of the server returns an error.
         * @param handler The management handler to send the message with.
         * @param timeout The maximum time to wait for a response in milliseconds.
         * @return The ID of the message.
         */
        public int send(Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler, long timeout) {
            return send(null, r -> {}, errorConsumer, handler, timeout);
        }

        /**
         * Sends a message on the method.
         * @param errorConsumer A method that is called if there is an error sending the request, receiving the response of the server returns an error.
         * @param handler The management handler to send the message with.
         * @return The ID of the message.
         */
        public int send(Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler) {
            return send(null, r -> {}, errorConsumer, handler);
        }

        /**
         * Adds a request parameter to this method.
         * @param sendingSerializer The serializer to serialize the parameter with.
         * @return The request method.
         * @param <S> The type of the parameter.
         */
        public <S> Responseless<S> withParameter(DataSerializer<S> sendingSerializer) {
            return new Responseless<>(this.method, sendingSerializer);
        }

        /**
         * Adds a request parameter to this method.
         * @param sendType A TypeToken which is used to serialize the parameter.
         * @return The request method.
         * @param <S> The type of the parameter.
         */
        public final <S> Responseless<S> withParameter(TypeToken<S> sendType) {
            return withParameter(DataSerializer.forType(sendType));
        }

        /**
         * Adds a response parameter to this method.
         * @param receivingSerializer The serializer to deserialize the parameter with.
         * @return The request method.
         * @param <R> The type of the parameter.
         */
        public <R> Parameterless<R> withResponse(DataSerializer<R> receivingSerializer) {
            return new Parameterless<>(this.method, receivingSerializer);
        }

        /**
         * Adds a response parameter to this method.
         * @param receiveType A TypeToken which is used to deserialize the parameter.
         * @return The request method.
         * @param <R> The type of the parameter.
         */
        public final <R> Parameterless<R> withResponse(TypeToken<R> receiveType) {
            return withResponse(DataSerializer.forType(receiveType));
        }

        /**
         * Adds a request parameter and a response parameter to this method.
         * @param sendingSerializer A TypeToken which is used to serialize the request parameter.
         * @param receivingSerializer A TypeToken which is used to deserialize the response parameter.
         * @return The request method.
         * @param <R> The type of the parameter.
         */
        public <S,R> OutgoingMethod<S,R> withParameterAndResponse(DataSerializer<S> sendingSerializer, DataSerializer<R> receivingSerializer) {
            return new OutgoingMethod<>(method, sendingSerializer, receivingSerializer);
        }

        /**
         * Adds a request parameter and a response parameter to this method.
         * @param sendType A TypeToken which is used to serialize the request parameter.
         * @param receiveType A TypeToken which is used to deserialize the response parameter.
         * @return The request method.
         * @param <R> The type of the parameter.
         */
        public final <S,R> OutgoingMethod<S,R> withParameterAndResponse(TypeToken<S> sendType, TypeToken<R> receiveType) {
            return withParameterAndResponse(DataSerializer.forType(sendType), DataSerializer.forType(receiveType));
        }
    }

    /**
     * Constructs a new request method.
     * @param method The method to construct.
     * @return The request method.
     */
    public static ParameterlessResponseless of(String method) {
        return new ParameterlessResponseless(method);
    }
}
