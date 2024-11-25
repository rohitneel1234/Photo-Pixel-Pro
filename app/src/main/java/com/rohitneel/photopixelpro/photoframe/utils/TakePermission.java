package com.rohitneel.photopixelpro.photoframe.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class TakePermission {
    Activity activity;
    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] PERMISSIONS_33 = {
            Manifest.permission.READ_MEDIA_IMAGES
    };

    public TakePermission(Activity activity) {
        this.activity = activity;
    }

    public boolean TakePermissionAS() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (hasPermission(activity, PERMISSIONS_33)) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_33, 1);
            }
        } else {
            if (hasPermission(activity, PERMISSIONS)) {
                return true;
            } else {
                ActivityCompat.requestPermissions(activity, PERMISSIONS, 1);
            }
        }
        return false;
    }

    public boolean hasPermission(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
