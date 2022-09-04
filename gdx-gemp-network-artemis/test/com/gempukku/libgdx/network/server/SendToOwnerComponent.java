package com.gempukku.libgdx.network.server;

import com.artemis.Component;
import com.gempukku.libgdx.network.OwnedComponent;
import com.gempukku.libgdx.network.ReplicateToOwner;

@ReplicateToOwner
public class SendToOwnerComponent extends Component implements OwnedComponent {
    private String owner;

    @Override
    public String getOwner() {
        return owner;
    }

    @Override
    public void setOwner(String owner) {
        this.owner = owner;
    }
}
