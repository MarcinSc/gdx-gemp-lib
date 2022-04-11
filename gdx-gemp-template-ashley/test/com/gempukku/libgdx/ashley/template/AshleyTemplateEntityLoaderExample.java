package com.gempukku.libgdx.ashley.template;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;

public class AshleyTemplateEntityLoaderExample {
    public Entity loadEnemy() {
        Engine engine = new Engine();
        AshleyEngineJson json = new AshleyEngineJson();
        json.setEngine(engine);
        return AshleyTemplateEntityLoader.loadTemplateToEngine(
                engine,
                "tutorial/ashley/enemy.json",
                json,
                new ClasspathFileHandleResolver());
    }
}
