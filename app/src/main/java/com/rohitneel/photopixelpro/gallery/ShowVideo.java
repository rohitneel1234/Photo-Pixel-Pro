package com.rohitneel.photopixelpro.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import com.rohitneel.photopixelpro.R;


import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class ShowVideo extends AppCompatActivity {
    private VideoView mVideoView;
    private MediaController mController;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.video_element_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mVideoView  = (VideoView) findViewById(R.id.video_element_view);
        mController = new MediaController(ShowVideo.this);
        mVideoView.setMediaController(mController);

        if(mPath == null) mPath = getIntent().getExtras().getString("PATH");
        mVideoView.setVideoPath(mPath);
        mVideoView.start();
    }

    @Override
    public void onSaveInstanceState(Bundle savedState){
        super.onSaveInstanceState(savedState);
        savedState.putString("PATH", mPath);
        savedState.putInt("POSITION", mVideoView.getCurrentPosition());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedState){
        super.onRestoreInstanceState(savedState);
        mPath = savedState.getString("PATH");
        mVideoView.seekTo(savedState.getInt("POSITION"));
        mVideoView.start();
    }
}
