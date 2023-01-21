package com.gempukku.libgdx.lib.artemis.hierarchy;

import com.artemis.*;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.lib.artemis.event.EventSystem;

import java.util.Collections;

public class HierarchySystem extends BaseEntitySystem {
    private IntMap<ObjectSet<Entity>> childMap = new IntMap<>();

    private ComponentMapper<HierarchyComponent> hierarchyMapper;

    private EventSystem eventSystem;

    public HierarchySystem() {
        super(Aspect.all(HierarchyComponent.class));
    }

    @Override
    protected void initialize() {
        world.getAspectSubscriptionManager().get(Aspect.all()).addSubscriptionListener(
                new EntitySubscription.SubscriptionListener() {
                    @Override
                    public void inserted(IntBag entities) {

                    }

                    @Override
                    public void removed(IntBag entities) {
                        for (int i = 0, s = entities.size(); s > i; i++) {
                            entityRemovedFromWorld(entities.get(i));
                        }
                    }
                }
        );
    }

    private void entityRemovedFromWorld(int entityId) {
        ObjectSet<Entity> children = childMap.get(entityId);
        if (children != null) {
            for (Entity entity : children) {
                world.deleteEntity(entity);
            }
            childMap.remove(entityId);
        }
    }

    @Override
    protected void inserted(int entityId) {
        Entity child = world.getEntity(entityId);
        HierarchyComponent hierarchy = hierarchyMapper.get(entityId);
        int parentId = hierarchy.getParentId();
        ObjectSet<Entity> children = childMap.get(parentId);
        if (children == null || !children.contains(child)) {
            addHierarchyToMap(parentId, child);

            eventSystem.fireEvent(new ChildAdded(parentId), child);
        }
    }

    @Override
    protected void removed(int entityId) {
        Entity child = world.getEntity(entityId);
        HierarchyComponent hierarchy = hierarchyMapper.get(entityId);
        int parentId = hierarchy.getParentId();
        ObjectSet<Entity> children = childMap.get(parentId);
        if (children != null && children.contains(child)) {
            removeHierarchyFromMap(parentId, child);

            eventSystem.fireEvent(new ChildRemoved(parentId), child);
        }
    }

    public void addHierarchy(Entity parent, Entity child) {
        if (hierarchyMapper.has(child)) {
            updateHierarchy(parent, child);
        } else {
            int parentId = parent.getId();

            HierarchyComponent hierarchyComponent = hierarchyMapper.create(child);
            hierarchyComponent.setParentId(parentId);

            addHierarchyToMap(parentId, child);

            eventSystem.fireEvent(new ChildAdded(parentId), child);
        }
    }

    public void updateHierarchy(Entity parent, Entity child) {
        HierarchyComponent hierarchy = hierarchyMapper.get(child);
        int oldParentId = hierarchy.getParentId();
        int newParentId = parent.getId();

        if (oldParentId != newParentId) {
            hierarchy.setParentId(newParentId);

            removeHierarchyFromMap(oldParentId, child);
            addHierarchyToMap(newParentId, child);

            eventSystem.fireEvent(new ChildUpdated(oldParentId, newParentId), child);
        }
    }

    public void removeFromHierarchy(Entity child) {
        if (hierarchyMapper.has(child)) {
            HierarchyComponent hierarchy = hierarchyMapper.get(child);
            int parentId = hierarchy.getParentId();
            hierarchyMapper.remove(child);

            removeHierarchyFromMap(parentId, child);

            eventSystem.fireEvent(new ChildRemoved(parentId), child);
        }
    }

    public void removeHierarchy(Entity child) {
        HierarchyComponent hierarchy = hierarchyMapper.get(child);
        int parentId = hierarchy.getParentId();
        hierarchyMapper.remove(child);

        removeHierarchyFromMap(parentId, child);

        eventSystem.fireEvent(new ChildRemoved(parentId), child);
    }

    private void addHierarchyToMap(int parentId, Entity child) {
        ObjectSet<Entity> children = childMap.get(parentId);
        if (children == null) {
            children = new ObjectSet<>();
            childMap.put(parentId, children);
        }
        children.add(child);
    }

    private void removeHierarchyFromMap(int parentId, Entity child) {
        ObjectSet<Entity> children = childMap.get(parentId);
        children.remove(child);
        if (children.isEmpty())
            childMap.remove(parentId);
    }

    public Iterable<Entity> getChildren(Entity parent) {
        ObjectSet<Entity> children = childMap.get(parent.getId());
        if (children == null)
            return Collections.emptySet();
        return children;
    }

    public Entity getParent(Entity child) {
        if (hierarchyMapper.has(child)) {
            HierarchyComponent hierarchy = hierarchyMapper.get(child);
            return world.getEntity(hierarchy.getParentId());
        }
        return null;
    }

    public Entity findFirstAncestorWithComponent(Entity child, Class<? extends Component> componentClass) {
        Entity parent = getParent(child);
        while (parent != null) {
            if (parent.getComponent(componentClass) != null)
                return parent;
            parent = getParent(child);
        }
        return null;
    }

    public Entity findInHierarchyEntityWithComponent(Entity entity, Class<? extends Component> componentClass) {
        while (entity != null) {
            if (entity.getComponent(componentClass) != null)
                return entity;
            entity = getParent(entity);
        }
        return null;
    }

    public Array<Entity> findDescendantsWithComponent(Entity entity, Array<Entity> result, Class<? extends Component> componentClass) {
        for (Entity child : getChildren(entity)) {
            if (child.getComponent(componentClass) != null)
                result.add(child);
            findDescendantsWithComponent(child, result, componentClass);
        }

        return result;
    }

    @Override
    protected void processSystem() {

    }
}
