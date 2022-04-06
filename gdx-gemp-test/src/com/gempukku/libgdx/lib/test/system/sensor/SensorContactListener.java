package com.gempukku.libgdx.lib.test.system.sensor;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.lib.test.entity.SensorData;

public interface SensorContactListener {
    void contactBegun(SensorData sensor, Fixture other);

    void contactEnded(SensorData sensor, Fixture other);
}
