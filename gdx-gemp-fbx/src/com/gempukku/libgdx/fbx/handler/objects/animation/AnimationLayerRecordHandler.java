package com.gempukku.libgdx.fbx.handler.objects.animation;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.common.Function;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;
import com.gempukku.libgdx.fbx.handler.generic.GenericRecordHandler;
import com.gempukku.libgdx.fbx.handler.properties.FbxProperty;
import com.gempukku.libgdx.fbx.handler.properties.PropertiesRecordHandler;

public class AnimationLayerRecordHandler extends GenericRecordHandler implements Function<String, FbxRecordHandler> {
    private Array<FbxAnimationLayer> animationLayers = new Array<>();

    public AnimationLayerRecordHandler() {
        super("AnimationLayer");
    }

    @Override
    public FbxRecordHandler evaluate(String s) {
        if (s.equals("AnimationLayer"))
            return this;
        return null;
    }

    @Override
    public FbxRecordHandler newRecord(String name) {
        return super.newRecord(name);
    }

    @Override
    public void endOfRecord() {
        super.endOfRecord();

        animationLayers.add(new FbxAnimationLayer(values));
        values = new Array<>();
    }
}
