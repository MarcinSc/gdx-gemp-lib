package com.gempukku.libgdx.template;

import com.badlogic.gdx.assets.loaders.resolvers.ClasspathFileHandleResolver;
import com.badlogic.gdx.utils.JsonValue;

public class JsonTemplateLoaderExample {
    public JsonValue loadInheritTemplate() {
        return JsonTemplateLoader.loadTemplateFromFile(
                "tutorial/inherit.json",
                new ClasspathFileHandleResolver());
    }
}
