package com.rohitneel.photopixelpro.photocollage.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;

import java.io.IOException;

public class DripUtils {

    public static Bitmap getBitmapFromAsset(Context context, String str) {
        if (str == null) {
            return null;
        }
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(str));
        } catch (IOException unused) {
            return null;
        }
    }

    public static Bitmap changeBitmapColor(Bitmap bitmap, int i) {
        Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
        Paint paint = new Paint();
        paint.setColorFilter(new LightingColorFilter(i, 1));
        new Canvas(copy).drawBitmap(copy, 0.0f, 0.0f, paint);
        return copy;
    }

    public static int dpToPx(int i) {
        return (int) (((float) i) * Resources.getSystem().getDisplayMetrics().density);
    }

}
