package com.gempukku.libgdx.lib.artemis.camera.orthographic;

import com.artemis.Component;
import com.badlogic.gdx.math.Vector3;

public class OrthographicCameraComponent extends Component {
    private String name;
    private float near;
    private float far;
    private Vector3 position = new Vector3(0, 0, 0);
    private Vector3 direction = new Vector3(0, 0, -1);
    private Vector3 up = new Vector3(0, 1, 0);

    private float height;

    public Vector3 getPosition() {
        return position;
    }

    public Vector3 getDirection() {
        return direction;
    }

    public Vector3 getUp() {
        return up;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        this.near = near;
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        this.far = far;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
