package com.gempukku.libgdx.lib.artemis.animation;

import com.artemis.BaseSystem;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.lib.artemis.animation.animator.Animator;

public class AnimationDirectorSystem extends BaseSystem {
    private ObjectMap<String, AnimationChannel> animationChannels = new ObjectMap<>();

    public void enqueueAnimator(String channel, Animator animator) {
        AnimationChannel animationChannel = getChannel(channel);
        animationChannel.enqueueAnimator(animator);
    }

    public void completeAnimator(String channel) {
        AnimationChannel animationChannel = getChannel(channel);
        animationChannel.completeAnimator();
    }

    public boolean isAnimating(String channel) {
        return getChannel(channel).hasAnimator();
    }

    private AnimationChannel getChannel(String channel) {
        AnimationChannel result = animationChannels.get(channel);
        if (result == null) {
            result = new AnimationChannel();
            animationChannels.put(channel, result);
        }
        return result;
    }

    @Override
    protected void processSystem() {
        for (AnimationChannel value : animationChannels.values()) {
            value.update(world.getDelta());
        }
    }
}
