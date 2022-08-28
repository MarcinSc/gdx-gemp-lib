package com.gempukku.libgdx.lib.artemis.shape;

import com.artemis.Component;

public class ShapeComponent extends Component {
    private String name;
    private int vertexCount;
    private short[] indices;

    public String getName() {
        return name;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public short[] getIndices() {
        return indices;
    }
}
