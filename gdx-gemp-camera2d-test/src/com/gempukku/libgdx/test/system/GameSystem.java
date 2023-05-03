package com.gempukku.libgdx.test.system;

import com.badlogic.gdx.utils.Disposable;

public interface GameSystem extends Disposable {
    void update(float delta);
}
