package com.gempukku.libgdx.ui.graph.data;

import com.badlogic.gdx.utils.ObjectMap;

public interface NodeConfiguration {
    String getName();

    ObjectMap<String, GraphNodeInput> getNodeInputs();

    ObjectMap<String, GraphNodeOutput> getNodeOutputs();
}
