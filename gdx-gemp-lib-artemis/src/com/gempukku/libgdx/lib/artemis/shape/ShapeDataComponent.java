package com.gempukku.libgdx.lib.artemis.shape;

import com.artemis.Component;
import com.badlogic.gdx.utils.ObjectMap;

public class ShapeDataComponent extends Component {
    private ObjectMap<String, ShapeDataDefinition> namedData;

    public ObjectMap<String, ShapeDataDefinition> getNamedData() {
        return namedData;
    }
}
