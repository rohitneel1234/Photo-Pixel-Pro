package com.rohitneel.photopixelpro.photoeditor;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import android.widget.Button;
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
    private Button btnCreateFolder;
    private String selectedItem;
    private String rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    private String currentDirectoryPath;
    private String rootStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
    private String imageFilePath = rootPath + "/Photo Pixel Pro";
    private SessionManager mSession;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_file_path);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left,  systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        listview = findViewById(R.id.list);
        read = findViewById(R.id.read);
        currentPath = findViewById(R.id.txtCurrentPath);
        btnCreateFolder = findViewById(R.id.btnCreateFolder);
        clickToSetImagePath();

        rootPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        currentDirectoryPath = rootPath;
        rootStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        imageFilePath = rootPath + "/" + getString(R.string.app_name);

        currentPath.setText(currentDirectoryPath);
        mSession = new SessionManager(getApplicationContext());
        rootPictureList = new ArrayList<>();
        mediaStoreList = new ArrayList<>();
        updatedRootPictureList = new ArrayList<>();
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if (Build.VERSION.SDK_INT >= 24) {
                if (checkPermission()) {
                    setPictureListAdapter(currentDirectoryPath);
                } else {
                    requestPermission(); // Code for permission
                }
            }

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedItem = (String) parent.getItemAtPosition(position);
                    File selectedFile = new File(currentDirectoryPath, selectedItem);
                    if (selectedFile.isDirectory()) {
                        currentDirectoryPath = selectedFile.getAbsolutePath();
                        setPictureListAdapter(currentDirectoryPath);
                    }
                }
            });

            btnCreateFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCreateFolderDialog();
                }
            });

            imgSetFilePath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String relativePath = "";
                    if (currentDirectoryPath.startsWith(rootPath)) {
                        relativePath = currentDirectoryPath.substring(rootPath.length());
                        if (relativePath.startsWith("/")) {
                            relativePath = relativePath.substring(1);
                        }
                    } else {
                        relativePath = new File(currentDirectoryPath).getName();
                    }
                    
                    if (relativePath.isEmpty()) {
                        relativePath = getString(R.string.app_name);
                    }
                    
                    mSession.saveSavePath(relativePath);
                    Toast.makeText(ShowSavedFilePath.this, "Save folder set to: " + relativePath, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        }

        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File currentDir = new File(currentDirectoryPath);
                File parentDir = currentDir.getParentFile();
                if (parentDir != null && currentDirectoryPath.startsWith(rootPath) && !currentDirectoryPath.equals(rootPath)) {
                    currentDirectoryPath = parentDir.getAbsolutePath();
                    setPictureListAdapter(currentDirectoryPath);
                }
            }
        });
    }

    private void setPictureListAdapter(String path) {
        rootPictureList.clear();
        File dir = new File(path);
        if (dir.exists()) {
            Log.d("path", dir.toString());
            File[] list = dir.listFiles();
            if (list != null) {
                for (File file : list) {
                    if (file.isDirectory()) {
                        rootPictureList.add(file.getName());
                    }
                }
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(ShowSavedFilePath.this, android.R.layout.simple_list_item_1, rootPictureList);
            listview.setAdapter(arrayAdapter);
            currentPath.setText(path);
        }
    }

    private void showCreateFolderDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Create New Folder");

        final android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Folder Name");
        builder.setView(input);

        builder.setPositiveButton("Create", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(android.content.DialogInterface dialog, int which) {
                String folderName = input.getText().toString().trim();
                if (!folderName.isEmpty()) {
                    File newDir = new File(currentDirectoryPath, folderName);
                    if (!newDir.exists()) {
                        if (newDir.mkdirs()) {
                            Toast.makeText(ShowSavedFilePath.this, "Folder created", Toast.LENGTH_SHORT).show();
                            setPictureListAdapter(currentDirectoryPath);
                        } else {
                            Toast.makeText(ShowSavedFilePath.this, "Failed to create folder", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ShowSavedFilePath.this, "Folder already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
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

    private void clickToSetImagePath() {
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
            imgSetFilePath = new ImageView(actionBar.getThemedContext());
            imgSetFilePath.setScaleType(ImageView.ScaleType.CENTER);
            imgSetFilePath.setImageResource(R.drawable.ic_baseline_check_24);
            ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            layoutParams.rightMargin = 40;
            imgSetFilePath.setLayoutParams(layoutParams);
            actionBar.setCustomView(imgSetFilePath);
        }
    }


    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(ShowSavedFilePath.this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(ShowSavedFilePath.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(ShowSavedFilePath.this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, PERMISSION_REQUEST_CODE);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ShowSavedFilePath.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(ShowSavedFilePath.this, "Read External Storage permission allows us to read files." +
                        " Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(ShowSavedFilePath.this, new String[]
                        {android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String @NotNull [] permissions, int @NotNull [] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted, Now you can use local drive .");
                setPictureListAdapter(currentDirectoryPath);
            } else {
                Log.e("value", "Permission Denied, You cannot use local drive .");
            }
        }
    }
}