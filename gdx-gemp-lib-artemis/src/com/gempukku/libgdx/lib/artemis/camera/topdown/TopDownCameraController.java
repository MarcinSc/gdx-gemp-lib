package com.gempukku.libgdx.lib.artemis.camera.topdown;

import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.lib.artemis.camera.CameraController;
import com.gempukku.libgdx.lib.artemis.camera.CameraUpdated;
import com.gempukku.libgdx.lib.artemis.camera.ZoomCameraController;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;

import java.util.Arrays;

public class TopDownCameraController implements CameraController, ZoomCameraController {
    private EventSystem eventSystem;

    private ComponentMapper<TopDownCameraComponent> topDownCameraComponentMapper;
    private EntitySubscription cameraEntitySubscription;

    private Array<Entity> newCameraEntities = new Array<>();

    private ObjectMap<String, PerspectiveCamera> perspectiveCameras = new ObjectMap<>();
    private ObjectMap<String, Entity> cameraEntities = new ObjectMap<>();

    private Matrix4 oldCombined = new Matrix4();
    private Matrix4 tmpMatrix4 = new Matrix4();

    @Override
    public void setupWithWorld(final World world) {
        eventSystem = world.getSystem(EventSystem.class);
        topDownCameraComponentMapper = world.getMapper(TopDownCameraComponent.class);
        cameraEntitySubscription = world.getAspectSubscriptionManager().get(Aspect.all(TopDownCameraComponent.class));
        cameraEntitySubscription.
                addSubscriptionListener(
                        new EntitySubscription.SubscriptionListener() {
                            @Override
                            public void inserted(IntBag entities) {
                                for (int i = 0; i < entities.size(); i++) {
                                    newCameraEntities.add(world.getEntity(entities.get(i)));
                                }
                            }

                            @Override
                            public void removed(IntBag entities) {
                                for (int i = 0; i < entities.size(); i++) {
                                    cameraRemoved(world.getEntity(entities.get(i)));
                                }
                            }
                        });
    }

    private void cameraRemoved(Entity cameraEntity) {
        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);
        cameraEntities.remove(topDownCamera.getName());
        perspectiveCameras.remove(topDownCamera.getName());
    }

    @Override
    public void update(float deltaTime) {
        processNewCameras();

        for (ObjectMap.Entry<String, Entity> cameraEntityEntry : cameraEntities) {
            String cameraName = cameraEntityEntry.key;
            Entity cameraEntity = cameraEntityEntry.value;

            TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

            PerspectiveCamera perspectiveCamera = perspectiveCameras.get(cameraName);

            oldCombined.set(perspectiveCamera.combined);

            perspectiveCamera.near = topDownCamera.getNear();
            perspectiveCamera.far = topDownCamera.getFar();
            perspectiveCamera.fieldOfView = topDownCamera.getFieldOfView();

            tmpMatrix4.idt()
                    .rotate(0, 0, 1, topDownCamera.getRotation())
                    .rotate(1, 0, 0, topDownCamera.getAngle())
                    .rotate(0, 1, 0, topDownCamera.getTilt());

            perspectiveCamera.position.set(0, 1, 0)
                    .mul(tmpMatrix4)
                    .nor()
                    .scl(topDownCamera.getDistance())
                    .add(topDownCamera.getFocus());

            perspectiveCamera.up.set(0, 0, 1).mul(tmpMatrix4);
            perspectiveCamera.lookAt(topDownCamera.getFocus());
            perspectiveCamera.update();

            if (!Arrays.equals(oldCombined.val, perspectiveCamera.combined.val)) {
                eventSystem.fireEvent(new CameraUpdated(cameraName), cameraEntity);
            }
        }
    }

    private void processNewCameras() {
        for (Entity newCameraEntity : newCameraEntities) {
            TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(newCameraEntity);
            cameraEntities.put(topDownCamera.getName(), newCameraEntity);
            perspectiveCameras.put(topDownCamera.getName(), new PerspectiveCamera(topDownCamera.getFieldOfView(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        }
        newCameraEntities.clear();
    }

    @Override
    public PerspectiveCamera getCamera(String cameraName) {
        return perspectiveCameras.get(cameraName);
    }

    @Override
    public Entity getCameraEntity(String cameraName) {
        return cameraEntities.get(cameraName);
    }

    @Override
    public void screenResized(int width, int height) {
        for (PerspectiveCamera perspectiveCamera : perspectiveCameras.values()) {
            perspectiveCamera.viewportWidth = width;
            perspectiveCamera.viewportHeight = height;
        }
    }

    public void moveTo(String cameraName, float x, float y, float z) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        Rectangle bounds = topDownCamera.getBounds();
        Vector3 focus = topDownCamera.getFocus();

        if (bounds != null) {
            float resultX = Math.min(bounds.x + bounds.width, Math.max(focus.x + x, bounds.x));
            float resultY = Math.min(bounds.y + bounds.height, Math.max(focus.y + y, bounds.y));

            focus.set(resultX, resultY, focus.z + z);
        } else {
            focus.set(focus.x + x, focus.y + y, focus.z + z);
        }
    }

    public void moveBy(String cameraName, float x, float y, float z) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        Rectangle bounds = topDownCamera.getBounds();

        if (bounds != null) {
            float resultX = Math.min(bounds.x + bounds.width, Math.max(x, bounds.x));
            float resultY = Math.min(bounds.y + bounds.height, Math.max(y, bounds.y));

            topDownCamera.getFocus().set(resultX, resultY, z);
        } else {
            topDownCamera.getFocus().set(x, y, z);
        }
    }

    @Override
    public void zoom(String cameraName, float distance) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        Vector2 distanceRange = topDownCamera.getDistanceRange();
        float cameraDistance = topDownCamera.getDistance();

        float resultDistance = Math.min(distanceRange.y, Math.max(cameraDistance + distance, distanceRange.x));

        topDownCamera.setDistance(resultDistance);
    }

    public void rotateBy(String cameraName, float angle) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        float rotationAngle = topDownCamera.getRotation();
        float requestedAngle = rotationAngle + angle;

        Vector2 rotationRange = topDownCamera.getRotationRange();
        float resultAngle = Math.min(rotationRange.y, Math.max(requestedAngle, rotationRange.x));

        topDownCamera.setRotation(resultAngle);
    }

    public void setRotation(String cameraName, float angle) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        Vector2 yAxisAngleRange = topDownCamera.getRotationRange();
        float resultAngle = Math.min(yAxisAngleRange.y, Math.max(angle, yAxisAngleRange.x));

        topDownCamera.setRotation(resultAngle);
    }

    public void angleBy(String cameraName, float angle) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        float angleValue = topDownCamera.getAngle();
        float requestedAngle = angleValue + angle;

        Vector2 angleRange = topDownCamera.getAngleRange();
        float resultAngle = Math.min(angleRange.y, Math.max(requestedAngle, angleRange.x));

        topDownCamera.setAngle(resultAngle);
    }

    public void setAngle(String cameraName, float angle) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        Vector2 angleRange = topDownCamera.getAngleRange();
        float resultAngle = Math.min(angleRange.y, Math.max(angle, angleRange.x));

        topDownCamera.setAngle(resultAngle);
    }

    public void tiltBy(String cameraName, float tilt) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        float tiltValue = topDownCamera.getTilt();
        float requestedAngle = tiltValue + tilt;

        Vector2 tiltRange = topDownCamera.getTiltRange();
        float resultAngle = Math.min(tiltRange.y, Math.max(requestedAngle, tiltRange.x));

        topDownCamera.setTilt(resultAngle);
    }

    public void setTilt(String cameraName, float tilt) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        Vector2 tiltRange = topDownCamera.getTiltRange();
        float resultAngle = Math.min(tiltRange.y, Math.max(tilt, tiltRange.x));

        topDownCamera.setTilt(resultAngle);
    }
}