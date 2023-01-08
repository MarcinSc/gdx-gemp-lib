package com.gempukku.libgdx.lib.artemis.camera;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Matrix4;
import com.gempukku.libgdx.lib.artemis.event.EventListener;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;

public class CameraHookSystem extends EntitySystem {
    private TransformSystem transformSystem;
    private CameraSystem cameraSystem;

    private Matrix4 tmpMatrix4 = new Matrix4();

    public CameraHookSystem() {
        super(Aspect.all(CameraHookComponent.class));
    }

    @EventListener
    public void cameraUpdated(CameraUpdated cameraUpdated, Entity cameraEntity) {
        String cameraName = cameraUpdated.getCameraName();
        Camera camera = cameraSystem.getCamera(cameraName);
        IntBag entities = getSubscription().getEntities();
        for (int i = 0; i < entities.size(); i++) {
            Entity cameraHookEntity = world.getEntity(entities.get(i));
            CameraHookComponent cameraHook = cameraHookEntity.getComponent(CameraHookComponent.class);
            if (cameraHook.getCameraName().equals(cameraName)) {
                Matrix4 inverted = tmpMatrix4.set(camera.combined).inv();
                if (cameraHook.isCorrectingForAspect()) {
                    float aspect = camera.viewportWidth / camera.viewportHeight;
                    inverted.scale(1f / aspect, 1f, -1f);
                }
                transformSystem.setTransform(cameraHookEntity, inverted);
            }
        }
    }

    @Override
    protected void processSystem() {

    }
}
