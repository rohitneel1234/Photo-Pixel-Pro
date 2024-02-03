package com.rohitneel.photopixelpro.photoframe.utils;

import android.graphics.Bitmap;

public class CompressImage {
    public static final float BITMAP_SCALE = 0.4f;

    public static Bitmap originalBitmap(Bitmap bitmap) {
        int n;
        int n2;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > height) {
            n = 960;
            n2 = (height * 960) / width;
        } else {
            n2 = 960;
            n = (width * 960) / height;
        }
        return Bitmap.createScaledBitmap(bitmap, n, n2, false);
    }
}
