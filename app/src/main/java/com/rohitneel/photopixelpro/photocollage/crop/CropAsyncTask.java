package com.rohitneel.photopixelpro.photocollage.crop;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.rohitneel.photopixelpro.photocollage.constants.StoreManager;
import com.rohitneel.photopixelpro.photocollage.support.SupportedClass;
import com.rohitneel.photopixelpro.photocollage.utils.MotionUtils;


public class CropAsyncTask extends AsyncTask<Void, Void, Void> {
    Activity activity;
    ProgressBar progressBar;
    Bitmap croppedBitmap;
    int left;
    Bitmap maskBitmap;
    CropTaskCompleted onTaskCompleted;
    int top;

    private void show(final boolean z) {
        if (this.activity != null) {
            this.activity.runOnUiThread(new Runnable() {
                public void run() {
                    if (z) {
                        CropAsyncTask.this.progressBar.setVisibility(View.VISIBLE);
                    } else {
                        CropAsyncTask.this.progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public CropAsyncTask(CropTaskCompleted mLOnCropTaskCompleted, Activity activity2, ProgressBar progressBar) {
        this.onTaskCompleted = mLOnCropTaskCompleted;
        this.activity = activity2;
        this.progressBar = progressBar;
    }

    public void onPreExecute() {
        super.onPreExecute();
        show(true);
    }

    public Void doInBackground(Void... voidArr) {
        this.croppedBitmap = StoreManager.getCurrentCropedBitmap(this.activity);
        this.left = StoreManager.croppedLeft;
        this.top = StoreManager.croppedTop;
        this.maskBitmap = StoreManager.getCurrentCroppedMaskBitmap(this.activity);
        if (this.croppedBitmap != null || this.maskBitmap != null) {
            return null;
        }
        DeeplabMobile deeplabMobile = new DeeplabMobile();
        deeplabMobile.initialize(this.activity.getApplicationContext());
        this.croppedBitmap = loadInBackground(StoreManager.getCurrentOriginalBitmap(this.activity), deeplabMobile);
        StoreManager.setCurrentCropedBitmap(this.activity, this.croppedBitmap);
        return null;
    }

    public void onPostExecute(Void voidR) {
        show(false);
        this.onTaskCompleted.onTaskCompleted(this.croppedBitmap, this.maskBitmap, this.left, this.top);
    }

    public Bitmap loadInBackground(Bitmap bitmap, DeeplabMobile deeplabMobile) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float max = 513.0f / ((float) Math.max(bitmap.getWidth(), bitmap.getHeight()));
        int round = Math.round(((float) width) * max);
        int round2 = Math.round(((float) height) * max);
        this.maskBitmap = deeplabMobile.segment(SupportedClass.tfResizeBilinear(bitmap, round, round2));
        if (this.maskBitmap == null) {
            return null;
        }
        this.maskBitmap = BitmapUtils.createClippedBitmap(this.maskBitmap, (this.maskBitmap.getWidth() - round) / 2, (this.maskBitmap.getHeight() - round2) / 2, round, round2);
        this.maskBitmap = BitmapUtils.scaleBitmap(this.maskBitmap, width, height);
        this.left = (this.maskBitmap.getWidth() - width) / 2;
        this.top = (this.maskBitmap.getHeight() - height) / 2;
        StoreManager.croppedLeft = this.left;
        int i = this.top;
        StoreManager.croppedTop = i;
        this.top = i;
        StoreManager.setCurrentCroppedMaskBitmap(this.activity, this.maskBitmap);
        return MotionUtils.cropBitmapWithMask(bitmap, this.maskBitmap, 0, 0);
    }
}
