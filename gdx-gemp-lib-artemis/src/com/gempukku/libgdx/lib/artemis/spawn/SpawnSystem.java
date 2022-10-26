package com.gempukku.libgdx.lib.artemis.spawn;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.gempukku.libgdx.artemis.template.ArtemisGameStateSerializer;
import com.gempukku.libgdx.artemis.template.ArtemisTemplateEntityLoader;
import com.gempukku.libgdx.lib.artemis.hierarchy.HierarchyArtemisLoader;
import com.gempukku.libgdx.lib.artemis.hierarchy.HierarchySystem;

public class SpawnSystem extends BaseSystem {
    private final InternalFileHandleResolver internalResolver = new InternalFileHandleResolver();

    @Wire(failOnNull = false)
    private HierarchySystem hierarchySystem;

    public void spawnEntities(String internalFilePath) {
        spawnEntities(internalFilePath, internalResolver);
    }

    public void spawnEntities(String filePath, FileHandleResolver resolver) {
        if (filePath.endsWith(".entities")) {
            ArtemisGameStateSerializer.loadIntoEngine(world, filePath, resolver);
        } else if (filePath.endsWith(".hierarchy")) {
            HierarchyArtemisLoader.loadHierarchicalEntityToEngine(world, hierarchySystem, filePath, resolver);
        } else if (filePath.endsWith(".template")) {
            ArtemisTemplateEntityLoader.loadTemplateToWorld(world, filePath, resolver);
        }
    }

    public Entity spawnEntity(String internalFilePath) {
        return spawnEntity(internalFilePath, internalResolver);
    }

    public Entity spawnEntity(String filePath, FileHandleResolver resolver) {
        if (filePath.endsWith(".template")) {
            return ArtemisTemplateEntityLoader.loadTemplateToWorld(world, filePath, resolver);
        } else if (filePath.endsWith(".hierarchy")) {
            return HierarchyArtemisLoader.loadHierarchicalEntityToEngine(world, hierarchySystem, filePath, resolver);
        }
        return null;
    }

    @Override
    protected void processSystem() {

    }
}
