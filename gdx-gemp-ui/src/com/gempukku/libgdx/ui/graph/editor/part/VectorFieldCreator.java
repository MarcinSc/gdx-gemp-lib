package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class VectorFieldCreator {
    public static VisValidatableTextField createInput(InputValidator inputValidator, float defaultValue, VisTextField.VisTextFieldStyle textFieldStyle) {
        final VisValidatableTextField result = new VisValidatableTextField(String.valueOf(defaultValue), textFieldStyle) {
            @Override
            public float getPrefWidth() {
                return 50;
            }
        };
        result.addValidator(Validators.FLOATS);
        if (inputValidator != null)
            result.addValidator(inputValidator);
        result.setRestoreLastValid(true);
        result.setAlignment(Align.right);
        result.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        if (result.isInputValid()) {
                            result.fire(new GraphChangedEvent(false, true));
                        }
                    }
                });
        return result;
    }
}
