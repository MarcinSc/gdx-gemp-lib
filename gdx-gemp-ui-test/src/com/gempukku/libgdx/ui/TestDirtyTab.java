package com.gempukku.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.ui.tabbedpane.*;
import com.kotcrab.vis.ui.widget.VisTable;

public class TestDirtyTab extends VisTable implements GDirtyTab {
    private GDirtyTabLabel<TestDirtyTab> tabActor;
    private String title;
    private boolean dirty;

    public TestDirtyTab(GTabControl<TestDirtyTab> tabControl, String title, boolean closeable) {
        this.title = title;
        tabActor = new GDirtyTabLabel<>(tabControl, this, "default", title, closeable);
    }

    @Override
    public Actor getTabActor() {
        return tabActor;
    }

    @Override
    public Table getContentTable() {
        return this;
    }

    @Override
    public void tabAdded() {

    }

    @Override
    public void setActive(boolean active) {
        tabActor.setActive(active);
        System.out.println(title + " is active: " + active);
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    @Override
    public void tabClosed() {

    }
}
