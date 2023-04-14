package com.gempukku.libgdx.ui.graph.data;

import com.badlogic.gdx.utils.Array;

public interface GraphNodeInput {
    boolean isRequired();

    boolean isAcceptingMultiple();

    boolean isMainConnection();

    String getFieldName();

    String getFieldId();

    Array<String> getAcceptedPropertyTypes();
}
