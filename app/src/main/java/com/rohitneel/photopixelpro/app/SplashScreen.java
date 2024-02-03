package com.rohitneel.photopixelpro.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDelegate;

import com.rbddevs.splashy.Splashy;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.activities.LauncherActivity;
import com.rohitneel.photopixelpro.activities.MainActivity;
import com.rohitneel.photopixelpro.helper.SessionManager;
import static com.rbddevs.splashy.Splashy.Animation.GROW_LOGO_FROM_CENTER;


public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SessionManager mSession = new SessionManager(getApplicationContext());
        if (mSession.loadState()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.darkTheme);
        }
        setContentView(R.layout.activity_splash_screen);
        setSplashy();
        //TextView txtSplashDescription = findViewById(R.id.splashDescription);
        SharedPreferences mSharedPreferences = getSharedPreferences("introfirst", MODE_PRIVATE);
        boolean mFirstTime = mSharedPreferences.getBoolean("firsttime", true);

        if (mFirstTime) {
            mFirstTime = false;
            mSharedPreferences.edit().putBoolean("firsttime", mFirstTime).apply();
            Intent i = new Intent(SplashScreen.this, IntroActivity.class);
            finish();
            startActivity(i);
        }
    }

    private void setSplashy() {
        Splashy splashy = new Splashy(this);
        splashy.setLogo(R.drawable.app_logo);
        splashy.setAnimation(GROW_LOGO_FROM_CENTER,1000);
        splashy.setBackgroundResource(R.drawable.splash_screen);
        splashy.setTitleColor(R.color.white);
        splashy.setProgressColor(R.color.white);
        splashy.setTitle("Photo Pixel Pro");
        splashy.setTitleSize(20);
        splashy.setSubTitle("Made Easy using photo pixel pro app");
        splashy.setSubTitleColor(R.color.white);
        if(Build.VERSION.SDK_INT <= 29) {
            splashy.setFullScreen(true);
        }
        splashy.setSubTitleFontStyle("fonts/satisfy_regular.ttf");
        splashy.setClickToHide(false);
        splashy.setDuration(2000);
        splashy.show();

        Splashy.Companion.onComplete(new Splashy.OnComplete() {

            @Override
            public void onComplete() {
                Intent intent = new Intent(SplashScreen.this, LauncherActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}