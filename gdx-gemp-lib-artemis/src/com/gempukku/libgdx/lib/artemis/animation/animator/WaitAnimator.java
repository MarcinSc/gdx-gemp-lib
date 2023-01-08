package com.gempukku.libgdx.lib.artemis.animation.animator;

public class WaitAnimator implements Animator {
    private float duration;

    public WaitAnimator(float duration) {
        this.duration = duration;
    }

    @Override
    public boolean update(float delta, float animationTime) {
        return animationTime >= duration;
    }

    @Override
    public void processToEnd() {

    }
}
