package com.gempukku.libgdx.test.system.sensor;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.gempukku.libgdx.test.component.OutlineComponent;
import com.gempukku.libgdx.test.entity.SensorData;

public class InteractSensorContactListener implements SensorContactListener {
    @Override
    public void contactBegun(SensorData sensor, Fixture other) {
        Entity entity = (Entity) other.getUserData();
        OutlineComponent outline = entity.getComponent(OutlineComponent.class);
        outline.setOutline(true);
    }

    @Override
    public void contactEnded(SensorData sensor, Fixture other) {
        Entity entity = (Entity) other.getUserData();
        OutlineComponent outline = entity.getComponent(OutlineComponent.class);
        outline.setOutline(false);
    }
}
