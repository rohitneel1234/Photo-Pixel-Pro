package com.rohitneel.photopixelpro.photoframe.activities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.rohitneel.photopixelpro.photoframe.fragments.FragmentCreationsList;
import com.rohitneel.photopixelpro.R;
import com.google.android.gms.ads.AdView;


public class ActivityMyCreations extends AppCompatActivity {
    ImageView ivcancel;
    Context context;

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
        setContentView(R.layout.activity_my_creations);
        ivcancel = findViewById(R.id.ivbtnclose);
        context = ActivityMyCreations.this;

        ivcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ActivityCreatePhoto.class);
                context.startActivity(i);
            }
        });
        loadFragment(new FragmentCreationsList());

        getSupportFragmentManager();
    }


    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }
}
