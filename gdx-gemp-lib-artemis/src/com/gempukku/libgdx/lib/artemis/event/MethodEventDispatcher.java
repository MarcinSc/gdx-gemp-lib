package com.gempukku.libgdx.lib.artemis.event;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodEventDispatcher implements EventDispatcher {
    private float priority;
    private BaseSystem system;
    private Method method;

    public MethodEventDispatcher(float priority, BaseSystem system, Method method) {
        this.priority = priority;
        this.system = system;
        this.method = method;
    }

    @Override
    public float getPriority() {
        return priority;
    }

    @Override
    public void dispatchEvent(EntityEvent event, Entity entity) {
        try {
            method.invoke(system, event, entity);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new GdxRuntimeException("Illegal access to the event method", e);
        }
    }
}
