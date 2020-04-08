package com.example.clutterrevision;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class CustomRelativeLayout extends RelativeLayout {
    CallbackTouch callbackTouch;

    public CustomRelativeLayout(Context context) {
        super(context);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                callbackTouch.down(ev.getX());
                return false;
            case MotionEvent.ACTION_MOVE:
                callbackTouch.move(ev.getX());
                return false;
            case MotionEvent.ACTION_UP:
                callbackTouch.up();
                return false;
        }

        return super.onInterceptTouchEvent(ev);
    }

    public void setCallbackTouch(CallbackTouch callbackTouch){
        this.callbackTouch = callbackTouch;
    }

    public interface CallbackTouch{
        void down(float downX);
        void move(float moveX);
        void up();
    }
}

