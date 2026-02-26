package dev.treset.mcdl.servermanagement.vanilla;

import com.google.gson.reflect.TypeToken;
import dev.treset.mcdl.servermanagement.outgoing.OutgoingMethod;
import dev.treset.mcdl.servermanagement.vanilla.types.*;

import java.util.List;

public class RpcMethods {
    public static class Allowlist {
        public static final OutgoingMethod.Parameterless<List<RpcPlayer>> GET = OutgoingMethod.of("minecraft:allowlist").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<List<RpcPlayer>, List<RpcPlayer>> SET = OutgoingMethod.of("minecraft:allowlist/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod<List<RpcPlayer>, List<RpcPlayer>> ADD = OutgoingMethod.of("minecraft:allowlist/add").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod<List<RpcPlayer>, List<RpcPlayer>> REMOVE = OutgoingMethod.of("minecraft:allowlist/remove").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<List<RpcPlayer>> CLEAR = OutgoingMethod.of("minecraft:allowlist/clear").withResponse(new TypeToken<>() {});
    }

    public static class Bans {
        public static final OutgoingMethod.Parameterless<List<RpcUserBan>> GET = OutgoingMethod.of("minecraft:bans").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<List<RpcUserBan>, List<RpcUserBan>> SET = OutgoingMethod.of("minecraft:bans/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod<List<RpcUserBan>, List<RpcUserBan>> ADD = OutgoingMethod.of("minecraft:bans/add").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod<List<RpcPlayer>, List<RpcUserBan>> REMOVE = OutgoingMethod.of("minecraft:bans/remove").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<List<RpcUserBan>> CLEAR = OutgoingMethod.of("minecraft:bans/clear").withResponse(new TypeToken<>() {});
    }

    public static class GameRules {
        public static final OutgoingMethod.Parameterless<List<RpcTypedGameRule>> GET = OutgoingMethod.of("minecraft:gamerules").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<RpcUntypedGameRule, RpcTypedGameRule> SET = OutgoingMethod.of("minecraft:gamerules/update").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
    }

    public static class IpBans {
        public static final OutgoingMethod.Parameterless<List<RpcIpBan>> GET = OutgoingMethod.of("minecraft:ip_bans").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<List<RpcIpBan>, List<RpcIpBan>> SET = OutgoingMethod.of("minecraft:ip_bans/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod<List<RpcIncomingIpBan>, List<RpcIpBan>> ADD = OutgoingMethod.of("minecraft:ip_bans/add").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod<List<String>, List<RpcIpBan>> REMOVE = OutgoingMethod.of("minecraft:ip_bans/remove").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<List<RpcIpBan>> CLEAR = OutgoingMethod.of("minecraft:ip_bans/clear").withResponse(new TypeToken<>() {});
    }

    public static class Operators {
        public static final OutgoingMethod.Parameterless<List<RpcOperator>> GET = OutgoingMethod.of("minecraft:operators").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<List<RpcOperator>, List<RpcOperator>> SET = OutgoingMethod.of("minecraft:operators/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod<List<RpcOperator>, List<RpcOperator>> ADD = OutgoingMethod.of("minecraft:operators/add").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod<List<RpcPlayer>, List<RpcOperator>> REMOVE = OutgoingMethod.of("minecraft:operators/remove").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<List<RpcOperator>> CLEAR = OutgoingMethod.of("minecraft:operators/clear").withResponse(new TypeToken<>() {});
    }

    public static class Players {
        public static OutgoingMethod.Parameterless<List<RpcPlayer>> GET = OutgoingMethod.of("minecraft:players").withResponse(new TypeToken<>() {});
        public static OutgoingMethod<List<RpcKickPlayer>, List<RpcPlayer>> KICK = OutgoingMethod.of("minecraft:players/kick").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
    }

    public static class Server {
        public static final OutgoingMethod.Parameterless<RpcServerState> STATUS = OutgoingMethod.of("minecraft:server/status").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Boolean, Boolean> SAVE = OutgoingMethod.of("minecraft:server/save").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Boolean> STOP = OutgoingMethod.of("minecraft:server/stop").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<RpcSystemMessage, Boolean> SYSTEM_MESSAGE = OutgoingMethod.of("minecraft:server/system_message").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
    }

    public static class ServerSettings {
        public static final OutgoingMethod.Parameterless<Boolean> AUTOSAVE_GET = OutgoingMethod.of("minecraft:serversettings/autosave").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Boolean, Boolean> AUTOSAVE_SET = OutgoingMethod.of("minecraft:serversettings/autosave/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<RpcDifficulty> DIFFICULTY_GET = OutgoingMethod.of("minecraft:serversettings/difficulty").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<RpcDifficulty, RpcDifficulty> DIFFICULTY_SET = OutgoingMethod.of("minecraft:serversettings/difficulty/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Boolean> ENFORCE_ALLOWLIST_GET = OutgoingMethod.of("minecraft:serversettings/enforce_allowlist").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Boolean, Boolean> ENFORCE_ALLOWLIST_SET = OutgoingMethod.of("minecraft:serversettings/enforce_allowlist/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Boolean> USE_ALLOWLIST_GET = OutgoingMethod.of("minecraft:serversettings/use_allowlist").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Boolean, Boolean> USE_ALLOWLIST_SET = OutgoingMethod.of("minecraft:serversettings/use_allowlist/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Integer> MAX_PLAYERS_GET = OutgoingMethod.of("minecraft:serversettings/max_players").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Integer, Integer> MAX_PLAYERS_SET = OutgoingMethod.of("minecraft:serversettings/max_players/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Integer> PAUSE_WHEN_EMPTY_SECONDS_GET = OutgoingMethod.of("minecraft:serversettings/pause_when_empty_seconds").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Integer, Integer> PAUSE_WHEN_EMPTY_SECONDS_SET = OutgoingMethod.of("minecraft:serversettings/pause_when_empty_seconds/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Integer> PLAYER_IDLE_TIMEOUT_GET = OutgoingMethod.of("minecraft:serversettings/player_idle_timeout").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Integer, Integer> PLAYER_IDLE_TIMEOUT_SET = OutgoingMethod.of("minecraft:serversettings/player_idle_timeout/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Boolean> ALLOW_FLIGHT_GET = OutgoingMethod.of("minecraft:serversettings/allow_flight").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Boolean, Boolean> ALLOW_FLIGHT_SET = OutgoingMethod.of("minecraft:serversettings/allow_flight/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<String> MOTD_GET = OutgoingMethod.of("minecraft:serversettings/motd").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<String, String> MOTD_SET = OutgoingMethod.of("minecraft:serversettings/motd/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Integer> SPAWN_PROTECTION_RADIUS_GET = OutgoingMethod.of("minecraft:serversettings/spawn_protection_radius").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Integer, Integer> SPAWN_PROTECTION_RADIUS_SET = OutgoingMethod.of("minecraft:serversettings/spawn_protection_radius/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Boolean> FORCE_GAME_MODE_GET = OutgoingMethod.of("minecraft:serversettings/force_game_mode").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Boolean, Boolean> FORCE_GAME_MODE_SET = OutgoingMethod.of("minecraft:serversettings/force_game_mode/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<RpcGameMode> GAME_MODE_GET = OutgoingMethod.of("minecraft:serversettings/game_mode").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<RpcGameMode, RpcGameMode> GAME_MODE_SET = OutgoingMethod.of("minecraft:serversettings/game_mode/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Integer> VIEW_DISTANCE_GET = OutgoingMethod.of("minecraft:serversettings/view_distance").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Integer, Integer> VIEW_DISTANCE_SET = OutgoingMethod.of("minecraft:serversettings/view_distance/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Integer> SIMULATION_DISTANCE_GET = OutgoingMethod.of("minecraft:serversettings/simulation_distance").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Integer, Integer> SIMULATION_DISTANCE_SET = OutgoingMethod.of("minecraft:serversettings/simulation_distance/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Boolean> ACCEPT_TRANSFERS_GET = OutgoingMethod.of("minecraft:serversettings/accept_transfers").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Boolean, Boolean> ACCEPT_TRANSFERS_SET = OutgoingMethod.of("minecraft:serversettings/accept_transfers/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Integer> STATUS_HEARTBEAT_INTERVAL_GET = OutgoingMethod.of("minecraft:serversettings/status_hearbeat_interval").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Integer, Integer> STATUS_HEARTBEAT_INTERVAL_SET = OutgoingMethod.of("minecraft:serversettings/status_hearbeat_interval/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Integer> OPERATOR_USER_PERMISSION_LEVEL_GET = OutgoingMethod.of("minecraft:serversettings/operator_user_permission_level").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Integer, Integer> OPERATOR_USER_PERMISSION_LEVEL_SET = OutgoingMethod.of("minecraft:serversettings/operator_user_permission_level/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Boolean> HIDE_ONLINE_PLAYERS_GET = OutgoingMethod.of("minecraft:serversettings/hide_online_players").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Boolean, Boolean> HIDE_ONLINE_PLAYERS_SET = OutgoingMethod.of("minecraft:serversettings/hide_online_players/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Boolean> STATUS_REPLIES_GET = OutgoingMethod.of("minecraft:serversettings/status_replies").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Boolean, Boolean> STATUS_REPLIES_SET = OutgoingMethod.of("minecraft:serversettings/status_replies/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
        public static final OutgoingMethod.Parameterless<Integer> ENTITY_BROADCAST_RANGE_GET = OutgoingMethod.of("minecraft:serversettings/entity_broadcast_range").withResponse(new TypeToken<>() {});
        public static final OutgoingMethod<Integer, Integer> ENTITY_BROADCAST_RANGE_SET = OutgoingMethod.of("minecraft:serversettings/entity_broadcast_range/set").withParameterAndResponse(new TypeToken<>() {}, new TypeToken<>() {});
    }
}
