package com.gempukku.libgdx.undo.event;

import com.gempukku.libgdx.undo.UndoableAction;

public interface UndoableEvent {
    UndoableAction getUndoableAction();
}
