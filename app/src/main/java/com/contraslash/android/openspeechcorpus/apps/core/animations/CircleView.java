package com.contraslash.android.openspeechcorpus.apps.core.animations;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by ma0 on 10/30/15.
 */
public class CircleView extends View {


    String TAG = "CircleView";

    private static final int START_ANGLE_POINT = 90;

    private final Paint paint;

    int radious;

    int baseRadious = 50;
    int width;
    int height;

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

        final int strokeWidth = 100;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(strokeWidth);
        //Circle color
        paint.setColor(Color.RED);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(this.width / 2, this.height / 2, baseRadious + radious, paint);



    }

    public int getRadious() {
        return radious;
    }

    public void setRadious(int radious) {
        this.radious = radious;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    public void setHeight(int height) {
        this.height = height;
    }

    public void setBaseRadious(int baseRadious) {
        this.baseRadious = baseRadious;
    }
}