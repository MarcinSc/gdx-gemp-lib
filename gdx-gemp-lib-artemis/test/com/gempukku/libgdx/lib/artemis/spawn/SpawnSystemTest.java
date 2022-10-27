package com.gempukku.libgdx.lib.artemis.spawn;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import org.junit.Test;

public class SpawnSystemTest {
    @Test
    public void setupWithoutHierarchySystem() {
        WorldConfigurationBuilder builder = new WorldConfigurationBuilder();
        builder.with(new SpawnSystem());

        World world = new World(builder.build());
    }
}