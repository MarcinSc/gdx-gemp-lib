package com.gempukku.libgdx.lib.artemis.camera;

import com.gempukku.libgdx.lib.artemis.event.EntityEvent;

public class CameraUpdated implements EntityEvent {
    private String cameraName;

    public CameraUpdated(String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraName() {
        return cameraName;
    }
}
