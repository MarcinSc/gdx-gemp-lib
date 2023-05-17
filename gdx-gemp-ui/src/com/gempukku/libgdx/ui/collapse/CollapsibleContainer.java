package com.gempukku.libgdx.ui.collapse;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.UndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImageButton;
import com.kotcrab.vis.ui.widget.VisTable;

public class CollapsibleContainer extends VisTable {
    private Actor actor;
    private VisImageButton expandButton;
    private boolean collapsed = true;

    private VisTable containerTable;

    public CollapsibleContainer(Actor actor, String styleName) {
        this(actor, VisUI.getSkin().get(styleName, VisImageButton.VisImageButtonStyle.class));
    }

    public CollapsibleContainer(Actor actor, VisImageButton.VisImageButtonStyle buttonStyle) {
        this.actor = actor;
        containerTable = new VisTable();

        expandButton = new VisImageButton(buttonStyle);
        expandButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (expandButton.isChecked() == collapsed) {
                            ExpandAction expandAction = new ExpandAction(expandButton.isChecked());
                            expandAction.doAction();

                            UndoableChangeEvent undoableChangeEvent = Pools.obtain(UndoableChangeEvent.class);
                            undoableChangeEvent.setUndoableAction(expandAction);
                            containerTable.fire(undoableChangeEvent);
                            Pools.free(undoableChangeEvent);
                            event.stop();
                        }
                    }
                });

        add(containerTable).row();
        add(expandButton).row();
    }

    public void collapse() {
        setCollapsed(true);
    }

    public void expand() {
        setCollapsed(false);
    }

    public void setCollapsed(boolean collapsed) {
        if (this.collapsed != collapsed) {
            this.collapsed = collapsed;
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
                containerTable.add(actor);
            } else {
                actor.remove();
            }
            collapsed = expand;
            expandButton.setChecked(!expand);
            containerTable.invalidateHierarchy();
        }

        @Override
        public void redoAction() {
            if (expand) {
                containerTable.add(actor);
            } else {
                actor.remove();
            }
            collapsed = !expand;
            expandButton.setChecked(expand);
            containerTable.invalidateHierarchy();
        }
    }
}
