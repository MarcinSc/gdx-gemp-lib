package com.gempukku.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public abstract class DisposableWidget extends Widget {
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
