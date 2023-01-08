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
    private Vector3 center;
    private float oldDistance;
    private float distance;
    private float xAxisAngle;
    private float zAxisAngle;

    private Rectangle bounds;
    private Vector2 distanceRange;
    private Vector2 xAxisAngleRange;
    private Vector2 zAxisAngleRange;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getOldDistance() {
        return oldDistance;
    }

    public void setOldDistance(float oldDistance) {
        this.oldDistance = oldDistance;
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

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public Vector3 getCenter() {
        return center;
    }

    public float getDistance() {
        return distance;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getDistanceRange() {
        return distanceRange;
    }

    public float getxAxisAngle() {
        return xAxisAngle;
    }

    public void setxAxisAngle(float xAxisAngle) {
        this.xAxisAngle = xAxisAngle;
    }

    public float getzAxisAngle() {
        return zAxisAngle;
    }

    public void setzAxisAngle(float zAxisAngle) {
        this.zAxisAngle = zAxisAngle;
    }

    public Vector2 getxAxisAngleRange() {
        return xAxisAngleRange;
    }

    public Vector2 getzAxisAngleRange() {
        return zAxisAngleRange;
    }
}
