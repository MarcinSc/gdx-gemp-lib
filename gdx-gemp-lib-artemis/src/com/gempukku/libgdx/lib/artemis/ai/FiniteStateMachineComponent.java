package com.gempukku.libgdx.lib.artemis.ai;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectMap;

public class FiniteStateMachineComponent extends Component {
    private String name;
    private String initialState;
    private ObjectMap<String, ArtemisStateDefinition> states;

    public String getName() {
        return name;
    }

    public String getInitialState() {
        return initialState;
    }

    public ObjectMap<String, ArtemisStateDefinition> getStates() {
        return states;
    }
}
