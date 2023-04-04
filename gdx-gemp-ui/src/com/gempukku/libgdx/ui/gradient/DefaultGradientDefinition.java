package com.gempukku.libgdx.ui.gradient;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

public class DefaultGradientDefinition implements GradientDefinition{
    private static final Comparator<ColorPosition> positionAscending = new Comparator<ColorPosition>() {
        @Override
        public int compare(ColorPosition o1, ColorPosition o2) {
            return Float.compare(o1.position, o2.position);
        }
    };

    private Array<ColorPosition> colorPositions = new Array<>();

    public DefaultGradientDefinition() {
    }

    public DefaultGradientDefinition(Array<ColorPosition> colorPositions) {
        colorPositions.addAll(colorPositions);
        colorPositions.sort(positionAscending);
    }

    @Override
    public Array<ColorPosition> getColorPositions() {
        return colorPositions;
    }

    @Override
    public void removeColor(int index) {
        colorPositions.removeIndex(index);
    }

    @Override
    public void addColor(float position, Color color) {
        colorPositions.add(new ColorPosition(position, color));
        colorPositions.sort(positionAscending);
    }

    @Override
    public void updatePoint(int index, float position, Color color) {
        ColorPosition colorPosition = colorPositions.get(index);
        colorPosition.position = position;
        colorPosition.color = color.cpy();
    }
}
