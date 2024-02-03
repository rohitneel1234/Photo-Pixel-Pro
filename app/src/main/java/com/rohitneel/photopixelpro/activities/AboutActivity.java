package com.rohitneel.photopixelpro.activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.helper.SessionManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AboutActivity extends AppCompatActivity {

    private ImageView imgAboutBack;
    private SessionManager sessionManager;
    private TextView txtAppVersion, txtHeadline, txtFeatureTitle, txtFeatures;
    private String versionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.launcher_status_bar_color));
        setContentView(R.layout.activity_about);
        imgAboutBack = findViewById(R.id.imgAboutBack);
        sessionManager = new SessionManager(getApplicationContext());
        txtAppVersion = findViewById(R.id.txtAppName);
        txtHeadline = findViewById(R.id.txtHeadline);
        txtFeatureTitle = findViewById(R.id.txtFeatureTitle);
        //txtFeatures = findViewById(R.id.txtFeatures);

        if (sessionManager.loadState()) {
            txtAppVersion.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            txtHeadline.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
            txtFeatureTitle.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
           // txtFeatures.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        }

        if (sessionManager.loadFullScreenState()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        PackageManager manager = this.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), PackageManager.GET_ACTIVITIES);
            versionName = info.versionName;
            txtAppVersion.setText(String.format("%s","Photo Pixel Pro v"+versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        imgAboutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager.loadFullScreenState()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}