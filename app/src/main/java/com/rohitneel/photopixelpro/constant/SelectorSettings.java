package com.rohitneel.photopixelpro.constant;

import android.net.Uri;
import com.facebook.common.util.UriUtil;

public class SelectorSettings {
    public static final String SELECTOR_INITIAL_SELECTED_LIST = "selector_initial_selected_list";
    public static final String SELECTOR_MAX_IMAGE_NUMBER = "selector_max_image_number";
    public static final String SELECTOR_MIN_IMAGE_SIZE = "selector_min_image_size";
    public static final String SELECTOR_RESULTS = "selector_results";
    public static int mMaxImageNumber = 9;
    public static int mMinImageSize = 50000;

    public static String getLastPathSegment(String content) {
        if (content == null || content.length() == 0) {
            return "";
        }
        String[] segments = content.split("/");
        if (segments.length > 0) {
            return segments[segments.length - 1];
        }
        return "";
    }

    public static Uri getUriByResId(int resId) {
        return new Uri.Builder().scheme(UriUtil.LOCAL_RESOURCE_SCHEME).path(String.valueOf(resId)).build();
    }
}
