package com.rohitneel.photopixelpro.tutorials;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.activities.MainActivity;
import com.rohitneel.photopixelpro.activities.SettingsActivity;
import com.rohitneel.photopixelpro.app.ScreenItem;
import com.rohitneel.photopixelpro.helper.SessionManager;

import java.util.ArrayList;
import java.util.List;

import static com.rohitneel.photopixelpro.constant.CommonKeys.mHomeKey;
import static com.rohitneel.photopixelpro.constant.CommonKeys.mSettingsKey;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPager;
    TutorialViewPagerAdapter introViewPagerAdapter ;
    Button btnNext;
    private Button btnSkip;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private ImageView mTutorialBack;
    private List<ScreenItem> mList;
    private String home, settings;
    private String homeValues = "home", activities = "settings";
    private SessionManager mSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        ActionBar actionBar = this.getSupportActionBar();
        mSession = new SessionManager(getApplicationContext());
        CharSequence actionbarTitle = null;
        if (actionBar != null) {
            actionbarTitle = actionBar.getTitle();
            Log.d("Title", "Toolbar: " + actionbarTitle);
        }
        assert actionBar != null;
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.tutorial_toolbar_title);

        Intent intent = getIntent();
        activities = intent.getStringExtra(mHomeKey);
        settings = intent.getStringExtra(mSettingsKey);

        // init view
        btnNext = findViewById(R.id.btn_next);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
        mTutorialBack = findViewById(R.id.tutorial_back_button);

        // fill list screen
        mList = new ArrayList<>();
        mList.add(new ScreenItem(getString(R.string.slide_0_title), getString(R.string.slide_0_desc), R.drawable.tutorial_home_page));
        mList.add(new ScreenItem(getString(R.string.slide_1_title), getString(R.string.slide_1_desc), R.drawable.tutorial_main_side_bar_page));
        mList.add(new ScreenItem(getString(R.string.slide_3_title), getString(R.string.slide_3_desc), R.drawable.tutorial_choose_photo_page));
        mList.add(new ScreenItem(getString(R.string.slide_4_title), getString(R.string.slide_4_desc), R.drawable.tutorial_photo_editor_page));
        mList.add(new ScreenItem(getString(R.string.slide_5_title), getString(R.string.slide_5_desc),R.drawable.tutorial_drip_effect_page));
        mList.add(new ScreenItem(getString(R.string.slide_6_title), getString(R.string.slide_6_desc),R.drawable.tutorial_neon_effect_page));
        mList.add(new ScreenItem(getString(R.string.slide_7_title), getString(R.string.slide_7_desc), R.drawable.tutorial_gallery_page));
        mList.add(new ScreenItem(getString(R.string.slide_8_title), getString(R.string.slide_8_desc), R.drawable.tutorial_photo_collage_maker));
        mList.add(new ScreenItem(getString(R.string.slide_9_title), getString(R.string.slide_9_desc), R.drawable.tutorial_photo_frame_category));
        mList.add(new ScreenItem(getString(R.string.slide_10_title), getString(R.string.slide_10_desc), R.drawable.tutorial_photo_select_frame));
        mList.add(new ScreenItem(getString(R.string.slide_11_title), getString(R.string.slide_11_desc), R.drawable.tutorial_background_eraser));
        mList.add(new ScreenItem(getString(R.string.slide_12_title), getString(R.string.slide_12_desc), R.drawable.tutorial_settings_page));

        // setup viewpager
        viewPager = findViewById(R.id.view_pager);
        introViewPagerAdapter = new TutorialViewPagerAdapter(this,mList);
        viewPager.setAdapter(introViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        addBottomDots(0);

        mTutorialBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (homeValues.equals(activities)) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                }
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchSettingsScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem();
                if (current < mList.size()) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    launchSettingsScreen();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSession.loadFullScreenState()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    private void addBottomDots(int currentPage) {
        dots = new TextView[mList.size()];

        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }

    private void launchSettingsScreen() {
        if(homeValues.equals(activities)) {
            startActivity(new Intent(TutorialActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(TutorialActivity.this, SettingsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        TutorialActivity.this.finish();
    }


    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);

            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == mList.size() - 1) {
                // last page. make button text to GOT IT
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                // still pages are left
                btnNext.setText(getString(R.string.next));
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private int getItem() {
        return viewPager.getCurrentItem() + 1;
    }

}
