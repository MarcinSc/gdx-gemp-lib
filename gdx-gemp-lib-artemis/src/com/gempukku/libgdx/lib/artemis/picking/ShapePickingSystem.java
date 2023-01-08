package com.gempukku.libgdx.lib.artemis.picking;

import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Predicate;
import com.gempukku.libgdx.lib.artemis.shape.ShapeDataDefinition;
import com.gempukku.libgdx.lib.artemis.shape.ShapeSystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;

public class ShapePickingSystem extends BaseSystem {
    private final Vector3 tmpVector1 = new Vector3();
    private final Vector3 tmpVector2 = new Vector3();
    private final Vector3 tmpVector3 = new Vector3();
    private final Vector3 tmpVectorIntersection = new Vector3();

    private final Matrix4 tmpMatrix4 = new Matrix4();

    private ComponentMapper<ShapePickableComponent> shapePickableComponentMapper;
    private TransformSystem transformSystem;
    private ShapeSystem shapeSystem;
    private EntitySubscription pickableSubscription;

    @Override
    protected void initialize() {
        pickableSubscription = world.getAspectSubscriptionManager().get(Aspect.all(ShapePickableComponent.class));
    }

    public Entity pickEntity(Ray ray, String maskType, Predicate<Entity> entityPredicate) {
        Entity closestEntity = null;
        float shortestDistance = Float.MAX_VALUE;

        IntBag entities = pickableSubscription.getEntities();
        for (int i = 0, s = entities.size(); s > i; i++) {
            int entityId = entities.get(i);
            Entity pickableEntity = world.getEntity(entityId);
            if (entityPredicate.evaluate(pickableEntity)) {
                ShapePickableComponent pickable = shapePickableComponentMapper.get(pickableEntity);
                if (pickable.getPickingMask().contains(maskType, false)) {
                    float distanceToPickable = getPickableIntersectionDistance2(shapeSystem, ray, pickable, pickableEntity);
                    if (distanceToPickable < shortestDistance) {
                        shortestDistance = distanceToPickable;
                        closestEntity = pickableEntity;
                    }
                }
            }
        }

        return closestEntity;
    }

    public Entity findClosest(Vector3 position, String maskType, Predicate<Entity> entityPredicate) {
        Entity closestEntity = null;
        float shortestDistance = Float.MAX_VALUE;

        IntBag entities = pickableSubscription.getEntities();
        for (int i = 0, s = entities.size(); s > i; i++) {
            int entityId = entities.get(i);
            Entity pickableEntity = world.getEntity(entityId);
            if (entityPredicate.evaluate(pickableEntity)) {
                ShapePickableComponent pickable = shapePickableComponentMapper.get(pickableEntity);
                if (pickable.getPickingMask().contains(maskType, false)) {
                    float distanceToPickable = getPickableDistance2(position, pickableEntity);
                    if (distanceToPickable < shortestDistance) {
                        shortestDistance = distanceToPickable;
                        closestEntity = pickableEntity;
                    }
                }
            }
        }

        return closestEntity;
    }

    private float getPickableDistance2(Vector3 position, Entity pickableEntity) {
        Matrix4 transform = transformSystem.getResolvedTransform(pickableEntity);
        Vector3 pickablePosition = transform.getTranslation(tmpVector1);
        return position.dst2(pickablePosition);
    }

    private float getPickableIntersectionDistance2(ShapeSystem shapeSystem, Ray ray, ShapePickableComponent pickable, Entity pickableEntity) {
        String shapeName = pickable.getShape();
        Matrix4 localTransform = pickable.getTransform();
        String positionDataType = pickable.getPositionDataType();

        ShapeDataDefinition positionData = shapeSystem.getShapeVertexData(shapeName, positionDataType);
        Matrix4 transform = transformSystem.getResolvedTransform(pickableEntity);

        short[] indices = shapeSystem.getIndices(shapeName);
        float[] data = positionData.getData();

        float shortestDistance = Float.MAX_VALUE;

        Matrix4 resultTransform = tmpMatrix4.set(transform).mul(localTransform);

        for (int i = 0; i < indices.length; i += 3) {
            setVectorFromData(indices[i + 0], data, tmpVector1);
            setVectorFromData(indices[i + 1], data, tmpVector2);
            setVectorFromData(indices[i + 2], data, tmpVector3);

            tmpVector1.prj(resultTransform);
            tmpVector2.prj(resultTransform);
            tmpVector3.prj(resultTransform);

            if (Intersector.intersectRayTriangle(ray, tmpVector1, tmpVector2, tmpVector3, tmpVectorIntersection)) {
                float distance = ray.origin.dst2(tmpVectorIntersection);
                shortestDistance = Math.min(distance, shortestDistance);
            }
        }
        return shortestDistance;
    }

    private void setVectorFromData(short index, float[] data, Vector3 vector) {
        vector.set(data[index * 3 + 0], data[index * 3 + 1], data[index * 3 + 2]);
    }

    @Override
    protected void processSystem() {

    }
}
