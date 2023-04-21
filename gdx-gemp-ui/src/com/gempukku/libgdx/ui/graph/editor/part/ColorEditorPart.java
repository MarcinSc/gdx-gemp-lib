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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.graph.data.Graph;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorInput;
import com.gempukku.libgdx.ui.graph.editor.GraphNodeEditorOutput;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.color.ColorPicker;
import com.kotcrab.vis.ui.widget.color.ColorPickerAdapter;
import com.kotcrab.vis.ui.widget.color.ColorPickerStyle;

public class ColorEditorPart extends VisTable implements GraphNodeEditorPart, Disposable {
    private final String property;
    private final VisImage image;
    private final ColorPicker picker;

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

        final Drawable drawable = getSkin().getDrawable("white");
        BaseDrawable baseDrawable = new BaseDrawable(drawable) {
            @Override
            public void draw(Batch batch, float x, float y, float width, float height) {
                drawable.draw(batch, x, y, width, height);
            }
        };
        baseDrawable.setMinSize(20, 20);

        image = new VisImage(baseDrawable);
        image.setColor(defaultColor);

        picker = new ColorPicker(colorPickerStyleName, new ColorPickerAdapter() {
            @Override
            public void finished(Color newColor) {
                image.setColor(newColor);
                image.fire(new GraphChangedEvent(false, true));
            }
        });
        picker.setColor(defaultColor);

        image.addListener(
                new ClickListener(Input.Buttons.LEFT) {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        //displaying picker with fade in animation
                        image.getStage().addActor(picker.fadeIn());
                    }
                });

        add(new VisLabel(label, labelStyle)).growX();
        add(image);
        row();
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
            image.setColor(color);
        }
    }

    @Override
    public void serializePart(JsonValue object) {
        object.addChild(property, new JsonValue(image.getColor().toString()));
    }

    @Override
    public void dispose() {
        picker.dispose();
    }
}
