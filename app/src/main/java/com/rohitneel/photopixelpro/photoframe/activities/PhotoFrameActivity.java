package com.rohitneel.photopixelpro.photoframe.activities;

import static com.rohitneel.photopixelpro.constant.CommonKeys.mBirthdayKey;
import static com.rohitneel.photopixelpro.constant.CommonKeys.mFlowersKey;
import static com.rohitneel.photopixelpro.constant.CommonKeys.mLoveAnniversaryKey;
import static com.rohitneel.photopixelpro.constant.CommonKeys.mWallKey;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.photoframe.adapters.AdapterFrameList;
import com.rohitneel.photopixelpro.photoframe.model.ModelclassFrameList;
import com.rohitneel.photopixelpro.R;

import java.util.ArrayList;
import java.util.TimerTask;

public class PhotoFrameActivity extends AppCompatActivity {
    Context context;
    RecyclerView recyclerView;
    public static PhotoFrameActivity instance = null;
    TimerTask hourlyTask;
    public PhotoFrameActivity() {
        instance = PhotoFrameActivity.this;
    }

    public static synchronized PhotoFrameActivity getInstance() {
        if (instance == null) {
            instance = new PhotoFrameActivity();
        }
        return instance;
    }
    private final String TAG = PhotoFrameActivity.class.getSimpleName();
    private String frameValue;
    private int frames[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_frame);

        context = PhotoFrameActivity.this;
        recyclerView = findViewById(R.id.rv_framelist);

        ArrayList<ModelclassFrameList> frameLists = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            frameValue = extras.getString("frame");
        }

        if (frameValue.equals(mBirthdayKey)) {
            frames = new int[]{
                    R.drawable.b1,
                    R.drawable.b2,
                    R.drawable.b3,
                    R.drawable.b4,
                    R.drawable.b5,
                    R.drawable.b6,
                    R.drawable.b7,
                    R.drawable.b8,
                    R.drawable.b9,
                    R.drawable.b10,
                    R.drawable.b11,
                    R.drawable.b12,
                    R.drawable.b13,
                    R.drawable.b14,
                    R.drawable.b15,
                    R.drawable.b16,
                    R.drawable.b17,
                    R.drawable.b18,
                    R.drawable.b19,
                    R.drawable.b20
            };
        } else if (frameValue.equals(mFlowersKey)) {
            frames = new int[]{
                    R.drawable.f1,
                    R.drawable.f2,
                    R.drawable.f3,
                    R.drawable.f4,
                    R.drawable.f5,
                    R.drawable.f6,
                    R.drawable.f7,
                    R.drawable.f8,
                    R.drawable.f9,
                    R.drawable.f10,
                    R.drawable.f11,
                    R.drawable.f12,
            };
        } else if(frameValue.equals(mLoveAnniversaryKey)) {
            frames = new int[]{
                    R.drawable.la1,
                    R.drawable.la2,
                    R.drawable.la3,
                    R.drawable.la4,
                    R.drawable.la5,
                    R.drawable.la6,
                    R.drawable.la7,
                    R.drawable.la8,
                    R.drawable.la9,
                    R.drawable.la10,
                    R.drawable.la11,
                    R.drawable.la12
            };
        } else if(frameValue.equals(mWallKey)){
           frames = new int[]{
                        R.drawable.wf3,
                        R.drawable.wf4,
                        R.drawable.wf5,
                        R.drawable.wf6,
                        R.drawable.wf11,
                        R.drawable.wf12,
                        R.drawable.wf13,
                        R.drawable.wf14,
                        R.drawable.wf15,
                        R.drawable.wf16,
                        R.drawable.wf17,
                        R.drawable.wf18,
           };
        }

        for (int i = 0; i < frames.length; i++) {
            ModelclassFrameList modelclassFrameList = new ModelclassFrameList();
            modelclassFrameList.setFrame(frames[i]);
            frameLists.add(modelclassFrameList);
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        AdapterFrameList adapterCategory = new AdapterFrameList(context, frameLists);
        recyclerView.setAdapter(adapterCategory);

        getToolBarTitle();
    }

    private void getToolBarTitle() {
        ActionBar bar = this.getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4030E8")));
        }
        if (frameValue.equals(mBirthdayKey)) {
            ActionBar actionBar = this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle("Select Birthday Frame");
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0B0B0B")));
            }
        } else if (frameValue.equals(mFlowersKey)) {
            ActionBar actionBar = this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0B0B0B")));
                actionBar.setTitle("Select Flowers Frame");
            }
        } else if (frameValue.equals(mLoveAnniversaryKey)) {
            ActionBar actionBar = this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0B0B0B")));
                actionBar.setTitle("Select Love or Anniversary Frame");
            }
        } else {
            ActionBar actionBar = this.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0B0B0B")));
                actionBar.setTitle("Select Frame");
            }
        }
    }
}
