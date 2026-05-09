package com.rohitneel.photopixelpro.photocollage.utils;

import android.content.Context;
import android.graphics.*;

import com.rohitneel.photopixelpro.photocollage.crop.BitmapUtils;

public class FilterUtils {

    public static Bitmap getBitmapWithFilter(Context context, Bitmap sourceBitmap, String filterCode) {
        if (sourceBitmap == null || filterCode == null || filterCode.isEmpty()) {
            return sourceBitmap;
        }
        try {
            if (filterCode.startsWith("@adjust lut filter/")) {
                return applyLUTFilter(context, sourceBitmap, filterCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceBitmap;
    }

    // 🔹 2. LUT Filter (approximation)
    private static Bitmap applyLUTFilter(Context context, Bitmap src, String filterCode) {
        try {
            String assetPath = filterCode.replace("@adjust lut filter/", "");
            Bitmap lut = BitmapUtils.loadBitmapFromAssets(context, assetPath);

            if (lut == null) return src;

            // Approximate LUT using saturation boost
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(1.2f);

            return applyColorMatrix(src, matrix);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return src;
    }

    // 🔹 3. Blur (Stack Blur – fast, no native)
    public static Bitmap getBlurImageFromBitmap(Context context, Bitmap sentBitmap, float radius) {
        if (sentBitmap == null) return null;

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) return bitmap;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = (int) radius + (int) radius + 1;

        int[] r = new int[wh];
        int[] g = new int[wh];
        int[] b = new int[wh];

        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;

        yw = yi = 0;

        for (y = 0; y < h; y++) {
            rsum = gsum = bsum = 0;
            for (i = -((int) radius); i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                rsum += (p & 0xff0000) >> 16;
                gsum += (p & 0x00ff00) >> 8;
                bsum += (p & 0x0000ff);
            }

            for (x = 0; x < w; x++) {
                r[yi] = rsum / div;
                g[yi] = gsum / div;
                b[yi] = bsum / div;

                if (x == 0) continue;

                int p1 = pix[yw + Math.min(x + (int) radius, wm)];
                int p2 = pix[yw + Math.max(x - (int) radius - 1, 0)];

                rsum += ((p1 & 0xff0000) - (p2 & 0xff0000)) >> 16;
                gsum += ((p1 & 0x00ff00) - (p2 & 0x00ff00)) >> 8;
                bsum += (p1 & 0x0000ff) - (p2 & 0x0000ff);

                yi++;
            }
            yw += w;
        }

        for (i = 0; i < wh; i++) {
            pix[i] = 0xff000000 | (r[i] << 16) | (g[i] << 8) | b[i];
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);
        return bitmap;
    }

    // 🔹 4. Clone
    public static Bitmap cloneBitmap(Context context, Bitmap bitmap) {
        if (bitmap == null) return null;
        return bitmap.copy(bitmap.getConfig(), true);
    }

    // 🔹 5. Sketch (approximation)
    public static Bitmap getSketchImageFromBitmap(Context context, Bitmap bitmap, float intensity) {
        ColorMatrix matrix = new ColorMatrix(new float[]{
                -1, 0, 0, 0, 255,
                0, -1, 0, 0, 255,
                0, 0, -1, 0, 255,
                0, 0, 0, 1, 0
        });
        return applyColorMatrix(bitmap, matrix);
    }

    // 🔹 6. Grayscale
    public static Bitmap getBlackAndWhiteImageFromBitmap(Context context, Bitmap bitmap) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        return applyColorMatrix(bitmap, matrix);
    }

    // 🔹 7. Vignette
    public static Bitmap getShapeImageFromBitmap(Context context, Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        canvas.drawBitmap(bitmap, 0, 0, null);

        Paint paint = new Paint();
        RadialGradient gradient = new RadialGradient(
                bitmap.getWidth() / 2f,
                bitmap.getHeight() / 2f,
                Math.max(bitmap.getWidth(), bitmap.getHeight()) / 1.2f,
                new int[]{0x00000000, 0xAA000000},
                new float[]{0.7f, 1.0f},
                Shader.TileMode.CLAMP
        );

        paint.setShader(gradient);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);

        return output;
    }

    // 🔹 Core helper
    private static Bitmap applyColorMatrix(Bitmap src, ColorMatrix matrix) {
        Bitmap result = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(result);

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(matrix));

        canvas.drawBitmap(src, 0, 0, paint);
        return result;
    }
}