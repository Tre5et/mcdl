package dev.treset.mcdl.servermanagement.outgoing;

import com.google.gson.JsonArray;
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

    public int send(S data, Consumer<R> resultConsumer, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler, long timeout) {
        IncomingReceiver.Response<R> receiver = new IncomingReceiver.Response<>(
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

    public int send(S data, Consumer<R> resultConsumer, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler) {
        return send(data, resultConsumer, errorConsumer, handler, DEFAULT_RESPONSE_TIMEOUT);
    }

    public R sendBlocking(S data, ManagementHandler handler, long timeout) throws RpcCommunicationException {
        IncomingReceiver.Response<R> receiver = new IncomingReceiver.Response<>(
                handler.responseHandler.newId(),
                receivingSerializer,
                r -> {},
                e -> {}
        );
        receiver.register(handler.responseHandler, timeout);

        return receiver.waitForNext(
                () -> handler.sendConstructedMessage(constructRequest(receiver.identification(), data)),
                timeout
        );
    }

    public R sendBlocking(S data, ManagementHandler handler) throws RpcCommunicationException {
        return sendBlocking(data, handler, DEFAULT_RESPONSE_TIMEOUT);
    }

    public String constructRequest(int id, S data) {
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

        public int send(Consumer<R> resultConsumer, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler, long timeout) {
            return send(null, resultConsumer, errorConsumer, handler, timeout);
        }

        public int send(Consumer<R> resultConsumer, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler) {
            return send(null, resultConsumer, errorConsumer, handler);
        }

        public R sendBlocking(ManagementHandler handler, long timeout) throws RpcCommunicationException {
            return sendBlocking(null, handler, timeout);
        }

        public R sendBlocking(ManagementHandler handler) throws RpcCommunicationException {
            return sendBlocking(null, handler);
        }

        public <S> OutgoingMethod<S,R> withParameter(DataSerializer<S> sendingSerializer) {
            return new OutgoingMethod<>(this.method, sendingSerializer, this.receivingSerializer);
        }
    }

    public static class Responseless<S> extends OutgoingMethod<S, Void> {
        public Responseless(String method, DataSerializer<S> sendingSerializer) {
            super(method, sendingSerializer, DataSerializer.VOID);
        }

        public int send(S data, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler, long timeout) {
            return send(data, r -> {}, errorConsumer, handler, timeout);
        }

        public int send(S data, Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler) {
            return send(data, r -> {}, errorConsumer, handler);
        }

        public <R> OutgoingMethod<S,R> withResponse(DataSerializer<R> receivingSerializer) {
            return new OutgoingMethod<>(this.method, this.sendingSerializer, receivingSerializer);
        }
    }

    public static class ParameterlessResponseless extends Parameterless<Void> {
        public ParameterlessResponseless(String method) {
            super(method, DataSerializer.VOID);
        }

        public int send(Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler, long timeout) {
            return send(null, r -> {}, errorConsumer, handler, timeout);
        }

        public int send(Consumer<RpcCommunicationException> errorConsumer, ManagementHandler handler) {
            return send(null, r -> {}, errorConsumer, handler);
        }

        public <S> Responseless<S> withParameter(DataSerializer<S> sendingSerializer) {
            return new Responseless<>(this.method, sendingSerializer);
        }

        public <R> Parameterless<R> withResponse(DataSerializer<R> receivingSerializer) {
            return new Parameterless<>(this.method, receivingSerializer);
        }
    }

    public static ParameterlessResponseless of(String method) {
        return new ParameterlessResponseless(method);
    }
}
