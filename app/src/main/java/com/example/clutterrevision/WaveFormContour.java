package com.example.clutterrevision;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

public class WaveFormContour {
    private RectF[] rects;
    public WaveFormContour(RectF[] rects){
        this.rects = rects;
    }
    public Path pathFinder(){
        Path path = new Path();
        RectF previousRect = rects[0];
        path.moveTo(rects[0].left,rects[0].top);
        path.lineTo(rects[0].left,rects[0].bottom);
        path.lineTo(rects[0].right,rects[0].bottom);
        path.moveTo(rects[0].left,rects[0].top);
        path.lineTo(rects[0].right,rects[0].top);
        for(int i = 1; i < rects.length; i ++){
            RectF r = rects[i];
            path.moveTo(previousRect.right,previousRect.top);
            path.lineTo(r.left,r.top);
            path.lineTo(r.right,r.top);
            path.moveTo(previousRect.right,previousRect.bottom);
            path.lineTo(r.left,r.bottom);
            path.lineTo(r.right,r.bottom);
            previousRect = r;
        }
        RectF lastRect = rects[rects.length-1];
        path.moveTo(previousRect.right,previousRect.top);
        path.lineTo(lastRect.left,lastRect.top);
        path.lineTo(lastRect.right,lastRect.top);
        path.lineTo(lastRect.right,lastRect.bottom);
        path.lineTo(lastRect.left,lastRect.bottom);
        path.lineTo(previousRect.right,previousRect.bottom);
        path.close();
        return path;
    }

}
