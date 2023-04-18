package com.gempukku.libgdx.ui.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.IntArray;

public interface KeyCombination {
    boolean contains(int keycode);
    boolean isDown(Input input);
    int getCombinationCount();
    IntArray getShortCutRepresentation();
}
