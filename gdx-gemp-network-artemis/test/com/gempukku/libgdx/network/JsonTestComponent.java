package com.gempukku.libgdx.network;

import com.artemis.Component;
import com.badlogic.gdx.utils.JsonValue;

public class JsonTestComponent extends Component {
    private JsonValue field;

    public JsonValue getField() {
        return field;
    }

    public void setField(JsonValue field) {
        this.field = field;
    }
}
