package com.gempukku.libgdx.template.ashley;

import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.utils.Json;

public class AshleyTemplateEntityLoaderExample {
    public EntityDef loadEnemy() {
        Json json = new Json();
        return AshleyTemplateEntityLoader.loadTemplate(
                "tutorial/ashley/enemy.json",
                json,
                new ClasspathFileHandleResolver());
    }
}
