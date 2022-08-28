package com.gempukku.libgdx.lib.artemis.input;

import com.artemis.Aspect;
import com.artemis.BaseEntitySystem;
import com.artemis.ComponentMapper;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class UserInputSystem extends BaseEntitySystem implements InputProcessorProvider {
    private UserInputProcessor userInputProcessor = new UserInputProcessor();
    private int processorPriority;

    private ComponentMapper<UserInputStateComponent> inputStateMapper;
    private ComponentMapper<UserInputComponent> inputMapper;

    public UserInputSystem(int processorPriority) {
        super(Aspect.all(UserInputComponent.class));
        this.processorPriority = processorPriority;
    }

    @Override
    public int getInputPriority() {
        return processorPriority;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return userInputProcessor;
    }

    protected void inserted(int entityId) {
        UserInputStateComponent userInputState = inputStateMapper.create(entityId);
        UserInputComponent userInput = inputMapper.get(entityId);

        userInputProcessor.register(userInput, userInputState);
    }

    protected void removed(int entityId) {
        UserInputStateComponent userInputState = inputStateMapper.create(entityId);
        UserInputComponent userInput = inputMapper.get(entityId);
        userInputProcessor.unregister(userInput, userInputState);

        inputStateMapper.remove(entityId);
    }

    @Override
    protected void processSystem() {
        userInputProcessor.process();
    }

    private int getButton(String name) {
        if (name.equalsIgnoreCase("left"))
            return Input.Buttons.LEFT;
        if (name.equalsIgnoreCase("right"))
            return Input.Buttons.RIGHT;
        if (name.equalsIgnoreCase("middle"))
            return Input.Buttons.MIDDLE;
        if (name.equalsIgnoreCase("back"))
            return Input.Buttons.BACK;
        if (name.equalsIgnoreCase("forward"))
            return Input.Buttons.FORWARD;

        return -1;
    }
}
