package com.gempukku.libgdx.lib.artemis.hierarchy;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.artemis.template.ArtemisTemplateEntityLoader;
import com.gempukku.libgdx.template.JsonTemplateLoader;

import java.io.Reader;

public class HierarchyArtemisLoader {
    public static Entity loadHierarchicalEntityToEngine(World world, HierarchySystem hierarchySystem, String filePath, FileHandleResolver resolver) {
        JsonValue value = JsonTemplateLoader.loadTemplateFromFile(filePath, resolver);
        return loadHierarchicalEntityToEngine(world, hierarchySystem, value);
    }

    public static Entity loadHierarchicalEntityToEngine(World world, HierarchySystem hierarchySystem, JsonValue value) {
        return loadHierarchyRecursively(world, hierarchySystem, value);
    }

    public static Array<Entity> loadHierarchyToEngine(World world, HierarchySystem hierarchySystem, Array<Entity> result,
                                                      String filePath, FileHandleResolver resolver) {
        JsonValue value = JsonTemplateLoader.loadTemplateFromFile(filePath, resolver);
        return loadHierarchyToEngine(world, hierarchySystem, result, value);
    }

    public static Array<Entity> loadHierarchyToEngine(World world, HierarchySystem hierarchySystem, Array<Entity> result,
                                                      Reader reader, FileHandleResolver resolver) {
        JsonValue value = JsonTemplateLoader.loadTemplateFromFile(reader, resolver);
        return loadHierarchyToEngine(world, hierarchySystem, result, value);
    }

    public static Array<Entity> loadHierarchyToEngine(World world, HierarchySystem hierarchySystem, Array<Entity> result, JsonValue value) {
        result.clear();

        JsonValue entities = value.get("entities");
        for (JsonValue jsonEntity : entities) {
            result.add(loadHierarchyRecursively(world, hierarchySystem, jsonEntity));
        }
        return result;
    }

    private static Entity loadHierarchyRecursively(World world, HierarchySystem hierarchySystem, JsonValue definitionJson) {
        JsonValue entityJson = definitionJson.get("entity");
        Entity parent = ArtemisTemplateEntityLoader.loadTemplateToWorld(world, entityJson);
        JsonValue childrenJson = definitionJson.get("children");
        if (childrenJson != null) {
            for (JsonValue childJson : childrenJson) {
                Entity child = loadHierarchyRecursively(world, hierarchySystem, childJson);
                hierarchySystem.addHierarchy(parent, child);
            }
        }
        return parent;
    }
}
