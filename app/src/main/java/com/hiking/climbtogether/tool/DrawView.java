package com.hiking.climbtogether.tool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;

public class DrawView extends ImageView {
    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(10);
        float lefetx = 20;
        float topy = 20;
        float rightx = 50;
        float bottomy = 100;
        canvas.drawRect(lefetx,topy,rightx,bottomy,paint);

    }
}
