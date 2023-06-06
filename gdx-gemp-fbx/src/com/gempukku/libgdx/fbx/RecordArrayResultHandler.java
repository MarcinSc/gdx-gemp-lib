package com.gempukku.libgdx.fbx;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.fbx.handler.FbxRecordHandler;

public interface RecordArrayResultHandler<T> {
    FbxRecordHandler create(Array<T> result);
}
