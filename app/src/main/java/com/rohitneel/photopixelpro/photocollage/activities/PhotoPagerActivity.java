package com.rohitneel.photopixelpro.photocollage.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.fragment.ImagePagerFragment;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoPickerView;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoPreview;

import java.util.ArrayList;

public class PhotoPagerActivity extends AppCompatActivity {
    public ActionBar actionBar;
    private ImagePagerFragment fragment_photo_pager;
    public boolean setDelete;
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    private AdView adView;
    private AdRequest adRequest;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_photo_pager);
        int intExtra = getIntent().getIntExtra(PhotoPreview.EXTRA_CURRENT_ITEM, 0);
        ArrayList<String> stringArrayListExtra = getIntent().getStringArrayListExtra(PhotoPreview.EXTRA_PHOTOS);
        this.setDelete = getIntent().getBooleanExtra(PhotoPreview.EXTRA_SHOW_DELETE, true);
        if (this.fragment_photo_pager == null) {
            this.fragment_photo_pager = (ImagePagerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_photo_pager);
        }
        this.fragment_photo_pager.setPhotos(stringArrayListExtra, intExtra);
        setSupportActionBar(findViewById(R.id.toolbar));
        this.actionBar = getSupportActionBar();
        if (this.actionBar != null) {
            this.actionBar.setDisplayHomeAsUpEnabled(true);
            if (Build.VERSION.SDK_INT >= 21) {
                this.actionBar.setElevation(25.0f);
            }
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(PhotoPickerView.KEY_SELECTED_PHOTOS, this.fragment_photo_pager.getPaths());
        setResult(-1, intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }
}
