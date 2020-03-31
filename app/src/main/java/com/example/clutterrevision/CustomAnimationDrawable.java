package com.example.clutterrevision;

import android.graphics.drawable.AnimationDrawable;

public class CustomAnimationDrawable extends AnimationDrawable {
    @Override
    protected boolean onStateChange(int[] state) {
        System.out.println("animation drawable: state = " + state);
        System.out.println("animation drawable: frame = " + getCurrent());
        return super.onStateChange(state);

    }
}
