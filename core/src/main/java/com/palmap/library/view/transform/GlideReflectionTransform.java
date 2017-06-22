package com.palmap.library.view.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by 王天明 on 2016/1/22 0022.
 * glide加载图片带有倒影
 */
public class GlideReflectionTransform extends BitmapTransformation {

    /**
     * 倒影比例
     */
    private float retio = .5f;

    public GlideReflectionTransform(Context context) {
        super(context);
    }

    public GlideReflectionTransform(Context context, float retio) {
        super(context);
        this.retio = retio;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap srcBitmap, int outWidth, int outHeight) {
        if (null == srcBitmap) {
            return null;
        }

        final int REFLECTION_GAP = 4;

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int reflectionWidth = srcBitmap.getWidth();
        int reflectionHeight = (int) (srcBitmap.getHeight() * retio);

        if (0 == srcWidth || srcHeight == 0) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        try {
            Bitmap reflectionBitmap = Bitmap.createBitmap(
                    srcBitmap,
                    0,
                    srcHeight / 2,
                    srcWidth,
                    srcHeight / 2,
                    matrix,
                    false);

            if (null == reflectionBitmap) {
                return null;
            }

            Bitmap bitmapWithReflection = Bitmap.createBitmap(
                    reflectionWidth,
                    srcHeight + reflectionHeight + REFLECTION_GAP,
                    Bitmap.Config.ARGB_8888);

            if (null == bitmapWithReflection) {
                return null;
            }

            Canvas canvas = new Canvas(bitmapWithReflection);

            canvas.drawBitmap(srcBitmap, 0, 0, null);

            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader = new LinearGradient(
                    0,
                    srcHeight,
                    0,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP,
                    0x70FFFFFF,
                    0x00FFFFFF,
                    Shader.TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));

            canvas.drawRect(
                    0,
                    srcHeight,
                    srcWidth,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP,
                    paint);

            return bitmapWithReflection;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return srcBitmap;
        //        return BitmapUtil.createReflectedBitmap(toTransform);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }
}
