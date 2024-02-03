package com.rohitneel.photopixelpro.photocollage.assets;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class MosaicAsset {

    public static Bitmap getMosaic(Bitmap bitmap) {
        int i;
        Bitmap bitmap2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        int i2 = 50;
        float f = (float) 50;
        int ceil = (int) Math.ceil((((float) width) / f));
        int ceil2 = (int) Math.ceil((((float) height) / f));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        int i3 = 0;
        while (i3 < ceil) {
            int i4 = 0;
            while (i4 < ceil2) {
                int i5 = i2 * i3;
                int i6 = i2 * i4;
                int i7 = i5 + 50;
                if (i7 > width) {
                    i7 = width;
                }
                int i8 = i6 + 50;
                if (i8 > height) {
                    bitmap2 = bitmap;
                    i = height;
                } else {
                    i = i8;
                    bitmap2 = bitmap;
                }
                int pixel = bitmap2.getPixel(i5, i6);
                Rect rect = new Rect(i5, i6, i7, i);
                paint.setColor(pixel);
                canvas.drawRect(rect, paint);
                i4++;
                i2 = 50;
            }
            i3++;
            i2 = 50;
        }
        canvas.save();
        return createBitmap;
    }


}
