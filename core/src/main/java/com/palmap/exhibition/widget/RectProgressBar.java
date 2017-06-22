package com.palmap.exhibition.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.palmap.library.utils.DeviceUtils;

/**
 * Created by 王天明 on 2016/10/24.
 * 长方形的颜色进度条
 */
public class RectProgressBar extends ProgressBar {

    private int color = Color.RED;

    private Paint paint;

    private int progress;

    public RectProgressBar(Context context) {
        this(context, null);
    }

    public RectProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint();

        paint.setStyle(Paint.Style.FILL);

        paint.setColor(color);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        double percentage = getProgress() * 1.0f / getMax();
        int proWidth = (int) (width * percentage);
//        canvas.drawRect(0, 0, proWidth, height, paint);
        int rx = DeviceUtils.dip2px(getContext(), 5);
        canvas.drawRoundRect(new RectF(0, 0, proWidth, height), rx, rx, paint);
    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }
}
