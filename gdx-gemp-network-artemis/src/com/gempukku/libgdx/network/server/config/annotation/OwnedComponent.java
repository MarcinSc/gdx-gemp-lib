package com.gempukku.libgdx.network.server.config.annotation;

public interface OwnedComponent {
    boolean isOwnedBy(String username);
}
