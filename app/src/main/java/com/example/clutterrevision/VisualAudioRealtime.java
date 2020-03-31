package com.example.clutterrevision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class VisualAudioRealtime extends View {

    private static final int shortMin = -32768;
    private static final int shortMax = 32767;

    float txtWidth = 0f;
    float txtHeight = 0f;
    float rightTxtPadding = 0f;
    float bottomTxtPadding = 0f;

    int gray = this.getResources().getColor(android.R.color.tab_indicator_text,null);

    public short[] pnts;

    public String time = "";

    int w = 0, h = 0, cWidth = 0, cHeight = 0;

    Paint maxPaint = new Paint();
    Paint txtPaint = new Paint();

    public VisualAudioRealtime(Context context) {
        super(context);
    }

    public VisualAudioRealtime(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VisualAudioRealtime(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VisualAudioRealtime(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh){
        this.w = w;
        this.h = h;
        this.cWidth = w/2;
        this.cHeight = h/2;
        txtPaint.setTextSize(findTextSz());
        setupPaint();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(pnts != null) {
            drawPath(canvas, maxPaint);
            drawText(canvas);
        }
    }

    public float findTextSz(){
        String s = "00.00.00.00";
        float textSz = 0f;
        float targetSz = w/4;
        Rect rect = new Rect();
        while(txtWidth < targetSz){
            txtPaint.setTextSize(textSz);
            txtPaint.getTextBounds(s,0,s.length(),rect);
            txtWidth = rect.width();
            txtHeight = rect.height();
            rightTxtPadding = w * .05f;
            bottomTxtPadding = h * .10f;
            textSz ++;
        }
        return textSz;
    }

    private void drawPath(Canvas c, Paint p){
        c.drawPath(setPathNew(pnts),p);
    }

    private void drawText(Canvas c){
        c.drawText(time,w-(txtWidth+rightTxtPadding), h - (txtHeight + bottomTxtPadding),txtPaint);
    }

    public Path setPathNew(short[] s){
        float[] f = new float[s.length];

        for(int i = 0; i < s.length; i ++){
            int t = s[i];
            f[i]= ((t - shortMin) * (h) / (shortMax-shortMin));
        }

        float length = f.length;
        float incr = (w/length);
        float xVal = 0;
        Path p = new Path();
        p.moveTo(xVal,cHeight);
        for(int i = 0; i < f.length-1;i++){
            xVal += incr;
            p.lineTo(xVal,f[i]);
        }
        return p;
    }

    private void setupPaint(){

        // setup view paint
        maxPaint.setStyle(Paint.Style.STROKE);
        int[] color = new int[]{Color.TRANSPARENT,gray,Color.TRANSPARENT};
        LinearGradient linearGradient = new LinearGradient(0,cHeight,w,cHeight,color,null,Shader.TileMode.MIRROR);
        maxPaint.setShader(linearGradient);
        maxPaint.setAntiAlias(true);
        maxPaint.setStrokeWidth(10f);
        // setup text paint
        txtPaint.setColor(gray);
        txtPaint.setAntiAlias(true);

    }

}
