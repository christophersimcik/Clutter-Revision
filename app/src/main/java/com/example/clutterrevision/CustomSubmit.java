package com.example.clutterrevision;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.Button;

public class CustomSubmit extends androidx.appcompat.widget.AppCompatButton {
    public CustomSubmit(Context context) {
        super(context);
    }

    public CustomSubmit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSubmit(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){

        Display d = findActivity().getWindowManager().getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int w = p.x;
        int h = p.y;
        int min = Math.min(w,h);
        int buttonWidth = min / 4;
        int buttonHeight = min / 8;
        int mSpec = Math.min(widthMeasureSpec,heightMeasureSpec);
        buttonWidth = resolveSize(buttonWidth,mSpec);
        buttonWidth = measureDimension(buttonWidth,mSpec);
        setMeasuredDimension(buttonWidth,buttonHeight);

    }

    private int measureDimension(int dynamicSize, int measureSpec){

        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if(specMode == MeasureSpec.EXACTLY){
            result =  specSize;
        }else{
            if(specMode == MeasureSpec.AT_MOST){
                result = Math.min(dynamicSize,specSize);
            }else{
                result = dynamicSize;
            }
        }
        return result;
    }

    private Activity findActivity(){
        Context c = this.getContext();
        while(c instanceof ContextWrapper){
            if( c instanceof Activity){
                return (Activity) c;
            }
            c = ((ContextWrapper)c).getBaseContext();
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }
}
