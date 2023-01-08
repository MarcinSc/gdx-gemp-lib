package com.gempukku.libgdx.lib.artemis.animation;

import com.badlogic.gdx.utils.Queue;
import com.gempukku.libgdx.lib.artemis.animation.animator.Animator;

public class AnimationChannel {
    private Queue<Animator> animatorQueue = new Queue<>();
    private float animationTime = 0f;

    public void enqueueAnimator(Animator animator) {
        animatorQueue.addLast(animator);
    }

    public void completeAnimator() {
        Animator animator = animatorQueue.removeFirst();
        animator.processToEnd();
        animationTime = 0f;
    }

    public boolean hasAnimator() {
        return !animatorQueue.isEmpty();
    }

    public void update(float delta) {
        if (hasAnimator()) {
            Animator first = animatorQueue.first();
            animationTime += delta;
            boolean finished = first.update(delta, animationTime);
            if (finished) {
                animatorQueue.removeFirst();
                animationTime = 0f;
            }
        }
    }
}
