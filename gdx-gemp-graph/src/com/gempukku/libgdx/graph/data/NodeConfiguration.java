package com.gempukku.libgdx.graph.data;

import com.badlogic.gdx.utils.ObjectMap;

public interface NodeConfiguration {
    String getType();

    String getName();

    ObjectMap<String, ? extends GraphNodeInput> getNodeInputs();

    ObjectMap<String, ? extends GraphNodeOutput> getNodeOutputs();
}
