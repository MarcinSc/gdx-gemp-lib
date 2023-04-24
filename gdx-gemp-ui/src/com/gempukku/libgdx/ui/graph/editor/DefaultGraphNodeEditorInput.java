package com.gempukku.libgdx.ui.graph.editor;

import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.ui.graph.data.GraphNodeInputSide;

public class DefaultGraphNodeEditorInput implements GraphNodeEditorInput {
    private GraphNodeInputSide side;
    private Supplier<Float> offsetSupplier;
    private String fieldId;

    public DefaultGraphNodeEditorInput(GraphNodeInputSide side, Supplier<Float> offsetSupplier, String fieldId) {
        this.side = side;
        this.offsetSupplier = offsetSupplier;
        this.fieldId = fieldId;
    }

    @Override
    public GraphNodeInputSide getSide() {
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
