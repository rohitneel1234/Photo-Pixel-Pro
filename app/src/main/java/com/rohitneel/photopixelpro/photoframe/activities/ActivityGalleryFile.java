package com.rohitneel.photopixelpro.photoframe.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photoframe.adapters.AdapterGalleryFileList;
import com.rohitneel.photopixelpro.photoframe.model.Model_images;
import com.rohitneel.photopixelpro.photoframe.utils.TakePermission;

import java.util.ArrayList;

public class ActivityGalleryFile extends Activity {
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
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(getColor(R.color.login_sign_up_background));
        } else {
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
        }
        setContentView(R.layout.activity_gallery_file);
        context = ActivityGalleryFile.this;
        ivbtnclose = findViewById(R.id.ivbtnclose);
        takePermission = new TakePermission(ActivityGalleryFile.this);

        ivbtnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                // finish();
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
