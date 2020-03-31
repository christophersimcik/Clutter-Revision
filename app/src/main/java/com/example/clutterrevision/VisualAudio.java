package com.example.clutterrevision;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;

import com.example.clutterrevision.ObserverAudioImage;
import com.example.clutterrevision.R;
import com.example.clutterrevision.WaveFormContour;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class VisualAudio extends View implements ObserverAudioImage {
    //reduced by 1000
    private static final int shortMin = -22768;
    private static final int shortMax = 22767;

    float txtWidth = 0f;
    float txtHeight = 0f;
    float rightTxtPadding = 0f;
    float bottomTxtPadding = 0f;
    float span;

    RectF[] rects;
    Path path;
    RectF pathBounds = new RectF();
    List<RectF> animateRects = new ArrayList<>();
    List<Integer> list;
    String time = "";
    int gray = this.getResources().getColor(android.R.color.tab_indicator_text, null);
    int orange = getResources().getColor(R.color.audio_orange, null);
    int transparent = Color.TRANSPARENT;
    public float[] pos;
    int[] colorsAnimate = new int[]{getResources().getColor(R.color.audio_orange, null), Color.TRANSPARENT};
    int w = 0, h = 0, cWidth = 0, cHeight = 0;

    Paint outlinePaint = new Paint();
    Paint fillPaint = new Paint();
    Paint txtPaint = new Paint();

    public VisualAudio(Context context) {
        super(context);
    }

    public VisualAudio(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VisualAudio(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VisualAudio(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        this.cWidth = w / 2;
        this.cHeight = h / 2;
        txtPaint.setTextSize(findTextSz());
        rightTxtPadding = w * .05f;
        bottomTxtPadding = h * .10f;
        setupPaint();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (!animateRects.isEmpty()) {
            for (RectF r : animateRects) {
                float x = r.left + (r.width() / 2);
                fillPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(r, fillPaint);
            }
        } else {
            if (path != null) {
                canvas.drawPath(path, outlinePaint);
            }
        }
        drawText(canvas);

    }

    public float findTextSz() {
        String s = "00.00.00.00";
        float textSz = 0f;
        float targetSz = w / 4;
        Rect rect = new Rect();
        while (txtWidth < targetSz) {
            txtPaint.setTextSize(textSz);
            txtPaint.getTextBounds(s, 0, s.length(), rect);
            txtWidth = rect.width();
            txtHeight = rect.height();
            textSz++;
        }
        return textSz;

    }

    private void drawText(Canvas c) {
        c.drawText(time, w - (txtWidth + rightTxtPadding), h - (txtHeight + bottomTxtPadding) / 4, txtPaint);
    }

    private RectF[] drawRects() {

        this.rects = new RectF[list.size() / 2];
        this.span = ((float) w / (((float) (list.size()) / 2)));
        this.span = ((float) w / (float) rects.length);
        float left = 0, right = span;
        float top, bot;

        for (int i = 0; i < list.size(); i += 2) {
            top = 2 * (remap(list.get(i)));
            bot = 2 * (remap(list.get(i + 1)));
            if (top < 0) top = 0;
            if (bot > h) bot = h;
            rects[i / 2] = new RectF(left, top, right, bot);
            left = right;
            right += span;
        }
        return rects;
    }

    public void updateView(String time, List<Integer> list) {
        if (time != null && list != null) {
            this.list = list;
            this.time = time;
            this.rects = drawRects();
            path = new WaveFormContour(rects).pathFinder();
            path.computeBounds(pathBounds, false);
            this.invalidate();
        }
    }

    public float remap(int i) {
        float f = ((i - shortMin) * (h / 2f) / (shortMax - shortMin));
        if (f < 0) {
            f = 0;
        }
        if (f > h) {
            f = h;
        }
        return f;
    }

    public void playback(int dur) {
        final long increment = dur / rects.length;
        final Handler handler = new Handler(Looper.getMainLooper());

        final Runnable runnable = new Runnable() {
            int counter = 0;

            @Override
            public void run() {
                if (animateRects.size() < rects.length) {
                    animateRects.add(rects[counter]);
                    counter++;
                    handler.postDelayed(this, increment);
                    invalidate();
                    System.out.println("animate rects size = " + animateRects.size());
                } else {
                    animateRects.clear();
                    invalidate();
                }
            }
        };
        handler.post(runnable);
    }

    private void setupPaint() {

        // set image paint
        outlinePaint.setAntiAlias(true);
        outlinePaint.setStyle(Paint.Style.STROKE);
        outlinePaint.setStrokeWidth(1);
        outlinePaint.setColor(getResources().getColor(R.color.default_text, null));

        fillPaint.setAntiAlias(true);
        ;
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(getResources().getColor(R.color.audio_orange, null));
        // set time paint
        txtPaint.setColor(gray);
        txtPaint.setAntiAlias(true);
    }

    @Override
    public void onImageDataRetrieved(String time, List<Integer> list) {
        updateView(time, list);
    }
}
