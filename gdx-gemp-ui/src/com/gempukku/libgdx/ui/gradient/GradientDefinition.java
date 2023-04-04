package com.gempukku.libgdx.ui.gradient;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

public interface GradientDefinition {
    Array<ColorPosition> getColorPositions();

    void removeColor(int index);
    void addColor(float position, Color color);
    void updatePoint(int index, float position, Color color);

    class ColorPosition {
        public float position;
        public Color color;

        public ColorPosition() {
            this(0, Color.WHITE);
        }

        public ColorPosition(float position, Color color) {
            this.position = position;
            this.color = color.cpy();
        }
    }
}
