package com.gempukku.libgdx.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.ui.tabbedpane.GTab;
import com.gempukku.libgdx.ui.tabbedpane.GTabControl;
import com.gempukku.libgdx.ui.tabbedpane.GTabLabel;
import com.kotcrab.vis.ui.widget.VisLabel;

public class TestTab extends Table implements GTab {
    private GTabLabel<TestTab> tabActor;
    private String title;

    public TestTab(GTabControl<TestTab> tabControl, String title, boolean closeable) {
        this.title = title;
        tabActor = new GTabLabel<>(tabControl, this, "default", title, closeable);
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
    public void tabClosed() {

    }
}
