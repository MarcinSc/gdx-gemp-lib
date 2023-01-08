package com.gempukku.libgdx.lib.artemis.animation.animator;

public interface Animator {
    boolean update(float delta, float animationTime);

    void processToEnd();
}
