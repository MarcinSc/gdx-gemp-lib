package com.gempukku.libgdx.lib.artemis.input;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.artemis.template.InternalComponent;

@InternalComponent
public class UserInputStateComponent extends Component {
    private ObjectSet<String> signals = new ObjectSet<>();
    private ObjectSet<String> states = new ObjectSet<>();

    public ObjectSet<String> getSignals() {
        return signals;
    }

    public ObjectSet<String> getStates() {
        return states;
    }
}
