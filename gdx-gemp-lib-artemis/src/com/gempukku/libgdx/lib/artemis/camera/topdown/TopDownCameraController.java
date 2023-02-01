package com.gempukku.libgdx.lib.artemis.camera.topdown;

import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.lib.artemis.camera.CameraController;
import com.gempukku.libgdx.lib.artemis.camera.CameraUpdated;
import com.gempukku.libgdx.lib.artemis.camera.YAxisTiltCameraController;
import com.gempukku.libgdx.lib.artemis.camera.ZoomCameraController;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;

import java.util.Arrays;

public class TopDownCameraController implements CameraController, ZoomCameraController, YAxisTiltCameraController {
    private World world;
    private EventSystem eventSystem;

    private ComponentMapper<TopDownCameraComponent> topDownCameraComponentMapper;
    private EntitySubscription cameraEntitySubscription;

    private Array<Entity> newCameraEntities = new Array<>();

    private ObjectMap<String, PerspectiveCamera> perspectiveCameras = new ObjectMap<>();
    private ObjectMap<String, Entity> cameraEntities = new ObjectMap<>();

    private Matrix4 tmpMatrix4 = new Matrix4();

    @Override
    public void setupWithWorld(final World world) {
        this.world = world;
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

            tmpMatrix4.set(perspectiveCamera.combined);

            perspectiveCamera.near = topDownCamera.getNear();
            perspectiveCamera.far = topDownCamera.getFar();
            perspectiveCamera.fieldOfView = topDownCamera.getFieldOfView();

            Vector3 position = perspectiveCamera.position;
            float oldDistance = topDownCamera.getOldDistance();

            float newDistance = MathUtils.lerp(oldDistance, topDownCamera.getDistance(), 5f * deltaTime);
            newDistance = MathUtils.clamp(newDistance,
                    Math.min(oldDistance, topDownCamera.getDistance()),
                    Math.max(oldDistance, topDownCamera.getDistance()));

            position.set(0, 0, 1);
            position.rotate(topDownCamera.getyAxisAngle(), 0, 1, 0);
            position.rotate(topDownCamera.getxAxisAngle(), 1, 0, 0);
            position.scl(newDistance);
            position.add(topDownCamera.getCenter());

            perspectiveCamera.up.set(0, 1, 0);
            perspectiveCamera.lookAt(topDownCamera.getCenter());
            perspectiveCamera.update();

            topDownCamera.setOldDistance(newDistance);

            if (!Arrays.equals(tmpMatrix4.val, perspectiveCamera.combined.val)) {
                eventSystem.fireEvent(new CameraUpdated(cameraName), cameraEntity);
            }
        }
    }

    private void processNewCameras() {
        for (Entity newCameraEntity : newCameraEntities) {
            TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(newCameraEntity);
            topDownCamera.setOldDistance(topDownCamera.getDistance());
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

    public void moveBy(String cameraName, float x, float z) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        Rectangle bounds = topDownCamera.getBounds();
        Vector3 cameraCenter = topDownCamera.getCenter();

        float resultX = Math.min(bounds.x + bounds.width, Math.max(cameraCenter.x + x, bounds.x));
        float resultY = Math.min(bounds.y + bounds.height, Math.max(cameraCenter.z + z, bounds.y));

        cameraCenter.set(resultX, cameraCenter.y, resultY);
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

    @Override
    public void moveYAxisAngle(String cameraName, float angle) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        float yAxisAngle = topDownCamera.getyAxisAngle();
        float requestedAngle = yAxisAngle + angle;

        Vector2 yAxisAngleRange = topDownCamera.getyAxisAngleRange();
        float resultAngle = Math.min(yAxisAngleRange.y, Math.max(requestedAngle, yAxisAngleRange.x));

        topDownCamera.setyAxisAngle(resultAngle);
    }

    @Override
    public void setYAxisAngle(String cameraName, float angle) {
        Entity cameraEntity = getCameraEntity(cameraName);

        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        Vector2 yAxisAngleRange = topDownCamera.getyAxisAngleRange();
        float resultAngle = Math.min(yAxisAngleRange.y, Math.max(angle, yAxisAngleRange.x));

        topDownCamera.setyAxisAngle(resultAngle);
    }
}
