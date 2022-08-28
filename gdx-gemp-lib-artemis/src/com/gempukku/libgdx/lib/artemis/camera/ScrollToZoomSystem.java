package com.gempukku.libgdx.lib.artemis.camera;

import com.artemis.BaseSystem;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.gempukku.libgdx.lib.artemis.input.InputProcessorProvider;

public class ScrollToZoomSystem extends BaseSystem implements InputProcessorProvider {
    private final InputAdapter inputAdapter;
    private final float distanceMultiplier;
    private final int processorPriority;

    private CameraSystem cameraSystem;

    public ScrollToZoomSystem(int processorPriority, float distanceMultiplier) {
        this.processorPriority = processorPriority;
        this.distanceMultiplier = distanceMultiplier;
        inputAdapter = new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                return mouseWheelScrolled(amountY);
            }
        };
    }

    private boolean mouseWheelScrolled(float amount) {
        CameraController cameraController = cameraSystem.getCameraController();
        if (cameraController instanceof ZoomCameraController) {
            ((ZoomCameraController) cameraController).zoom(amount * distanceMultiplier);
            return true;
        }
        return false;
    }

    @Override
    public int getInputPriority() {
        return processorPriority;
    }

    @Override
    public InputProcessor getInputProcessor() {
        return inputAdapter;
    }

    @Override
    protected void processSystem() {

    }
}
