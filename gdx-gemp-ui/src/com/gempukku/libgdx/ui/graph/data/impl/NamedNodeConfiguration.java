package com.gempukku.libgdx.ui.graph.data.impl;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.ui.graph.data.NodeConfiguration;

public interface NamedNodeConfiguration extends NodeConfiguration {
    @Override
    ObjectMap<String, ? extends NamedGraphNodeInput> getNodeInputs();

    @Override
    ObjectMap<String, ? extends NamedGraphNodeOutput> getNodeOutputs();
}
