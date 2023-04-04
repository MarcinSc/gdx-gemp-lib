package com.gempukku.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public abstract class DisposableWidget extends Widget {
    private boolean initialized;

    private float minWidth;
    private float minHeight;
    private float prefWidth;
    private float prefHeight;
    private float maxWidth;
    private float maxHeight;

    @Override
    public float getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(float minWidth) {
        this.minWidth = minWidth;
    }

    @Override
    public float getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(float minHeight) {
        this.minHeight = minHeight;
    }

    @Override
    public float getPrefWidth() {
        return prefWidth;
    }

    public void setPrefWidth(float prefWidth) {
        this.prefWidth = prefWidth;
    }

    @Override
    public float getPrefHeight() {
        return prefHeight;
    }

    public void setPrefHeight(float prefHeight) {
        this.prefHeight = prefHeight;
    }

    @Override
    public float getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    @Override
    public float getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
    }

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
