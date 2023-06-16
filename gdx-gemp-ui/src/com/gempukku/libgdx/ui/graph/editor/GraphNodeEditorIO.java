package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public interface GraphNodeEditorIO {
    Drawable getConnectorDrawable(boolean valid);

    float getOffset();

    String getFieldId();
}
