package com.gempukku.libgdx.ui.graph.editor;

import com.gempukku.libgdx.common.Supplier;

public class DefaultGraphNodeEditorOutput implements GraphNodeEditorOutput {
    private Side side;
    private Supplier<Float> offsetSupplier;
    private String fieldId;

    public DefaultGraphNodeEditorOutput(Side side, Supplier<Float> offsetSupplier, String fieldId) {
        this.side = side;
        this.offsetSupplier = offsetSupplier;
        this.fieldId = fieldId;
    }

    @Override
    public Side getSide() {
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
