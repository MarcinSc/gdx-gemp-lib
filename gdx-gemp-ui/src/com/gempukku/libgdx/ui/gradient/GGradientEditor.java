package com.gempukku.libgdx.ui.gradient;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.TimeUtils;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.gempukku.libgdx.ui.DisposableWidget;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerListener;

public class GGradientEditor extends DisposableWidget {
    private static final float MIN_POINT_X_DRAG_DIFFERENCE = 0.001f;
    private static final int PIXMAP_WIDTH = 1024;

    private final DefaultGradientDefinition gradientDefinition;
    private final GGradientEditorStyle style;
    private final Supplier<ColorPicker> colorPickerSupplier;

    private Pixmap pixmap;
    private Texture gradientTexture;

    public GGradientEditor(Supplier<ColorPicker> colorPickerSupplier) {
        this(colorPickerSupplier, new DefaultGradientDefinition());
    }

    public GGradientEditor(Supplier<ColorPicker> colorPickerSupplier, GradientDefinition gradientDefinition) {
        this(colorPickerSupplier, gradientDefinition, "default");
    }

    public GGradientEditor(Supplier<ColorPicker> colorPickerSupplier, GradientDefinition gradientDefinition, String styleName) {
        this(colorPickerSupplier, gradientDefinition, VisUI.getSkin().get(styleName, GGradientEditorStyle.class));
    }

    public GGradientEditor(Supplier<ColorPicker> colorPickerSupplier, GradientDefinition gradientDefinition, GGradientEditorStyle style) {
        this.colorPickerSupplier = colorPickerSupplier;
        this.gradientDefinition = new DefaultGradientDefinition(gradientDefinition);
        this.style = style;
        addListener(new GradientEditorListener());
    }

    public GradientDefinition getGradientDefinition() {
        return gradientDefinition;
    }

    public void setGradientDefinition(GradientDefinition gradientDefinition) {
        GradientDefinition oldGradientDefinition = this.gradientDefinition.cpy();
        this.gradientDefinition.copy(gradientDefinition);
        updateGradientTexture();
        GradientDefinition newGradientDefinition = this.gradientDefinition.cpy();

        SetGradientDefinitionAction setGradientDefinitionAction = new SetGradientDefinitionAction(oldGradientDefinition, newGradientDefinition);
        UndoableChangeEvent event = Pools.obtain(UndoableChangeEvent.class);
        event.setUndoableAction(setGradientDefinitionAction);
        fire(event);
        Pools.free(event);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (style.background != null)
            style.background.draw(batch, getX(), getY(), getWidth(), getHeight());

        float sidePadding = getSidePadding();
        float bottomPadding = getBottomPadding();
        float topPadding = getTopPadding();
        float availableWidth = getWidth() - 2 * sidePadding;
        float availableHeight = getHeight() - bottomPadding - topPadding;
        batch.draw(gradientTexture, getX() + sidePadding, getY() + bottomPadding, availableWidth, availableHeight);

        drawColorTicks(batch, parentAlpha);
    }

    private void drawColorTicks(Batch batch, float parentAlpha) {
        float sidePadding = getSidePadding();
        float bottomPadding = getBottomPadding();
        float availableWidth = getWidth() - 2 * sidePadding;
        for (GradientDefinition.ColorPosition colorPosition : gradientDefinition.getColorPositions()) {
            batch.setColor(colorPosition.color);
            style.tick.draw(batch, getX() + sidePadding + availableWidth * colorPosition.position - (style.tickWidth / 2f),
                    getY() + bottomPadding - (style.tickHeight / 2) - 2,
                    style.tickWidth, style.tickHeight);
        }
    }

    private float getBottomPadding() {
        return (style.tickHeight + 8) / 2f;
    }

    private float getTopPadding() {
        return 8 / 2f;
    }

    private float getSidePadding() {
        return (style.tickWidth + 8) / 2f;
    }

    private float wrapPosition(float x) {
        float padding = getSidePadding();
        float width = getWidth() - 2 * padding;
        return padding + x * width;
    }

    private float unwrapPosition(float x) {
        float padding = getSidePadding();
        float width = getWidth() - 2 * padding;
        return (x - padding) / width;
    }

    @Override
    protected void initializeWidget() {
        pixmap = new Pixmap(PIXMAP_WIDTH, 1, Pixmap.Format.RGB888);
        updateGradientTexture();
    }

    private void updateGradientTexture() {
        if (pixmap != null) {
            if (gradientTexture != null) {
                gradientTexture.dispose();
            }

            pixmap.setColor(Color.WHITE);
            pixmap.fill();
            Array<GradientDefinition.ColorPosition> colorPositions = gradientDefinition.getColorPositions();
            if (colorPositions.size > 0) {
                Color lerpColor = Pools.obtain(Color.class);

                GradientDefinition.ColorPosition firstPoint = colorPositions.get(0);
                Color lastColor = firstPoint.color;
                int drawStart = 0;
                for (GradientDefinition.ColorPosition colorPosition : colorPositions) {
                    int colorStart = MathUtils.round(colorPosition.position * PIXMAP_WIDTH);
                    for (int position = drawStart; position < colorStart; position++) {
                        float gradientT = 1f * (position - drawStart) / (colorStart - drawStart);
                        lerpColor.set(lastColor).lerp(colorPosition.color, gradientT);
                        lerpColor.a = 1;
                        pixmap.drawPixel(position, 0, toRGBA(lerpColor));
                    }
                    drawStart = colorStart;
                    lastColor = colorPosition.color;
                }
                if (drawStart < PIXMAP_WIDTH - 1) {
                    pixmap.setColor(toRGBA(lastColor));
                    pixmap.drawLine(drawStart, 0, PIXMAP_WIDTH - 1, 0);
                }

                Pools.free(lerpColor);
            }
            gradientTexture = new Texture(pixmap);
        }
    }

    private Color calculateColorAt(float position) {
        Color result = Color.WHITE;
        Array<GradientDefinition.ColorPosition> colorPositions = gradientDefinition.getColorPositions();
        if (colorPositions.size > 0) {
            GradientDefinition.ColorPosition firstPoint = colorPositions.get(0);
            if (position <= firstPoint.position)
                return firstPoint.color;
            for (int i = 1; i < colorPositions.size; i++) {
                GradientDefinition.ColorPosition lastPos = colorPositions.get(i - 1);
                GradientDefinition.ColorPosition nextPos = colorPositions.get(i);
                if (lastPos.position < position && position <= nextPos.position) {
                    float t = (position - lastPos.position) / (nextPos.position - lastPos.position);
                    return new Color(lastPos.color).lerp(nextPos.color, t);
                }

            }
            return colorPositions.get(colorPositions.size - 1).color;
        }
        return result;
    }

    private int toRGBA(Color color) {
        return ((int) (255 * color.r) << 24) | ((int) (255 * color.g) << 16) | ((int) (255 * color.b) << 8) | ((int) (255 * color.a));
    }

    @Override
    protected void disposeWidget() {
        gradientTexture.dispose();
        pixmap.dispose();
        gradientTexture = null;
        pixmap = null;
    }

    private void addColor(float position, Color color, GradientDefinition fromGradientDefinition) {
        gradientDefinition.addColor(position, color);
        updateGradientTexture();
        generateEvent(fromGradientDefinition);
    }

    private void updateColor(int index, float position, Color color, GradientDefinition fromGradientDefinition) {
        gradientDefinition.updatePoint(index, position, color);
        updateGradientTexture();
        generateEvent(fromGradientDefinition);
    }

    private void removeColor(int index, GradientDefinition fromGradientDefinition) {
        gradientDefinition.removeColor(index);
        updateGradientTexture();
        generateEvent(fromGradientDefinition);
    }

    private void generateEvent(GradientDefinition fromGradientDefinition) {
        if (fromGradientDefinition != null) {
            GradientDefinition newGradientDefinition = gradientDefinition.cpy();

            SetGradientDefinitionAction setGradientDefinitionAction = new SetGradientDefinitionAction(fromGradientDefinition, newGradientDefinition);
            UndoableChangeEvent event = Pools.obtain(UndoableChangeEvent.class);
            event.setUndoableAction(setGradientDefinitionAction);
            fire(event);
            Pools.free(event);
        } else {
            UndoableChangeEvent event = Pools.obtain(UndoableChangeEvent.class);
            fire(event);
            Pools.free(event);
        }
    }

    private class GradientEditorListener extends InputListener {
        private int draggedIndex = -1;
        private GradientDefinition beforeDragGradientDefinition;
        private long lastClickTime;

        private boolean isPointHit(float point, float position) {
            float distance = Math.abs(wrapPosition(point) - position);
            return distance < style.tickWidth + 2;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (button == Input.Buttons.LEFT) {
                int index = getHitPointIndex(x);
                if (index != -1) {
                    long now = TimeUtils.millis();

                    if (now - lastClickTime < 200) {
                        editColor(index);
                    } else {
                        draggedIndex = index;
                        beforeDragGradientDefinition = gradientDefinition.cpy();
                    }

                    lastClickTime = now;
                } else {
                    final GradientDefinition fromGradientDefinition = gradientDefinition.cpy();
                    final float hitX = clampValue(unwrapPosition(x));
                    Color startColor = calculateColorAt(hitX).cpy();
                    addColor(hitX, startColor, null);
                    final int modifiedIndex = findPointIndex(hitX);

                    ColorPicker colorPicker = colorPickerSupplier.get();
                    colorPicker.setColor(startColor);
                    colorPicker.setModal(true);
                    colorPicker.setAllowAlphaEdit(false);
                    colorPicker.setListener(
                            new ColorPickerListener() {
                                private boolean cancelled = false;

                                @Override
                                public void canceled(Color oldColor) {
                                    cancelled = true;
                                    removeColor(modifiedIndex, null);
                                }

                                @Override
                                public void changed(Color newColor) {
                                    if (!cancelled)
                                        updateColor(modifiedIndex, hitX, newColor, null);
                                }

                                @Override
                                public void reset(Color previousColor, Color newColor) {
                                    if (!cancelled)
                                        updateColor(modifiedIndex, hitX, newColor, null);
                                }

                                @Override
                                public void finished(Color newColor) {
                                    if (!cancelled)
                                        updateColor(modifiedIndex, hitX, newColor, fromGradientDefinition);
                                }
                            });
                    getStage().addActor(colorPicker.fadeIn());
                }
            } else if (button == Input.Buttons.RIGHT) {
                int hitPointIndex = getHitPointIndex(x);
                if (hitPointIndex != -1) {
                    removeColor(hitPointIndex, gradientDefinition.cpy());
                }
            }
            return true;
        }

        private void editColor(final int index) {
            final GradientDefinition fromGradientDefinition = gradientDefinition.cpy();
            final GradientDefinition.ColorPosition colorPosition = gradientDefinition.getColorPositions().get(index);
            Color oldColor = colorPosition.color.cpy();

            ColorPicker colorPicker = colorPickerSupplier.get();
            colorPicker.setColor(oldColor);
            colorPicker.setModal(true);
            colorPicker.setAllowAlphaEdit(false);
            colorPicker.setListener(
                    new ColorPickerListener() {
                        private boolean cancelled;

                        @Override
                        public void canceled(Color oldColor) {
                            cancelled = true;
                            updateColor(index, colorPosition.position, oldColor, null);
                        }

                        @Override
                        public void changed(Color newColor) {
                            if (!cancelled)
                                updateColor(index, colorPosition.position, newColor, null);
                        }

                        @Override
                        public void reset(Color previousColor, Color newColor) {
                            if (!cancelled)
                                updateColor(index, colorPosition.position, newColor, null);
                        }

                        @Override
                        public void finished(Color newColor) {
                            if (!cancelled)
                                updateColor(index, colorPosition.position, newColor, fromGradientDefinition);
                        }
                    });
            getStage().addActor(colorPicker.fadeIn());
        }

        @Override
        public void touchDragged(InputEvent event, float x, float y, int pointer) {
            if (draggedIndex > -1) {
                moveDraggedPoint(x, false);
            }
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            if (draggedIndex > -1 && button == Input.Buttons.LEFT) {
                moveDraggedPoint(x, true);
                draggedIndex = -1;
                beforeDragGradientDefinition = null;
            }
        }

        private void moveDraggedPoint(float x, boolean generateUndoable) {
            Array<GradientDefinition.ColorPosition> colorPositions = gradientDefinition.getColorPositions();
            float hitPosition = clampValue(unwrapPosition(x));

            if (draggedIndex > 0) {
                GradientDefinition.ColorPosition previousPoint = colorPositions.get(draggedIndex - 1);
                if (hitPosition <= previousPoint.position) {
                    hitPosition = previousPoint.position + MIN_POINT_X_DRAG_DIFFERENCE;
                }
            }
            if (draggedIndex < colorPositions.size - 1) {
                GradientDefinition.ColorPosition nextPoint = colorPositions.get(draggedIndex + 1);
                if (hitPosition >= nextPoint.position)
                    hitPosition = nextPoint.position - MIN_POINT_X_DRAG_DIFFERENCE;
            }
            updateColor(draggedIndex, hitPosition, colorPositions.get(draggedIndex).color, generateUndoable?beforeDragGradientDefinition:null);
        }

        private float clampValue(float value) {
            return MathUtils.clamp(value, 0, 1);
        }

        private int findPointIndex(float position) {
            Array<GradientDefinition.ColorPosition> colorPositions = gradientDefinition.getColorPositions();
            for (int i = 0; i < colorPositions.size; i++) {
                if (colorPositions.get(i).position == position)
                    return i;
            }
            return -1;
        }

        private int getHitPointIndex(float mouseX) {
            Array<GradientDefinition.ColorPosition> colorPositions = gradientDefinition.getColorPositions();
            for (int i = 0; i < colorPositions.size; i++) {
                if (isPointHit(colorPositions.get(i).position, mouseX))
                    return i;
            }
            return -1;
        }
    }

    public static class GGradientEditorStyle {
        public Drawable background;
        public float tickWidth = 9f;
        public float tickHeight = 9f;
        public Drawable tick;
    }

    private class SetGradientDefinitionAction extends DefaultUndoableAction {
        private final GradientDefinition oldGradientDefinition;
        private final GradientDefinition newGradientDefinition;

        public SetGradientDefinitionAction(GradientDefinition oldGradientDefinition, GradientDefinition newGradientDefinition) {
            this.oldGradientDefinition = oldGradientDefinition;
            this.newGradientDefinition = newGradientDefinition;
        }

        @Override
        public void undoAction() {
            setGradientDefinition(oldGradientDefinition);
        }

        @Override
        public void redoAction() {
            setGradientDefinition(newGradientDefinition);
        }
    }
}
