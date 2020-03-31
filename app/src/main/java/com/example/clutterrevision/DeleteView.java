package com.example.clutterrevision;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.View;

import androidx.annotation.Nullable;

public class DeleteView extends View {
    private Boolean pressed = false;
    float width, height, centerVert, centerHor;
    Paint barPaint = new Paint();
    float start = -25, end = -25;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run(){
            if(pressed){
                if(end < width){
                    System.out.println(" start and end " + start + "< s / e > " + end);
                    advanceDeletionBar();
                    handler.postDelayed(this,10);
                }else{
                    handler.removeCallbacks(this);
                }
            }else{
               if( end > -25){
                   recedeDeletionBar();
                   handler.postDelayed(this,10);
               }else{
                   handler.removeCallbacks(this);
               }
            }
        }
    };

    public DeleteView(Context context) {
        super(context);
        setupPaint();
    }

    public DeleteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    public DeleteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupPaint();
    }

    public DeleteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setupPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(start,centerVert,end,centerVert,barPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        this.centerHor = w/2;
        this.centerVert = h/2;
        System.out.println("delete_view width = " + w);
        System.out.println("delete_view height = " + h);
    }

    private void start(){
        handler.post(runnable);
    }

    private void advanceDeletionBar(){
        if(end < width) {
            end+=10;
        }
        invalidate();
    }

    private void recedeDeletionBar(){
        if(end > -25) {
            end-=25;
        }
        invalidate();
    }


    private void setupPaint(){
        barPaint.setStrokeWidth(50);
        barPaint.setStyle(Paint.Style.STROKE);
        barPaint.setColor(Color.BLACK);
        barPaint.setStrokeCap(Paint.Cap.BUTT);
    }

    public void inputPressed(Boolean bool){
        if(!pressed && bool){
            System.out.println("delete view: pressed = " + bool);
            pressed = bool;
            start();
        }
        if(!bool){
            pressed = bool;
        }

    }

    public Boolean retrievePressed(){
        return pressed;
    }

}
