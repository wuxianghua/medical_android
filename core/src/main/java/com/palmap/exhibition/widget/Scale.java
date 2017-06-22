package com.palmap.exhibition.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.palmaplus.nagrand.view.MapView;


/**
 * Created by TYT on 2016/1/22.
 * 调用完setMapview之后就可以使用，刷新使用invalidate
 * 自定义属性LineWidth可以控制比例尺线的粗细
 */
public class Scale extends View {
    private Context mContext;
    private MapView mMapView = null;
    private int canvasHeight = 0;
    private int canvasWidth = 0;
    private Paint mPaint = new Paint();
    private float mLineWidth = 0;

    private int meter;

    public Scale(Context context) {
        super(context);
        initView(context, null);
    }

    public Scale(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context, attributeSet);
    }

    public Scale(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /*public NewScale(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
        initView(context,attrs);
    }*/
    private void initView(Context context, AttributeSet attributeSet) {
        mContext = context;
        mPaint.setAntiAlias(true);
        /*TypedArray a = mContext.obtainStyledAttributes(attributeSet, R.styleable.Scale);
        mLineWidth = a.getFloat(R.styleable.Scale_LineWidth,0);*/
        mLineWidth = attributeSet.getAttributeFloatValue("http://schemas.android.com/apk/res-auto", "LineWidth", 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mMapView != null) {
            canvasHeight = canvas.getHeight();
            canvasWidth = canvas.getWidth();

            meter = 200;
            float length = mMapView.getPixelLengthFromRealDistance(meter);
            if (mMapView.getPixelLengthFromRealDistance(meter) > canvasWidth) {
                meter = 150;
                length = mMapView.getPixelLengthFromRealDistance(meter);
                if (mMapView.getPixelLengthFromRealDistance(meter) > canvasWidth) {
                    meter = 100;
                    length = mMapView.getPixelLengthFromRealDistance(meter);
                    if (mMapView.getPixelLengthFromRealDistance(meter) > canvasWidth) {
                        meter = 50;
                        length = mMapView.getPixelLengthFromRealDistance(meter);
                        if (mMapView.getPixelLengthFromRealDistance(meter) > canvasWidth) {
                            meter = 20;
                            length = mMapView.getPixelLengthFromRealDistance(meter);
                            if (mMapView.getPixelLengthFromRealDistance(meter) > canvasWidth) {
                                meter = 10;
                                length = mMapView.getPixelLengthFromRealDistance(meter);
                                if (mMapView.getPixelLengthFromRealDistance(meter) > canvasWidth) {
                                    meter = 5;
                                    length = mMapView.getPixelLengthFromRealDistance(meter);
                                    if (mMapView.getPixelLengthFromRealDistance(meter) > canvasWidth) {
                                        meter = 2;
                                        length = mMapView.getPixelLengthFromRealDistance(meter);
                                        if (mMapView.getPixelLengthFromRealDistance(meter) > canvasWidth) {
                                            meter = 1;
                                            length = mMapView.getPixelLengthFromRealDistance(meter);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }
            }
            drawScale(canvas, length);
            drawText(canvas, meter, length);
        }
    }

    private void drawScale(Canvas canvas, float length) {
        mPaint.setStrokeWidth(mLineWidth);
//        length = length < minLen ? minLen : length;

        float offset = mLineWidth / 2;

        canvas.drawLine(0, canvasHeight - offset, length, canvasHeight - offset, mPaint);
        canvas.drawLine(offset, 0.8f * canvasHeight, offset, canvasHeight, mPaint);
        canvas.drawLine(length - offset, 0.8f * canvasHeight, length - offset, canvasHeight, mPaint);

        //中间竖线
        //canvas.drawLine(canvasWidth / 2 - 0.5f * length, 0.9f * canvasHeight, canvasWidth / 2 + 0.5f * length, 0.9f * canvasHeight, mPaint);
        //左边竖线
        //canvas.drawLine(canvasWidth / 2 - 0.5f * length, 0.8f * canvasHeight, canvasWidth / 2 - 0.5f * length, canvasHeight, mPaint);
        //右边竖线
        //canvas.drawLine(canvasWidth / 2 + 0.5f * length, 0.8f * canvasHeight, canvasWidth / 2 + 0.5f * length, canvasHeight, mPaint);
    }

    private void drawText(Canvas canvas, int meter, float length) {
        mPaint.setStrokeWidth(0f);
        mPaint.setTextSize(canvasHeight / 3);
        mPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        int textLen = canvasHeight;
        float startX = length / 2;
        if (startX < textLen / 2) {
            startX = textLen / 2;
        }
        canvas.drawText(meter + "m", startX, canvasHeight / 3 - fontMetrics.ascent, mPaint);
    }

    public void setMapView(MapView mapView) {
        mMapView = mapView;
        this.postInvalidate();
    }

    public int getMeter() {
        return meter;
    }
}
