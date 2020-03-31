package com.example.clutterrevision;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class CustomGestureListener extends GestureDetector.SimpleOnGestureListener {
    FragmentCalendar fragmentCalendar;
    public CustomGestureListener(FragmentCalendar fragmentCalendar){
        this.fragmentCalendar = fragmentCalendar;
    }
    @Override
    public boolean onDown(MotionEvent e) {
        return super.onDown(e);
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e2.getX()<e1.getX()) {
            this.fragmentCalendar.onFling(true);
        }else{
            this.fragmentCalendar.onFling(false);
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }
}
