package com.rohitneel.photopixelpro.photoframe.filterclass;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;

import com.rohitneel.photopixelpro.R;

import java.util.ArrayList;


public class DataBinder {
    public static ArrayList<Filter> fetchFilters() {
        ArrayList<Filter> filterArrayList = new ArrayList<>();
        filterArrayList.add(new Filter(R.drawable.filter_1, "Original"));
        filterArrayList.add(new Filter(R.drawable.filter_2, "Tropic"));
        filterArrayList.add(new Filter(R.drawable.filter_3, "Valencia"));
        filterArrayList.add(new Filter(R.drawable.filter_5, "B&W"));
        filterArrayList.add(new Filter(R.drawable.filter_6, "Lomo"));
        filterArrayList.add(new Filter(R.drawable.filter_7, "Autumn"));
        filterArrayList.add(new Filter(R.drawable.filter_9, "Elegance"));
        filterArrayList.add(new Filter(R.drawable.filter_10, "Mellow"));
        filterArrayList.add(new Filter(R.drawable.filter_11, "Time"));
        filterArrayList.add(new Filter(R.drawable.filter_12, "Earlybird"));
        filterArrayList.add(new Filter(R.drawable.filter_13, "Dark"));
        filterArrayList.add(new Filter(R.drawable.filter_14, "Retro"));
        filterArrayList.add(new Filter(R.drawable.filter_15, "Twilight"));
        return filterArrayList;
    }

    public static Bitmap applyFilter(int position, Activity activity, Bitmap originalBitmap) {
        switch (position) {
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
            // Additional cases for other filters...
            default:
                return null;
        }
    }

    private static Bitmap applyTropicFilter(Bitmap original) {
        // Apply your Tropic filter logic here
        return original; // Placeholder for actual filtered image
    }

    private static Bitmap applyValenciaFilter(Bitmap original) {
        // Apply your Valencia filter logic here
        return original; // Placeholder for actual filtered image
    }

    private static Bitmap applyBWFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0); // Convert to grayscale
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        Bitmap filteredBitmap = Bitmap.createBitmap(original);
        // Apply filter to the Bitmap (may require a Canvas)
        // For demonstration purposes, returning original
        return filteredBitmap;
    }

    private static Bitmap applyLomoFilter(Bitmap original) {
        // Apply your Lomo filter logic here
        return original; // Placeholder for actual filtered image
    }

    private static Bitmap applyAutumnFilter(Bitmap original) {
        // Apply your Autumn filter logic here
        return original; // Placeholder for actual filtered image
    }
}
