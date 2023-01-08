package com.gempukku.libgdx.lib.artemis.camera;

import com.artemis.BaseSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.gempukku.libgdx.lib.artemis.input.InputProcessorProvider;

public class DragRightButtonToTiltZAxisSystem extends BaseSystem implements InputProcessorProvider {
    private final InputAdapter inputAdapter;

    private String cameraName;
    private final int processorPriority;
    private final float angleMultiplier;

    private CameraSystem cameraSystem;

    public DragRightButtonToTiltZAxisSystem(String cameraName, int processorPriority, float angleMultiplier) {
        this.cameraName = cameraName;
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
        CameraController cameraController = cameraSystem.getCameraController(cameraName);
        if (cameraController instanceof ZAxisTiltCameraController) {
            ((ZAxisTiltCameraController) cameraController).moveZAxisAngle(cameraName, amount * angleMultiplier);
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