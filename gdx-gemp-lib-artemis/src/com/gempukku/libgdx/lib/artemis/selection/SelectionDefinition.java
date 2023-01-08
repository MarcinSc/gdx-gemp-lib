package com.gempukku.libgdx.lib.artemis.selection;

import com.artemis.Entity;
import com.badlogic.gdx.utils.ObjectSet;
import com.badlogic.gdx.utils.Predicate;

public interface SelectionDefinition {
    boolean isSelectionTriggered();

    boolean canDeselect(ObjectSet<Entity> selectedEntities, Entity selected);

    boolean canSelect(ObjectSet<Entity> selectedEntities, Entity newSelected);

    void selectionChanged(ObjectSet<Entity> selectedEntities);

    String getMask();

    String getCameraName();

    Predicate<Entity> getEntityPredicate();
}
