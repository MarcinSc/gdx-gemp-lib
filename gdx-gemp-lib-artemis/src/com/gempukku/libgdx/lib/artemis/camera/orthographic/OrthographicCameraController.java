package com.gempukku.libgdx.lib.artemis.camera.orthographic;

import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.lib.artemis.camera.CameraController;
import com.gempukku.libgdx.lib.artemis.camera.CameraUpdated;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;

import java.util.Arrays;

public class OrthographicCameraController implements CameraController {
    private World world;
    private EventSystem eventSystem;

    private ComponentMapper<OrthographicCameraComponent> orthographicCameraComponentMapper;
    private EntitySubscription cameraEntitySubscription;

    private Array<Entity> newCameraEntities = new Array<>();

    private ObjectMap<String, OrthographicCamera> orthographicCameras = new ObjectMap<>();
    private ObjectMap<String, Entity> cameraEntities = new ObjectMap<>();

    private Matrix4 tmpMatrix4 = new Matrix4();

    @Override
    public void setupWithWorld(final World world) {
        this.world = world;
        eventSystem = world.getSystem(EventSystem.class);
        orthographicCameraComponentMapper = world.getMapper(OrthographicCameraComponent.class);
        cameraEntitySubscription = world.getAspectSubscriptionManager().get(Aspect.all(OrthographicCameraComponent.class));
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
        OrthographicCameraComponent orthographicCamera = orthographicCameraComponentMapper.get(cameraEntity);
        cameraEntities.remove(orthographicCamera.getName());
        orthographicCameras.remove(orthographicCamera.getName());
    }

    @Override
    public void update(float deltaTime) {
        processNewCameras();

        for (ObjectMap.Entry<String, Entity> cameraEntityEntry : cameraEntities) {
            String cameraName = cameraEntityEntry.key;
            Entity cameraEntity = cameraEntityEntry.value;

            OrthographicCameraComponent orthographicCamera = orthographicCameraComponentMapper.get(cameraEntity);

            OrthographicCamera camera = orthographicCameras.get(cameraName);

            float aspectRatio = 1f * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();

            float viewportWidth = aspectRatio * orthographicCamera.getHeight();
            float viewportHeight = orthographicCamera.getHeight();

            tmpMatrix4.set(camera.combined);

            camera.near = orthographicCamera.getNear();
            camera.far = orthographicCamera.getFar();

            camera.position.set(orthographicCamera.getPosition());
            camera.up.set(orthographicCamera.getUp());
            camera.direction.set(orthographicCamera.getDirection());

            camera.viewportWidth = viewportWidth;
            camera.viewportHeight = viewportHeight;
            camera.update(true);

            if (!Arrays.equals(tmpMatrix4.val, camera.combined.val)) {
                eventSystem.fireEvent(new CameraUpdated(cameraName), cameraEntity);
            }
        }
    }

    private void processNewCameras() {
        for (Entity newCameraEntity : newCameraEntities) {
            OrthographicCameraComponent orthographicCamera = orthographicCameraComponentMapper.get(newCameraEntity);
            cameraEntities.put(orthographicCamera.getName(), newCameraEntity);

            OrthographicCamera camera = new OrthographicCamera();

            orthographicCameras.put(orthographicCamera.getName(), camera);
        }
        newCameraEntities.clear();
    }

    public void moveTo(String cameraName, float x, float y) {
        OrthographicCameraComponent camera = cameraEntities.get(cameraName).getComponent(OrthographicCameraComponent.class);
        Vector3 cameraPosition = camera.getPosition();
        cameraPosition.set(x, y, cameraPosition.z);
    }

    public void moveBy(String cameraName, float x, float y) {
        OrthographicCameraComponent camera = cameraEntities.get(cameraName).getComponent(OrthographicCameraComponent.class);
        Vector3 cameraPosition = camera.getPosition();
        cameraPosition.add(x, y, 0);
    }

    public void resizeTo(String cameraName, float width, float height) {
        OrthographicCameraComponent camera = cameraEntities.get(cameraName).getComponent(OrthographicCameraComponent.class);
        camera.setHeight(height);
    }

    @Override
    public OrthographicCamera getCamera(String cameraName) {
        return orthographicCameras.get(cameraName);
    }

    @Override
    public Entity getCameraEntity(String cameraName) {
        return cameraEntities.get(cameraName);
    }

    @Override
    public void screenResized(int width, int height) {
    }
}
