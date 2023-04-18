package com.gempukku.libgdx.ui.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.IntArray;

import java.util.Objects;

/**
 * Represents an activating key and a set of modifier keys (ctrl/sym, shift, alt).
 */
public final class KeyCombination {
    private static final int[] ctrlKeys = new int[]{Input.Keys.SYM, Input.Keys.CONTROL_LEFT, Input.Keys.CONTROL_RIGHT};
    private static final int[] shiftKeys = new int[]{Input.Keys.SHIFT_LEFT, Input.Keys.SHIFT_RIGHT};
    private static final int[] altKeys = new int[]{Input.Keys.ALT_LEFT, Input.Keys.ALT_RIGHT};

    private final boolean ctrl;
    private final boolean shift;
    private final boolean alt;
    private final int activating;
    private final int[] shortcut;

    public KeyCombination(boolean ctrl, boolean shift, boolean alt, int activating) {
        this.ctrl = ctrl;
        this.shift = shift;
        this.alt = alt;
        this.activating = activating;

        IntArray shortcutArr = new IntArray();
        if (ctrl)
            shortcutArr.add(Input.Keys.CONTROL_LEFT);
        if (shift)
            shortcutArr.add(Input.Keys.SHIFT_LEFT);
        if (alt)
            shortcutArr.add(Input.Keys.ALT_LEFT);
        shortcutArr.add(activating);

        shortcut = shortcutArr.toArray();
    }

    public boolean isActivated(Input input, int keycode) {
        return activating == keycode && matchesModifiers(input);
    }

    private boolean matchesModifiers(Input input) {
        if (!matchesModifier(input, ctrl, ctrlKeys))
            return false;
        if (!matchesModifier(input, shift, shiftKeys))
            return false;
        if (!matchesModifier(input, alt, altKeys))
            return false;
        return true;
    }

    private boolean matchesModifier(Input input, boolean modifierRequired, int[] keys) {
        return modifierRequired == isAnyPressed(input, keys);
    }

    private boolean isAnyPressed(Input input, int... keycodes) {
        for (int keycode : keycodes) {
            if (input.isKeyPressed(keycode))
                return true;
        }
        return false;
    }

    public int[] getShortCutRepresentation() {
        return shortcut;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KeyCombination that = (KeyCombination) o;
        return ctrl == that.ctrl && shift == that.shift && alt == that.alt && activating == that.activating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ctrl, shift, alt, activating);
    }
}
