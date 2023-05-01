package com.gempukku.libgdx.ui.gradient;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;
import java.util.Objects;

public class DefaultGradientDefinition implements GradientDefinition{
    private static final Comparator<ColorPosition> positionAscending = new Comparator<ColorPosition>() {
        @Override
        public int compare(ColorPosition o1, ColorPosition o2) {
            return Float.compare(o1.position, o2.position);
        }
    };

    private final Array<ColorPosition> colorPositions = new Array<>();

    public DefaultGradientDefinition() {
        addColor(0, Color.WHITE);
    }

    public DefaultGradientDefinition(GradientDefinition gradientDefinition) {
        this(gradientDefinition.getColorPositions());
    }

    public DefaultGradientDefinition(Iterable<ColorPosition> colorPositions) {
        for (ColorPosition colorPosition : colorPositions) {
            addColor(colorPosition);
        }
        this.colorPositions.sort(positionAscending);
    }

    public void copy(GradientDefinition gradientDefinition) {
        clear();
        for (ColorPosition colorPosition : gradientDefinition.getColorPositions()) {
            addColor(colorPosition);
        }
    }

    @Override
    public Array<ColorPosition> getColorPositions() {
        return colorPositions;
    }

    @Override
    public void removeColor(int index) {
        colorPositions.removeIndex(index);
    }

    public void addColor(ColorPosition colorPosition) {
        addColor(colorPosition.position, colorPosition.color);
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

    public void clear() {
        colorPositions.clear();
    }

    public GradientDefinition cpy() {
        return new DefaultGradientDefinition(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultGradientDefinition that = (DefaultGradientDefinition) o;
        return colorPositions.equals(that.colorPositions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(colorPositions);
    }
}
