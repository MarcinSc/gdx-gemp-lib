package com.gempukku.libgdx.lib.artemis.camera.topdown;

import com.artemis.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TopDownCameraComponent extends Component {
    private String name;
    private float near;
    private float far;
    private float fieldOfView;
    private Vector3 focus = new Vector3();
    private float distance;
    private float rotation;
    private float angle;

    private Rectangle bounds;
    private Vector2 distanceRange = new Vector2();
    private Vector2 rotationRange = new Vector2();
    private Vector2 angleRange = new Vector2();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getFieldOfView() {
        return fieldOfView;
    }

    public void setFieldOfView(float fieldOfView) {
        this.fieldOfView = fieldOfView;
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

    public Vector3 getFocus() {
        return focus;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDistance() {
        return distance;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public Vector2 getDistanceRange() {
        return distanceRange;
    }

    public Vector2 getRotationRange() {
        return rotationRange;
    }

    public Vector2 getAngleRange() {
        return angleRange;
    }
}
