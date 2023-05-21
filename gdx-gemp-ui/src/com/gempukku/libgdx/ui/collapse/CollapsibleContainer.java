package com.gempukku.libgdx.ui.collapse;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.widget.VisTable;

public class CollapsibleContainer extends VisTable {
    private final Actor actor;
    private boolean expanded = false;

    public CollapsibleContainer(Actor actor) {
        this(actor, false);
    }

    public CollapsibleContainer(Actor actor, boolean expanded) {
        this.actor = actor;
        if (expanded) {
            new ExpandAction(true).doAction();
        }
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void collapse() {
        setExpanded(false);
    }

    public void expand() {
        setExpanded(true);
    }

    public void setExpanded(boolean expanded) {
        if (this.expanded != expanded) {
            ExpandAction expandAction = new ExpandAction(expanded);
            expandAction.doAction();

            UndoableChangeEvent undoableChangeEvent = Pools.obtain(UndoableChangeEvent.class);
            undoableChangeEvent.setUndoableAction(expandAction);
            fire(undoableChangeEvent);
            Pools.free(undoableChangeEvent);
        }
    }

    private class ExpandAction extends DefaultUndoableAction {
        private final boolean expand;

        public ExpandAction(boolean expand) {
            this.expand = expand;
        }

        @Override
        public void undoAction() {
            if (!expand) {
                add(actor).growX().row();
            } else {
                actor.remove();
            }
            expanded = !expand;
            invalidateHierarchy();
        }

        @Override
        public void redoAction() {
            if (expand) {
                add(actor).growX().row();
            } else {
                actor.remove();
            }
            expanded = expand;
            invalidateHierarchy();
        }
    }
}
