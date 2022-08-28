package com.gempukku.libgdx.lib.artemis.ai;

import com.artemis.PooledComponent;
import com.badlogic.gdx.utils.ObjectMap;

public class FiniteStateMachineAIComponent extends PooledComponent {
    private String state;
    private String fsmName;
    private ObjectMap<String, Object> blackboard = new ObjectMap<>();

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public String getFsmName() {
        return fsmName;
    }

    public ObjectMap<String, Object> getBlackboard() {
        return blackboard;
    }

    @Override
    protected void reset() {
        state = null;
        fsmName = null;
        blackboard = null;
    }
}
