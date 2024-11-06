package com.rohitneel.photopixelpro.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.activities.LauncherActivity;
import com.rohitneel.photopixelpro.helper.SessionManager;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SessionManager mSession = new SessionManager(getApplicationContext());
        if (mSession.loadState()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_splash_screen);
        ImageView logo = findViewById(R.id.imageViewLogo); // Your logo ImageView ID
        TextView title = findViewById(R.id.splashTitle); // Your title TextView ID
        TextView subTitle = findViewById(R.id.splashDescription); // Your subtitle TextView ID

        // Set logo and text
        logo.setImageResource(R.drawable.app_logo);
        title.setText("Photo Pixel Pro");
        subTitle.setText("Made Easy using photo pixel pro app");

        // Optionally customize the splash screen appearance
        if (Build.VERSION.SDK_INT <= 29) {
            getWindow().getDecorView().setSystemUiVisibility(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        // Check SharedPreferences for the first-time launch
        SharedPreferences mSharedPreferences = getSharedPreferences("introfirst", MODE_PRIVATE);
        boolean mFirstTime = mSharedPreferences.getBoolean("firsttime", true);

        if (mFirstTime) {
            mFirstTime = false;
            mSharedPreferences.edit().putBoolean("firsttime", mFirstTime).apply();
            Intent i = new Intent(SplashScreen.this, IntroActivity.class);
            startActivity(i);
            finish();
        } else {
            // Proceed to LauncherActivity after SPLASH_DURATION
            new android.os.Handler().postDelayed(() -> {
                Intent intent = new Intent(SplashScreen.this, LauncherActivity.class);
                startActivity(intent);
                finish();
            }, 2000);
        }
    }
}
