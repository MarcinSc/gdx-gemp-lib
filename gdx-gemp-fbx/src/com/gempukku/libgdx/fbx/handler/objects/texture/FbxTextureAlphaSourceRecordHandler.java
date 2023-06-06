package com.gempukku.libgdx.fbx.handler.objects.texture;

import com.gempukku.libgdx.common.Consumer;
import com.gempukku.libgdx.fbx.handler.AbstractEnumRecordHandler;

public class FbxTextureAlphaSourceRecordHandler  extends AbstractEnumRecordHandler<FbxTextureAlphaSource> {
    public FbxTextureAlphaSourceRecordHandler(Consumer<FbxTextureAlphaSource> textureAlphaSourceConsumer) {
        super(textureAlphaSourceConsumer);
    }

    protected FbxTextureAlphaSource mapToEnum(String valueString) {
        switch (valueString) {
            case "None":
                return FbxTextureAlphaSource.None;
            case "Alpha_Black":
                return FbxTextureAlphaSource.Black;
            case "RgbIntensity":
                return FbxTextureAlphaSource.RgbIntensity;
            default:
                throw new UnsupportedOperationException("Unknown type of FbxTextureAlphaSource: "+valueString);
        }
    }
}
