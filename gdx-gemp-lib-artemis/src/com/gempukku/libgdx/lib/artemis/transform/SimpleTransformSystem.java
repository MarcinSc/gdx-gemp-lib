package com.gempukku.libgdx.lib.artemis.transform;

import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class SimpleTransformSystem extends BaseEntitySystem {
    private TransformSystem transformSystem;
    private ComponentMapper<SimpleTransformComponent> simpleTransformMapper;

    private final Pool<Matrix4> matrix4Pool = Pools.get(Matrix4.class);

    @Override
    protected void inserted(int entityId) {
        SimpleTransformComponent simpleTransform = simpleTransformMapper.get(entityId);
        Entity entity = world.getEntity(entityId);

        updateEntity(simpleTransform, entity);
    }

    private void updateEntity(SimpleTransformComponent simpleTransform, Entity entity) {
        Matrix4 transform = matrix4Pool.obtain();
        try {
            transform.setToScaling(simpleTransform.getScale());
            transform.setTranslation(simpleTransform.getTranslate());
            Vector3 rotate = simpleTransform.getRotate();
            transform.setFromEulerAnglesRad(rotate.x, rotate.y, rotate.z);
            transformSystem.setTransform(entity, transform);
        } finally {
            matrix4Pool.free(transform);
        }
    }

    public Vector3 getTranslate(Entity entity) {
        SimpleTransformComponent simpleTransform = simpleTransformMapper.get(entity);
        return simpleTransform.getTranslate();
    }

    public Vector3 getRotation(Entity entity) {
        SimpleTransformComponent simpleTransform = simpleTransformMapper.get(entity);
        return simpleTransform.getRotate();
    }

    public Vector3 getScale(Entity entity) {
        SimpleTransformComponent simpleTransform = simpleTransformMapper.get(entity);
        return simpleTransform.getScale();
    }

    public void setTranslate(Entity entity, float x, float y, float z) {
        SimpleTransformComponent simpleTransform = simpleTransformMapper.get(entity);
        simpleTransform.getTranslate().set(x, y, z);
        updateEntity(simpleTransform, entity);
    }

    public void setRotation(Entity entity, float yaw, float pitch, float roll) {
        SimpleTransformComponent simpleTransform = simpleTransformMapper.get(entity);
        simpleTransform.getRotate().set(yaw, pitch, roll);
        updateEntity(simpleTransform, entity);
    }

    public void setScale(Entity entity, float x, float y, float z) {
        SimpleTransformComponent simpleTransform = simpleTransformMapper.get(entity);
        simpleTransform.getScale().set(x, y, z);
        updateEntity(simpleTransform, entity);
    }

    public void setAll(Entity entity,
                       float translateX, float translateY, float translateZ,
                       float yaw, float pitch, float roll,
                       float scaleX, float scaleY, float scaleZ) {
        SimpleTransformComponent simpleTransform = simpleTransformMapper.get(entity);
        simpleTransform.getTranslate().set(translateX, translateY, translateZ);
        simpleTransform.getRotate().set(yaw, pitch, roll);
        simpleTransform.getScale().set(scaleX, scaleY, scaleZ);
        updateEntity(simpleTransform, entity);
    }

    @Override
    protected void processSystem() {

    }
}
