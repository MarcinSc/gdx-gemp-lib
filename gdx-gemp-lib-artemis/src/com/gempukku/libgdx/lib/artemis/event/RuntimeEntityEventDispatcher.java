package com.gempukku.libgdx.lib.artemis.event;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Sort;

import java.lang.reflect.Method;
import java.util.Comparator;

public class RuntimeEntityEventDispatcher implements EntityEventDispatcher {
    private final PrioritySource prioritySource;
    private final ObjectMap<Class<? extends EntityEvent>, Array<EventDispatcher>> eventDispatchers = new ObjectMap<>();

    public RuntimeEntityEventDispatcher() {
        this(
                new PrioritySource() {
                    @Override
                    public float getPriority(String priorityName) {
                        return 0;
                    }
                });
    }

    public RuntimeEntityEventDispatcher(PrioritySource prioritySource) {
        this.prioritySource = prioritySource;
    }

    @Override
    public void initializeForWorld(World world) {
        for (BaseSystem system : world.getSystems()) {
            Class<? extends BaseSystem> systemClass = system.getClass();
            scanClass(system, systemClass);
        }

        Comparator<EventDispatcher> comparator = new Comparator<EventDispatcher>() {
            @Override
            public int compare(EventDispatcher o1, EventDispatcher o2) {
                float firstPriority = o1.getPriority();
                float secondPriority = o2.getPriority();
                if (firstPriority > secondPriority)
                    return -1;
                if (firstPriority < secondPriority)
                    return 1;
                return 0;
            }
        };

        for (Array<EventDispatcher> dispatcherArray : eventDispatchers.values()) {
            Sort.instance().sort(dispatcherArray, comparator);
        }
    }

    private void scanClass(BaseSystem system, Class<? extends BaseSystem> systemClass) {
        for (Method method : systemClass.getMethods()) {
            EventListener eventListener = method.getAnnotation(EventListener.class);
            if (eventListener != null) {
                assertThat(method.getParameterCount() == 2, method, "Method does not have 2 parameters");
                assertThat(EntityEvent.class.isAssignableFrom(method.getParameterTypes()[0]), method, "Method's first parameter does not implement EntityEvent");
                assertThat(method.getParameterTypes()[1] == Entity.class, method, "Method's second parameter is not Entity");

                float priority = determineListenerPriority(eventListener);

                Class<? extends EntityEvent> eventType = (Class<? extends EntityEvent>) method.getParameterTypes()[0];
                Array<EventDispatcher> eventDispatcherArray = this.eventDispatchers.get(eventType);
                if (eventDispatcherArray == null) {
                    eventDispatcherArray = new Array<>();
                    eventDispatchers.put(eventType, eventDispatcherArray);
                }
                eventDispatcherArray.add(new MethodEventDispatcher(priority, system, method));
            }
        }
    }

    private float determineListenerPriority(EventListener eventListener) {
        float priority = eventListener.priority();
        String priorityName = eventListener.priorityName();
        if (!priorityName.equals("")) {
            priority = prioritySource.getPriority(priorityName);
        }
        return priority;
    }

    private void assertThat(boolean test, Method method, String message) {
        if (!test) {
            throw new GdxRuntimeException(method.getClass().getSimpleName() + ":" + method.getName() + " - " + message);
        }
    }

    @Override
    public void dispatchEvent(EntityEvent event, Entity entity) {
        Array<EventDispatcher> eventDispatchers = this.eventDispatchers.get(event.getClass());
        if (eventDispatchers != null) {
            for (EventDispatcher eventDispatcher : eventDispatchers) {
                eventDispatcher.dispatchEvent(event, entity);
            }
        }
    }
}
