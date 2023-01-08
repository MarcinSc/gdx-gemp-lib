package com.gempukku.libgdx.lib.artemis.selection;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.lib.artemis.camera.CameraSystem;
import com.gempukku.libgdx.lib.artemis.hierarchy.HierarchySystem;
import com.gempukku.libgdx.lib.artemis.picking.ShapePickingSystem;

// Should not be necessary, as we only want to make the HierarchySystem optional, but
// that is what I consider a bug in Artemis, where it doesn't work, if you just
// put it on field.
@Wire(failOnNull = false)
public class SelectionSystem extends BaseSystem {
    private ShapePickingSystem shapePickingSystem;
    private CameraSystem cameraSystem;
    @Wire(failOnNull = false)
    private HierarchySystem hierarchySystem;

    private boolean selecting;
    private SelectionDefinition selectionDefinition;

    private final ObjectSet<Entity> selectedEntities = new ObjectSet<>();

    public void startSelection(SelectionDefinition selectionDefinition) {
        this.selecting = true;
        this.selectionDefinition = selectionDefinition;
    }

    public void updateSelection(SelectionDefinition selectionDefinition) {
        if (!selecting)
            throw new IllegalStateException("System not currently selecting");
        this.selectionDefinition = selectionDefinition;
    }

    public void finishSelection() {
        this.selecting = false;
        this.selectionDefinition = null;

        this.selectedEntities.clear();
    }

    public Iterable<Entity> getSelectedEntities() {
        return selectedEntities;
    }

    @Override
    protected void processSystem() {
        if (selecting && selectionDefinition.isSelectionTriggered()) {
            Ray pickRay = cameraSystem.getCamera(selectionDefinition.getCameraName()).getPickRay(Gdx.input.getX(), Gdx.input.getY());
            Entity pickedEntity = shapePickingSystem.pickEntity(pickRay,
                    selectionDefinition.getMask(), selectionDefinition.getEntityPredicate());
            if (pickedEntity != null) {
                Entity selectedEntity = findSelectedEntity(pickedEntity);
                boolean fireSelectionChaned = false;
                if (selectedEntities.contains(selectedEntity)) {
                    if (selectionDefinition.canDeselect(selectedEntities, selectedEntity)) {
                        selectedEntities.remove(selectedEntity);
                        fireSelectionChaned = true;
                    }
                } else {
                    if (selectionDefinition.canSelect(selectedEntities, selectedEntity)) {
                        selectedEntities.add(selectedEntity);
                        fireSelectionChaned = true;
                    }
                }
                if (fireSelectionChaned)
                    selectionDefinition.selectionChanged(selectedEntities);
            }
        }
    }

    private Entity findSelectedEntity(Entity pickedEntity) {
        if (hierarchySystem != null) {
            if (pickedEntity.getComponent(SelectParentComponent.class) != null) {
                return findSelectedEntity(hierarchySystem.getParent(pickedEntity));
            }
        }

        SelectTargetComponent selectTarget = pickedEntity.getComponent(SelectTargetComponent.class);
        if (selectTarget != null)
            return findSelectedEntity(selectTarget.getTargetEntity());

        return pickedEntity;
    }
}
