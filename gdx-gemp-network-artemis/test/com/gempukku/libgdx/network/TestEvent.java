package com.gempukku.libgdx.network;

import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public class TestEvent implements EntityEvent {
    private int testInt;
    private String testString;

    public int getTestInt() {
        return testInt;
    }

    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }
}
