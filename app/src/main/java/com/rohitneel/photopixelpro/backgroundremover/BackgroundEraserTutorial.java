package com.rohitneel.photopixelpro.backgroundremover;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.rohitneel.photopixelpro.R;

public class BackgroundEraserTutorial extends AppCompatActivity {

    ImageView imgViewBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_eraser_tutorial);
        imgViewBack =  findViewById(R.id.bgTutorialBack);

        imgViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }
}