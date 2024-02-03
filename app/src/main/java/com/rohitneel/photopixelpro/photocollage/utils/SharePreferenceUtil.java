package com.rohitneel.photopixelpro.photocollage.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {
    private static final String PHOTO_EDITOR_PRO_2019 = "PHOTO_EDITOR_PRO_2019";

    public static void setHeightOfKeyboard(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
        edit.putInt("height_of_keyboard", i);
        edit.apply();
    }

    public static int getHeightOfKeyboard(Context context) {
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getInt("height_of_keyboard", -1);
    }

    public static void setHeightOfNotch(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
        edit.putInt("height_of_notch", i);
        edit.apply();
    }

    public static int getHeightOfNotch(Context context) {
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getInt("height_of_notch", -1);
    }

    public static void setFirstAdjustBoob(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
        edit.putBoolean("first_boob", z);
        edit.apply();
    }

    public static boolean isFirstAdjustBoob(Context context) {
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getBoolean("first_boob", true);
    }

    public static void setFirstAdjustWaist(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
        edit.putBoolean("first_waise", z);
        edit.apply();
    }

    public static boolean isFirstAdjustWaise(Context context) {
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getBoolean("first_waise", true);
    }

    public static void setFirstAdjustFace(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
        edit.putBoolean("first_face", z);
        edit.apply();
    }

    public static boolean isFirstAdjusFace(Context context) {
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getBoolean("first_face", true);
    }

    public static void setFirstAdjustHip(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
        edit.putBoolean("first_hip", z);
        edit.apply();
    }

    public static boolean isFirstAdjusHip(Context context) {
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getBoolean("first_hip", true);
    }

    public static void setFirstShapeSplash(Context context, boolean z) {
        if (context != null) {
            SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
            edit.putBoolean("first_shape_splash", z);
            edit.apply();
        }
    }

    public static boolean isFirstShapeSplash(Context context) {
        if (context == null) {
            return false;
        }
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getBoolean("first_shape_splash", true);
    }

    public static void setFirstDrawSplash(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
        edit.putBoolean("first_draw_splash", z);
        edit.apply();
    }

    public static boolean isFirstDrawSplash(Context context) {
        if (context == null) {
            return false;
        }
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getBoolean("first_draw_splash", true);
    }

    public static void setFirstShapeBlur(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
        edit.putBoolean("first_shape_blur", z);
        edit.apply();
    }

    public static boolean isFirstShapeBlur(Context context) {
        if (context == null) {
            return false;
        }
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getBoolean("first_shape_blur", true);
    }

    public static void setFirstDrawBlur(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
        edit.putBoolean("first_draw_blur", z);
        edit.apply();
    }

    public static boolean isFirstDrawBlur(Context context) {
        if (context == null) {
            return false;
        }
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getBoolean("first_draw_blur", true);
    }

    public static boolean isPurchased(Context context) {
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getBoolean("is_purchased", false);
    }


    public static String getConsentStatus(Context context) {
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getString("consent_status", "unknown");
    }


    public static boolean isRated(Context context) {
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getBoolean("is_rated_2", false);
    }

    public static void setRated(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
        edit.putBoolean("is_rated_2", z);
        edit.apply();
    }

    public static void increateCounter(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).edit();
        edit.putInt("counter", getCounter(context) + 1);
        edit.apply();
    }

    public static int getCounter(Context context) {
        return context.getSharedPreferences(PHOTO_EDITOR_PRO_2019, 0).getInt("counter", 1);
    }
}
