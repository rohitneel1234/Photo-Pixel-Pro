package com.rohitneel.photopixelpro.photoframe.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.graphics.Color;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photoframe.adapters.AdapterGalleryImagesList;


public class ActivityGalleryImages extends AppCompatActivity {
    int int_position;
    private GridView gridView;
    AdapterGalleryImagesList adapter;
    ImageView ivBack, ivCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this, SystemBarStyle.dark(Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_images);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);

            View statusBarSpacer = findViewById(R.id.statusBarSpacer);
            if (statusBarSpacer != null) {
                statusBarSpacer.getLayoutParams().height = systemBars.top;
                statusBarSpacer.requestLayout();
            }

            return insets;
        });

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
