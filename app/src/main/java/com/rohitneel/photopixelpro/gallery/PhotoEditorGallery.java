package com.rohitneel.photopixelpro.gallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.adapters.GlideApp;
import com.rohitneel.photopixelpro.adapters.ImageListAdapter;
import com.rohitneel.photopixelpro.helper.SessionManager;
import com.rohitneel.photopixelpro.photocollage.utils.FilePathUtil;
import com.rohitneel.photopixelpro.util.Utils;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Objects;

public class PhotoEditorGallery extends AppCompatActivity {

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private File mDirectory;
    private ArrayList<String> list;
    private ArrayList<String> fileList;
    private String mOutputPath;
    private File file;
    private SessionManager mSession;
    private Dialog dialog;
    private Button btnOk;
    private TextView txtWarningMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_media);
        list = new ArrayList<>();
        fileList = new ArrayList<>();
        mSession = new SessionManager(getApplicationContext());
        if (mSession.loadFullScreenState()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

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
                    if (filePath.getPath().contains("PhotoEditor_") || (filePath.getPath().contains(".mp4"))) {
                        fileList.add(filePath.getPath());
                    }
                }
            }
        }
        try {
            populateRecyclerView();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     *  Requesting user to grant write external storage permission.
     */
    private void CheckRequestPermissions() {
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED) {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(PhotoEditorGallery.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    /**
     * For every file in the current directory of our application, get a image or video to display in our RecyclerView.
     */
    public void populateRecyclerView() throws Exception {
        final Handler handler = new Handler();
        checkWarningForFileNotExist();

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (file.exists()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            RecyclerView recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new GridLayoutManager(PhotoEditorGallery.this, 3));
                           /* recyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,
                                    StaggeredGridLayoutManager.VERTICAL));*/
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                                    DividerItemDecoration.VERTICAL));
                            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                                    DividerItemDecoration.HORIZONTAL));

                            if (fileList != null) {
                                ImageListAdapter adapter = new ImageListAdapter(fileList);
                                recyclerView.setAdapter(adapter);
                                adapter.setOnItemClickListener(new ImageListAdapter.OnItemClickListener() {
                                    @Override
                                    public void onClick(int position) {
                                        ShowDialogBox(fileList.get(position));
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
        if (!file.exists() || fileList.isEmpty()) {
            dialog = new Dialog(PhotoEditorGallery.this);
            dialog.setContentView(R.layout.custom_warning_dialog);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.getWindow().setWindowAnimations(R.style.AnimationsForDialog);
            btnOk = dialog.findViewById(R.id.btnOK);
            txtWarningMessage = dialog.findViewById(R.id.txtWarningMessage);
            txtWarningMessage.setText("To view the gallery first capture photos from the camera or select photos from the system gallery using the Photo Editor button!");
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
        if (mediaPath.contains(".mp4")) {
            Intent intent = new Intent(PhotoEditorGallery.this, ShowVideo.class);
            intent.putExtra("PATH", mediaPath);
            startActivity(intent);
        }
        else {
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

                    Intent intent = new Intent(PhotoEditorGallery.this, FullView.class);
                    intent.putExtra("img_id", mediaPath);
                    startActivity(intent);
                }
            });

            dialog.show();
        }
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
