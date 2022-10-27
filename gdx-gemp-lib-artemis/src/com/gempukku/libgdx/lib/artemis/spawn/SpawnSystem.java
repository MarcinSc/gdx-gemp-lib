package com.gempukku.libgdx.lib.artemis.spawn;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.artemis.template.ArtemisGameStateSerializer;
import com.gempukku.libgdx.artemis.template.ArtemisTemplateEntityLoader;
import com.gempukku.libgdx.lib.artemis.hierarchy.HierarchyArtemisLoader;
import com.gempukku.libgdx.lib.artemis.hierarchy.HierarchySystem;
import com.gempukku.libgdx.template.JsonTemplateLoader;

@Wire(failOnNull = false)
public class SpawnSystem extends BaseSystem {
    private final InternalFileHandleResolver internalResolver = new InternalFileHandleResolver();
    private final ObjectMap<String, JsonValue> cachedJsons = new ObjectMap<>();

    @Wire(failOnNull = false)
    private HierarchySystem hierarchySystem;

    public void spawnEntities(String internalFilePath) {
        spawnEntities(internalFilePath, internalResolver);
    }

    public void spawnEntities(String filePath, FileHandleResolver resolver) {
        if (Gdx.app.getLogLevel() >= Application.LOG_DEBUG)
            Gdx.app.debug("SpawnSystem", "Spawning: " + filePath);
        if (filePath.endsWith(".entities")) {
            ArtemisGameStateSerializer.loadIntoEngine(world, loadJson(filePath, resolver));
        } else if (filePath.endsWith(".hierarchy")) {
            HierarchyArtemisLoader.loadHierarchicalEntityToEngine(world, hierarchySystem, loadJson(filePath, resolver));
        } else if (filePath.endsWith(".template")) {
            ArtemisTemplateEntityLoader.loadTemplateToWorld(world, loadJson(filePath, resolver));
        } else {
            throw new GdxRuntimeException("Unable to resolve: " + filePath);
        }
    }

    public Entity spawnEntity(String internalFilePath) {
        return spawnEntity(internalFilePath, internalResolver);
    }

    public Entity spawnEntity(String filePath, FileHandleResolver resolver) {
        if (Gdx.app.getLogLevel() >= Application.LOG_DEBUG)
            Gdx.app.debug("SpawnSystem", "Spawning: " + filePath);
        if (filePath.endsWith(".template")) {
            return ArtemisTemplateEntityLoader.loadTemplateToWorld(world, loadJson(filePath, resolver));
        } else if (filePath.endsWith(".hierarchy")) {
            return HierarchyArtemisLoader.loadHierarchicalEntityToEngine(world, hierarchySystem, loadJson(filePath, resolver));
        } else {
            throw new GdxRuntimeException("Unable to resolve: " + filePath);
        }
    }

    private JsonValue loadJson(String filePath, FileHandleResolver fileHandleResolver) {
        JsonValue result = cachedJsons.get(filePath);
        if (result == null) {
            result = JsonTemplateLoader.loadTemplateFromFile(filePath, fileHandleResolver);
            cachedJsons.put(filePath, result);
        }
        return result;
    }

    @Override
    protected void processSystem() {

    }
}
