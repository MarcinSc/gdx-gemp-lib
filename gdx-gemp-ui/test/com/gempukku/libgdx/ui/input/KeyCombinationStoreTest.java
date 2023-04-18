package com.gempukku.libgdx.ui.input;

import com.badlogic.gdx.Input;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class KeyCombinationStoreTest {
    @Test
    public void chooseLeastKeystrokes() {
        KeyCombinationStore<KeyCombination> store = new KeyCombinationStore<>();
        KeyCombination ctrlZ = KeyCombinations.and(KeyCombinations.ctrl(), KeyCombinations.of(Input.Keys.Z));
        KeyCombination ctrlShiftZ = KeyCombinations.and(KeyCombinations.ctrl(), KeyCombinations.shift(), KeyCombinations.of(Input.Keys.Z));

        store.addKeyCombination(ctrlZ, ctrlZ);
        store.addKeyCombination(ctrlShiftZ, ctrlShiftZ);

        Input input = Mockito.mock(Input.class);
        Mockito.when(input.isKeyPressed(Input.Keys.CONTROL_LEFT)).thenReturn(true);
        Mockito.when(input.isKeyPressed(Input.Keys.SHIFT_LEFT)).thenReturn(false);
        Mockito.when(input.isKeyPressed(Input.Keys.Z)).thenReturn(true);

        assertEquals(ctrlZ, store.getBestMatchingKeyCombination(Input.Keys.Z, input));
    }

    @Test
    public void chooseBestMatch() {
        KeyCombinationStore<KeyCombination> store = new KeyCombinationStore<>();
        KeyCombination ctrlZ = KeyCombinations.and(KeyCombinations.ctrl(), KeyCombinations.of(Input.Keys.Z));
        KeyCombination ctrlShiftZ = KeyCombinations.and(KeyCombinations.ctrl(), KeyCombinations.shift(), KeyCombinations.of(Input.Keys.Z));

        store.addKeyCombination(ctrlZ, ctrlZ);
        store.addKeyCombination(ctrlShiftZ, ctrlShiftZ);

        Input input = Mockito.mock(Input.class);
        Mockito.when(input.isKeyPressed(Input.Keys.CONTROL_LEFT)).thenReturn(true);
        Mockito.when(input.isKeyPressed(Input.Keys.SHIFT_LEFT)).thenReturn(true);
        Mockito.when(input.isKeyPressed(Input.Keys.Z)).thenReturn(true);

        assertEquals(ctrlShiftZ, store.getBestMatchingKeyCombination(Input.Keys.Z, input));
    }
}
