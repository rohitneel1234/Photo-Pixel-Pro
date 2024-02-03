package com.rohitneel.photopixelpro.photocollage.picker;

import android.app.Activity;
import android.content.Context;
import android.os.Build;

import androidx.fragment.app.Fragment;

public class AndroidLifecycleUtils {
    public static boolean canLoadImage(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        return canLoadImage(fragment.getActivity());
    }

    public static boolean canLoadImage(Context context) {
        if (context != null && (context instanceof Activity)) {
            return canLoadImage((Activity) context);
        }
        return true;
    }

    public static boolean canLoadImage(Activity activity) {
        if (activity == null) {
            return true;
        }
        return !(Build.VERSION.SDK_INT >= 17 && activity.isDestroyed()) && !activity.isFinishing();
    }
}
