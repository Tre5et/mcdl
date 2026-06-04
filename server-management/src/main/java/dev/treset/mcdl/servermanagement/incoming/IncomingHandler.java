package dev.treset.mcdl.servermanagement.incoming;

import dev.treset.mcdl.servermanagement.data.DataProvider;
import dev.treset.mcdl.servermanagement.data.IdentificationProvider;
import dev.treset.mcdl.servermanagement.data.RpcRequest;
import dev.treset.mcdl.servermanagement.notification.RpcNotification;
import dev.treset.mcdl.servermanagement.data.RpcResponse;

import java.util.*;

public class IncomingHandler<T extends DataProvider & IdentificationProvider<I>,I> {
    Map<I, List<IncomingReceiver<T,?,I>>> receivers = new HashMap<>();

    public void handle(T message) {
        List<IncomingReceiver<T,?,I>> receivers = this.receivers.get(message.identification());
        if(receivers != null) {
            for(IncomingReceiver<T,?,I> r : new ArrayList<>(receivers)) { // new list to prevent concurrent modification
                r.receive(message);
            }
        }
    }

    public List<IncomingReceiver<T,?,I>> get(I identifier) {
        return receivers.get(identifier);
    }

    public boolean isRegistered(I identifier) {
        return receivers.containsKey(identifier);
    }

    public boolean isRegistered(IdentificationProvider<I> object) {
        return isRegistered(object.identification());
    }

    public void register(IncomingReceiver<T,?,I> receiver) {
        if(isRegistered(receiver)) {
            get(receiver.identification()).add(receiver);
        } else {
            receivers.put(receiver.identification(), new ArrayList<>(List.of(receiver)));
        }
    }

    public boolean unregister(IncomingReceiver<T,?,I> receiver) {
        if(!isRegistered(receiver)) {
            return false;
        }
        return get(receiver.identification()).removeIf(r -> r.equals(receiver));
    }

    public static class Notification extends IncomingHandler<RpcNotification,String> {}

    public static class Request extends IncomingHandler<RpcRequest, String> {}

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
