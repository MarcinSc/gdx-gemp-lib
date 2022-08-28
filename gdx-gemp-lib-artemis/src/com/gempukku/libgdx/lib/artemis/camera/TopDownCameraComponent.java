package com.gempukku.libgdx.lib.artemis.camera;

import com.artemis.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TopDownCameraComponent extends Component {
    private float near;
    private float far;
    private float fieldOfView;
    private Vector3 center;
    private float distance;
    private float yAxisAngle;
    private float zAxisAngle;

    private Rectangle bounds;
    private Vector2 distanceRange;
    private Vector2 yAxisAngleRange;
    private Vector2 zAxisAngleRange;

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

    public void setyAxisAngle(float yAxisAngle) {
        this.yAxisAngle = yAxisAngle;
    }

    public void setzAxisAngle(float zAxisAngle) {
        this.zAxisAngle = zAxisAngle;
    }

    public Vector3 getCenter() {
        return center;
    }

    public float getDistance() {
        return distance;
    }

    public float getyAxisAngle() {
        return yAxisAngle;
    }

    public float getzAxisAngle() {
        return zAxisAngle;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Vector2 getDistanceRange() {
        return distanceRange;
    }

    public Vector2 getyAxisAngleRange() {
        return yAxisAngleRange;
    }

    public Vector2 getzAxisAngleRange() {
        return zAxisAngleRange;
    }
}
