package com.gempukku.libgdx.lib.test.system.sensor;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.lib.test.entity.SensorData;
import com.gempukku.libgdx.lib.test.system.PhysicsSystem;

public class FootSensorContactListener implements SensorContactListener {
    @Override
    public void contactBegun(SensorData sensor, Fixture other) {
        if (other.getFilterData().categoryBits == PhysicsSystem.ENVIRONMENT) {
            FootSensorData footSensorData = (FootSensorData) sensor.getValue();
            if (footSensorData == null) {
                footSensorData = new FootSensorData();
                sensor.setValue(footSensorData);
            }
            footSensorData.setGrounded(true);
        }
    }

    @Override
    public void contactEnded(SensorData sensor, Fixture other) {
        if (other.getFilterData().categoryBits == PhysicsSystem.ENVIRONMENT) {
            FootSensorData footSensorData = (FootSensorData) sensor.getValue();
            footSensorData.setGrounded(false);
        }
    }
}
