package dev.treset.mcdl.servermanagement.incoming;

import dev.treset.mcdl.servermanagement.data.DataProvider;
import dev.treset.mcdl.servermanagement.data.IdentificationProvider;
import dev.treset.mcdl.servermanagement.notification.RpcNotification;
import dev.treset.mcdl.servermanagement.data.RpcResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class IncomingHandler<T extends DataProvider & IdentificationProvider<I>,I> implements MapContainer<I,IncomingReceiver<T,?,I>> {
    Map<I, IncomingReceiver<T,?,I>> receivers = new HashMap<>();

    public void handle(T message) {
        IncomingReceiver<T,?,I> receiver = receivers.get(message.identification());
        if(receiver != null) {
            receiver.receive(message);
        }
    }

    public IncomingReceiver<T,?,I> get(I identifier) {
        return receivers.get(identifier);
    }

    public boolean isRegistered(I identifier) {
        return receivers.containsKey(identifier);
    }

    public boolean isRegistered(IdentificationProvider<I> object) {
        return isRegistered(object.identification());
    }

    @Override
    public Map<I, IncomingReceiver<T, ?, I>> map() {
        return this.receivers;
    }

    public static class Notification extends IncomingHandler<RpcNotification,String> {}

    public static class Response extends IncomingHandler<RpcResponse,Integer> {
        private static final Random RANDOM = new Random();

        public int newId() {
            int id = RANDOM.nextInt();
            while(receivers.containsKey(id)) {
                id = RANDOM.nextInt();
            }
            return id;
        }
    }
}
