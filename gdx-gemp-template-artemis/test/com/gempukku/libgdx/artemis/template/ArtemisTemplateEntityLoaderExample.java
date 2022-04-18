package com.gempukku.libgdx.artemis.template;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;

public class ArtemisTemplateEntityLoaderExample {
    public Entity loadEnemy() {
        World world = new World();
        return ArtemisTemplateEntityLoader.loadTemplateToWorld(
                world,
                "tutorial/ashley/enemy.json",
                new ClasspathFileHandleResolver());
    }
}
