package com.gempukku.libgdx.lib.artemis.camera;

import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TopDownCameraController implements CameraController, ZoomCameraController, ZAxisTiltCameraController {
    private final PerspectiveCamera perspectiveCamera = new PerspectiveCamera();
    private Entity cameraEntity;
    private float oldDistance;
    private ComponentMapper<TopDownCameraComponent> topDownCameraComponentMapper;

    @Override
    public void setupWithWorld(final World world) {
        topDownCameraComponentMapper = world.getMapper(TopDownCameraComponent.class);
        world.getAspectSubscriptionManager().get(Aspect.all(TopDownCameraComponent.class)).
                addSubscriptionListener(
                        new EntitySubscription.SubscriptionListener() {
                            @Override
                            public void inserted(IntBag entities) {
                                cameraEntity = world.getEntity(entities.get(0));
                                oldDistance = topDownCameraComponentMapper.get(cameraEntity).getDistance();
                            }

                            @Override
                            public void removed(IntBag entities) {

                            }
                        });
    }

    @Override
    public void update(float deltaTime) {
        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        perspectiveCamera.near = topDownCamera.getNear();
        perspectiveCamera.far = topDownCamera.getFar();
        perspectiveCamera.fieldOfView = topDownCamera.getFieldOfView();

        Vector3 position = perspectiveCamera.position;

        float newDistance = MathUtils.lerp(oldDistance, topDownCamera.getDistance(), 5f * deltaTime);
        newDistance = MathUtils.clamp(newDistance,
                Math.min(oldDistance, topDownCamera.getDistance()),
                Math.max(oldDistance, topDownCamera.getDistance()));

        position.set(0, 1, 0);
        position.rotate(topDownCamera.getzAxisAngle(), 0, 0, 1);
        position.rotate(topDownCamera.getyAxisAngle(), 0, 1, 0);
        position.scl(newDistance);
        position.add(topDownCamera.getCenter());

        perspectiveCamera.up.set(0, 0, -1);
        perspectiveCamera.lookAt(topDownCamera.getCenter());
        perspectiveCamera.update();

        oldDistance = newDistance;
    }

    @Override
    public Camera getCamera() {
        return perspectiveCamera;
    }

    @Override
    public Entity getCameraEntity() {
        return cameraEntity;
    }

    @Override
    public void screenResized(int width, int height) {
        perspectiveCamera.viewportWidth = width;
        perspectiveCamera.viewportHeight = height;
    }

    public void moveBy(float x, float z) {
        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        Rectangle bounds = topDownCamera.getBounds();
        Vector3 cameraCenter = topDownCamera.getCenter();

        float resultX = Math.min(bounds.x + bounds.width, Math.max(cameraCenter.x + x, bounds.x));
        float resultY = Math.min(bounds.y + bounds.height, Math.max(cameraCenter.z + z, bounds.y));

        cameraCenter.set(resultX, cameraCenter.y, resultY);
    }

    @Override
    public void zoom(float distance) {
        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        Vector2 distanceRange = topDownCamera.getDistanceRange();
        float cameraDistance = topDownCamera.getDistance();

        float resultDistance = Math.min(distanceRange.y, Math.max(cameraDistance + distance, distanceRange.x));

        topDownCamera.setDistance(resultDistance);
    }

    @Override
    public void moveZAxisAngle(float angle) {
        TopDownCameraComponent topDownCamera = topDownCameraComponentMapper.get(cameraEntity);

        Vector2 zAxisAngleRange = topDownCamera.getzAxisAngleRange();
        float zAxisAngle = topDownCamera.getzAxisAngle();

        float resultAngle = Math.min(zAxisAngleRange.y, Math.max(zAxisAngle + angle, zAxisAngleRange.x));

        topDownCamera.setzAxisAngle(resultAngle);
    }
}
