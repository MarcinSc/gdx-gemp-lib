package com.gempukku.libgdx.ui.graph.data;

import com.badlogic.gdx.utils.ObjectSet;

public interface NodeGroup {
    String getName();

    ObjectSet<String> getNodeIds();
}
