package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Pools;
import com.gempukku.libgdx.common.Supplier;
import com.gempukku.libgdx.ui.DisposableTable;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class ColorEditorPart extends VisTable implements GraphNodeEditorPart {
    private final String property;
    private final VisImage image;

    private Color oldColor;

    public ColorEditorPart(Supplier<ColorPicker> colorPickerSupplier, String label, String property) {
        this(colorPickerSupplier, label, property, Color.WHITE);
    }

    public ColorEditorPart(Supplier<ColorPicker> colorPickerSupplier, String label, String property, Color defaultColor) {
        this(colorPickerSupplier, label, property, defaultColor, "default");
    }

    public ColorEditorPart(Supplier<ColorPicker> colorPickerSupplier, String label, String property, Color defaultColor, String labelStyleName) {
        this(colorPickerSupplier, label, property, defaultColor, VisUI.getSkin().get(labelStyleName, Label.LabelStyle.class));
    }

    public ColorEditorPart(final Supplier<ColorPicker> colorPickerSupplier, String label, String property, Color defaultColor, Label.LabelStyle labelStyle) {
        this.property = property;
        this.oldColor = defaultColor;

        final Drawable drawable = getSkin().getDrawable("white");
        BaseDrawable baseDrawable = new BaseDrawable(drawable) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                drawable.draw(batch, x, y, width, height);
            }
        };
        baseDrawable.setMinWidth(20);

        image = new VisImage(baseDrawable);
        image.setColor(defaultColor);

        image.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        ColorPicker colorPicker = colorPickerSupplier.get();
                        //displaying picker with fade in animation
                        colorPicker.setColor(oldColor);
                        colorPicker.setListener(
                                new ColorPickerAdapter() {
                                    @Override
                                    public void finished(Color newColor) {
                                        if (!oldColor.equals(newColor)) {
                                            setPickedColor(newColor);
                                        }
                                    }
                                });
                        image.getStage().addActor(colorPicker.fadeIn());
                    }
                });

        add(new VisLabel(label, labelStyle)).growX();
        add(image).fillY();
        row();
    }

    private void setPickedColor(Color color) {
        image.setColor(color);
        UndoableChangeEvent event = Pools.obtain(UndoableChangeEvent.class);
        event.setUndoableAction(new SetColorAction(oldColor, color));
        fire(event);
        Pools.free(event);
        oldColor = color;
    }

    @Override
    public Actor getActor() {
        return this;
    }

    @Override
    public GraphNodeEditorOutput getOutputConnector() {
        return null;
    }

    @Override
    public GraphNodeEditorInput getInputConnector() {
        return null;
    }

    public Color getColor() {
        return new Color(image.getColor());
    }

    @Override
    public void initialize(JsonValue data) {
        if (data != null) {
            String value = data.getString(property);
            Color color = Color.valueOf(value);
            setPickedColor(color);
        }
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(image.getColor().toString()));
    }

    private class SetColorAction extends DefaultUndoableAction {
        private final Color oldColor;
        private final Color newColor;

        public SetColorAction(Color oldColor, Color newColor) {
            this.oldColor = oldColor;
            this.newColor = newColor;
        }

        @Override
        public void undoAction() {
            setPickedColor(oldColor);
        }

        @Override
        public void redoAction() {
            setPickedColor(newColor);
        }
    }
}
