package com.rohitneel.photopixelpro.photoeditor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.helper.SessionManager;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class ShowSavedFilePath extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 100;
    TextView read, currentPath;
    ArrayList<String> rootPictureList, mediaStoreList, updatedRootPictureList;
    ListView listview;
    private ImageView imgSetFilePath;
    private String selectedItem;
    private final String rootPath = "/storage/emulated/0/Pictures";
    private final String rootStoragePath = "/storage/emulated/0";
    private final String imageFilePath = "/storage/emulated/0/Pictures/Photo Pixel Pro";
    private SessionManager mSession;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_file_path);
        listview = findViewById(R.id.list);
        read = findViewById(R.id.read);
        currentPath = findViewById(R.id.txtCurrentPath);
        clickToSetImagePath();
        currentPath.setText(rootPath);
        mSession = new SessionManager(getApplicationContext());
        rootPictureList = new ArrayList<>();
        mediaStoreList = new ArrayList<>();
        updatedRootPictureList = new ArrayList<>();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()) {
                    setPictureListAdapter(rootPictureList);
                } else {
                    requestPermission(); // Code for permission
                }
            }

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedItem = (String) parent.getItemAtPosition(position);
                    if (selectedItem.equals(getString(R.string.app_name))) {
                        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Pictures" + "/" + getString(R.string.app_name) + "/");
                        if (dir.exists()) {
                            Log.d("path", dir.toString());
                            File[] list = dir.listFiles();
                            for (File file : list) {
                                mediaStoreList.add(file.getName());
                            }
                            ArrayAdapter arrayAdapter = new ArrayAdapter(ShowSavedFilePath.this, android.R.layout.simple_list_item_1, mediaStoreList);
                            listview.setAdapter(arrayAdapter);
                        }
                        currentPath.setText(dir.toString());
                    }
                }
            });

            imgSetFilePath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

        }

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = currentPath.getText().toString();
                if (path.equals(imageFilePath)) {
                    if (selectedItem.equals("Photo Pixel Pro")) {
                        listview.setVisibility(View.VISIBLE);
                        setPictureListAdapter(rootPictureList);
                    }
                } else if (path.equals(rootPath)) {
                    currentPath.setText(rootStoragePath);
                    listview.setVisibility(View.INVISIBLE);
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

    private void setPictureListAdapter(ArrayList<String> rootPictureList) {
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "Pictures" + "/");
        if (dir.exists()) {
            Log.d("path", dir.toString());
            File[] list = dir.listFiles();
            for (File file : list) {
                rootPictureList.add(file.getName());
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(ShowSavedFilePath.this, android.R.layout.simple_list_item_1, rootPictureList);
            listview.setAdapter(arrayAdapter);
            currentPath.setText(dir.toString());
        }
    }

    private void clickToSetImagePath() {
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayOptions(actionBar.getDisplayOptions()
                | ActionBar.DISPLAY_SHOW_CUSTOM);
        imgSetFilePath = new ImageView(actionBar.getThemedContext());
        imgSetFilePath.setScaleType(ImageView.ScaleType.CENTER);
        imgSetFilePath.setImageResource(R.drawable.ic_baseline_check_24);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imgSetFilePath.setLayoutParams(layoutParams);
        actionBar.setCustomView(imgSetFilePath);
    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(ShowSavedFilePath.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ShowSavedFilePath.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(ShowSavedFilePath.this, "Write External Storage permission allows us to read  files." +
                    " Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(ShowSavedFilePath.this, new String[]
                    {android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String @NotNull [] permissions, int @NotNull [] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
        }
    }
}