package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.ui.graph.data.GraphNodeOutputSide;

public class DefaultGraphNodeEditorOutput implements GraphNodeEditorOutput {
    private GraphNodeOutputSide side;
    private Supplier<Float> offsetSupplier;
    private String fieldId;
    private final Drawable validDrawable;
    private final Drawable invalidDrawable;

    public DefaultGraphNodeEditorOutput(GraphNodeOutputSide side, Supplier<Float> offsetSupplier, String fieldId,
                                        Drawable validDrawable, Drawable invalidDrawable) {
        this.side = side;
        this.offsetSupplier = offsetSupplier;
        this.fieldId = fieldId;
        this.validDrawable = validDrawable;
        this.invalidDrawable = invalidDrawable;
    }

    @Override
    public GraphNodeOutputSide getSide() {
        return side;
    }

    @Override
    public Drawable getDrawable(boolean valid) {
        return valid ? validDrawable : invalidDrawable;
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
