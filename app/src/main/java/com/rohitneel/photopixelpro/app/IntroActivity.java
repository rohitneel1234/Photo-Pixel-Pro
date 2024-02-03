package com.rohitneel.photopixelpro.app;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.activities.LauncherActivity;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btnNext;
    int position = 0 ;
    Button btnGetStarted;
    Animation btnAnim ;
    TextView tvSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);
        // hide the action bar
        getSupportActionBar().hide();
        // ini views
        btnNext = findViewById(R.id.btn_next);
        btnGetStarted = findViewById(R.id.btn_get_started);
        tabIndicator = findViewById(R.id.tab_indicator);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_animation);
        tvSkip = findViewById(R.id.tv_skip);
        // fill list screen
        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Welcome to Photo Pixel Pro","Photo editing is made fun, fast, and easy. Simply touch your way to better-looking pictures. This photo pixel pro offers all the advanced features as other best pic editing apps with all-in-one!",R.drawable.app_logo));
        mList.add(new ScreenItem("Photo Editor - Beauty Effect ","Photo editor offers you the best photo editing experience with a complete toolkit to make each picture beautiful. Comes with tools for cropping, trimming, filters, adding text, and much more!",R.drawable.photeditor));
        mList.add(new ScreenItem("Collage Maker ","Collage maker helps your photos together vertically or horizontally easily. You can combine many photos into a beautiful collage!",R.drawable.intro_collage_maker_logo));
        mList.add(new ScreenItem("Photo Frame ","Photo frame which helps you to add a different category of frames to your photos!",R.drawable.photo_frame_intro));
        mList.add(new ScreenItem("Background Remover","Background remover can easily remove background from your photos and save the transparent image in PNG or JPG format!",R.drawable.bg_eraser_home));
        mList.add(new ScreenItem("Photo Gallery","Spend less time managing pictures. You can view images on the list view in form of a gallery!",R.drawable.intro_photo_gallery));
        mList.add(new ScreenItem("Dark Mode Support","It helps to reduce power usage and improves visibility for you with low vision and those who are sensitive to bright light.",R.drawable.dark_theme_logo));
        mList.add(new ScreenItem("Now It's Your Turn","Capture a photo or choose from the gallery and apply different effects using editor tools where it offers massive popular photo filters, neon, collage layouts, frames, and much more!",R.drawable.photographerimg));
        // setup viewpagerx
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);
        // setup tablayout with viewpager
        tabIndicator.setupWithViewPager(screenPager);
        // next button click Listner
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = screenPager.getCurrentItem();
                if (position < mList.size()) {
                    position++;
                    screenPager.setCurrentItem(position);
                }

                if (position == mList.size()-1) { // when we rech to the last screen
                    // TODO : show the GETSTARTED Button and hide the indicator and the next button
                    loadLastScreen();
                }
            }
        });
        // tablayout add change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size()-1) {
                    loadLastScreen();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Get Started button click listener
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open main activity
                Intent loginActivity = new Intent(getApplicationContext(), LauncherActivity.class);
                startActivity(loginActivity);
                // also we need to save a boolean value to storage so next time when the user run the app
                // we could know that he is already checked the intro screen activity
                // i'm going to use shared preferences to that process
                //savePrefsData();
                finish();
            }
        });

        // skip button click listener
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                screenPager.setCurrentItem(mList.size());
            }
        });
    }

    // show the GET STARTED Button and hide the indicator and the next button
    private void loadLastScreen() {
        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tvSkip.setVisibility(View.INVISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);
        // TODO : ADD an animation the getstarted button
        // setup animation
        btnGetStarted.setAnimation(btnAnim);
    }
}
