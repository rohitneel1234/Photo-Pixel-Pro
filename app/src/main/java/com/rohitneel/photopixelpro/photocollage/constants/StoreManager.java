package com.rohitneel.photopixelpro.photocollage.constants;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

import com.rohitneel.photopixelpro.R;

import java.io.File;
import java.io.FileOutputStream;

public class StoreManager {
    public static int croppedLeft = 0;
    public static int croppedTop = 0;
    public static boolean isNull = false;
    private static String BITMAP_CROPED_FILE_NAME = "temp_croped_bitmap.png";
    private static String BITMAP_CROPED_MASK_FILE_NAME = "temp_croped_mask_bitmap.png";
    private static String BITMAP_FILE_NAME = "temp_bitmap.png";
    private static String BITMAP_ORIGINAL_FILE_NAME = "temp_original_bitmap.png";


    public static Bitmap getCurrentCroppedMaskBitmap(Activity activity) {
        if (isNull) {
            return null;
        }
        return getBitmapByFileName(activity, BITMAP_CROPED_MASK_FILE_NAME);
    }

    public static Bitmap getCurrentCropedBitmap(Activity activity) {
        if (isNull) {
            return null;
        }
        return getBitmapByFileName(activity, BITMAP_CROPED_FILE_NAME);
    }

    public static Bitmap getCurrentOriginalBitmap(Activity activity) {
        return getBitmapByFileName(activity, BITMAP_ORIGINAL_FILE_NAME);
    }

    private static Bitmap getBitmapByFileName(Activity r1, String r2) {
        String file_path = getWorkspaceDirPath(r1) + "/" + r2;
        //Environment.getExternalStorageDirectory().getAbsolutePath() + r1.getResources().getString(R.string.directory) +"/"+r2;
        return BitmapFactory.decodeFile(file_path);
    }

    private static String getWorkspaceDirPath(Context context) {

        if (Build.VERSION.SDK_INT < 29)
            return Environment.getExternalStorageDirectory().getAbsolutePath() + context.getResources().getString(R.string.directory);
        else
            return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();//Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Environment.DIRECTORY_PICTURES + context.getResources().getString(R.string.directory);

    }

    public static void saveFile(Context context, Bitmap bitmap, String fileName) {
        if (bitmap == null)
            return;
        String file_path = getWorkspaceDirPath(context);
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, fileName);
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (fOut != null) fOut.close();
            }catch (Exception e){}
        }

    }

    public static void deleteFile(Context context, String str) {
        File file = new File(getWorkspaceDirPath(context) + "/" + str);
        if (file.exists()) {
            file.delete();
        }
    }

    public static void setCurrentCropedBitmap(Activity activity, Bitmap bitmap) {
        if (bitmap == null) {
            deleteFile(activity, BITMAP_CROPED_FILE_NAME);
            isNull = true;
        } else {
            isNull = false;
        }
        saveFile(activity, bitmap, BITMAP_CROPED_FILE_NAME);
    }

    public static void setCurrentCroppedMaskBitmap(Activity activity, Bitmap bitmap) {
        if (bitmap == null) {
            deleteFile(activity, BITMAP_CROPED_MASK_FILE_NAME);
        }
        saveFile(activity, bitmap, BITMAP_CROPED_MASK_FILE_NAME);
    }

    public static void setCurrentOriginalBitmap(Activity activity, Bitmap bitmap) {
        if (bitmap == null) {
            deleteFile(activity, BITMAP_ORIGINAL_FILE_NAME);
        }
        saveFile(activity, bitmap, BITMAP_ORIGINAL_FILE_NAME);
    }

}
