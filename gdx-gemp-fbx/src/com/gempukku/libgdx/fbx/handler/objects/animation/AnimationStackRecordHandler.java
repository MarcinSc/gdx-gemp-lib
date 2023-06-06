package com.gempukku.libgdx.fbx.handler.objects.animation;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.generic.GenericRecordHandler;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;
import com.gempukku.libgdx.fbx.handler.properties.PropertiesRecordHandler;

public class AnimationStackRecordHandler extends GenericRecordHandler implements Function<String, FbxRecordHandler> {
    private Array<FbxProperty> properties = new Array<>();

    private Array<FbxAnimationStack> animationStacks = new Array<>();

    public AnimationStackRecordHandler() {
        super("AnimationStack");
    }

    @Override
    public FbxRecordHandler evaluate(String s) {
        if (s.equals("AnimationStack"))
            return this;
        return null;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        switch (name) {
            case "Properties70":
                return new PropertiesRecordHandler(new Consumer<Array<FbxProperty>>() {
                    @Override
                    public void consume(Array<FbxProperty> fbxProperties) {
                        properties.addAll(fbxProperties);
                    }
                });
        }
        return super.newRecord(name);
    }

    @Override
    public void endOfRecord() {
        super.endOfRecord();

        animationStacks.add(new FbxAnimationStack(values, properties));
        values = new Array<>();
        properties = new Array<>();
    }
}
