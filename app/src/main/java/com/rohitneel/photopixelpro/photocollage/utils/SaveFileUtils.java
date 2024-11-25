package com.rohitneel.photopixelpro.photocollage.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.rohitneel.photopixelpro.photocollage.photo.PhotoGridView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SaveFileUtils {

    private static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static Uri saveBitmapFile(Context context, Bitmap bitmap, String name, String dir) throws IOException {
        OutputStream fos = null;
        Uri imageUri = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Scoped Storage logic for Android 10 and above
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();

                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + dir);

                // Insert into MediaStore and get the Uri
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                if (imageUri == null) throw new IOException("Failed to create new MediaStore record.");

                fos = resolver.openOutputStream(imageUri);
            } else {
                // Legacy storage logic for Android 9 and below
                File imagesDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
                        + File.separator + dir);

                if (!imagesDir.exists()) imagesDir.mkdirs();

                File imageFile = new File(imagesDir, name);
                fos = new FileOutputStream(imageFile);
                imageUri = Uri.fromFile(imageFile);

                // Notify the media scanner
                MediaScannerConnection.scanFile(context, new String[]{imageFile.toString()}, null, null);
            }

            // Write bitmap to file
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)) {
                throw new IOException("Failed to save bitmap.");
            }
            fos.flush();
        } finally {
            if (fos != null) fos.close();
        }

        return imageUri;
    }

    public static Bitmap createBitmap(PhotoGridView quShotCollageView, int i) {
        quShotCollageView.clearHandling();
        quShotCollageView.invalidate();
        Bitmap createBitmap = Bitmap.createBitmap(i, (int) (((float) i) / (((float) quShotCollageView.getWidth()) / ((float) quShotCollageView.getHeight()))), Bitmap.Config.ARGB_8888);
        quShotCollageView.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public static Bitmap createBitmap(PhotoGridView quShotCollageView) {
        quShotCollageView.clearHandling();
        quShotCollageView.invalidate();
        Bitmap createBitmap = Bitmap.createBitmap(quShotCollageView.getWidth(), quShotCollageView.getHeight(), Bitmap.Config.ARGB_8888);
        quShotCollageView.draw(new Canvas(createBitmap));
        return createBitmap;
    }

}
