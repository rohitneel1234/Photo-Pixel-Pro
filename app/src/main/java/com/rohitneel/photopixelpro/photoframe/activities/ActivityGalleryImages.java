package com.rohitneel.photopixelpro.photoframe.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photoframe.adapters.AdapterGalleryImagesList;


public class ActivityGalleryImages extends AppCompatActivity {
    int int_position;
    private GridView gridView;
    AdapterGalleryImagesList adapter;
    ImageView ivBack, ivCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(getColor(R.color.login_sign_up_background));
        } else {
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
        }
        setContentView(R.layout.activity_gallery_images);
        gridView = (GridView) findViewById(R.id.gv_folder);

        ivBack = findViewById(R.id.ivBack);
       // ivCamera = findViewById(R.id.ivCamera);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ActivityGalleryImages.this,ActivityGalleryFile.class);
                startActivity(i);//  finish();
            }
        });


        int_position = getIntent().getIntExtra("value", 0);
        adapter = new AdapterGalleryImagesList(this, ActivityGalleryFile.al_images, int_position);
        gridView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {

        Intent i=new Intent(ActivityGalleryImages.this,ActivityGalleryFile.class);
        startActivity(i);//  finish();
        super.onBackPressed();
    }
}
