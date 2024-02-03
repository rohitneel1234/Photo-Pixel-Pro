package com.rohitneel.photopixelpro.photocollage.eraser;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.rohitneel.photopixelpro.R;

import java.io.IOException;

public class ImageUtils {


    public static Bitmap scaleCenterCrop(Bitmap bitmap, int i, int i2) {
        float f = (float) i2;
        float width = (float) bitmap.getWidth();
        float f2 = (float) i;
        float height = (float) bitmap.getHeight();
        float max = Math.max(f / width, f2 / height);
        float f3 = width * max;
        float f4 = max * height;
        float f5 = (f - f3) / 2.0f;
        float f6 = (f2 - f4) / 2.0f;
        RectF rectF = new RectF(f5, f6, f3 + f5, f4 + f6);
        Bitmap createBitmap = Bitmap.createBitmap(i2, i, bitmap.getConfig());
        new Canvas(createBitmap).drawBitmap(bitmap, null, rectF, null);
        return createBitmap;
    }

    public static Bitmap getResampleImageBitmap(Uri uri, Context context) throws IOException {
        String realPathFromURI = getRealPathFromURI(uri, context);
        try {
            return resampleImage(realPathFromURI, 800);
        } catch (Exception e) {
            e.printStackTrace();
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(realPathFromURI));
        }
    }

    public static Bitmap getResampleImageBitmap(Uri uri, Context context, int i) throws IOException {
        try {
            return resampleImage(getRealPathFromURI(uri, context), i);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap resampleImage(String str, int i) throws Exception {
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(str, options);
            Options options2 = new Options();
            options2.inSampleSize = getClosestResampleSize(options.outWidth, options.outHeight, i);
            Bitmap decodeFile = BitmapFactory.decodeFile(str, options2);
            if (decodeFile == null) {
                return null;
            }
            Matrix matrix = new Matrix();
            if (decodeFile.getWidth() > i || decodeFile.getHeight() > i) {
                Options resampling = getResampling(decodeFile.getWidth(), decodeFile.getHeight(), i);
                matrix.postScale(((float) resampling.outWidth) / ((float) decodeFile.getWidth()), ((float) resampling.outHeight) / ((float) decodeFile.getHeight()));
            }
            if (new Integer(VERSION.SDK).intValue() > 4) {
                int exifRotation = getExifRotation(str);
                if (exifRotation != 0) {
                    matrix.postRotate((float) exifRotation);
                }
            }
            return Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile.getHeight(), matrix, true);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getExifRotation(String str) {
        try {
            String attribute = new ExifInterface(str).getAttribute(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION);
            if (TextUtils.isEmpty(attribute)) {
                return 0;
            }
            int parseInt = Integer.parseInt(attribute);
            if (parseInt == 3) {
                return 180;
            }
            if (parseInt == 6) {
                return 90;
            }
            if (parseInt != 8) {
                return 0;
            }
            return 270;
        } catch (Exception unused) {
            return 0;
        }
    }

    static Options getResampling(int i, int i2, int i3) {
        float f;
        float f2;
        Options options = new Options();
        if (i <= i2 && i2 > i) {
            f = (float) i3;
            f2 = (float) i2;
        } else {
            f = (float) i3;
            f2 = (float) i;
        }
        float f3 = f / f2;
        options.outWidth = (int) ((((float) i) * f3) + 0.5f);
        options.outHeight = (int) ((((float) i2) * f3) + 0.5f);
        return options;
    }

    public static int getClosestResampleSize(int i, int i2, int i3) {
        int max = Math.max(i, i2);
        int i4 = 1;
        while (true) {
            if (i4 >= Integer.MAX_VALUE) {
                break;
            } else if (i4 * i3 > max) {
                i4--;
                break;
            } else {
                i4++;
            }
        }
        if (i4 > 0) {
            return i4;
        }
        return 1;
    }

    public static Options getBitmapDims(Uri uri, Context context) {
        String realPathFromURI = getRealPathFromURI(uri, context);
        StringBuilder sb = new StringBuilder();
        sb.append("Path ");
        sb.append(realPathFromURI);
        Log.i("texting", sb.toString());
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(realPathFromURI, options);
        return options;
    }

    public static String getRealPathFromURI(Uri uri, Context context) {
        try {
            Cursor query = context.getContentResolver().query(uri, null, null, null, null);
            if (query == null) {
                return uri.getPath();
            }
            query.moveToFirst();
            String string = query.getString(query.getColumnIndex("_data"));
            query.close();
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return uri.toString();
        }
    }

    public static Bitmap resizeBitmap(Bitmap bitmap, int i, int i2) {
        float f;
        float f2;
        if (bitmap == null) {
            return null;
        }
        float f3 = (float) i;
        float f4 = (float) i2;
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append(f3);
        String str = "  ";
        sb.append(str);
        sb.append(f4);
        sb.append("  and  ");
        sb.append(width);
        sb.append(str);
        sb.append(height);
        String str2 = "testings";
        Log.i(str2, sb.toString());
        float f5 = width / height;
        float f6 = height / width;
        String str3 = "  if (he > hr) ";
        String str4 = " in else ";
        if (width > f3) {
            f = f3 * f6;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("if (wd > wr) ");
            sb2.append(f3);
            sb2.append(str);
            sb2.append(f);
            Log.i(str2, sb2.toString());
            if (f > f4) {
                f3 = f4 * f5;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str3);
                sb3.append(f3);
                sb3.append(str);
                sb3.append(f4);
                Log.i(str2, sb3.toString());
                return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
            }
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str4);
            sb4.append(f3);
            sb4.append(str);
            sb4.append(f);
            Log.i(str2, sb4.toString());
        } else {
            if (height > f4) {
                f2 = f4 * f5;
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str3);
                sb5.append(f2);
                sb5.append(str);
                sb5.append(f4);
                Log.i(str2, sb5.toString());
                if (f2 <= f3) {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(str4);
                    sb6.append(f2);
                    sb6.append(str);
                    sb6.append(f4);
                    Log.i(str2, sb6.toString());
                    f3 = f2;
                    return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
                }
            } else if (f5 > 0.75f) {
                f = f3 * f6;
                Log.i(str2, " if (rat1 > .75f) ");
                if (f > f4) {
                    f3 = f4 * f5;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str3);
                    sb7.append(f3);
                    sb7.append(str);
                    sb7.append(f4);
                    Log.i(str2, sb7.toString());
                    return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
                }
                StringBuilder sb8 = new StringBuilder();
                sb8.append(str4);
                sb8.append(f3);
                sb8.append(str);
                sb8.append(f);
                Log.i(str2, sb8.toString());
            } else if (f6 > 1.5f) {
                f2 = f4 * f5;
                Log.i(str2, " if (rat2 > 1.5f) ");
                if (f2 <= f3) {
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(str4);
                    sb9.append(f2);
                    sb9.append(str);
                    sb9.append(f4);
                    Log.i(str2, sb9.toString());
                    f3 = f2;
                    return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
                }
            } else {
                f = f3 * f6;
                Log.i(str2, str4);
                if (f > f4) {
                    f3 = f4 * f5;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append(str3);
                    sb10.append(f3);
                    sb10.append(str);
                    sb10.append(f4);
                    Log.i(str2, sb10.toString());
                    return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
                }
                StringBuilder sb11 = new StringBuilder();
                sb11.append(str4);
                sb11.append(f3);
                sb11.append(str);
                sb11.append(f);
                Log.i(str2, sb11.toString());
            }
            f4 = f3 * f6;
            return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
        }
        f4 = f;
        return Bitmap.createScaledBitmap(bitmap, (int) f3, (int) f4, false);
    }


    public static int dpToPx(Context context, int i) {
        float f = (float) i;
        context.getResources();
        return (int) (f * Resources.getSystem().getDisplayMetrics().density);
    }
    public static int dpToPx(Context context, float i) {
        float f = (float) i;
        context.getResources();
        return (int) (f * Resources.getSystem().getDisplayMetrics().density);
    }


    public static Bitmap CropBitmapTransparency(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int i = -1;
        int height = bitmap.getHeight();
        int i2 = -1;
        int i3 = width;
        int i4 = 0;
        while (i4 < bitmap.getHeight()) {
            int i5 = i2;
            int i6 = i;
            int i7 = i3;
            for (int i8 = 0; i8 < bitmap.getWidth(); i8++) {
                if (((bitmap.getPixel(i8, i4) >> 24) & 255) > 0) {
                    if (i8 < i7) {
                        i7 = i8;
                    }
                    if (i8 > i6) {
                        i6 = i8;
                    }
                    if (i4 < height) {
                        height = i4;
                    }
                    if (i4 > i5) {
                        i5 = i4;
                    }
                }
            }
            i4++;
            i3 = i7;
            i = i6;
            i2 = i5;
        }
        if (i < i3 || i2 < height) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, i3, height, (i - i3) + 1, (i2 - height) + 1);
    }

    public static Bitmap bitmapmasking(Bitmap bitmap, Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return createBitmap;
    }

    public static Bitmap getTiledBitmap(Context context, int i, int i2, int i3) {
        Rect rect = new Rect(0, 0, i2, i3);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(BitmapFactory.decodeResource(context.getResources(), i, new Options()), TileMode.REPEAT, TileMode.REPEAT));
        Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Config.ARGB_8888);
        new Canvas(createBitmap).drawRect(rect, paint);
        return createBitmap;
    }

    public static Bitmap getBgCircleBit(Context context, int i) {
        int dpToPx = dpToPx(context, 150);
        return bitmapmasking1(getTiledBitmap(context, i, dpToPx, dpToPx), Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.circle1), dpToPx, dpToPx, true));
    }

    public static Bitmap bitmapmasking1(Bitmap bitmap, Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return createBitmap;
    }


}
