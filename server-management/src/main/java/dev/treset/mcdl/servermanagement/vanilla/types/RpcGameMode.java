package dev.treset.mcdl.servermanagement.vanilla.types;

import com.google.gson.annotations.SerializedName;

public enum RpcGameMode {
    @SerializedName("creative") CREATIVE,
    @SerializedName("survival") SURVIVAL,
    @SerializedName("adventure") ADVENTURE,
    @SerializedName("spectator") SPECTATOR,
}
