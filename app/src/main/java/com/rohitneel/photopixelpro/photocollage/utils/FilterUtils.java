package com.rohitneel.photopixelpro.photocollage.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.rohitneel.photopixelpro.photocollage.crop.BitmapUtils;

import jp.co.cyberagent.android.gpuimage.GPUImage;
import jp.co.cyberagent.android.gpuimage.filter.*;

public class FilterUtils {

    public static Bitmap getBitmapWithFilter(Context context, Bitmap sourceBitmap, String filterCode) {
        if (sourceBitmap == null || filterCode == null || filterCode.isEmpty()) {
            return sourceBitmap;
        }
        try {
            GPUImage gpuImage = new GPUImage(context);
            gpuImage.setImage(sourceBitmap);
            GPUImageFilter filter = getFilterForCode(context, filterCode);
            gpuImage.setFilter(filter);
            return gpuImage.getBitmapWithFilterApplied();
        } catch (Exception e) {
            e.printStackTrace();
            return sourceBitmap;
        }
    }

    private static GPUImageFilter getFilterForCode(Context context, String filterCode) {
        if (filterCode.startsWith("@adjust lut filter/")) {
            String assetPath = filterCode.replace("@adjust lut filter/", "");
            GPUImageLookupFilter lookupFilter = new GPUImageLookupFilter();
            try {
                Bitmap lutBitmap = BitmapUtils.loadBitmapFromAssets(context, assetPath);
                if (lutBitmap != null) {
                    lookupFilter.setBitmap(lutBitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return lookupFilter;
        }
        return new GPUImageFilter();
    }

    public static Bitmap getBlurImageFromBitmap(Context context, Bitmap bitmap, float f) {
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(bitmap);
        GPUImageGaussianBlurFilter filter = new GPUImageGaussianBlurFilter(f);
        gpuImage.setFilter(filter);
        return gpuImage.getBitmapWithFilterApplied();
    }

    public static Bitmap cloneBitmap(Context context, Bitmap bitmap) {
        GPUImage gpuImage = new GPUImage(context);
        gpuImage.setImage(bitmap);
        gpuImage.setFilter(new GPUImageFilter());
        return gpuImage.getBitmapWithFilterApplied();
    }

    public static Bitmap getSketchImageFromBitmap(Context context, Bitmap bitmap, float intensity) {
        GPUImage gpuImage = new GPUImage(context);  // Provide context if needed
        GPUImageSketchFilter sketchFilter = new GPUImageSketchFilter();
        gpuImage.setFilter(sketchFilter);
        gpuImage.setImage(bitmap);
        return gpuImage.getBitmapWithFilterApplied();
    }

    public static Bitmap getBlackAndWhiteImageFromBitmap(Context context, Bitmap bitmap) {
        GPUImage gpuImage = new GPUImage(context);  // Provide context if needed
        gpuImage.setFilter(new GPUImageGrayscaleFilter());
        gpuImage.setImage(bitmap);
        return gpuImage.getBitmapWithFilterApplied();
    }

    public static Bitmap getShapeImageFromBitmap(Context context, Bitmap bitmap) {
        GPUImage gpuImage = new GPUImage(context);  // Provide context if needed
        gpuImage.setFilter(new GPUImageVignetteFilter());
        gpuImage.setImage(bitmap);
        return gpuImage.getBitmapWithFilterApplied();
    }
}
