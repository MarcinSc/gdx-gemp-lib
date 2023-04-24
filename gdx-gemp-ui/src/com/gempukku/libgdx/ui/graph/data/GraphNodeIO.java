package com.gempukku.libgdx.ui.graph.data;

import com.badlogic.gdx.utils.Array;

public interface GraphNodeIO {
    boolean acceptsMultipleConnections();
    String getFieldName();
    String getFieldId();
    Array<String> getConnectableFieldTypes();
}
