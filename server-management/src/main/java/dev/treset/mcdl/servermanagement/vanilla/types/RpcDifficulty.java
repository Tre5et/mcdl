package dev.treset.mcdl.servermanagement.vanilla.types;

import com.google.gson.annotations.SerializedName;

public enum RpcDifficulty {
    @SerializedName("peaceful") PEACEFUL,
    @SerializedName("easy") EASY,
    @SerializedName("normal") NORMAL,
    @SerializedName("hard") HARD,
}
