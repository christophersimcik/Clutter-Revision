package com.example.clutterrevision;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;


public class FragmentButton extends androidx.appcompat.widget.AppCompatImageButton{

    public FragmentButton(Context context) {
        super(context);
    }

    public FragmentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FragmentButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){

        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        Display d = findActivity().getWindowManager().getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int w = p.x;
        int h = p.y;
        int dSize = Math.min(w,h);
        dSize = (int)(dSize * .125);
        int mSpec = Math.min(widthMeasureSpec,heightMeasureSpec);
        dSize = resolveSize(dSize,mSpec);
        dSize = measureDimension(dSize,mSpec);
        setMeasuredDimension(dSize,dSize);

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
