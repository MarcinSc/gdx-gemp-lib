package com.gempukku.libgdx.undo.event;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class UndoableChangeListener implements EventListener {
    public boolean handle(Event event) {
        if (!(event instanceof UndoableChangeEvent)) return false;
        changed((UndoableChangeEvent) event);
        return false;
    }

    public abstract void changed(UndoableChangeEvent event);
}
