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
import android.util.Log;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoGridView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SaveFileUtils {

    private static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File saveBitmapFileEditor(Context context, Bitmap bitmap, String name, String dir) throws IOException {

        if (Build.VERSION.SDK_INT >= 29) {
            boolean isWritable = isExternalStorageWritable();
            String imagesRelPath = null;
            if (isWritable) {
                FileOutputStream fos = null;
                try {
                    String relativePath = dir != null ? Environment.DIRECTORY_PICTURES + File.separator + dir : Environment.DIRECTORY_PICTURES + File.separator + context.getString(R.string.app_name) + File.separator;
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                    contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);

                    Uri contUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    imagesRelPath = FilePathUtil.getPath(context, contUri);
                    fos = (FileOutputStream) resolver.openOutputStream(contUri);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                } catch (Exception e) {
                    String err = e.getMessage();
                    Log.e("File save exception", err);
                } finally {
                    if (fos != null) fos.close();
                }
                return imagesRelPath != null ? new File(imagesRelPath) : null;
            }
            return null;

        }
        String imagesDir2 = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + context.getString(R.string.app_name) + File.separator;
        File file2 = new File(imagesDir2);
        if (!file2.exists()) {
            file2.mkdir();
        }

        File image = new File(imagesDir2, name + ".jpg");
        OutputStream fos2 = new FileOutputStream(image);
        MediaScannerConnection.scanFile(context, new String[]{image.getAbsolutePath()}, (String[]) null, new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
            }

            public void onScanCompleted(String path, Uri uri) {
            }
        });
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos2);
        fos2.flush();
        fos2.close();
        return image;
    }

    public static File saveBitmapFileCollage(Context context, Bitmap bitmap, String name, String dir) throws IOException {

        if (Build.VERSION.SDK_INT >= 29) {
            boolean isWritable = isExternalStorageWritable();
            String imagesRelPath = null;
            if (isWritable) {
                FileOutputStream fos = null;
                try {
                    String relativePath = dir != null ? Environment.DIRECTORY_PICTURES + File.separator + dir : Environment.DIRECTORY_PICTURES + File.separator + context.getString(R.string.app_name) + File.separator;
                    ContentResolver resolver = context.getContentResolver();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
                    //contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
                    contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);

                    Uri contUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    imagesRelPath = FilePathUtil.getPath(context, contUri);
                    fos = (FileOutputStream) resolver.openOutputStream(contUri);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                } catch (Exception e) {
                    String err = e.getMessage();
                } finally {
                    if (fos != null) fos.close();
                }
            }
            return imagesRelPath != null ? new File(imagesRelPath) : null;
        }
        String imagesDir2 = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_PICTURES + File.separator + context.getString(R.string.app_name) + File.separator;
        File file2 = new File(imagesDir2);
        if (!file2.exists()) {
            file2.mkdir();
        }
        File image = new File(imagesDir2, name);
        OutputStream fos2 = new FileOutputStream(image);
        MediaScannerConnection.scanFile(context, new String[]{image.getAbsolutePath()}, (String[]) null, new MediaScannerConnection.MediaScannerConnectionClient() {
            public void onMediaScannerConnected() {
            }

            public void onScanCompleted(String path, Uri uri) {
            }
        });
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos2);
        fos2.flush();
        fos2.close();
        return image;
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
