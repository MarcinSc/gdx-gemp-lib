package com.gempukku.libgdx.ui.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.IntArray;
import com.kotcrab.vis.ui.util.OsUtils;

public class KeyCombinations {
    public static KeyCombination ctrl() {
        if (OsUtils.isMac()) {
            return of(Input.Keys.SYM);
        } else {
            return or(Input.Keys.CONTROL_LEFT, Input.Keys.CONTROL_RIGHT);
        }
    }

    public static KeyCombination shift() {
        return or(Input.Keys.SHIFT_LEFT, Input.Keys.SHIFT_RIGHT);
    }

    public static KeyCombination alt() {
        return or(Input.Keys.ALT_LEFT, Input.Keys.ALT_RIGHT);
    }

    public static KeyCombination of(final int keycode) {
        return new KeyCombination() {
            @Override
            public boolean contains(int keycodeCheck) {
                return keycode == keycodeCheck;
            }

            @Override
            public boolean isDown(Input input) {
                return input.isKeyPressed(keycode);
            }

            @Override
            public int getCombinationCount() {
                return 1;
            }

            @Override
            public IntArray getShortCutRepresentation() {
                return new IntArray(new int[] {keycode});
            }
        };
    }

    public static KeyCombination and(int... keycodes) {
        KeyCombination[] combination = new KeyCombination[keycodes.length];
        for (int i=0; i<keycodes.length; i++) {
            combination[i] = of(keycodes[i]);
        }
        return and(combination);
    }

    public static KeyCombination and(final KeyCombination... keyCombinations) {
        return new KeyCombination() {
            @Override
            public boolean contains(int keycode) {
                for (KeyCombination keyCombination : keyCombinations) {
                    if (keyCombination.contains(keycode))
                        return true;
                }
                return false;
            }

            @Override
            public boolean isDown(Input input) {
                for (KeyCombination keyCombination : keyCombinations) {
                    if (!keyCombination.isDown(input))
                        return false;
                }
                return true;
            }

            @Override
            public int getCombinationCount() {
                int result = 0;
                for (KeyCombination keyCombination : keyCombinations) {
                    result+=keyCombination.getCombinationCount();
                }
                return result;
            }

            @Override
            public IntArray getShortCutRepresentation() {
                IntArray result = new IntArray();
                for (KeyCombination keyCombination : keyCombinations) {
                    result.addAll(keyCombination.getShortCutRepresentation());
                }
                return result;
            }
        };
    }

    public static KeyCombination or(int... keycodes) {
        KeyCombination[] combination = new KeyCombination[keycodes.length];
        for (int i=0; i<keycodes.length; i++) {
            combination[i] = of(keycodes[i]);
        }
        return or(combination);
    }

    public static KeyCombination or(final KeyCombination... keyCombinations) {
        return new KeyCombination() {
            @Override
            public boolean contains(int keycode) {
                for (KeyCombination keyCombination : keyCombinations) {
                    if (keyCombination.contains(keycode))
                        return true;
                }
                return false;
            }

            @Override
            public boolean isDown(Input input) {
                for (KeyCombination keyCombination : keyCombinations) {
                    if (keyCombination.isDown(input))
                        return true;
                }
                return false;
            }

            @Override
            public int getCombinationCount() {
                int min = Integer.MAX_VALUE;
                for (KeyCombination keyCombination : keyCombinations) {
                    min = Math.min(min, keyCombination.getCombinationCount());
                }
                return min;
            }

            @Override
            public IntArray getShortCutRepresentation() {
                return keyCombinations[0].getShortCutRepresentation();
            }
        };
    }
}
