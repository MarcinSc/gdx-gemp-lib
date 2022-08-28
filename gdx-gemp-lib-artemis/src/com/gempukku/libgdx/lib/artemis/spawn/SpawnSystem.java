package com.gempukku.libgdx.lib.artemis.spawn;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.gempukku.libgdx.artemis.template.ArtemisGameStateSerializer;
import com.gempukku.libgdx.artemis.template.ArtemisTemplateEntityLoader;
import com.gempukku.libgdx.lib.artemis.hierarchy.HierarchyArtemisLoader;
import com.gempukku.libgdx.lib.artemis.hierarchy.HierarchySystem;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;

public class SpawnSystem extends BaseSystem {
    private InternalFileHandleResolver resolver = new InternalFileHandleResolver();

    private HierarchySystem hierarchySystem;
    private TransformSystem transformSystem;

    public void spawnEntities(String internalFilePath) {
        if (internalFilePath.endsWith(".entities")) {
            ArtemisGameStateSerializer.loadIntoEngine(world, internalFilePath, resolver);
        } else if (internalFilePath.endsWith(".hierarchy")) {
            HierarchyArtemisLoader.loadHierarchicalEntityToEngine(world, hierarchySystem, internalFilePath, resolver);
        } else if (internalFilePath.endsWith(".template")) {
            ArtemisTemplateEntityLoader.loadTemplateToWorld(world, internalFilePath, resolver);
        }
    }

    public Entity spawnEntity(String internalFilePath) {
        if (internalFilePath.endsWith(".template")) {
            return ArtemisTemplateEntityLoader.loadTemplateToWorld(world, internalFilePath, resolver);
        } else if (internalFilePath.endsWith(".hierarchy")) {
            return HierarchyArtemisLoader.loadHierarchicalEntityToEngine(world, hierarchySystem, internalFilePath, resolver);
        }
        return null;
    }

    public Entity spawnEntityAndMoveBy(String internalFilePath, float x, float y, float z) {
        if (internalFilePath.endsWith(".template")) {
            Entity entity = ArtemisTemplateEntityLoader.loadTemplateToWorld(world, internalFilePath, resolver);
            transformSystem.moveBy(entity, x, y, z);
            return entity;
        } else if (internalFilePath.endsWith(".hierarchy")) {
            Entity entity = HierarchyArtemisLoader.loadHierarchicalEntityToEngine(world, hierarchySystem, internalFilePath, resolver);
            transformSystem.moveBy(entity, x, y, z);
            return entity;
        }
        return null;
    }

    @Override
    protected void processSystem() {

    }
}
