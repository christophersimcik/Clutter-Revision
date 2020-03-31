package com.example.clutterrevision;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;

public class CustomCheckBox extends View {

    Circle circle;
    Boolean checked;
    TypedArray attributes;

    public CustomCheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        attributes = getContext().obtainStyledAttributes(attrs, R.styleable.CustomCheckBox, 0, 0);
        try {
            checked = attributes.getBoolean(R.styleable.CustomCheckBox_checked, false);
        } finally {
            attributes.recycle();
        }
        circle = new Circle();
    }

    public CustomCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        try {
            checked = attributes.getBoolean(R.styleable.CustomCheckBox_checked, false);
        } finally {
            attributes.recycle();
        }
        circle = new Circle();
    }

    public CustomCheckBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        try {
            checked = attributes.getBoolean(R.styleable.CustomCheckBox_checked, false);
        } finally {
            attributes.recycle();
        }
        circle = new Circle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        circle.draw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        circle.setMeasurements();
        this.invalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int lesser = Math.min(widthSize, heightSize);
        setMeasuredDimension(lesser, lesser);
    }

    public void onClicked() {
        circle.animate();

    }


    public class Circle {
        int padding = 5;
        int strokeWidth = 10;
        int colorOuter = getContext().getColor(R.color.colorPrimary);
        int colorInner = getContext().getColor(R.color.colorAccent);
        int radiusInner;
        int radiusOuter = 0;
        int maxRadius = 25;
        int alphaInner;
        int alphaOuter;
        Point center = new Point(0, 0);
        Paint paintOuter = new Paint();
        Paint paintInner = new Paint();
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (checked) {
                    if (radiusInner > 0) {
                        radiusInner -= 1;
                        alphaInner = innerAlpha(radiusInner);
                        alphaOuter = outerAlpha(alphaInner);
                        handler.postDelayed(this, 1);
                    } else {
                        setChecked(false);
                    }
                } else {
                    if (radiusInner < maxRadius) {
                        radiusInner += 1;
                        alphaInner = innerAlpha(radiusInner);
                        alphaOuter = outerAlpha(alphaInner);
                        handler.postDelayed(this, 1);
                    } else {
                        setChecked(true);
                    }
                }
                update();
            }

        };

        //constructor
        Circle() {
            if (checked) {
                radiusInner = maxRadius;
                alphaInner = innerAlpha(radiusInner);
                alphaOuter = outerAlpha(alphaInner);
            } else {
                radiusInner = 0;
                alphaInner = innerAlpha(radiusInner);
                alphaOuter = outerAlpha(alphaInner);
            }

            initOuterPaint();
            initInnerPaint();
        }

        // initialize paint objects
        void initOuterPaint() {
            paintOuter.setAntiAlias(true);
            paintOuter.setStyle(Paint.Style.STROKE);
            paintOuter.setStrokeWidth(strokeWidth);
            paintOuter.setColor(colorOuter);
            paintOuter.setAlpha(alphaOuter);
        }

        void initInnerPaint() {
            paintOuter.setAntiAlias(true);
            paintInner.setStyle(Paint.Style.FILL);
            paintInner.setColor(colorInner);
            paintInner.setAlpha(alphaInner);
        }

        // measure
        void setMeasurements() {
            center.x = getWidth() / 2;
            center.y = getHeight() / 2;
            maxRadius = getWidth() / 2 - padding;
            if (getWidth() / 4 < maxRadius) {
                radiusOuter = getWidth() / 4;
            } else {
                radiusOuter = maxRadius;
            }
            int j = 10;
            int i = radiusOuter - j;
            if (i > 0) {
                maxRadius = i;
            } else {
                while (i < 0) {
                    j--;
                    i = radiusOuter - j;
                }
                maxRadius = i;
            }
            if (checked) {
                radiusInner = maxRadius;
            } else {
                radiusInner = 0;
            }
        }

        // update
        void update() {
            paintOuter.setAlpha(alphaOuter);
            paintInner.setAlpha(alphaInner);
            paintOuter.setAlpha(alphaOuter);
            paintInner.setAlpha(alphaInner);
            invalidate();
        }

        // draw the circle
        void draw(Canvas canvas) {
            canvas.drawCircle(center.x, center.y, radiusOuter, paintOuter);
            canvas.drawCircle(center.x, center.y, radiusInner, paintInner);
        }

        void animate() {
            post(runnable);
        }


        private int innerAlpha(int radius) {
            return Math.round(((float) 255 / (float) maxRadius) * (float) radius);
        }

        private int outerAlpha(int alpha) {
            return 255 - alpha;
        }


    }

    public void setChecked(Boolean bool) {
        checked = bool;
        animate();
    }

    public Boolean getChecked() {
        return checked;
    }


}

