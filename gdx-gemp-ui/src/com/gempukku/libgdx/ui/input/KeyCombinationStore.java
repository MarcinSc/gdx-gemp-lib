package com.gempukku.libgdx.ui.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.Comparator;

public class KeyCombinationStore<T> {
    private Comparator<KeyCombination> keyStrokeSizeComparator = new Comparator<KeyCombination>() {
        @Override
        public int compare(KeyCombination o1, KeyCombination o2) {
            return o2.getCombinationCount() - o1.getCombinationCount();
        }
    };
    private ObjectMap<KeyCombination, T> keyCombinationMap = new ObjectMap<>();
    private Array<KeyCombination> tmpArray = new Array<>();

    public void addKeyCombination(KeyCombination keyCombination, T value) {
        keyCombinationMap.put(keyCombination, value);
    }

    public void removeKeyCombination(KeyCombination keyCombination) {
        keyCombinationMap.remove(keyCombination);
    }

    public T getBestMatchingKeyCombination(int keycode, Input input) {
        tmpArray.clear();
        for (KeyCombination keyCombination : keyCombinationMap.keys()) {
            if (keyCombination.contains(keycode) && keyCombination.isDown(input)) {
                tmpArray.add(keyCombination);
            }
        }
        if (tmpArray.size > 0) {
            tmpArray.sort(keyStrokeSizeComparator);
            return keyCombinationMap.get(tmpArray.get(0));
        } else {
            return null;
        }
    }
}
