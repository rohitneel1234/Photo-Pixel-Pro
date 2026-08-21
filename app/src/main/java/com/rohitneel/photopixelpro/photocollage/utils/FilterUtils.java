package com.rohitneel.photopixelpro.photocollage.utils;

import android.content.Context;
import android.graphics.*;

import com.rohitneel.photopixelpro.photocollage.crop.BitmapUtils;

public class FilterUtils {

    public static Bitmap getBitmapWithFilter(Context context, Bitmap sourceBitmap, String filterCode) {
        return getBitmapWithFilter(context, sourceBitmap, filterCode, 1.0f);
    }

    public static Bitmap getBitmapWithFilter(Context context, Bitmap sourceBitmap, String filterCode, float intensity) {
        if (sourceBitmap == null || filterCode == null || filterCode.isEmpty()) {
            return sourceBitmap;
        }
        try {
            if (filterCode.startsWith("@adjust lut filter/")) {
                String assetPath = "filter/" + filterCode.replace("@adjust lut filter/", "");
                Bitmap lut = BitmapUtils.loadBitmapFromAssets(context, assetPath);
                if (lut != null) {
                    return applyLUT(sourceBitmap, lut, intensity);
                }
            } else if (filterCode.contains("@adjust") || filterCode.contains("@vignette")) {
                return applyAdjustments(sourceBitmap, filterCode);
            } else if (filterCode.startsWith("#unpack @krblend")) {
                return applyKrBlend(context, sourceBitmap, filterCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceBitmap;
    }

    private static Bitmap applyAdjustments(Bitmap src, String filterCode) {
        ColorMatrix matrix = new ColorMatrix();

        // Brightness
        float brightness = parseAdjust(filterCode, "brightness", 0.0f);
        if (brightness != 0.0f) {
            ColorMatrix bMatrix = new ColorMatrix();
            float b = brightness * 255;
            bMatrix.set(new float[]{
                    1, 0, 0, 0, b,
                    0, 1, 0, 0, b,
                    0, 0, 1, 0, b,
                    0, 0, 0, 1, 0
            });
            matrix.postConcat(bMatrix);
        }

        // Contrast
        float contrast = parseAdjust(filterCode, "contrast", 1.0f);
        if (contrast != 1.0f) {
            float t = (1.0f - contrast) / 2.0f * 255.0f;
            ColorMatrix cMatrix = new ColorMatrix();
            cMatrix.set(new float[]{
                    contrast, 0, 0, 0, t,
                    0, contrast, 0, 0, t,
                    0, 0, contrast, 0, t,
                    0, 0, 0, 1, 0
            });
            matrix.postConcat(cMatrix);
        }

        // Saturation
        float saturation = parseAdjust(filterCode, "saturation", 1.0f);
        if (saturation != 1.0f) {
            ColorMatrix sMatrix = new ColorMatrix();
            sMatrix.setSaturation(saturation);
            matrix.postConcat(sMatrix);
        }

        // Exposure (approximation using brightness)
        float exposure = parseAdjust(filterCode, "exposure", 0.0f);
        if (exposure != 0.0f) {
            float e = exposure * 128;
            ColorMatrix eMatrix = new ColorMatrix();
            eMatrix.set(new float[]{
                    1, 0, 0, 0, e,
                    0, 1, 0, 0, e,
                    0, 0, 1, 0, e,
                    0, 0, 0, 1, 0
            });
            matrix.postConcat(eMatrix);
        }

        Bitmap result = applyColorMatrix(src, matrix);

        // Vignette
        if (filterCode.contains("@vignette")) {
            float vignetteValue = parseAdjust(filterCode, "vignette", 0.0f);
            if (vignetteValue > 0) {
                result = applyVignette(result, vignetteValue);
            }
        }

        return result;
    }

    private static float parseAdjust(String filterCode, String name, float defaultValue) {
        try {
            String pattern = "@adjust " + name + " ";
            if (!filterCode.contains(pattern)) {
                if (name.equals("vignette") && filterCode.contains("@vignette ")) {
                    pattern = "@vignette ";
                } else {
                    return defaultValue;
                }
            }
            int start = filterCode.indexOf(pattern) + pattern.length();
            int end = filterCode.indexOf(" ", start);
            if (end == -1) end = filterCode.length();
            return Float.parseFloat(filterCode.substring(start, end));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static Bitmap applyKrBlend(Context context, Bitmap src, String filterCode) {
        try {
            String[] parts = filterCode.split(" ");
            if (parts.length < 5) return src;
            
            String blendMode = parts[2];
            String assetPath = parts[3];
            int intensity = Integer.parseInt(parts[4]);

            Bitmap overlay = BitmapUtils.loadBitmapFromAssets(context, assetPath);
            if (overlay == null) return src;

            Bitmap resizedOverlay = Bitmap.createScaledBitmap(overlay, src.getWidth(), src.getHeight(), true);
            Bitmap result = src.copy(src.getConfig(), true);
            Canvas canvas = new Canvas(result);
            
            Paint paint = new Paint();
            paint.setAlpha((int) (intensity * 2.55f));
            
            if (blendMode.equals("sr")) {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));
            } else if (blendMode.equals("cl")) {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
            } else {
                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
            }
            
            canvas.drawBitmap(resizedOverlay, 0, 0, paint);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return src;
    }

    private static Bitmap applyVignette(Bitmap bitmap, float value) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        canvas.drawBitmap(bitmap, 0, 0, null);

        Paint paint = new Paint();
        float radius = Math.max(bitmap.getWidth(), bitmap.getHeight()) * (1.2f - value);
        RadialGradient gradient = new RadialGradient(
                bitmap.getWidth() / 2f,
                bitmap.getHeight() / 2f,
                radius,
                new int[]{0x00000000, 0xAA000000},
                new float[]{0.6f, 1.0f},
                Shader.TileMode.CLAMP
        );

        paint.setShader(gradient);
        canvas.drawRect(0, 0, bitmap.getWidth(), bitmap.getHeight(), paint);
        return output;
    }

    public static Bitmap applyLUT(Bitmap source, Bitmap lut, float intensity) {
        if (source == null || lut == null) return source;
        int width = source.getWidth();
        int height = source.getHeight();

        Bitmap result = Bitmap.createBitmap(width, height, source.getConfig() != null ? source.getConfig() : Bitmap.Config.ARGB_8888);

        int[] pixels = new int[width * height];
        source.getPixels(pixels, 0, width, 0, 0, width, height);

        int lutWidth = lut.getWidth();
        int lutHeight = lut.getHeight();

        int[] lutPixels = new int[lutWidth * lutHeight];
        lut.getPixels(lutPixels, 0, lutWidth, 0, 0, lutWidth, lutHeight);

        // Determine size of the LUT cube side (e.g. 16, 32, 64)
        int size = 0;
        if (lutWidth == 512 && lutHeight == 512) size = 64;
        else if (lutWidth == 64 && lutHeight == 64) size = 16;
        else if (lutWidth == 4096 && lutHeight == 64) size = 64;
        else if (lutWidth == 256 && lutHeight == 16) size = 16;
        else if (lutWidth == 1024 && lutHeight == 32) size = 32;
        else {
            if (lutWidth == lutHeight) {
                size = (int) Math.pow(lutWidth * lutHeight, 1.0/3.0);
                if (size * size * size != lutWidth * lutHeight) {
                    size = lutWidth / 8;
                }
            } else {
                size = lutHeight;
            }
        }
        
        if (size <= 0) size = 64; 

        int columnCount = lutWidth / size;

        for (int i = 0; i < pixels.length; i++) {
            int color = pixels[i];

            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            int rIndex = r * (size - 1) / 255;
            int gIndex = g * (size - 1) / 255;
            int bIndex = b * (size - 1) / 255;

            int lutX = (bIndex % columnCount) * size + rIndex;
            int lutY = (bIndex / columnCount) * size + gIndex;

            lutX = Math.min(lutX, lutWidth - 1);
            lutY = Math.min(lutY, lutHeight - 1);

            int lutColor = lutPixels[lutY * lutWidth + lutX];

            if (intensity >= 1.0f) {
                pixels[i] = (color & 0xFF000000) | (lutColor & 0x00FFFFFF);
            } else {
                int lr = (lutColor >> 16) & 0xFF;
                int lg = (lutColor >> 8) & 0xFF;
                int lb = lutColor & 0xFF;

                int finalR = (int) (r * (1.0f - intensity) + lr * intensity);
                int finalG = (int) (g * (1.0f - intensity) + lg * intensity);
                int finalB = (int) (b * (1.0f - intensity) + lb * intensity);

                pixels[i] = (color & 0xFF000000) | (finalR << 16) | (finalG << 8) | finalB;
            }
        }
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
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