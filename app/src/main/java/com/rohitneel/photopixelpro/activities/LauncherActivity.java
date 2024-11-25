/**
 * Author: Rohit Neel
 */

package com.rohitneel.photopixelpro.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.app.IntroActivity;
import com.rohitneel.photopixelpro.constant.CommonKeys;

public class LauncherActivity extends AppCompatActivity {

    private Button btnLogin;
    private int wantToExit=0;

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(getColor(R.color.launcher_status_bar_color));
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        SharedPreferences mSharedPreferences = getSharedPreferences(CommonKeys.INTRO_SCREEN_PREF, MODE_PRIVATE);
        boolean mFirstTime = mSharedPreferences.getBoolean(CommonKeys.IS_FIRST_TIME, true);
        if (mFirstTime) {
            Intent i = new Intent(this, IntroActivity.class);
            startActivity(i);
            finish();
            return;
        }
        setContentView(R.layout.activity_launcher);
        btnLogin = findViewById(R.id.btnLogin);

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (wantToExit == 1) {
            moveTaskToBack(true);
            finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            System.exit(0);
        } else {
            Toast.makeText(getApplicationContext(), "Press again to exit.", Toast.LENGTH_SHORT).show();
        }
        wantToExit++;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                wantToExit=0;
            }
        }).start();
    }

}

