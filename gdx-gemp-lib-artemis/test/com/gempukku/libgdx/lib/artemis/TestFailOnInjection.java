package com.gempukku.libgdx.lib.artemis;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.annotations.Wire;
import org.junit.Test;

public class TestFailOnInjection {
    @Test
    public void testFailOnNullOnField() {
        SystemOne systemOne = new SystemOne();
        WorldConfiguration worldConfiguration = new WorldConfiguration();
        worldConfiguration.setSystem(systemOne);

        World world = new World(worldConfiguration);
    }

    private static class SystemOne extends BaseSystem {
        @Wire(failOnNull = false)
        private SystemTwo systemTwo;

        @Override
        protected void processSystem() {

        }
    }

    private static class SystemTwo {

    }
}
