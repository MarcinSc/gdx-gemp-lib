package com.gempukku.libgdx.lib.artemis.picking.ai;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.lib.artemis.ai.ArtemisTriggerCondition;
import com.gempukku.libgdx.lib.artemis.camera.CameraSystem;
import com.gempukku.libgdx.lib.artemis.picking.ShapePickingSystem;

public class PickEntity implements ArtemisTriggerCondition {
    private static final ExcludeEntityPredicate excludeEntityPredicate = new ExcludeEntityPredicate();

    private ShapePickingSystem shapePickingSystem;
    private CameraSystem cameraSystem;

    private String pickMask;
    private String pickEntityId;
    private String excludeEntityId;

    @Override
    public void initializeForWorld(World world) {
        shapePickingSystem = world.getSystem(ShapePickingSystem.class);
        cameraSystem = world.getSystem(CameraSystem.class);
    }

    @Override
    public boolean isTriggered(float deltaTime, Entity entity, ObjectMap<String, Object> blackboard) {
        Camera camera = cameraSystem.getCamera();
        Ray pickRay = camera.getPickRay(Gdx.input.getX(), Gdx.input.getY());
        Integer excludedEntity = getExcludeEntityId(blackboard);
        excludeEntityPredicate.setEntityId(excludedEntity);

        Entity pickedEntity = shapePickingSystem.pickEntity(pickRay, pickMask, excludeEntityPredicate);
        if (pickedEntity != null) {
            blackboard.put(pickEntityId, pickedEntity.getId());
            return true;
        }
        return false;
    }

    private Integer getExcludeEntityId(ObjectMap<String, Object> blackboard) {
        if (excludeEntityId == null)
            return null;

        Number excludeId = (Number) blackboard.get(excludeEntityId);
        if (excludeId == null)
            return null;
        return excludeId.intValue();
    }

    @Override
    public void reset(Entity entity, ObjectMap<String, Object> blackboard) {

    }
}
