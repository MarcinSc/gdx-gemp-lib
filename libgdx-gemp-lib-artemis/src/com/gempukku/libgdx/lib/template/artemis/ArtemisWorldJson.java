package com.gempukku.libgdx.lib.template.artemis;

import com.artemis.Component;
import com.artemis.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.reflect.ClassReflection;

public class ArtemisWorldJson extends Json {
    private World world;

    public ArtemisWorldJson(World world) {
        this.world = world;
    }

    public ArtemisWorldJson(JsonWriter.OutputType outputType, World world) {
        super(outputType);
        this.world = world;
    }

    @Override
    protected Object newInstance(Class type) {
        if (ClassReflection.isAssignableFrom(Component.class, type))
            return world.createComponent(type);
        return super.newInstance(type);
    }
}
