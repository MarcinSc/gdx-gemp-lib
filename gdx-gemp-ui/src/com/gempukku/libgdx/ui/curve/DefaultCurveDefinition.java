package com.gempukku.libgdx.ui.curve;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.Comparator;
import java.util.Objects;

public class DefaultCurveDefinition implements CurveDefinition {
    private static final Comparator<Vector2> xAscending = new Comparator<Vector2>() {
        @Override
        public int compare(Vector2 o1, Vector2 o2) {
            return Float.compare(o1.x, o2.x);
        }
    };

    private final Array<Vector2> points = new Array<>();

    public DefaultCurveDefinition() {
        addPoint(0, 0);
    }

    public DefaultCurveDefinition(CurveDefinition curveDefinition) {
        this(curveDefinition.getPoints());
    }

    public DefaultCurveDefinition(Iterable<Vector2> points) {
        for (Vector2 point : points) {
            addPoint(point);
        }
        this.points.sort(xAscending);
    }

    public void copy(CurveDefinition curveDefinition) {
        points.clear();
        for (Vector2 point : curveDefinition.getPoints()) {
            addPoint(point);
        }
    }

    public Array<Vector2> getPoints() {
        return points;
    }

    public void removePoint(int index) {
        points.removeIndex(index);
    }

    public void addPoint(Vector2 point) {
        addPoint(point.x, point.y);
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

    public void clear() {
        points.clear();
    }

    private Vector2 getPointWithX(float x) {
        for (Vector2 point : points) {
            if (point.x == x)
                return point;
        }
        return null;
    }

    public DefaultCurveDefinition cpy() {
        return new DefaultCurveDefinition(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultCurveDefinition that = (DefaultCurveDefinition) o;
        return points.equals(that.points);
    }

    @Override
    public int hashCode() {
        return Objects.hash(points);
    }
}
