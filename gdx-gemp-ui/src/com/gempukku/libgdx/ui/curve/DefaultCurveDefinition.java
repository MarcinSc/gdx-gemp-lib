package com.gempukku.libgdx.ui.curve;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;

public class DefaultCurveDefinition implements CurveDefinition {
    private static final Comparator<Vector2> xAscending = new Comparator<Vector2>() {
        @Override
        public int compare(Vector2 o1, Vector2 o2) {
            return Float.compare(o1.x, o2.x);
        }
    };

    private Array<Vector2> points = new Array<>();

    public DefaultCurveDefinition() {
    }

    public DefaultCurveDefinition(Array<Vector2> points) {
        this.points.addAll(points);
        this.points.sort(xAscending);
    }

    public Array<Vector2> getPoints() {
        return points;
    }

    public void removePoint(int index) {
        points.removeIndex(index);
    }

    public void addPoint(float x, float y) {
        Vector2 pointWithX = getPointWithX(x);
        if (pointWithX != null) {
            pointWithX.y = y;
        } else {
            points.add(new Vector2(x, y));
            points.sort(xAscending);
        }
    }

    @Override
    public void updatePoint(int index, float x, float y) {
        points.get(index).set(x, y);
        points.sort(xAscending);
    }

    private Vector2 getPointWithX(float x) {
        for (Vector2 point : points) {
            if (point.x == x)
                return point;
        }
        return null;
    }
}
