package com.rohitneel.photopixelpro.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


/**
 * Created by Rohit Neel on 31/07/2020.
 */
public class Utils {
    public static final String TAG     = "GCP_Media";

    @SuppressLint("StaticFieldLeak")
    private static final Utils instance = new Utils();
    @SuppressLint("StaticFieldLeak")
    static Context context;
    private ConnectivityManager connectivityManager;
    NetworkInfo wifiInfo, mobileInfo;
    private boolean connected = false;

    public static Utils getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    /**
     * Create toast for an activity based on the context
     */
    public static void createToast(String s, Context ctx){
        Toast ToastMessage = Toast.makeText(ctx, s, Toast.LENGTH_LONG);
        ToastMessage.setGravity(Gravity.BOTTOM, 30, 40);
        ToastMessage.show();
    }


    /**
     * Get the file directory for our application where all media elements are stored. Creates
     * the directory if it doesn't already exist.
     * @return - File representing the applications directory
     */
    public static File getApplicationDirectory() {
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "jtgAppMedia/");
        if(!path.exists()){
            if (!path.mkdirs()) {
                Log.d("Filmstrip", "failed to create directory");
                return null;
            }
        }
        return path;
    }

    /**
     * Get the string path for a file located from our media gallery.
     * @param activity - Current activity using the function
     * @param uri      - URI for the media file
     * @return         - Path of the URI file
     */
    public static String getPath(Activity activity, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = activity.managedQuery(uri, projection, null, null, null);
        activity.startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    /**
     * Check if a file path exists in a directory.
     * @param directory - Directory we are checking
     * @param path      - path of the file we want to determine is in the directory or not.
     * @return          - True if exists, false if the path doesn't
     */
    public static boolean inDirectory(File directory, String path){

        File[] files = directory.listFiles();
        if(files != null) {
            for (File file : files) {
                String fileName = file.getAbsolutePath().split(directory.getAbsolutePath())[1];
                if (fileName.equals(File.separator + path)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isNetworkAvailable(){
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;

        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }


    public static void deleteFiles(String path) {
        File file = new File(path);
        if (file.exists()) {
            String deleteCmd = "rm -r " + path;
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            } catch (IOException e) { }
        }
    }

    public static boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

}