package com.gempukku.libgdx.ui.tabbedpane;

public interface GTabControl<T extends GTab> {
    void setTabActive(T tab);
    void closeTab(T tab);
}
