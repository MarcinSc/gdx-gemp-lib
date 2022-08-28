package com.gempukku.libgdx.lib.artemis.camera;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.gempukku.libgdx.lib.artemis.input.InputProcessorProvider;

public class DragRightButtonToTiltZAxisSystem extends BaseSystem implements InputProcessorProvider {
    private final InputAdapter inputAdapter;
    private final float angleMultiplier;
    private final int processorPriority;

    private CameraSystem cameraSystem;

    public DragRightButtonToTiltZAxisSystem(int processorPriority, float angleMultiplier) {
        this.processorPriority = processorPriority;
        this.angleMultiplier = angleMultiplier;
        inputAdapter = new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                    int deltaY = Gdx.input.getDeltaY();
                    rightButtonDragged(deltaY);
                    return true;
                }
                return false;
            }
        };
    }

    private boolean rightButtonDragged(float amount) {
        CameraController cameraController = cameraSystem.getCameraController();
        if (cameraController instanceof ZAxisTiltCameraController) {
            ((ZAxisTiltCameraController) cameraController).moveZAxisAngle(amount * angleMultiplier);
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