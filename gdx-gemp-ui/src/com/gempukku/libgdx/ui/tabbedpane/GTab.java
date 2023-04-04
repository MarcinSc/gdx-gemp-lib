package com.gempukku.libgdx.ui.tabbedpane;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public interface GTab {
    /**
     * Retrieves the actor user to represent the tab in the row of tabs.
     * @return
     */
    Actor getTabActor();

    /**
     * Retrieves the actor to be displayed as the contents of this tab.
     * @return
     */
    Table getContentTable();

    /**
     * Notifies the tab, that it has been added to the GempTabbedPane.
     */
    void tabAdded();

    /**
     * Notifies the tab, that the tab has become active/inactive, that is, that its contents are being displayed, or
     * stopped being displayed.
     * @param active
     */
    void setActive(boolean active);

    /**
     * Notifies the tab, that it has been removed from the GempTabbedPane.
     */
    void tabClosed();
}
