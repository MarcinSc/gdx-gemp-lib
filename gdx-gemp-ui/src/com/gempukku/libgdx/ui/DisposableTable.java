package com.gempukku.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.kotcrab.vis.ui.widget.VisTable;

public abstract class DisposableTable extends VisTable {
    private boolean initialized;

    @Override
    protected void setStage(Stage stage) {
        super.setStage(stage);

        if (stage != null && !initialized) {
            initializeWidget();
            initialized = true;
        } else if (stage == null && initialized) {
            disposeWidget();
            initialized = false;
        }
    }

    protected abstract void initializeWidget();

    protected abstract void disposeWidget();
}
