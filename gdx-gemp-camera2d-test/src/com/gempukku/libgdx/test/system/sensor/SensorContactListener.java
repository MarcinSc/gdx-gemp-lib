package com.gempukku.libgdx.test.system.sensor;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.test.entity.SensorData;

public interface SensorContactListener {
    void contactBegun(SensorData sensor, Fixture other);

    void contactEnded(SensorData sensor, Fixture other);
}
