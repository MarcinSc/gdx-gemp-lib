package com.gempukku.libgdx.ui.graph;

import com.badlogic.gdx.math.Rectangle;
import com.gempukku.libgdx.ui.graph.data.NodeGroup;
import com.gempukku.libgdx.ui.graph.data.impl.DefaultNodeGroup;

public class RectangleNodeGroup extends DefaultNodeGroup {
    private Rectangle rectangle = new Rectangle();

    public RectangleNodeGroup(NodeGroup nodeGroup) {
        super(nodeGroup.getName(), nodeGroup.getNodeIds());
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
