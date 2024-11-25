package com.rohitneel.photopixelpro.photocollage.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Base64;

import androidx.core.view.ViewCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


public class BitmapUtils {


    private static final int f136a = 15;


    private static final int f137b = 10;


    private static int[] f138c = new int[256];


    private static int[] f139d = new int[256];


    private static int[] f140e = new int[256];


    private static int[] f141f = new int[256];

    public static int estimateSampleSize(String str, int i, int i2) {
        return estimateSampleSize(str, i, i2, 0);
    }

    public static int estimateSampleSize(String str, int i, int i2, int i3) {
        if (str == null || i <= 0 || i2 <= 0) {
            return 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeFile(str, options);
        } catch (OutOfMemoryError unused) {
        }
        int i4 = options.outWidth;
        int i5 = options.outHeight;
        if (i3 == 90 || i3 == 270) {
            i4 = options.outHeight;
            i5 = options.outWidth;
        }
        return Math.min(i4 / i, i5 / i2);
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int i) {
        if (i == 0 || bitmap == null) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate((float) i, ((float) bitmap.getWidth()) / 2.0f, ((float) bitmap.getHeight()) / 2.0f);
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            if (bitmap == createBitmap) {
                return bitmap;
            }
            bitmap.recycle();
            return createBitmap;
        } catch (OutOfMemoryError unused) {
            return bitmap;
        }
    }

    public static Bitmap scaleBitmapRatioLocked(Bitmap bitmap, int i, int i2) {
        int i3;
        if (bitmap == null) {
            return null;
        }
        int min = Math.min(i, i2);
        if (min <= 0) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > height) {
            i3 = (height * min) / width;
        } else if (width < height) {
            int i4 = (width * min) / height;
            i3 = min;
            min = i4;
        } else {
            i3 = min;
        }
        return scaleBitmap(bitmap, min, i3);
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        if (i <= 0 || i2 <= 0) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width > i) {
            if (height <= i2) {
                return createClippedBitmap(bitmap, (bitmap.getWidth() - i) / 2, 0, i, height);
            }
            float f = ((float) i) / ((float) width);
            float f2 = ((float) i2) / ((float) height);
            if (f > f2) {
                Bitmap a = m48a(bitmap, f, width, height);
                if (a != null) {
                    return createClippedBitmap(a, 0, (a.getHeight() - i2) / 2, i, i2);
                }
                return bitmap;
            }
            Bitmap a2 = m48a(bitmap, f2, width, height);
            if (a2 != null) {
                return createClippedBitmap(a2, (a2.getWidth() - i) / 2, 0, i, i2);
            }
            return bitmap;
        } else if (width > i) {
            return bitmap;
        } else {
            if (height > i2) {
                return createClippedBitmap(bitmap, 0, (bitmap.getHeight() - i2) / 2, width, i2);
            }
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, bitmap.getConfig());
            new Canvas(createBitmap).drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()), new Rect(0, 0, i, i2), new Paint(1));
            return createBitmap;
        }
    }


    private static Bitmap m48a(Bitmap bitmap, float f, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(f, f);
        return Bitmap.createBitmap(bitmap, 0, 0, i, i2, matrix, true);
    }

    public static Bitmap createClippedBitmap(Bitmap bitmap, int i, int i2, int i3, int i4) {
        if (bitmap == null) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, i, i2, i3, i4);
    }

    public static boolean saveBitmap(Bitmap bitmap, String str) {
        return saveBitmap(bitmap, str, 100);
    }

    public static boolean saveBitmap(Bitmap bitmap, String str, int i) {
        if (str == null) {
            return false;
        }
        return saveBitmap(bitmap, new File(str), i);
    }

    public static boolean saveBitmap(Bitmap bitmap, File file) {
        return saveBitmap(bitmap, file, 100);
    }

    public static boolean saveBitmap(Bitmap bitmap, File file, int i) {
        if (bitmap == null || file == null) {
            return false;
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            boolean compress = bitmap.compress(i >= 100 ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, i, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return compress;
        } catch (IOException unused) {
            return false;
        }
    }

    public static Bitmap createColorFilteredBitmap(Bitmap bitmap, ColorMatrix colorMatrix) {
        if (bitmap == null || colorMatrix == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= 0 || height <= 0) {
            return bitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        Paint paint = new Paint();
        paint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public static Bitmap createGrayScaledBitmap(Bitmap bitmap) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        return createColorFilteredBitmap(bitmap, colorMatrix);
    }

    public static String bitmapToBase64String(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 0);
        } catch (OutOfMemoryError unused) {
            return null;
        }
    }

    public static Bitmap bitmapFromBase64String(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            byte[] decode = Base64.decode(str, 0);
            if (decode == null || decode.length <= 0) {
                return null;
            }
            return BitmapFactory.decodeByteArray(decode, 0, decode.length);
        } catch (OutOfMemoryError unused) {
            return null;
        }
    }

    public static Bitmap compositeDrawableWithMask(Bitmap bitmap, Bitmap bitmap2) {
        if (bitmap == null) {
            return null;
        }
        if (bitmap2 == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int width2 = bitmap2.getWidth();
        int height2 = bitmap2.getHeight();
        if (width != width2 || height != height2) {
            return bitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int[] iArr = new int[width];
        int[] iArr2 = new int[width];
        int i = 0;
        while (i < height) {
            bitmap.getPixels(iArr, 0, width, 0, i, width, 1);
            int i2 = i;
            int[] iArr3 = iArr2;
            int[] iArr4 = iArr;
            int i3 = height;
            int i4 = width;
            bitmap2.getPixels(iArr2, 0, width, 0, i2, width, 1);
            for (int i5 = 0; i5 < i4; i5++) {
                iArr4[i5] = (iArr4[i5] & ViewCompat.MEASURED_SIZE_MASK) | ((iArr3[i5] << 8) & ViewCompat.MEASURED_STATE_MASK);
            }
            createBitmap.setPixels(iArr4, 0, i4, 0, i2, i4, 1);
            i = i2 + 1;
            width = i4;
            iArr2 = iArr3;
            iArr = iArr4;
            height = i3;
        }
        return createBitmap;
    }

    public static Bitmap compositeBitmaps(Bitmap bitmap, Bitmap bitmap2) {
        return compositeBitmaps(false, bitmap, bitmap2);
    }

    public static Bitmap compositeBitmaps(boolean z, Bitmap bitmap, Bitmap bitmap2) {
        return compositeBitmaps(z, bitmap, bitmap2);
    }

    public static Bitmap compositeBitmaps(Bitmap... bitmapArr) {
        return compositeBitmaps(false, bitmapArr);
    }

    public static Bitmap compositeBitmaps(boolean z, Bitmap... bitmapArr) {
        Bitmap bitmap;
        int i;
        int i2;
        int[] findMaxDimension;
        Bitmap[] bitmapArr2 = bitmapArr;
        if (bitmapArr2 == null) {
            return null;
        }
        final int r3 = bitmapArr2.length;
        if (r3 == 1) {
            return bitmapArr2[0];
        }
        if (bitmapArr2[0] == null) {
            return bitmapArr2[0];
        }
        int width = bitmapArr2[0].getWidth();
        int height = bitmapArr2[0].getHeight();
        Bitmap.Config config = bitmapArr2[0].getConfig();
        if (!z && (findMaxDimension = findMaxDimension(bitmapArr)) != null) {
            width = findMaxDimension[0];
            height = findMaxDimension[1];
        }
        try {
            bitmap = Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError unused) {
            bitmap = null;
        }
        if (bitmap == null) {
            return bitmapArr2[0];
        }
        Canvas canvas = new Canvas(bitmap);
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        for (Bitmap bitmap2 : bitmapArr2) {
            if (bitmap2 != null) {
                if (!(bitmap2.getWidth() == width && bitmap2.getHeight() == height)) {
                    if (z) {
                        bitmap2 = scaleBitmap(bitmap2, width, height);
                    } else {
                        i2 = (width - bitmap2.getWidth()) / 2;
                        i = (height - bitmap2.getHeight()) / 2;
                        rect.set(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
                        rect2.set(i2, i, bitmap2.getWidth() + i2, bitmap2.getHeight() + i);
                        canvas.drawBitmap(bitmap2, rect, rect2, (Paint) null);
                    }
                }
                i2 = 0;
                i = 0;
                rect.set(0, 0, bitmap2.getWidth(), bitmap2.getHeight());
                rect2.set(i2, i, bitmap2.getWidth() + i2, bitmap2.getHeight() + i);
                canvas.drawBitmap(bitmap2, rect, rect2, (Paint) null);
            }
        }
        return bitmap;
    }


    public static int[] findMaxDimension(Bitmap... bitmapArr) {
        if (bitmapArr == null) {
            return null;
        }
        int[] iArr = {0, 0};
        final int r1 = bitmapArr.length;
        if (r1 != 1) {
            for (Bitmap bitmap : bitmapArr) {
                if (bitmap != null) {
                    if (bitmap.getWidth() > iArr[0]) {
                        iArr[0] = bitmap.getWidth();
                    }
                    if (bitmap.getHeight() > iArr[1]) {
                        iArr[1] = bitmap.getHeight();
                    }
                }
            }
            return iArr;
        } else if (bitmapArr[0] == null) {
            return iArr;
        } else {
            iArr[0] = bitmapArr[0].getWidth();
            iArr[1] = bitmapArr[0].getHeight();
            return iArr;
        }
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap, int i) {
        if (!(bitmap.getWidth() == i && bitmap.getHeight() == i)) {
            int i2 = i * 2;
            bitmap = scaleBitmap(bitmap, i2, i2);
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle((float) (bitmap.getWidth() / 2), (float) (bitmap.getHeight() / 2), (float) (bitmap.getWidth() / 2), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    public static int calculateBrightnessEstimate(Bitmap bitmap, int i) {
        int i2 = 0;
        if (bitmap == null) {
            return 0;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= 0 || height <= 0) {
            return 0;
        }
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i2 < iArr.length) {
            int i7 = iArr[i2];
            i3 += Color.red(i7);
            i4 += Color.green(i7);
            i5 += Color.blue(i7);
            i6++;
            i2 += i;
        }
        return ((i3 + i5) + i4) / (i6 * 3);
    }

    public static int calculateBrightness(Bitmap bitmap) {
        return calculateBrightnessEstimate(bitmap, 1);
    }

    public static Bitmap extendBitmap(Bitmap bitmap, int i, int i2, int i3) {
        if (bitmap == null || i <= 0 || i2 <= 0) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (i < width || i2 < height) {
            return bitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        canvas.drawColor(i3);
        double d = (double) (i - width);
        Double.isNaN(d);
        double d2 = (double) (i2 - height);
        Double.isNaN(d2);
        canvas.drawBitmap(bitmap, (float) ((int) Math.round(d / 2.0d)), (float) ((int) Math.round(d2 / 2.0d)), paint);
        saveBitmap(createBitmap, "/sdcard/newone.png");
        return createBitmap;
    }

    public static Bitmap oilPaintBitmap(Bitmap bitmap) {
        return oilPaintBitmap(bitmap, 15, 10);
    }

    public static Bitmap oilPaintBitmap(Bitmap bitmap, int i, int i2) {
        int i3 = i;
        int i4 = i2;
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= 0 || height <= 0 || i3 <= 0 || i4 <= 0) {
            return bitmap;
        }
        System.currentTimeMillis();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        int[] copyOf = Arrays.copyOf(iArr, iArr.length);
        int i5 = i3;
        while (i5 < height - i3) {
            int i6 = i3;
            while (i6 < width - i3) {
                Arrays.fill(f138c, 0);
                Arrays.fill(f139d, 0);
                Arrays.fill(f140e, 0);
                Arrays.fill(f141f, 0);
                int i7 = -i3;
                int i8 = i7;
                while (i8 <= i3) {
                    int i9 = i7;
                    while (i9 <= i3) {
                        int i10 = iArr[i6 + i9 + ((i5 + i8) * width)];
                        int i11 = (i10 >> 16) & 255;
                        int i12 = (i10 >> 8) & 255;
                        int i13 = i10 & 255;
                        double d = (double) (i11 + i12 + i13);
                        Double.isNaN(d);
                        int i14 = height;
                        int[] iArr2 = iArr;
                        double d2 = (double) i4;
                        Double.isNaN(d2);
                        int i15 = (int) (((d / 3.0d) * d2) / 255.0d);
                        if (i15 > 255) {
                            i15 = 255;
                        }
                        int[] iArr3 = f138c;
                        iArr3[i15] = iArr3[i15] + 1;
                        int[] iArr4 = f139d;
                        iArr4[i15] = iArr4[i15] + i11;
                        int[] iArr5 = f140e;
                        iArr5[i15] = iArr5[i15] + i12;
                        int[] iArr6 = f141f;
                        iArr6[i15] = iArr6[i15] + i13;
                        i9++;
                        height = i14;
                        iArr = iArr2;
                        i3 = i;
                    }
                    int i16 = height;
                    int[] iArr7 = iArr;
                    i8++;
                    i3 = i;
                }
                int i17 = height;
                int[] iArr8 = iArr;
                int i18 = 0;
                int i19 = 0;
                for (int i20 = 0; i20 < 256; i20++) {
                    if (f138c[i20] > i18) {
                        i18 = f138c[i20];
                        i19 = i20;
                    }
                }
                int i21 = f139d[i19] / i18;
                int i22 = f140e[i19] / i18;
                copyOf[(i5 * width) + i6] = (i21 << 16) | ViewCompat.MEASURED_STATE_MASK | (i22 << 8) | (f141f[i19] / i18);
                i6++;
                height = i17;
                iArr = iArr8;
                i3 = i;
            }
            int i23 = height;
            int[] iArr9 = iArr;
            i5++;
            i3 = i;
        }
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        createBitmap.setPixels(copyOf, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        System.currentTimeMillis();
        return createBitmap;
    }

    public static Bitmap loadBitmapFromAssets(Context context, String filePath) {
        Bitmap bitmap = null;
        try (InputStream inputStream = context.getAssets().open(filePath)) {
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
