package com.palmap.library.view.transform;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by 王天明 on 2015/12/21 0021.
 * glide图片转换成圆角图片
 * v1.1-支持加入外边框
 */
public class GlideRoundTransform extends BitmapTransformation {
    private static float radius = 0f;
    private int borderWidth = 0;
    private Paint borderPaint = new Paint();

    public GlideRoundTransform(Context context) {
        this(context, 5);
    }

    public GlideRoundTransform(Context context, int dp) {
        super(context);
        radius = Resources.getSystem().getDisplayMetrics().density * dp;
    }

    public GlideRoundTransform(Context context, int dp, int borderWidth, int borderColor) {
        this(context, dp);
        this.borderWidth = borderWidth;
        if (borderWidth > 0) {
            borderPaint = new Paint();
            borderPaint.setStrokeWidth(borderWidth);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setAntiAlias(true);
            borderPaint.setColor(borderColor);
        }
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f + borderWidth, 0f + borderWidth, source.getWidth() - borderWidth, source.getHeight() - borderWidth);
        canvas.drawRoundRect(rectF, radius, radius, paint);
        if (borderPaint != null && borderWidth > 0) {
            canvas.drawRoundRect(new RectF(0f, 0f, source.getWidth(), source.getHeight()), radius, radius, borderPaint);
        }
        return result;
    }

    @Override
    public String getId() {
        return getClass().getName() + Math.round(radius);
    }
}