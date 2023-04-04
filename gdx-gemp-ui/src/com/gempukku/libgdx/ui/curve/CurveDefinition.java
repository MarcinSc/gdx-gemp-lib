package com.gempukku.libgdx.ui.curve;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public interface CurveDefinition {
    Array<Vector2> getPoints();

    void removePoint(int index);

    void addPoint(float x, float y);

    void updatePoint(int index, float x, float y);
}
