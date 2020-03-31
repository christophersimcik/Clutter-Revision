package com.example.clutterrevision;

import android.content.Context;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class TitleHelper{

    private Context c;
    private int screenWidth;

    public TitleHelper(Context c){
        this.c = c;
        screenWidth = getScreenWidth();
    }

    private int getScreenWidth(){
        WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public Boolean limitText(CharSequence text){
        float textSize = c.getResources().getDimension(R.dimen.title_text_size);
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        int textWidth = (int)(paint.measureText(text.toString()));
        int maxSize = screenWidth/2;
        if(textWidth > maxSize){
            return true;
        }
            return false;
        }
    }

