package com.gempukku.libgdx.undo.event;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;

public abstract class UndoableListener implements EventListener {
    public boolean handle(Event event) {
        if (!(event instanceof UndoableEvent)) return false;
        undoable((UndoableEvent) event);
        return false;
    }

    public abstract void undoable(UndoableEvent event);
}
