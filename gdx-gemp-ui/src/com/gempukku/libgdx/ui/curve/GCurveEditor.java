package com.gempukku.libgdx.ui.curve;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.ui.DisposableWidget;
import com.kotcrab.vis.ui.VisUI;

public class GCurveEditor extends DisposableWidget {
    private static final float MIN_POINT_X_DRAG_DIFFERENCE = 0.001f;

    private ShapeRenderer shapeRenderer;
    private CurveDefinition curveDefinition;
    private GCurveEditorStyle style;

    // Temporary drawing variables
    private float padding;
    private float availableWidth;
    private float availableHeight;

    public GCurveEditor() {
        this(new DefaultCurveDefinition());
    }

    public GCurveEditor(CurveDefinition curveDefinition) {
        this(curveDefinition, "default");
    }

    public GCurveEditor(CurveDefinition curveDefinition, String styleName) {
        this(curveDefinition, VisUI.getSkin().get(styleName, GCurveEditorStyle.class));
    }

    public GCurveEditor(CurveDefinition curveDefinition, GCurveEditorStyle style) {
        this.curveDefinition = curveDefinition;
        this.style = style;
        addListener(new CurveEditorListener());
    }

    public CurveDefinition getCurveDefinition() {
        return curveDefinition;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (style.background != null) {
            style.background.draw(batch, getX(), getY(), getWidth(), getHeight());
        }

        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.setTransformMatrix(batch.getTransformMatrix());
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        padding = getPadding();
        availableWidth = getWidth() - 2 * padding;
        availableHeight = getHeight() - 2 * padding;

        drawGrid(parentAlpha);

        drawCurve(parentAlpha);

        drawPoints(parentAlpha);

        shapeRenderer.end();

        batch.begin();
    }

    private void drawGrid(float parentAlpha) {
        Color color = style.gridColor;
        shapeRenderer.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        for (int i = 0; i < 11; i++) {
            // Horizontal
            drawParameterizedLine(0, i / 10f, 1, i / 10f, style.gridThickness);
            // Vertical
            drawParameterizedLine(i / 10f, 0, i / 10f, 1, style.gridThickness);
        }
    }

    private void drawParameterizedLine(float x1, float y1, float x2, float y2, float lineWidth) {
        Vector2 start = Pools.obtain(Vector2.class);
        localToStageCoordinates(start.set(0, 0));
        shapeRenderer.rectLine(start.x + padding + x1 * availableWidth, start.y + padding + y1 * availableHeight,
                start.x + padding + x2 * availableWidth, start.y + padding + y2 * availableHeight, lineWidth);
        Pools.free(start);
    }

    private void drawCurve(float parentAlpha) {
        Array<Vector2> points = curveDefinition.getPoints();
        if (!points.isEmpty()) {
            Color color = style.curveColor;
            shapeRenderer.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            if (points.get(0).x > 0) {
                // Draw first segment up to the point
                float y = points.get(0).y;
                drawParameterizedLine(0, y, points.get(0).x, y, style.curveThickness);
            }
            Vector2 lastPoint = points.get(0);
            for (int i = 1; i < points.size; i++) {
                Vector2 nextPoint = points.get(i);
                drawParameterizedLine(lastPoint.x, lastPoint.y, nextPoint.x, nextPoint.y, style.curveThickness);
                lastPoint = nextPoint;
            }
            if (lastPoint.x < 1) {
                drawParameterizedLine(lastPoint.x, lastPoint.y, 1, lastPoint.y, style.curveThickness);
            }
        }
    }

    private void drawPoints(float parentAlpha) {
        Vector2 start = Pools.obtain(Vector2.class);
        localToStageCoordinates(start.set(0, 0));
        Color color = style.pointsColor;
        shapeRenderer.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        for (Vector2 point : curveDefinition.getPoints()) {
            shapeRenderer.circle(start.x + padding + point.x * availableWidth, start.y + padding + point.y * availableHeight, style.pointSize);
        }
        Pools.free(start);
    }

    private Vector2 wrapPoint(Vector2 vector) {
        float padding = getPadding();
        float width = getWidth() - 2 * padding;
        float height = getHeight() - 2 * padding;
        return vector.set(padding + vector.x * width, padding + vector.y * height);
    }

    private Vector2 unwrapPoint(Vector2 vector) {
        float padding = getPadding();
        float width = getWidth() - 2 * padding;
        float height = getHeight() - 2 * padding;
        return vector.sub(padding, padding).scl(1 / width, 1 / height);
    }

    private float getPadding() {
        return (Math.max(style.pointSize, style.curveThickness) + 8) / 2f;
    }

    private void addPoint(float x, float y) {
        curveDefinition.addPoint(x, y);
        fire(new ChangeListener.ChangeEvent());
    }

    private void updatePoint(int index, float x, float y) {
        curveDefinition.updatePoint(index, x, y);
        fire(new ChangeListener.ChangeEvent());
    }

    private void removePoint(int index) {
        curveDefinition.removePoint(index);
        fire(new ChangeListener.ChangeEvent());
    }

    @Override
    protected void initializeWidget() {
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
    }

    @Override
    protected void disposeWidget() {
        shapeRenderer.dispose();
        shapeRenderer = null;
    }

    private class CurveEditorListener extends InputListener {
        private int draggedIndex = -1;

        private boolean isPointHit(Vector2 point, Vector2 position) {
            Vector2 tmpVector = Pools.obtain(Vector2.class);
            float distance2 = wrapPoint(tmpVector.set(point)).dst2(position);
            boolean hit = distance2 < style.pointSize * style.pointSize * 4;
            Pools.free(tmpVector);
            return hit;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            Vector2 mousePosition = Pools.obtain(Vector2.class).set(x, y);
            if (button == Input.Buttons.LEFT) {
                int hitPointIndex = getHitPointIndex(mousePosition);
                if (hitPointIndex != -1) {
                    draggedIndex = hitPointIndex;
                } else {
                    Vector2 hitPosition = unwrapPoint(mousePosition);
                    clampPoint(hitPosition);

                    addPoint(hitPosition.x, hitPosition.y);
                    draggedIndex = findPointIndex(hitPosition);
                }
            } else if (button == Input.Buttons.RIGHT) {
                int hitPointIndex = getHitPointIndex(mousePosition);
                if (hitPointIndex != -1) {
                    removePoint(hitPointIndex);
                }
            }
            Pools.free(mousePosition);
            return true;
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if (draggedIndex > -1) {
                moveDraggedPoint(x, y);
            }
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if (draggedIndex > -1 && button == Input.Buttons.LEFT) {
                moveDraggedPoint(x, y);
                draggedIndex = -1;
            }
        }

        private void moveDraggedPoint(float x, float y) {
            Vector2 mousePosition = Pools.obtain(Vector2.class).set(x, y);

            Array<Vector2> points = curveDefinition.getPoints();
            Vector2 hitPosition = unwrapPoint(mousePosition);
            clampPoint(hitPosition);

            if (draggedIndex > 0) {
                Vector2 previousPoint = points.get(draggedIndex - 1);
                if (hitPosition.x <= previousPoint.x) {
                    hitPosition.x = previousPoint.x + MIN_POINT_X_DRAG_DIFFERENCE;
                }
            }
            if (draggedIndex < points.size - 1) {
                Vector2 nextPoint = points.get(draggedIndex + 1);
                if (hitPosition.x >= nextPoint.x)
                    hitPosition.x = nextPoint.x - MIN_POINT_X_DRAG_DIFFERENCE;
            }
            updatePoint(draggedIndex, hitPosition.x, hitPosition.y);

            Pools.free(mousePosition);
        }

        private void clampPoint(Vector2 hitPosition) {
            if (hitPosition.x < 0) hitPosition.x = 0;
            if (hitPosition.x > 1) hitPosition.x = 1;
            if (hitPosition.y < 0) hitPosition.y = 0;
            if (hitPosition.y > 1) hitPosition.y = 1;
        }

        private int findPointIndex(Vector2 point) {
            Array<Vector2> points = curveDefinition.getPoints();
            for (int i = 0; i < points.size; i++) {
                if (points.get(i).equals(point))
                    return i;
            }
            return -1;
        }

        private int getHitPointIndex(Vector2 mousePosition) {
            Array<Vector2> points = curveDefinition.getPoints();
            for (int i = 0; i < points.size; i++) {
                if (isPointHit(points.get(i), mousePosition))
                    return i;
            }
            return -1;
        }
    }

    public static class GCurveEditorStyle {
        public Drawable background;
        public float gridThickness = 1f;
        public Color gridColor = new Color(0, 0, 0, 0.3f);
        public float pointSize = 5f;
        public Color pointsColor = new Color(0.3f, 0.3f, 0.7f, 0.9f);
        public float curveThickness = 3f;
        public Color curveColor = new Color(0.7f, 0.3f, 0.3f, 1f);
    }
}
