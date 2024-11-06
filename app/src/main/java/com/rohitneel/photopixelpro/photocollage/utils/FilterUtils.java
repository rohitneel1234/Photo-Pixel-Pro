package com.rohitneel.photopixelpro.photocollage.utils;

import android.graphics.Bitmap;


import java.text.MessageFormat;

public class FilterUtils {


    public static Bitmap getBlurImageFromBitmap(Bitmap bitmap, float f) {
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        return resultBitmap;
    }

    public static Bitmap cloneBitmap(Bitmap bitmap) {
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        return resultBitmap;
    }
}
