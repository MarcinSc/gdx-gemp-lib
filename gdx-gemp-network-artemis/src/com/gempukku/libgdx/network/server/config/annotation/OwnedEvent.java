package com.gempukku.libgdx.network.server.config.annotation;

public interface OwnedEvent {
    boolean isOwnedBy(String username);
}
