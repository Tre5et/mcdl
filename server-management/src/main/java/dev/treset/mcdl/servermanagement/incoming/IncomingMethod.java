package dev.treset.mcdl.servermanagement.incoming;

import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.servermanagement.ManagementHandler;
import dev.treset.mcdl.servermanagement.exception.RpcCommunicationException;
import dev.treset.mcdl.servermanagement.serialization.DataSerializer;

import java.util.function.Consumer;

public class IncomingMethod<T, R> {
    private final String method;
    private final RpcErrorFunction<T, R> methodFunction;
    private final DataSerializer<T> requestSerializer;
    private final DataSerializer<R> responseSerializer;
    private Consumer<RpcCommunicationException> errorConsumer = e -> {};

    public IncomingMethod(String method, RpcErrorFunction<T, R> methodFunction, DataSerializer<T> requestSerializer, DataSerializer<R> responseSerializer) {
        this.method = method;
        this.methodFunction = methodFunction;
        this.requestSerializer = requestSerializer;
        this.responseSerializer = responseSerializer;
    }

    public IncomingMethod<T,R> onError(Consumer<RpcCommunicationException> errorConsumer) {
        this.errorConsumer = errorConsumer;
        return this;
    }

    public IncomingReceiver.Method<T,R> constructReceiver(ManagementHandler handler) {
        return new IncomingReceiver.Method<>(
                method,
                requestSerializer,
                responseSerializer,
                methodFunction,
                errorConsumer,
                handler,
                false
        );
    }

    public static ParameterlessResponseless of(String method, RpcErrorRunnable function) {
        return new ParameterlessResponseless(method, function);
    }

    public static <R> Parameterless<R> withResponse(String method, RpcErrorSupplier<R> function, DataSerializer<R> serializer) {
        return new Parameterless<>(method, function, serializer);
    }

    public static <R> Parameterless<R> withResponse(String method, RpcErrorSupplier<R> function, Class<R> clazz) {
        return withResponse(method, function, DataSerializer.forType(clazz));
    }

    public static <R> Parameterless<R> withResponse(String method, RpcErrorSupplier<R> function, TypeToken<R> type) {
        return withResponse(method, function, DataSerializer.forType(type));
    }

    public static <T> Responseless<T> withParameter(String method, RpcErrorConsumer<T> function, DataSerializer<T> serializer) {
        return new Responseless<>(method, function, serializer);
    }

    public static <T> Responseless<T> withParameter(String method, RpcErrorConsumer<T> function, TypeToken<T> type) {
        return withParameter(method, function, DataSerializer.forType(type));
    }

    public static <T> Responseless<T> withParameter(String method, RpcErrorConsumer<T> function, Class<T> clazz) {
        return withParameter(method, function, DataSerializer.forType(clazz));
    }

    public static <T,R> IncomingMethod<T,R> withParameterAndResponse(String method, RpcErrorFunction<T,R> function, DataSerializer<T> requestSerializer, DataSerializer<R> responseSerializer) {
        return new IncomingMethod<>(method, function, requestSerializer, responseSerializer);
    }

    public static <T,R> IncomingMethod<T,R> withParameterAndResponse(String method, RpcErrorFunction<T,R> function, TypeToken<T> requestType, TypeToken<R> responseType) {
        return withParameterAndResponse(method, function, DataSerializer.forType(requestType), DataSerializer.forType(responseType));
    }

    public static <T,R> IncomingMethod<T,R> withParameterAndResponse(String method, RpcErrorFunction<T,R> function, Class<T> requestClazz, Class<R> responseClazz) {
        return withParameterAndResponse(method, function, DataSerializer.forType(requestClazz), DataSerializer.forType(responseClazz));
    }

    public static class ParameterlessResponseless extends IncomingMethod<Void, Void> {
        private ParameterlessResponseless(String method, RpcErrorRunnable function) {
            super(
                    method,
                    r -> {
                        function.run();
                        return null;
                    },
                    DataSerializer.VOID,
                    DataSerializer.VOID
            );
        }

        @Override
        public ParameterlessResponseless onError(Consumer<RpcCommunicationException> errorConsumer) {
            return (ParameterlessResponseless)super.onError(errorConsumer);
        }
    }

    public static class Parameterless<R> extends IncomingMethod<Void, R> {
        public Parameterless(String method, RpcErrorSupplier<R> methodFunction, DataSerializer<R> responseSerializer) {
            super(
                    method,
                    r -> methodFunction.get(),
                    DataSerializer.VOID,
                    responseSerializer
            );
        }

        @Override
        public Parameterless<R> onError(Consumer<RpcCommunicationException> errorConsumer) {
            return (Parameterless<R>)super.onError(errorConsumer);
        }
    }

    public static class Responseless<T> extends IncomingMethod<T, Void> {
        public Responseless(String method, RpcErrorConsumer<T> methodFunction, DataSerializer<T> requestSerializer) {
            super(
                    method,
                    r -> {
                        methodFunction.accept(r);
                        return null;
                    },
                    requestSerializer,
                    DataSerializer.VOID
            );
        }

        @Override
        public Responseless<T> onError(Consumer<RpcCommunicationException> errorConsumer) {
            return (Responseless<T>)super.onError(errorConsumer);
        }
    }
}
