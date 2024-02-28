package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.graph.data.GraphNodeInputSide;

public class DefaultGraphNodeEditorInput implements GraphNodeEditorInput {
    private GraphNodeInputSide side;
    private Supplier<Float> offsetSupplier;
    private String fieldId;
    private final Drawable validDrawable;
    private final Drawable invalidDrawable;

    public DefaultGraphNodeEditorInput(GraphNodeInputSide side, Supplier<Float> offsetSupplier, String fieldId,
                                       Drawable validDrawable, Drawable invalidDrawable) {
        this.side = side;
        this.offsetSupplier = offsetSupplier;
        this.fieldId = fieldId;
        this.validDrawable = validDrawable;
        this.invalidDrawable = invalidDrawable;
    }

    @Override
    public GraphNodeInputSide getSide() {
        return side;
    }

    @Override
    public Drawable getConnectorDrawable(boolean valid) {
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
