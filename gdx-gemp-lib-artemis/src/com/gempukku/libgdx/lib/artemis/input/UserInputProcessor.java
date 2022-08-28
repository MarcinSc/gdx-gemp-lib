package com.gempukku.libgdx.lib.artemis.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;

public class UserInputProcessor implements InputProcessor {
    private ObjectSet<String> keyDownNow = new ObjectSet<>();
    private ObjectSet<String> keyDown = new ObjectSet<>();
    private ObjectSet<String> mouseButtonDownNow = new ObjectSet<>();
    private ObjectSet<String> mouseButtonDown = new ObjectSet<>();

    private ObjectMap<UserInputComponent, UserInputStateComponent> inputs = new ObjectMap<>();

    public void register(UserInputComponent userInput, UserInputStateComponent userInputState) {
        inputs.put(userInput, userInputState);
    }

    public void unregister(UserInputComponent userInput, UserInputStateComponent userInputState) {
        inputs.remove(userInput);
    }

    @Override
    public boolean keyDown(int keycode) {
        String keyName = Input.Keys.toString(keycode);
        for (UserInputComponent userInput : inputs.keys()) {
            if (userInput.getKeyState().containsKey(keyName)
                    || userInput.getKeySignal().containsKey(keyName)) {
                keyDown.add(keyName);
                keyDownNow.add(keyName);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        String keyName = Input.Keys.toString(keycode);
        return keyDown.remove(keyName);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        String buttonName = getButtonName(button);
        for (UserInputComponent userInput : inputs.keys()) {
            if (userInput.getButtonState().containsKey(buttonName)
                    || userInput.getButtonSignal().containsKey(buttonName)) {
                mouseButtonDown.add(buttonName);
                mouseButtonDownNow.add(buttonName);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        String buttonName = getButtonName(button);
        return mouseButtonDown.remove(buttonName);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    private String getButtonName(int button) {
        switch (button) {
            case Input.Buttons.LEFT:
                return "left";
            case Input.Buttons.RIGHT:
                return "right";
            case Input.Buttons.MIDDLE:
                return "middle";
            case Input.Buttons.BACK:
                return "back";
            case Input.Buttons.FORWARD:
                return "forward";
        }
        return null;
    }

    public void process() {
        for (ObjectMap.Entry<UserInputComponent, UserInputStateComponent> input : inputs) {
            UserInputComponent userInput = input.key;
            UserInputStateComponent state = input.value;

            state.getStates().clear();
            state.getSignals().clear();

            for (ObjectMap.Entry<String, String> buttonSignal : userInput.getButtonSignal()) {
                if (mouseButtonDownNow.contains(buttonSignal.key))
                    state.getSignals().add(buttonSignal.value);
            }
            for (ObjectMap.Entry<String, String> buttonState : userInput.getButtonState()) {
                if (mouseButtonDown.contains(buttonState.key))
                    state.getStates().add(buttonState.value);
            }
            for (ObjectMap.Entry<String, String> keySignal : userInput.getKeySignal()) {
                if (keyDownNow.contains(keySignal.key))
                    state.getSignals().add(keySignal.value);
            }
            for (ObjectMap.Entry<String, String> keyState : userInput.getKeyState()) {
                if (keyDown.contains(keyState.key))
                    state.getStates().add(keyState.value);
            }
        }

        keyDownNow.clear();
        mouseButtonDownNow.clear();
    }
}
