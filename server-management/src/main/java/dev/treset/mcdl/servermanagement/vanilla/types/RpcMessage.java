package dev.treset.mcdl.servermanagement.vanilla.types;

import java.util.List;

public record RpcMessage(
        String translatable,
        List<String> translatableParams,
        String literal
) {
}
