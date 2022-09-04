package com.gempukku.libgdx.network;

import java.util.List;

public interface OwnedByMultipleComponent {
    List<String> getOwners();

    void setOwners(List<String> owners);
}
