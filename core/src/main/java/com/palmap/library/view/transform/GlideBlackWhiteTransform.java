package com.palmap.library.view.transform;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.media.ThumbnailUtils;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;


/**
 * Created by 王天明 on 2016/10/21 0018.
 * 将图片转换成黑白图
 */
public class GlideBlackWhiteTransform extends BitmapTransformation {

    public GlideBlackWhiteTransform(Context context) {
        super(context);
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        int width = toTransform.getWidth();
        int height = toTransform.getHeight();
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        toTransform.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;

        for (int i = 0; i < pixels.length; i++) {
            int grey = pixels[i];
            if (grey == 0) {//如果颜色值为 0x00000000 它是透明色 不处理
                continue;
            }
            int red = ((grey & 0x00FF0000) >> 16);
            int green = ((grey & 0x0000FF00) >> 8);
            int blue = (grey & 0x000000FF);

            grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
            grey = alpha | (grey << 16) | (grey << 8) | grey;
            pixels[i] = grey;
        }
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return ThumbnailUtils.extractThumbnail(newBmp, outWidth, outHeight);
    }

    @Override
    public String getId() {
        return getClass().getName();
    }


    /***
     * 图片去色,返回灰度图片
     * @param bmpOriginal 传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
}
