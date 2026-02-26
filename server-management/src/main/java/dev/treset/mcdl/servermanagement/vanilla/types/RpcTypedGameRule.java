package dev.treset.mcdl.servermanagement.vanilla.types;

import com.google.gson.annotations.SerializedName;

public record RpcTypedGameRule(
    Type type,
    String value,
    String key
) {
    public enum Type {
        @SerializedName("integer") INTEGER,
        @SerializedName("boolean") BOOLEAN
    }
}
