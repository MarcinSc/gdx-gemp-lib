package com.gempukku.libgdx.lib.artemis.event;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.Sort;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Comparator;

public class EventSystemComposer {
    public static void scanSystems(String packageName, String className, PrioritySource prioritySource, File sourceFolder, Class<? extends BaseSystem>... systems) throws IOException {
        ObjectMap<String, String> systemClassToVariable = new ObjectMap<>();
        ObjectMap<String, Array<WritingEventDispatcher>> eventDispatchers = new ObjectMap<>();
        for (Class<? extends BaseSystem> system : systems) {
            for (Method method : system.getMethods()) {
                EventListener eventListenerAnnotation = method.getAnnotation(EventListener.class);
                if (eventListenerAnnotation != null) {
                    assertThat(method.getParameterCount() == 2, method, "Method does not have 2 parameters");
                    assertThat(EntityEvent.class.isAssignableFrom(method.getParameterTypes()[0]), method, "Method's first parameter does not implement EntityEvent");
                    assertThat(method.getParameterTypes()[1] == Entity.class, method, "Method's second parameter is not Entity");

                    float priority = determineListenerPriority(eventListenerAnnotation, prioritySource);

                    Class<? extends EntityEvent> eventType = (Class<? extends EntityEvent>) method.getParameterTypes()[0];

                    String eventTypeName = eventType.getName();
                    Array<WritingEventDispatcher> writingEventDispatchers = eventDispatchers.get(eventTypeName);
                    if (writingEventDispatchers == null) {
                        writingEventDispatchers = new Array<>();
                        eventDispatchers.put(eventTypeName, writingEventDispatchers);
                    }
                    String systemClassName = system.getName();
                    writingEventDispatchers.add(new WritingEventDispatcher(systemClassName, method.getName(), priority));

                    systemClassToVariable.put(systemClassName, system.getSimpleName().toLowerCase());
                }
            }
        }

        StringBuilder variablesSB = new StringBuilder();
        StringBuilder initializeSB = new StringBuilder();
        for (ObjectMap.Entry<String, String> systemVariable : systemClassToVariable) {
            variablesSB.append("    " + systemVariable.key + " " + systemVariable.value + ";\n");
            initializeSB.append("        " + systemVariable.value + " = world.getSystem(" + systemVariable.key + ".class);\n");
        }

        Comparator<WritingEventDispatcher> comparator = new Comparator<WritingEventDispatcher>() {
            @Override
            public int compare(WritingEventDispatcher o1, WritingEventDispatcher o2) {
                float firstPriority = o1.getPriority();
                float secondPriority = o2.getPriority();
                if (firstPriority > secondPriority)
                    return -1;
                if (firstPriority < secondPriority)
                    return 1;
                return 0;
            }
        };

        for (Array<WritingEventDispatcher> dispatcherArray : eventDispatchers.values()) {
            Sort.instance().sort(dispatcherArray, comparator);
        }

        String eventSystemTemplate = asString("eventSystemTemplate.tmp");
        String dispatchBodyTemplate = asString("dispatchBodyTemplate.tmp");
        String systemDispatchTemplate = asString("systemDispatchTemplate.tmp");

        StringBuilder eventSB = new StringBuilder();
        for (ObjectMap.Entry<String, Array<WritingEventDispatcher>> eventDispatcher : eventDispatchers) {
            StringBuilder systemSB = new StringBuilder();
            for (WritingEventDispatcher writingEventDispatcher : eventDispatcher.value) {
                systemSB.append(systemDispatchTemplate.replaceAll("<SYSTEM_VARIABLE>", systemClassToVariable.get(writingEventDispatcher.getClassName()))
                        .replaceAll("<METHOD_NAME>", writingEventDispatcher.getMethodName())
                        .replaceAll("<EVENT_CLASS>", eventDispatcher.key));
            }

            eventSB.append(dispatchBodyTemplate.replaceAll("<EVENT_CLASS>", eventDispatcher.key)
                    .replaceAll("<SYSTEM_DISPATCH>", systemSB.toString()));
        }

        String classCode = eventSystemTemplate.replaceAll("<PACKAGE>", packageName)
                .replaceAll("<CLASS_NAME>", className)
                .replaceAll("<DISPATCH_BODY>", eventSB.toString())
                .replaceAll("<VARIABLES>", variablesSB.toString())
                .replaceAll("<INITIALIZE_BODY>", initializeSB.toString());

        String[] packageSplit = packageName.split("\\.");
        File destinationFolder = sourceFolder;
        for (String packageNameElement : packageSplit) {
            destinationFolder = findFolder(destinationFolder, packageNameElement);
        }

        destinationFolder.mkdirs();
        File outputFile = new File(destinationFolder, className + ".java");

        FileWriter fileWriter = new FileWriter(outputFile);
        try {
            fileWriter.write(classCode);
        } finally {
            fileWriter.close();
        }
    }

    private static String asString(String path) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(EventSystemComposer.class.getClassLoader().getResourceAsStream(path)));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
                result.append("\n");
            }
        } finally {
            reader.close();
        }
        return result.toString();
    }

    private static File findFolder(File parent, String name) {
        return new File(parent, name);
    }

    private static float determineListenerPriority(EventListener eventListener, PrioritySource prioritySource) {
        float priority = eventListener.priority();
        String priorityName = eventListener.priorityName();
        if (!priorityName.equals("")) {
            priority = prioritySource.getPriority(priorityName);
        }
        return priority;
    }

    private static void assertThat(boolean test, Method method, String message) {
        if (!test) {
            throw new GdxRuntimeException(method.getClass().getSimpleName() + ":" + method.getName() + " - " + message);
        }
    }

    private static class WritingEventDispatcher {
        private String className;
        private String methodName;
        private float priority;

        public WritingEventDispatcher(String className, String methodName, float priority) {
            this.className = className;
            this.methodName = methodName;
            this.priority = priority;
        }

        public String getClassName() {
            return className;
        }

        public String getMethodName() {
            return methodName;
        }

        public float getPriority() {
            return priority;
        }
    }
}
