package com.gempukku.libgdx.ui.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.IntArray;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class KeyCombinationTest {
    @Test
    public void testShift() {
        KeyCombination shift = KeyCombinations.shift();
        assertEquals(1, shift.getCombinationCount());
        assertTrue(shift.contains(Input.Keys.SHIFT_LEFT));
        assertTrue(shift.contains(Input.Keys.SHIFT_RIGHT));
        assertFalse(shift.contains(Input.Keys.A));

        Input inputMock = Mockito.mock(Input.class);
        Mockito.when(inputMock.isKeyPressed(Input.Keys.SHIFT_LEFT)).thenReturn(true);
        assertTrue(shift.isDown(inputMock));

        IntArray shortCut = shift.getShortCutRepresentation();
        assertEquals(1, shortCut.size);
        assertEquals(Input.Keys.SHIFT_LEFT, shortCut.get(0));
    }

    @Test
    public void and() {
        KeyCombination keyCombination = KeyCombinations.and(Input.Keys.A, Input.Keys.S);
        assertEquals(2, keyCombination.getCombinationCount());
        assertTrue(keyCombination.contains(Input.Keys.A));
        assertTrue(keyCombination.contains(Input.Keys.S));
        assertFalse(keyCombination.contains(Input.Keys.R));

        Input inputMock = Mockito.mock(Input.class);
        Mockito.when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(true);
        Mockito.when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(true);
        assertTrue(keyCombination.isDown(inputMock));

        IntArray shortCut = keyCombination.getShortCutRepresentation();
        assertEquals(2, shortCut.size);
        assertEquals(Input.Keys.A, shortCut.get(0));
        assertEquals(Input.Keys.S, shortCut.get(1));
    }

    @Test
    public void or() {
        KeyCombination keyCombination = KeyCombinations.or(Input.Keys.A, Input.Keys.S);
        assertEquals(1, keyCombination.getCombinationCount());
        assertTrue(keyCombination.contains(Input.Keys.A));
        assertTrue(keyCombination.contains(Input.Keys.S));
        assertFalse(keyCombination.contains(Input.Keys.R));

        Input inputMock = Mockito.mock(Input.class);
        Mockito.when(inputMock.isKeyPressed(Input.Keys.A)).thenReturn(true);
        Mockito.when(inputMock.isKeyPressed(Input.Keys.S)).thenReturn(false);
        assertTrue(keyCombination.isDown(inputMock));

        IntArray shortCut = keyCombination.getShortCutRepresentation();
        assertEquals(1, shortCut.size);
        assertEquals(Input.Keys.A, shortCut.get(0));
    }
}
