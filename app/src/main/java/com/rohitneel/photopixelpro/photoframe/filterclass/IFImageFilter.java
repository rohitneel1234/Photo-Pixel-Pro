package com.rohitneel.photopixelpro.photoframe.filterclass;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 14-8-9.
 */
public class IFImageFilter {
    private List<Integer> mResIds;
    private Context mContext;

    public IFImageFilter(Context context) {
        mContext = context;
        mResIds = new ArrayList<>();
    }

    public void addInputTexture(int resId) {
        mResIds.add(resId);
    }

    public Bitmap applyFilter(Bitmap originalBitmap, int filterPosition) {
        switch (filterPosition) {
            case 0:
                return originalBitmap; // Original image
            case 1:
                return applyTropicFilter(originalBitmap);
            case 2:
                return applyValenciaFilter(originalBitmap);
            case 3:
                return applyBWFilter(originalBitmap);
            case 4:
                return applyLomoFilter(originalBitmap);
            case 5:
                return applyAutumnFilter(originalBitmap);
            // Add more cases for other filters...
            default:
                return originalBitmap;
        }
    }

    private Bitmap applyTropicFilter(Bitmap original) {
        // Implement your Tropic filter logic
        return original; // Placeholder
    }

    private Bitmap applyValenciaFilter(Bitmap original) {
        // Implement your Valencia filter logic
        return original; // Placeholder
    }

    private Bitmap applyBWFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0); // Convert to grayscale
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        Bitmap filteredBitmap = Bitmap.createBitmap(original);
        // Apply the filter to the bitmap (requires Canvas)
        return filteredBitmap; // This will need actual implementation
    }

    private Bitmap applyLomoFilter(Bitmap original) {
        // Implement your Lomo filter logic
        return original; // Placeholder
    }

    private Bitmap applyAutumnFilter(Bitmap original) {
        // Implement your Autumn filter logic
        return original; // Placeholder
    }
}