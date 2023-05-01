package com.gempukku.libgdx.ui.graph.editor;

import com.badlogic.gdx.scenes.scene2d.Event;

public class PropertyChangedEvent<T> extends Event {
    private String property;
    private T oldValue;
    private T newValue;

    public PropertyChangedEvent(String property, T oldValue, T newValue) {
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getProperty() {
        return property;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNewValue() {
        return newValue;
    }
}
