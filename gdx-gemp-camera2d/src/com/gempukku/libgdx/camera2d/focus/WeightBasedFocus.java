package com.gempukku.libgdx.camera2d.focus;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectSet;

public class WeightBasedFocus implements Camera2DFocus {
    private ObjectSet<WeightedCamera2DFocus> focuses = new ObjectSet<>();
    private Vector2 tmpVector = new Vector2();

    public WeightBasedFocus(WeightedCamera2DFocus... cameraFoci) {
        focuses.addAll(cameraFoci);
    }

    public void addFocus(WeightedCamera2DFocus focus) {
        focuses.add(focus);
    }

    public void removeFocus(WeightedCamera2DFocus focus) {
        focuses.remove(focus);
    }

    @Override
    public Vector2 getFocus(Vector2 focus) {
        focus.set(0, 0);
        float weightSum = 0;
        for (WeightedCamera2DFocus weightedFocus : focuses) {
            float weight = weightedFocus.getWeight();
            weightedFocus.getFocus(tmpVector);
            weightSum += weight;
            focus.mulAdd(tmpVector, weight);
        }
        if (weightSum > 0) {
            focus.scl(1 / weightSum);
        }

        return focus;
    }
}
