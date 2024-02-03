package com.rohitneel.photopixelpro.gallery;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.adapters.GlideApp;

public class FullView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_view);

        ImageView imageView = findViewById(R.id.img_full);

        String img_id = getIntent().getStringExtra("img_id");

        GlideApp.with(getApplicationContext())
                .load(img_id)
                .fitCenter()
                .into(imageView);
    }
}