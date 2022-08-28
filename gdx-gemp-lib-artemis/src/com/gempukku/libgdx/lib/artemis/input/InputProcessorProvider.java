package com.gempukku.libgdx.lib.artemis.input;

import com.badlogic.gdx.InputProcessor;

public interface InputProcessorProvider {
    int getInputPriority();

    InputProcessor getInputProcessor();
}
