package com.rohitneel.photopixelpro.photoframe.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.graphics.Color;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photoframe.adapters.AdapterGalleryFileList;
import com.rohitneel.photopixelpro.photoframe.model.Model_images;
import com.rohitneel.photopixelpro.photoframe.utils.TakePermission;

import java.util.ArrayList;

public class ActivityGalleryFile extends AppCompatActivity {
    public static ArrayList<Model_images> al_images = new ArrayList<>();
    boolean boolean_folder;
    AdapterGalleryFileList obj_adapter;
    GridView gv_folder;
    private static final int REQUEST_PERMISSIONS = 100;
    ImageView ivbtnclose;
    Context context;
    Uri photoURI;
    TakePermission takePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this, SystemBarStyle.dark(Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_file);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);

            View statusBarSpacer = findViewById(R.id.statusBarSpacer);
            if (statusBarSpacer != null) {
                statusBarSpacer.getLayoutParams().height = systemBars.top;
                statusBarSpacer.requestLayout();
            }

            return insets;
        });

        context = ActivityGalleryFile.this;
        ivbtnclose = findViewById(R.id.ivbtnclose);
        takePermission = new TakePermission(ActivityGalleryFile.this);

        ivbtnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = new Intent(ActivityGalleryFile.this, ActivityCreatePhoto.class);
        intent.putExtra("imageUri", photoURI);


        gv_folder = (GridView) findViewById(R.id.gv_folder);

        gv_folder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ActivityGalleryImages.class);
                intent.putExtra("value", i);
                startActivity(intent);
            }
        });

        if (takePermission.TakePermissionAS()) {
            imagesPath();
        }
    }

    public void imagesPath() {
        al_images.clear();
        int int_position = 0;
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;

        String absolutePathOfImage = null;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
        cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null, orderBy + " DESC");

        if (cursor != null) {
            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                for (int i = 0; i < al_images.size(); i++) {
                    if (al_images.get(i).getStr_folder() != null && cursor.getString(column_index_folder_name) != null) {
                        if (al_images.get(i).getStr_folder().equals(cursor.getString(column_index_folder_name))) {
                            boolean_folder = true;
                            int_position = i;
                            break;
                        } else {
                            boolean_folder = false;
                        }
                    }

                }

                ArrayList<String> al_path = new ArrayList<>();
                if (boolean_folder) {
                    al_path.addAll(al_images.get(int_position).getAl_imagepath());
                    al_path.add(absolutePathOfImage);
                    al_images.get(int_position).setAl_imagepath(al_path);
                } else {
                    al_path.add(absolutePathOfImage);
                    Model_images obj_model = new Model_images();
                    obj_model.setStr_folder(cursor.getString(column_index_folder_name));
                    obj_model.setAl_imagepath(al_path);
                    al_images.add(obj_model);
                }
            }
            cursor.close();
        }
        obj_adapter = new AdapterGalleryFileList(getApplicationContext(), al_images);
        gv_folder.setAdapter(obj_adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSIONS) {
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    imagesPath();
                } else {
                    Toast.makeText(ActivityGalleryFile.this, "The app was not allowed to read or write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
