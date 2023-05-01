package com.gempukku.libgdx.ui.graph.editor.part;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.ui.graph.GraphChangedEvent;
import com.gempukku.libgdx.ui.undo.UndoableValidatableTextField;
import com.kotcrab.vis.ui.util.InputValidator;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class VectorFieldCreator {
    public static VisValidatableTextField createInput(InputValidator inputValidator, float defaultValue, VisTextField.VisTextFieldStyle textFieldStyle) {
        final VisValidatableTextField result = new UndoableValidatableTextField(String.valueOf(defaultValue), textFieldStyle) {
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
        return result;
    }
}
