package com.rohitneel.photopixelpro.photoframe.filterclass;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

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
        if (originalBitmap == null) return null;
        switch (position) {
            case 0:
                return originalBitmap;
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
            case 6:
                return applyEleganceFilter(originalBitmap);
            case 7:
                return applyMellowFilter(originalBitmap);
            case 8:
                return applyTimeFilter(originalBitmap);
            case 9:
                return applyEarlybirdFilter(originalBitmap);
            case 10:
                return applyDarkFilter(originalBitmap);
            case 11:
                return applyRetroFilter(originalBitmap);
            case 12:
                return applyTwilightFilter(originalBitmap);
            default:
                return originalBitmap;
        }
    }

    private static Bitmap applyColorMatrix(Bitmap original, ColorMatrix colorMatrix) {
        Bitmap filteredBitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig() != null ? original.getConfig() : Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(filteredBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(original, 0, 0, paint);
        return filteredBitmap;
    }

    private static Bitmap applyTropicFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                1, 0, 0, 0, 0,
                0, 1.1f, 0, 0, 0,
                0, 0, 1.2f, 0, 0,
                0, 0, 0, 1, 0
        });
        return applyColorMatrix(original, matrix);
    }

    private static Bitmap applyValenciaFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                1.1f, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 0.9f, 0, 0,
                0, 0, 0, 1, 0
        });
        return applyColorMatrix(original, matrix);
    }

    private static Bitmap applyBWFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        return applyColorMatrix(original, matrix);
    }

    private static Bitmap applyLomoFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                1.2f, 0, 0, 0, -20,
                0, 1.2f, 0, 0, -20,
                0, 0, 1.2f, 0, -20,
                0, 0, 0, 1, 0
        });
        return applyColorMatrix(original, matrix);
    }

    private static Bitmap applyAutumnFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                1.2f, 0, 0, 0, 10,
                0, 0.9f, 0, 0, 5,
                0, 0, 0.8f, 0, 0,
                0, 0, 0, 1, 0
        });
        return applyColorMatrix(original, matrix);
    }

    private static Bitmap applyEleganceFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                1.1f, 0, 0, 0, 20,
                0, 1.1f, 0, 0, 20,
                0, 0, 1.1f, 0, 20,
                0, 0, 0, 1, 0
        });
        return applyColorMatrix(original, matrix);
    }

    private static Bitmap applyMellowFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                1, 0, 0, 0, 0,
                0, 0.8f, 0, 0, 0,
                0, 0, 0.7f, 0, 0,
                0, 0, 0, 1, 0
        });
        return applyColorMatrix(original, matrix);
    }

    private static Bitmap applyTimeFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0.2f);
        ColorMatrix sepia = new ColorMatrix(new float[]{
                0.393f, 0.769f, 0.189f, 0, 0,
                0.349f, 0.686f, 0.168f, 0, 0,
                0.272f, 0.534f, 0.131f, 0, 0,
                0, 0, 0, 1, 0
        });
        matrix.postConcat(sepia);
        return applyColorMatrix(original, matrix);
    }

    private static Bitmap applyEarlybirdFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                0.9f, 0, 0, 0, 30,
                0, 0.9f, 0, 0, 10,
                0, 0, 0.7f, 0, 0,
                0, 0, 0, 1, 0
        });
        return applyColorMatrix(original, matrix);
    }

    private static Bitmap applyDarkFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                0.6f, 0, 0, 0, 0,
                0, 0.6f, 0, 0, 0,
                0, 0.6f, 0, 0, 0,
                0, 0, 0, 1, 0
        });
        return applyColorMatrix(original, matrix);
    }

    private static Bitmap applyRetroFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                1, 0, 0, 0, 0,
                0, 1, 0, 0, 0,
                0, 0, 0.5f, 0, 0,
                0, 0, 0, 1, 0
        });
        return applyColorMatrix(original, matrix);
    }

    private static Bitmap applyTwilightFilter(Bitmap original) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                0.8f, 0, 0, 0, 0,
                0, 0.8f, 0, 0, 0,
                0, 0, 1.2f, 0, 0,
                0, 0, 0, 1, 0
        });
        return applyColorMatrix(original, matrix);
    }
}
