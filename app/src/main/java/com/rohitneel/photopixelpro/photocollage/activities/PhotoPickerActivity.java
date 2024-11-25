package com.rohitneel.photopixelpro.photocollage.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.entity.Photo;
import com.rohitneel.photopixelpro.photocollage.event.OnItemCheckListener;
import com.rohitneel.photopixelpro.photocollage.fragment.ImagePagerFragment;
import com.rohitneel.photopixelpro.photocollage.fragment.PhotoPickerFragment;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoPickerView;

import java.util.ArrayList;

public class PhotoPickerActivity extends AppCompatActivity {
    private boolean forwardMain;
    private ImagePagerFragment imagePagerFragment;
    private int maxCount = 9;
    private ArrayList<String> originalPhotos = null;
    private PhotoPickerFragment pickerFragment;
    private boolean showGif = false;
    public PhotoPickerActivity getActivity() {
        return this;
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(getColor(R.color.login_sign_up_background));
        } else {
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
        }
        boolean booleanExtra = getIntent().getBooleanExtra(PhotoPickerView.EXTRA_SHOW_CAMERA, true);
        boolean booleanExtra2 = getIntent().getBooleanExtra(PhotoPickerView.EXTRA_SHOW_GIF, false);
        boolean booleanExtra3 = getIntent().getBooleanExtra(PhotoPickerView.EXTRA_PREVIEW_ENABLED, true);
        this.forwardMain = getIntent().getBooleanExtra(PhotoPickerView.MAIN_ACTIVITY, false);
        setShowGif(booleanExtra2);
        setContentView(R.layout.activity_photo_picker);
        setSupportActionBar(findViewById(R.id.toolbar));
        setTitle("");
        ActionBar supportActionBar = getSupportActionBar();
        assert supportActionBar != null;
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setElevation(25.0f);
        this.maxCount = getIntent().getIntExtra(PhotoPickerView.EXTRA_MAX_COUNT, 9);
        int intExtra = getIntent().getIntExtra(PhotoPickerView.EXTRA_GRID_COLUMN, 4);
        this.originalPhotos = getIntent().getStringArrayListExtra(PhotoPickerView.EXTRA_ORIGINAL_PHOTOS);
        this.pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("tag");
        if (this.pickerFragment == null) {
            this.pickerFragment = PhotoPickerFragment.newInstance(booleanExtra, booleanExtra2, booleanExtra3, intExtra, this.maxCount, this.originalPhotos);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, this.pickerFragment, "tag").commit();
            getSupportFragmentManager().executePendingTransactions();
        }
        this.pickerFragment.getPhotoGridAdapter().setOnItemCheckListener(new OnItemCheckListener() {
            public final boolean onItemCheck(int i, Photo photo, int i2) {
                if (!forwardMain) {
                    Intent intent = new Intent(PhotoPickerActivity.this, PhotoEditorActivity.class);
                    intent.putExtra(PhotoPickerView.KEY_SELECTED_PHOTOS, photo.getPath());
                    startActivity(intent);
                    finish();
                    return true;
                }
                PhotoCollageActivity.getQueShotGridActivityInstance().replaceCurrentPiece(photo.getPath());
                finish();
                return true;
            }
        });
    }

    public void onBackPressed() {
        if (this.imagePagerFragment == null || !this.imagePagerFragment.isVisible()) {
            super.onBackPressed();
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        }
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void setShowGif(boolean z) {
        this.showGif = z;
    }
}
