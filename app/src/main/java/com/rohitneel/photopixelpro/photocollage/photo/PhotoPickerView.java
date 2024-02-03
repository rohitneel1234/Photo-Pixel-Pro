package com.rohitneel.photopixelpro.photocollage.photo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.rohitneel.photopixelpro.photocollage.activities.PhotoPickerActivity;
import com.rohitneel.photopixelpro.photocollage.picker.PermissionsUtils;

import java.util.ArrayList;

public class PhotoPickerView {
    public static final String EXTRA_GRID_COLUMN = "column";
    public static final String EXTRA_MAX_COUNT = "MAX_COUNT";
    public static final String EXTRA_ORIGINAL_PHOTOS = "ORIGINAL_PHOTOS";
    public static final String EXTRA_PREVIEW_ENABLED = "PREVIEW_ENABLED";
    public static final String EXTRA_SHOW_CAMERA = "SHOW_CAMERA";
    public static final String EXTRA_SHOW_GIF = "SHOW_GIF";
    public static final String KEY_SELECTED_PHOTOS = "SELECTED_PHOTOS";
    public static final String MAIN_ACTIVITY = "MAIN_ACTIVITY";
    public static final int REQUEST_CODE = 233;

    public static PhotoPickerBuilder builder() {
        return new PhotoPickerBuilder();
    }

    public static class PhotoPickerBuilder {
        private Intent mPickerIntent = new Intent();
        private Bundle mPickerOptionsBundle = new Bundle();

        public void start(@NonNull Activity activity, int i) {
            if (PermissionsUtils.checkReadStoragePermission(activity)) {
                activity.startActivityForResult(getIntent(activity), i);
            }
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment, int i) {
            if (PermissionsUtils.checkReadStoragePermission(fragment.getActivity())) {
                fragment.startActivityForResult(getIntent(context), i);
            }
        }

        public void start(@NonNull Context context, @NonNull Fragment fragment) {
            if (PermissionsUtils.checkReadStoragePermission(fragment.getActivity())) {
                fragment.startActivityForResult(getIntent(context), PhotoPickerView.REQUEST_CODE);
            }
        }

        public Intent getIntent(@NonNull Context context) {
            this.mPickerIntent.setClass(context, PhotoPickerActivity.class);
            this.mPickerIntent.putExtras(this.mPickerOptionsBundle);
            return this.mPickerIntent;
        }

        public void start(@NonNull Activity activity) {
            start(activity, (int) PhotoPickerView.REQUEST_CODE);
        }

        public PhotoPickerBuilder setPhotoCount(int i) {
            this.mPickerOptionsBundle.putInt(PhotoPickerView.EXTRA_MAX_COUNT, i);
            return this;
        }

        public PhotoPickerBuilder setGridColumnCount(int i) {
            this.mPickerOptionsBundle.putInt(PhotoPickerView.EXTRA_GRID_COLUMN, i);
            return this;
        }

        public PhotoPickerBuilder setShowGif(boolean z) {
            this.mPickerOptionsBundle.putBoolean(PhotoPickerView.EXTRA_SHOW_GIF, z);
            return this;
        }

        public PhotoPickerBuilder setShowCamera(boolean z) {
            this.mPickerOptionsBundle.putBoolean(PhotoPickerView.EXTRA_SHOW_CAMERA, z);
            return this;
        }

        public PhotoPickerBuilder setSelected(ArrayList<String> arrayList) {
            this.mPickerOptionsBundle.putStringArrayList(PhotoPickerView.EXTRA_ORIGINAL_PHOTOS, arrayList);
            return this;
        }

        public PhotoPickerBuilder setForwardMain(boolean z) {
            this.mPickerOptionsBundle.putBoolean(PhotoPickerView.MAIN_ACTIVITY, z);
            return this;
        }

        public PhotoPickerBuilder setPreviewEnabled(boolean z) {
            this.mPickerOptionsBundle.putBoolean(PhotoPickerView.EXTRA_PREVIEW_ENABLED, z);
            return this;
        }
    }
}
