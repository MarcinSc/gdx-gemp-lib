package com.gempukku.libgdx.lib.artemis.animation.animator;

import com.artemis.Entity;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.gempukku.libgdx.lib.artemis.transform.TransformSystem;

public class TransformAnimator implements Animator {
    private static final Matrix4 tmpMatrix4 = new Matrix4();
    private static final Vector3 tmpVector3_1 = new Vector3();
    private static final Vector3 tmpVector3_2 = new Vector3();
    private static final Quaternion tmpQuaternion_1 = new Quaternion();
    private static final Quaternion tmpQuaternion_2 = new Quaternion();

    private Entity entity;
    private Matrix4 from;
    private Matrix4 to;
    private float duration;
    private Interpolation interpolation;
    private TransformSystem transformSystem;

    public TransformAnimator(Entity entity, Matrix4 from, Matrix4 to, float duration,
                             Interpolation interpolation, TransformSystem transformSystem) {
        this.entity = entity;
        this.from = from;
        this.to = to;
        this.duration = duration;
        this.interpolation = interpolation;
        this.transformSystem = transformSystem;
    }

    @Override
    public boolean update(float delta, float animationTime) {
        float alpha = interpolation.apply(Math.min(1, animationTime / duration));

        tmpMatrix4.idt();

        Vector3 translation = from.getTranslation(tmpVector3_1).scl(1 - alpha).mulAdd(to.getTranslation(tmpVector3_2), alpha);
        tmpMatrix4.trn(translation);

        Quaternion rotation = from.getRotation(tmpQuaternion_1).mul(1 - alpha).add(to.getRotation(tmpQuaternion_2).mul(alpha));
        tmpMatrix4.rotate(rotation);

        Vector3 scale = from.getScale(tmpVector3_1).scl(1 - alpha).mulAdd(to.getTranslation(tmpVector3_2), alpha);
        tmpMatrix4.scl(scale);

        transformSystem.setTransform(entity, tmpMatrix4);

        return animationTime >= duration;
    }

    @Override
    public void processToEnd() {
        transformSystem.setTransform(entity, to);
    }
}
