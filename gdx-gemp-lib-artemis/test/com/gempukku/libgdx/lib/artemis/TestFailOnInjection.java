package com.gempukku.libgdx.lib.artemis;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.annotations.Wire;
import org.junit.Test;

public class TestFailOnInjection {
    @Test
    public void testFailOnNullOnField() {
        SystemOne systemOne = new SystemOne();
        WorldConfigurationBuilder builder = new WorldConfigurationBuilder();
        builder.with(systemOne);

        World world = new World(builder.build());
    }

    @Wire(failOnNull = false)
    private static class SystemOne extends BaseSystem {
        @Wire(failOnNull = false)
        private SystemTwo systemTwo;

        @Override
        protected void processSystem() {

        }
    }

    private static class SystemTwo extends BaseSystem {
        @Override
        protected void processSystem() {

        }
    }
}
