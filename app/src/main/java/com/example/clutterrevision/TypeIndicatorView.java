package com.example.clutterrevision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class TypeIndicatorView extends View {

    private Boolean note = false;
    private Boolean audio = false;
    private Boolean reference = false;
    private Boolean list = false;

    Boolean isCirclular = false;

    private float numberTypes = 4;
    private float divisions;
    private float offset = 5;
    private float myWidth;
    private float myHeight;
    private float center;
    private Path notePath, audioPath, referencePath, listPath;
    private Path circlularMask;

    public TypeIndicatorView(Context context) {
        super(context);
    }

    public TypeIndicatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TypeIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TypeIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isCirclular) {
            canvas.clipPath(circlularMask);
        }
        if(note) {
            drawNote(canvas);
        }
        if (audio) {
            drawAudio(canvas);
        }
        if (reference) {
            drawReference(canvas);
        }
        if (list) {
            drawList(canvas);
        }

        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.myHeight = (float) h - offset;
        this.myWidth = (float) w - offset;
        this.center = w/2;
        divisions= myWidth/numberTypes;
        circlularMask = makeCirclularMask();
        resetPaths();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void resetPaths() {
        notePath = makeNotePath();
        audioPath = makeAudioPath();
        referencePath = makeReferencePath();
        listPath = makeListPath();
        invalidate();
    }

    private Path makeCirclularMask(){
        Path circlularMask = new Path();
        circlularMask.addCircle(this.center,this.center,center-(offset*2), Path.Direction.CW);
        return circlularMask;
    }

    private void drawNote(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.note, null));
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(50);
        canvas.drawPath(notePath, paint);

    }

    private void drawAudio(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.audio, null));
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(50);
        canvas.drawPath(audioPath, paint);
    }

    private void drawReference(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.reference, null));
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(50);
        canvas.drawPath(referencePath, paint);
    }

    private void drawList(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.list, null));
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(50);
        canvas.drawPath(listPath, paint);

    }

    private Path makeNotePath() {
        Path notePath = new Path();
        notePath.moveTo(offset, offset);
        notePath.lineTo(divisions, offset);
        notePath.lineTo(divisions,myHeight);
        notePath.lineTo(offset,myHeight);
        notePath.lineTo(offset,offset);
        notePath.close();
        return notePath;
    }

    private Path makeReferencePath() {
        Path referencePath = new Path();
        referencePath.moveTo(divisions, offset);
        referencePath.lineTo(divisions*2, offset);
        referencePath.lineTo(divisions*2,myHeight);
        referencePath.lineTo(divisions, myHeight);
        referencePath.lineTo(divisions,offset);
        referencePath.close();
        return referencePath;
    }

    private Path makeAudioPath() {
        Path audioPath = new Path();
        audioPath.moveTo(divisions*2, offset);
        audioPath.lineTo(divisions*3, offset);
        audioPath.lineTo(divisions*3,myHeight);
        audioPath.lineTo(divisions*2, myHeight);
        audioPath.lineTo(divisions*2,offset);
        audioPath.close();
        return audioPath;
    }

    private Path makeListPath() {
        Path listPath = new Path();
        listPath.moveTo(divisions*3, offset);
        listPath.lineTo(divisions*4, offset);
        listPath.lineTo(divisions*4,myHeight);
        listPath.lineTo(divisions*3, myHeight);
        listPath.lineTo(divisions*3,offset);
        listPath.close();
        return listPath;
    }

    public void setIsCircular(Boolean isCirclular){
        System.out.println("set is Circular to :" + isCirclular);
        this.isCirclular = isCirclular;
        this.invalidate();
    }

    public Boolean getIsCirclular(){
        return this.isCirclular;
    }

    public void setTypes(int n, int a, int r, int b, int l) {
        if (n > 0) {
            this.note = true;
        }
        if (a > 0) {
            this.audio = true;
        }
        if (r > 0 || b > 0) {
            this.reference = true;
        }
        if (l > 0) {
            this.list = true;
        }
        this.invalidate();
    }

}
