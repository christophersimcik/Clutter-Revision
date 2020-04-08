package com.example.clutterrevision;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class InterceptRecyclerView extends RecyclerView {
    public InterceptRecyclerView(@NonNull Context context) {
        super(context);
    }

    public InterceptRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InterceptRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println(" h e l  p i n g ");
                return true;
        }
        return false;
        //return super.onInterceptTouchEvent(e);
    }
}
