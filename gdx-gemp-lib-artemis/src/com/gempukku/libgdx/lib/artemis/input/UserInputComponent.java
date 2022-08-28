package com.gempukku.libgdx.lib.artemis.input;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectMap;

public class UserInputComponent extends Component {
    private ObjectMap<String, String> keySignal = new ObjectMap<>();
    private ObjectMap<String, String> keyState = new ObjectMap<>();
    private ObjectMap<String, String> buttonSignal = new ObjectMap<>();
    private ObjectMap<String, String> buttonState = new ObjectMap<>();

    public ObjectMap<String, String> getKeySignal() {
        return keySignal;
    }

    public ObjectMap<String, String> getKeyState() {
        return keyState;
    }

    public ObjectMap<String, String> getButtonSignal() {
        return buttonSignal;
    }

    public ObjectMap<String, String> getButtonState() {
        return buttonState;
    }
}
