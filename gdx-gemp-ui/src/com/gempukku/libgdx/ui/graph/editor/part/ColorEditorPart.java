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
import com.gempukku.libgdx.ui.DisposableTable;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.gempukku.libgdx.undo.DefaultUndoableAction;
import com.gempukku.libgdx.undo.event.UndoableChangeEvent;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;

public class ColorEditorPart extends DisposableTable implements GraphNodeEditorPart {
    private final String property;
    private final String colorPickerStyleName;
    private final VisImage image;
    private ColorPicker picker;

    private Color oldColor;

    public ColorEditorPart(String label, String property) {
        this(label, property, Color.WHITE);
    }

    public ColorEditorPart(String label, String property, Color defaultColor) {
        this(label, property, defaultColor, "default", "default");
    }

    public ColorEditorPart(String label, String property, Color defaultColor, String labelStyleName, String colorPickerStyleName) {
        this(label, property, defaultColor, VisUI.getSkin().get(labelStyleName, Label.LabelStyle.class), colorPickerStyleName);
    }

    public ColorEditorPart(String label, String property, Color defaultColor, Label.LabelStyle labelStyle, String colorPickerStyleName) {
        this.property = property;
        this.colorPickerStyleName = colorPickerStyleName;
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
                        //displaying picker with fade in animation
                        picker.setColor(oldColor);
                        image.getStage().addActor(picker.fadeIn());
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

    @Override
    protected void initializeWidget() {
        picker = new ColorPicker(colorPickerStyleName, new ColorPickerAdapter() {
            @Override
            public void finished(Color newColor) {
                if (!oldColor.equals(newColor)) {
                    setPickedColor(newColor);
                }
            }
        });
    }

    @Override
    protected void disposeWidget() {
        picker.dispose();
        picker = null;
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
