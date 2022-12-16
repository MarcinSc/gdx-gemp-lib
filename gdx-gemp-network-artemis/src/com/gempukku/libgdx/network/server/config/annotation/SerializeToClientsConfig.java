package com.gempukku.libgdx.network.server.config.annotation;

import com.artemis.Component;
import com.gempukku.libgdx.network.server.ClientConnection;
import com.gempukku.libgdx.network.server.config.NetworkEntitySerializationConfig;

public class SerializeToClientsConfig implements NetworkEntitySerializationConfig {
    @Override
    public boolean isComponentSerializedToClient(Component component, ClientConnection clientConnection) {
        Class<? extends Component> componentClass = component.getClass();
        return componentClass.getAnnotation(ReplicateToClients.class) != null
                || componentClass.getAnnotation(ReplicateWithOtherComponents.class) != null
                || replicatesToUser(component, componentClass, clientConnection.getName());
    }

    private boolean replicatesToUser(Component component, Class<? extends Component> componentClass, String username) {
        ReplicateToOwner replicateToOwner = componentClass.getAnnotation(ReplicateToOwner.class);
        if (replicateToOwner != null) {
            if (component instanceof OwnedComponent && ((OwnedComponent) component).isOwnedBy(username))
                return true;
        }
        return false;
    }
}
