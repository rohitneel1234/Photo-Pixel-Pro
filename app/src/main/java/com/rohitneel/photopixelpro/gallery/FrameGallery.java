package com.rohitneel.photopixelpro.gallery;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitneel.photopixelpro.BuildConfig;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.adapters.GlideApp;
import com.rohitneel.photopixelpro.adapters.ImageListAdapter;
import com.rohitneel.photopixelpro.photocollage.utils.FilePathUtil;
import com.rohitneel.photopixelpro.util.Utils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

import static android.content.Intent.ACTION_VIEW;

public class FrameGallery extends AppCompatActivity {

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private ArrayList<String> frameFileList;
    private String mOutputPath;
    private File file;
    private AlertDialog alert;
    private Dialog dialog;
    private Button btnOk;
    private TextView txtWarningMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_gallery);
        ActionBar bar = this.getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4030E8")));
        }
        frameFileList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= 29) {
            boolean isWritable = Utils.isExternalStorageWritable();
            if(isWritable) {
                String relativePath = Environment.DIRECTORY_PICTURES + File.separator + getApplicationContext().getString(R.string.app_name) + File.separator;
                ContentResolver resolver = getApplicationContext().getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);
                Uri contUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                try {
                    String path = FilePathUtil.getPath(getApplicationContext(), contUri);
                    if (path != null) {
                        mOutputPath = new File(path).getParent();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            mOutputPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + getString(R.string.app_name) + File.separator;
        }
        if (mOutputPath != null) {
            file = new File(mOutputPath);
            CheckRequestPermissions();
            if (file.exists()) {
                for (File filePath : Objects.requireNonNull(file.listFiles())) {
                    if (filePath.getPath().contains("Frame_")) {
                        frameFileList.add(filePath.getPath());
                    }
                }
            }
        }
        try {
            populateCollageRecyclerView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Requesting user to grant write external storage permission.
     */
    private void CheckRequestPermissions() {
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(FrameGallery.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    /**
     * For every file in the current directory of our application, get a image or video to display in our RecyclerView.
     */
    public void populateCollageRecyclerView() throws Exception {
        final Handler handler = new Handler();
        checkWarningForFileNotExist();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (file.exists()) {
                    handler.post(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void run() {
                            RecyclerView recyclerView = findViewById(R.id.rcvFrameGallery);
                            recyclerView.setLayoutManager(new GridLayoutManager(FrameGallery.this, 2));
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                                    DividerItemDecoration.VERTICAL));
                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                                    DividerItemDecoration.HORIZONTAL));

                            if (frameFileList != null) {
                                ImageListAdapter adapter = new ImageListAdapter(frameFileList);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(new ImageListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onClick(int position) {
                                        ShowDialogBox(frameFileList.get(position));
                                    }
                                });
                            }
                        }
                    });
                }
            }
        }).start();
    }

    @SuppressLint("SetTextI18n")
    private void checkWarningForFileNotExist() {
        if (!file.exists() || frameFileList.isEmpty()) {
            dialog = new Dialog(FrameGallery.this);
            dialog.setContentView(R.layout.custom_warning_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setWindowAnimations(R.style.AnimationsForDialog);
            btnOk = dialog.findViewById(R.id.btnOK);
            txtWarningMessage = dialog.findViewById(R.id.txtWarningMessage);
            txtWarningMessage.setText("To view the gallery first select photos from the system gallery using the Photo Frame category!");
            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    /**
     * Create a dialog to view an image or video element. A video element allows the user to
     * watch the video, and the image element just displays the image larger.
     * @param mediaPath - mediapath the user wishes to view
     */
    private void ShowDialogBox(final String mediaPath) {

        final Dialog dialog = new Dialog(this);

        //Check the type of our element (video or image)
        dialog.setContentView(R.layout.custom_rate_us_dialog);
        ImageView Image = dialog.findViewById(R.id.img);
        Button btn_Full = dialog.findViewById(R.id.btn_full);
        Button btn_Close = dialog.findViewById(R.id.btn_close);
        //extracting name
        GlideApp.with(getApplicationContext())
                .load(mediaPath)
                .fitCenter()
                .into(Image);

        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_Full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = FileProvider.getUriForFile(FrameGallery.this, BuildConfig.APPLICATION_ID + ".provider", new File(mediaPath));
                Intent intent = new Intent(ACTION_VIEW);
                intent.setDataAndType(uri, "image/*");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }
        });

        dialog.show();
    }

    /**
     * @param requestCode - The requestCode help to identify from which Intent you came back
     * @param permissions - To check if write external storage permission granted.
     * @param grantResults - To compare with write permission is granted or not.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            int grantResultsLength = grantResults.length;
            if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "You granted write external storage permission", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
            }
        }
    }
}