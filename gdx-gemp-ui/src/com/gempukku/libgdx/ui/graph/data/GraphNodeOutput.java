package com.gempukku.libgdx.ui.graph.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public interface GraphNodeOutput extends GraphNodeIO {
    GraphNodeOutputSide getSide();

    String determineFieldType(ObjectMap<String, Array<String>> inputs);

    Array<String> getConnectableFieldTypes();
}

