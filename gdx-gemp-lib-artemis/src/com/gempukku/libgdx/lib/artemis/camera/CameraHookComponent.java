package com.gempukku.libgdx.lib.artemis.camera;

import com.artemis.Component;

public class CameraHookComponent extends Component {
    private boolean correctingForAspect = true;
    private String cameraName;

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public boolean isCorrectingForAspect() {
        return correctingForAspect;
    }

    public void setCorrectingForAspect(boolean correctingForAspect) {
        this.correctingForAspect = correctingForAspect;
    }
}
