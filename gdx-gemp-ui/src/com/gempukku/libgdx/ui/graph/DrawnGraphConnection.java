package com.gempukku.libgdx.ui.graph;

import com.gempukku.libgdx.graph.data.GraphConnection;
import com.gempukku.libgdx.graph.data.impl.DefaultGraphConnection;

public class DrawnGraphConnection extends DefaultGraphConnection {
    private boolean error;

    public DrawnGraphConnection(GraphConnection graphConnection) {
        this(graphConnection.getNodeFrom(), graphConnection.getFieldFrom(), graphConnection.getNodeTo(), graphConnection.getFieldTo());
    }

    public DrawnGraphConnection(String nodeFrom, String fieldFrom, String nodeTo, String fieldTo) {
        super(nodeFrom, fieldFrom, nodeTo, fieldTo);
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
