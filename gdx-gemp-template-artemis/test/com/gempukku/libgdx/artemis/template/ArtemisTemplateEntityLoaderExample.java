package com.gempukku.libgdx.artemis.template;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;

public class ArtemisTemplateEntityLoaderExample {
    public Entity loadEnemy() {
        World world = new World();
        ArtemisWorldJson json = new ArtemisWorldJson();
        json.setWorld(world);
        return ArtemisTemplateEntityLoader.loadTemplateToWorld(
                world,
                "tutorial/ashley/enemy.json",
                json,
                new ClasspathFileHandleResolver());
    }
}
