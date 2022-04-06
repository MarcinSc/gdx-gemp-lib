package com.gempukku.libgdx.lib.test.system;

import com.badlogic.gdx.utils.Disposable;

public interface GameSystem extends Disposable {
    void update(float delta);
}
