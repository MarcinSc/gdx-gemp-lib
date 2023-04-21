package com.gempukku.libgdx.ui.tabbedpane;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GTabbedPane<T extends GTab> extends Table implements GTabControl<T> {
    private HorizontalGroup tabGroup;
    private Table contentTable;

    private Array<T> tabs = new Array<>();
    private T activeTab;
    private Actor defaultContentActor;

    public GTabbedPane() {
        this(null);
    }

    public GTabbedPane(Actor defaultContentActor) {
        this.defaultContentActor = defaultContentActor;
        tabGroup = new HorizontalGroup().wrap();
        contentTable = new Table();
        add(tabGroup).growX().row();
        add(contentTable).grow().row();
        if (defaultContentActor != null) {
            contentTable.add(defaultContentActor).grow();
        }
    }

    public void addTab(T tab) {
        if (tabs.contains(tab, true))
            throw new GdxRuntimeException("This TabbedPane already contains this tab");
        tabs.add(tab);
        tabGroup.addActor(tab.getTabActor());
        tab.tabAdded();
        if (activeTab == null) {
            setTabActive(tab);
        }
    }

    public Array<T> getTabs() {
        return tabs;
    }

    @Override
    public void setTabActive(T tab) {
        if (!tabs.contains(tab, true))
            throw new GdxRuntimeException("This TabbedPane does not contain this tab");
        if (activeTab != tab) {
            contentTable.clearChildren();
            if (activeTab != null) {
                activeTab.setActive(false);
            }
            activeTab = tab;
            contentTable.add(tab.getContentTable()).grow();
            tab.setActive(true);
        }
    }

    public T getActiveTab() {
        return activeTab;
    }

    @Override
    public void closeTab(T tab) {
        if (!tabs.contains(tab, true))
            throw new GdxRuntimeException("This TabbedPane does not contain this tab");
        tabGroup.removeActor(tab.getTabActor());
        int tabIndex = tabs.indexOf(tab, true);
        tabs.removeValue(tab, true);
        if (activeTab == tab) {
            int newActiveIndex = Math.min(tabIndex, tabs.size - 1);
            if (newActiveIndex != -1) {
                setTabActive(tabs.get(newActiveIndex));
            } else {
                tab.setActive(false);
                contentTable.clearChildren();
                if (defaultContentActor != null) {
                    contentTable.add(defaultContentActor).grow();
                }
                activeTab = null;
            }
        }
        tab.tabClosed();
    }
}
