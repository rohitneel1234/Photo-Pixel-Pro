package com.rohitneel.photopixelpro.photocollage.picker;

import android.app.Activity;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class PermissionsUtils {
    public static boolean checkReadStoragePermission(Activity activity) {
        boolean z = true;
        if (Build.VERSION.SDK_INT < 16) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE") != 0) {
            z = false;
        }
        if (!z) {
            ActivityCompat.requestPermissions(activity, PermissionsConstant.PERMISSIONS_EXTERNAL_READ, 2);
        }
        return z;
    }

    public static boolean checkWriteStoragePermission(Fragment fragment) {
        if(Build.VERSION.SDK_INT>=29)return true;
        boolean z = ContextCompat.checkSelfPermission(fragment.getContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
        if (!z) {
            fragment.requestPermissions(PermissionsConstant.PERMISSIONS_EXTERNAL_WRITE, 3);
        }
        return z;
    }

    public static boolean checkCameraPermission(Fragment fragment) {
        boolean z = ContextCompat.checkSelfPermission(fragment.getContext(), "android.permission.CAMERA") == 0;
        if (!z) {
            fragment.requestPermissions(PermissionsConstant.PERMISSIONS_CAMERA, 1);
        }
        return z;
    }

    public static boolean checkCameraPermission(Activity activity) {
        boolean z = ContextCompat.checkSelfPermission(activity.getApplicationContext(), "android.permission.CAMERA") == 0;
        if (!z) {
            ActivityCompat.requestPermissions(activity, PermissionsConstant.PERMISSIONS_CAMERA, 1);
        }
        return z;
    }

    public static boolean checkWriteStoragePermission(Activity activity) {
        if(Build.VERSION.SDK_INT>=29)return true;
        boolean z = ContextCompat.checkSelfPermission(activity.getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
        if (!z) {
            ActivityCompat.requestPermissions(activity, PermissionsConstant.PERMISSIONS_EXTERNAL_WRITE, 3);
        }
        return z;
    }
}
