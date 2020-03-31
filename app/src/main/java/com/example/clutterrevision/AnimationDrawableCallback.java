package com.example.clutterrevision;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import javax.security.auth.callback.Callback;

public abstract class AnimationDrawableCallback extends AnimationDrawable implements Callback {

    Drawable lastFrame;
    Callback myCallback;
    Boolean isCallBackTriggered = false;

    public AnimationDrawableCallback(AnimationDrawable animationDrawable, Callback callback) {
        lastFrame = animationDrawable.getFrame(animationDrawable.getNumberOfFrames() - 1);
        myCallback = callback;
    }

    @Override
    protected boolean onStateChange(int[] state) {

        return super.onStateChange(state);
    }

    public abstract void onAnimationComplete();

}
