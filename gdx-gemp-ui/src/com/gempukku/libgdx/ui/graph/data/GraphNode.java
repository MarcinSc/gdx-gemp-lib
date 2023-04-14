package com.gempukku.libgdx.ui.graph.data;

import com.badlogic.gdx.utils.JsonValue;

public interface GraphNode {
    String getId();

    float getX();

    float getY();

    JsonValue getData();

    NodeConfiguration getConfiguration();
}
