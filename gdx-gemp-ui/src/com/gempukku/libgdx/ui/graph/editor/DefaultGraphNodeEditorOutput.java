package com.gempukku.libgdx.ui.graph.editor;

import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;

public class DefaultGraphNodeEditorOutput implements GraphNodeEditorOutput {
    private GraphNodeOutputSide side;
    private Supplier<Float> offsetSupplier;
    private String fieldId;

    public DefaultGraphNodeEditorOutput(GraphNodeOutputSide side, Supplier<Float> offsetSupplier, String fieldId) {
        this.side = side;
        this.offsetSupplier = offsetSupplier;
        this.fieldId = fieldId;
    }

    @Override
    public GraphNodeOutputSide getSide() {
        return side;
    }

    @Override
    public float getOffset() {
        return offsetSupplier.get();
    }

    @Override
    public String getFieldId() {
        return fieldId;
    }
}
