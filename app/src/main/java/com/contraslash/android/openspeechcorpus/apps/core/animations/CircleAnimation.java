package com.contraslash.android.openspeechcorpus.apps.core.animations;

import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by ma0 on 10/30/15.
 */
public class CircleAnimation extends Animation {

    private CircleView circle;

    private float oldRadious;
    private float newRadious;

    public CircleAnimation(CircleView circle, int newRadious) {
        this.oldRadious = circle.getRadious();
        this.newRadious = newRadious;
        this.circle = circle;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        float radious = oldRadious + ((newRadious - oldRadious) * interpolatedTime);

        circle.setRadious((int)radious);
        circle.requestLayout();

    }
}