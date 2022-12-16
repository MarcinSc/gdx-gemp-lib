package com.gempukku.libgdx.network.id;

import java.util.UUID;

public class UuidGenerator implements IdGenerator {
    @Override
    public String generateId() {
        return UUID.randomUUID().toString();
    }
}
