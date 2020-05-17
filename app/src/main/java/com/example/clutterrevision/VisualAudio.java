package com.example.clutterrevision;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Display;
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

    private Boolean cancelAnimate = false;

    Rect[] rects;
    Path path;
    Rect pathBounds = new Rect();
    List<Rect> animateRects = new ArrayList<>();
    List<Integer> list;
    String time = "";
    String timeRunning = "";
    int gray = this.getResources().getColor(android.R.color.tab_indicator_text, null);
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
            for (Rect r : animateRects) {
                float x = r.left + (r.width() / 2);
                fillPaint.setStyle(Paint.Style.FILL);
                canvas.drawRect(r, fillPaint);
                drawTextRunning(canvas);

            }
        } else {
            if (path != null) {
                canvas.drawPath(path, outlinePaint);
                drawText(canvas);
            }
        }
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
    private void drawTextRunning(Canvas c) {
        c.drawText(timeRunning, w - (txtWidth + rightTxtPadding), h - (txtHeight + bottomTxtPadding) / 4, txtPaint);
    }

    private Rect[] drawRects() {

        this.rects = new Rect[list.size() / 2];
        this.span = ((float) w / (((float) (list.size()) / 2)));
        this.span = ((float) w / (float) rects.length);
        float left = 0, right = span;
        float top, bot;

        for (int i = 0; i < list.size(); i += 2) {
            top = 2 * (remap(list.get(i)));
            bot = 2 * (remap(list.get(i + 1)));
            if (top < 0) top = 0;
            if (bot > h) bot = h;
            rects[i / 2] = new Rect((int) left, (int) top, (int) right, (int) bot);
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
            RectF rf = new RectF(pathBounds.left, pathBounds.top, pathBounds.right, pathBounds.bottom);
            path.computeBounds(rf, false);
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

    public void playback(int dur, final MediaPlayer mediaPlayer) {
        final long increment = dur / rects.length;
        final Handler handler = new Handler(Looper.getMainLooper());
        final Runnable runnable = new Runnable() {
            int counter = 0;

            @Override
            public void run() {
                if (animateRects.size() < rects.length) {
                    animateRects.add(rects[counter]);
                    counter++;
                    if (!getCancelAnimate()) {
                        handler.postDelayed(this, increment);
                        invalidate();
                        timeRunning = "";
                        if(mediaPlayer.isPlaying()) {
                            timeRunning = convertToString(mediaPlayer.getCurrentPosition());
                        }
                    } else {
                        animateRects.clear();
                        invalidate();
                        timeRunning = "";
                        setCancelAnimate(false);
                    }
                } else {
                    animateRects.clear();
                    invalidate();
                    setCancelAnimate(false);
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
        fillPaint.setColor(getResources().getColor(R.color.new_yellow, null));
        fillPaint.setAlpha(125);
        // set time paint
        txtPaint.setColor(gray);
        txtPaint.setAntiAlias(true);
    }

    @Override
    public void onImageDataRetrieved(String time, List<Integer> list) {
        updateView(time, list);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Display d = findActivity().getWindowManager().getDefaultDisplay();
        Point p = new Point();
        d.getSize(p);
        int w = p.x;
        int h = p.y;
        int dSize = Math.min(w, h);
        dSize = (int) (dSize * .75);
        int mSpec = Math.min(widthMeasureSpec, heightMeasureSpec);
        dSize = resolveSize(dSize, mSpec);
        dSize = measureDimension(dSize, mSpec);
        setMeasuredDimension(dSize, heightMeasureSpec);

    }

    private int measureDimension(int dynamicSize, int measureSpec) {

        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(dynamicSize, specSize);
            } else {
                result = dynamicSize;
            }
        }
        return result;
    }

    private Activity findActivity() {
        Context c = this.getContext();
        while (c instanceof ContextWrapper) {
            if (c instanceof Activity) {
                return (Activity) c;
            }
            c = ((ContextWrapper) c).getBaseContext();
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
    }

    public void setCancelAnimate(Boolean bool) {
        this.cancelAnimate = bool;
    }

    public Boolean getCancelAnimate() {
        return this.cancelAnimate;
    }

    private String convertToString(long m) {
        long hundreths = Math.round(m % 100);
        long seconds = Math.round((m / 1000) % 60);
        long minutes = Math.round((m / (1000 * 60)) % 60);
        long hours = Math.round((m / (1000 * 60 * 60)) % 24);

        String huns, secs, mins, hrs;

        if (hundreths < 10) {
            huns = "0" + hundreths;
        } else {
            huns = String.valueOf(hundreths);
        }

        if (seconds < 10) {
            secs = "0" + seconds;
        } else {
            secs = String.valueOf(seconds);
        }

        if (minutes < 10) {
            mins = "0" + minutes;
        } else {
            mins = String.valueOf(minutes);
        }

        if (hours < 10) {
            hrs = "0" + hours;
        } else {
            hrs = String.valueOf(hours);
        }

        return hrs + "." + mins + "." + secs + "." + huns;
    }
}