package dev.treset.mcdl.servermanagement.vanilla;

import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.servermanagement.incoming.IncomingReceiver;
import dev.treset.mcdl.servermanagement.vanilla.types.*;

import java.util.function.Consumer;

public class RpcNotifications {
    public static class Allowlist {
        public static IncomingReceiver.Notification<RpcPlayer> added(Consumer<RpcPlayer> resultConsumer) { return IncomingReceiver.notification("minecraft:notification/allowlist/added", new TypeToken<>() {}, resultConsumer); }
        public static IncomingReceiver.Notification<RpcPlayer> added() { return IncomingReceiver.notification("minecraft:notification/allowlist/added", new TypeToken<>() {}); }
        public static IncomingReceiver.Notification<RpcPlayer> removed(Consumer<RpcPlayer> resultConsumer) { return IncomingReceiver.notification("minecraft:notification/allowlist/removed", new TypeToken<>() {}, resultConsumer); }
        public static IncomingReceiver.Notification<RpcPlayer> removed() { return IncomingReceiver.notification("minecraft:notification/allowlist/removed", new TypeToken<>() {}); }
    }

    public static class Bans {
        public static IncomingReceiver.Notification<RpcUserBan> added(Consumer<RpcUserBan> resultConsumer) { return IncomingReceiver.notification("minecraft:notification/bans/added", new TypeToken<>() {}, resultConsumer); }
        public static IncomingReceiver.Notification<RpcUserBan> added() { return IncomingReceiver.notification("minecraft:notification/bans/added", new TypeToken<>() {}); }
        public static IncomingReceiver.Notification<RpcUserBan> removed(Consumer<RpcUserBan> resultConsumer) { return IncomingReceiver.notification("minecraft:notification/bans/removed", new TypeToken<>() {}, resultConsumer); }
        public static IncomingReceiver.Notification<RpcUserBan> removed() { return IncomingReceiver.notification("minecraft:notification/bans/removed", new TypeToken<>() {}); }
    }

    public static class GameRules {
        public static IncomingReceiver.Notification<RpcTypedGameRule> added(Consumer<RpcTypedGameRule> resultConsumer) { return IncomingReceiver.notification("minecraft:notification/gamerules/updated", new TypeToken<>() {}, resultConsumer); }
        public static IncomingReceiver.Notification<RpcTypedGameRule> added() { return IncomingReceiver.notification("minecraft:notification/gamerules/updated", new TypeToken<>() {}); }
    }

    public static class IpBans {
        public static IncomingReceiver.Notification<RpcIpBan> added(Consumer<RpcIpBan> resultConsumer) { return IncomingReceiver.notification("minecraft:notification/ip_bans/added", new TypeToken<>() {}, resultConsumer); }
        public static IncomingReceiver.Notification<RpcIpBan> added() { return IncomingReceiver.notification("minecraft:notification/ip_bans/added", new TypeToken<>() {}); }
        public static IncomingReceiver.Notification<RpcIpBan> removed(Consumer<RpcIpBan> resultConsumer) { return IncomingReceiver.notification("minecraft:notification/ip_bans/removed", new TypeToken<>() {}, resultConsumer); }
        public static IncomingReceiver.Notification<RpcIpBan> removed() { return IncomingReceiver.notification("minecraft:notification/ip_bans/removed", new TypeToken<>() {}); }
    }

    public static class Operators {
        public static IncomingReceiver.Notification<RpcOperator> added(Consumer<RpcOperator> resultConsumer) { return IncomingReceiver.notification("minecraft:notification/operators/added", new TypeToken<>() {}, resultConsumer); }
        public static IncomingReceiver.Notification<RpcOperator> added() { return IncomingReceiver.notification("minecraft:notification/operators/added", new TypeToken<>() {}); }
        public static IncomingReceiver.Notification<RpcOperator> removed(Consumer<RpcOperator> resultConsumer) { return IncomingReceiver.notification("minecraft:notification/operators/removed", new TypeToken<>() {}, resultConsumer); }
        public static IncomingReceiver.Notification<RpcOperator> removed() { return IncomingReceiver.notification("minecraft:notification/operators/removed", new TypeToken<>() {}); }
    }

    public static class Players {
        public static IncomingReceiver.Notification<RpcPlayer> joined(Consumer<RpcPlayer> resultConsumer) { return IncomingReceiver.notification("minecraft:notification/players/joined", new TypeToken<>() {}, resultConsumer); }
        public static IncomingReceiver.Notification<RpcPlayer> joined() { return IncomingReceiver.notification("minecraft:notification/players/joined", new TypeToken<>() {}); }
        public static IncomingReceiver.Notification<RpcPlayer> left(Consumer<RpcPlayer> resultConsumer) { return IncomingReceiver.notification("minecraft:notification/players/left", new TypeToken<>() {}, resultConsumer); }
        public static IncomingReceiver.Notification<RpcPlayer> left() { return IncomingReceiver.notification("minecraft:notification/players/left", new TypeToken<>() {}); }
    }

    public static class Server {
        public static IncomingReceiver.ParameterlessNotification started(Runnable onReceived) { return IncomingReceiver.notification("minecraft:notification/server/started", onReceived); }
        public static IncomingReceiver.ParameterlessNotification started() { return IncomingReceiver.notification("minecraft:notification/server/started"); }
        public static IncomingReceiver.ParameterlessNotification stopping(Runnable onReceived) { return IncomingReceiver.notification("minecraft:notification/server/stopping", onReceived); }
        public static IncomingReceiver.ParameterlessNotification stopping() { return IncomingReceiver.notification("minecraft:notification/server/stopping"); }
        public static IncomingReceiver.ParameterlessNotification saving(Runnable onReceived) { return IncomingReceiver.notification("minecraft:notification/server/saving", onReceived); }
        public static IncomingReceiver.ParameterlessNotification saving() { return IncomingReceiver.notification("minecraft:notification/server/saving"); }
        public static IncomingReceiver.ParameterlessNotification saved(Runnable onReceived) { return IncomingReceiver.notification("minecraft:notification/server/saved", onReceived); }
        public static IncomingReceiver.ParameterlessNotification saved() { return IncomingReceiver.notification("minecraft:notification/server/saved"); }
        public static IncomingReceiver.Notification<RpcServerState> status(Consumer<RpcServerState> onReceived) { return IncomingReceiver.notification("minecraft:notification/server/status", new TypeToken<>() {}, onReceived); }
        public static IncomingReceiver.Notification<RpcServerState> status() { return IncomingReceiver.notification("minecraft:notification/server/status", new TypeToken<>() {}); }
        public static IncomingReceiver.ParameterlessNotification activity(Runnable onReceived) { return IncomingReceiver.notification("minecraft:notification/server/activity", onReceived); }
        public static IncomingReceiver.ParameterlessNotification activity() { return IncomingReceiver.notification("minecraft:notification/server/activity"); }
    }
}
