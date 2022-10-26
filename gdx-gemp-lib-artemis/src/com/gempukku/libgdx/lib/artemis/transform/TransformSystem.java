package com.gempukku.libgdx.lib.artemis.transform;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Matrix4;
import com.gempukku.libgdx.lib.artemis.event.EventListener;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;
import com.gempukku.libgdx.lib.artemis.hierarchy.ChildAdded;
import com.gempukku.libgdx.lib.artemis.hierarchy.ChildRemoved;
import com.gempukku.libgdx.lib.artemis.hierarchy.ChildUpdated;
import com.gempukku.libgdx.lib.artemis.hierarchy.HierarchySystem;

public class TransformSystem extends BaseEntitySystem {
    private static final TransformUpdated updated = new TransformUpdated();

    private static final Matrix4 identityTransform = new Matrix4();

    @Wire(failOnNull = false)
    private HierarchySystem hierarchySystem;
    private EventSystem eventSystem;
    private ComponentMapper<TransformComponent> transformMapper;

    public TransformSystem() {
        super(Aspect.all(TransformComponent.class));
    }

    @Override
    protected void inserted(int entityId) {
        Entity entity = world.getEntity(entityId);
        setTransform(entity, transformMapper.get(entity).getLocalTransform());
    }

    @Override
    protected void removed(int entityId) {
        if (hierarchySystem != null) {
            Entity entity = world.getEntity(entityId);
            Matrix4 ancestorTransform = findFirstAncestorGlobalTransform(entity);
            updateAllChildrenOf(entity, ancestorTransform);
        }
    }

    @EventListener
    public void childAdded(ChildAdded childAdded, Entity entity) {
        TransformComponent transformComponent = transformMapper.get(entity);
        updateNodeAndDescendantsTransform(entity, transformComponent);
    }

    @EventListener
    public void childUpdated(ChildUpdated childUpdated, Entity entity) {
        TransformComponent transformComponent = transformMapper.get(entity);
        updateNodeAndDescendantsTransform(entity, transformComponent);
    }

    @EventListener
    public void childRemoved(ChildRemoved childRemoved, Entity entity) {
        TransformComponent transformComponent = transformMapper.get(entity);
        updateNodeAndDescendantsTransform(entity, transformComponent);
    }

    public void setTransform(Entity entity, Matrix4 localTransform) {
        TransformComponent transformComponent = transformMapper.get(entity);
        Matrix4 transform = transformComponent.getLocalTransform();
        transform.set(localTransform);

        updateNodeAndDescendantsTransform(entity, transformComponent);
    }

    public void setEffectiveTransform(Entity entity, Matrix4 effectiveTransform) {
        TransformComponent transformComponent = transformMapper.get(entity);
        Matrix4 transform = transformComponent.getLocalTransform();
        Matrix4 firstAncestorGlobalTransform = findFirstAncestorGlobalTransform(entity);
        transform.set(firstAncestorGlobalTransform).inv().mul(effectiveTransform);

        updateNodeAndDescendantsTransform(entity, transformComponent);
    }

    public void moveBy(Entity entity, float x, float y, float z) {
        TransformComponent transformComponent = transformMapper.get(entity);
        Matrix4 transform = transformComponent.getLocalTransform();
        transform.trn(x, y, z);

        updateNodeAndDescendantsTransform(entity, transformComponent);
    }

    public void moveTo(Entity entity, float x, float y, float z) {
        TransformComponent transformComponent = transformMapper.get(entity);
        Matrix4 transform = transformComponent.getLocalTransform();
        transform.setTranslation(x, y, z);

        updateNodeAndDescendantsTransform(entity, transformComponent);
    }

    public Matrix4 getResolvedTransform(Entity entity) {
        TransformComponent transformComponent = transformMapper.get(entity);
        if (transformComponent != null) {
            return transformComponent.getGlobalTransform();
        } else {
            return findFirstAncestorGlobalTransform(entity);
        }
    }

    private void updateNodeAndDescendantsTransform(Entity entity, TransformComponent transform) {
        if (hierarchySystem != null) {
            Matrix4 ancestorTransform = findFirstAncestorGlobalTransform(entity);
            if (transform != null) {
                calculateChildGlobalTransform(transform.getGlobalTransform(), transform.getLocalTransform(), ancestorTransform);
                eventSystem.fireEvent(updated, entity);
                updateAllChildrenOf(entity, transform.getGlobalTransform());
            } else {
                eventSystem.fireEvent(updated, entity);
                updateAllChildrenOf(entity, ancestorTransform);
            }
        } else {
            if (transform != null) {
                transform.getGlobalTransform().set(transform.getLocalTransform());
                eventSystem.fireEvent(updated, entity);
            }
        }
    }

    private Matrix4 calculateChildGlobalTransform(Matrix4 childGlobalTransform, Matrix4 childLocalTransform, Matrix4 ancestorGlobalTransform) {
        return childGlobalTransform.set(childLocalTransform).mulLeft(ancestorGlobalTransform);
    }

    private void updateAllChildrenOf(Entity entity, Matrix4 currentGlobalTransform) {
        for (Entity child : hierarchySystem.getChildren(entity)) {
            TransformComponent childTransform = transformMapper.get(child);
            if (childTransform != null) {
                calculateChildGlobalTransform(childTransform.getGlobalTransform(), childTransform.getLocalTransform(), currentGlobalTransform);
                eventSystem.fireEvent(updated, child);
                updateAllChildrenOf(child, childTransform.getGlobalTransform());
            } else {
                eventSystem.fireEvent(updated, child);
                updateAllChildrenOf(child, currentGlobalTransform);
            }
        }
    }

    private Matrix4 findFirstAncestorGlobalTransform(Entity entity) {
        if (hierarchySystem != null) {
            Entity parent = hierarchySystem.findFirstAncestorWithComponent(entity, TransformComponent.class);
            if (parent != null)
                return transformMapper.get(parent).getGlobalTransform();
        }
        return identityTransform;
    }

    @Override
    protected void processSystem() {

    }
}
