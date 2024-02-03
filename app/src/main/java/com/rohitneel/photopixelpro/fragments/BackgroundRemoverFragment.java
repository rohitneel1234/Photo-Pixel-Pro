package com.rohitneel.photopixelpro.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.backgroundremover.BackgroundEraserTutorial;
import com.rohitneel.photopixelpro.gallery.BackgroundEraserGallery;
import com.rohitneel.photopixelpro.backgroundremover.EraserActivity;
import com.rohitneel.photopixelpro.helper.SessionManager;

import java.io.ByteArrayOutputStream;
import java.io.File;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

public class BackgroundRemoverFragment extends Fragment {

    private static final int REQUEST_MULTIPLE_PERMISSION_CODE = 4;
    private LinearLayout cardBackgroundEraser, cardBackgroundEraserGallery, linear_layout_tutorial;
    private static final int LOAD_IMAGE = 5;
    private static final int LOAD_CAMERA = 123;
    private SessionManager sessionManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_background_remover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cardBackgroundEraser = view.findViewById(R.id.linear_layout_eraser_button);
        cardBackgroundEraserGallery =  view.findViewById(R.id.linear_layout_eraser_gallery);
        linear_layout_tutorial = view.findViewById(R.id.linear_layout_tutorial);
        sessionManager = new SessionManager(getContext());
        addListener();
    }

    private void addListener() {
        if (!CheckingPermissionIsEnabledOrNot()) {
            //Calling method to enable permission.
            RequestMultiplePermission();
        }
        cardBackgroundEraser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayChoiceDialog();
            }
        });
        cardBackgroundEraserGallery.setOnClickListener(v -> startActivity(new Intent(getContext(), BackgroundEraserGallery.class)));

        linear_layout_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), BackgroundEraserTutorial.class));
            }
        });
    }


    /**
     * Requesting multiple permission
     */
    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(getActivity(), new String[]
                {
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE
                }, REQUEST_MULTIPLE_PERMISSION_CODE);
    }


    /**
     * Checking Camera and Write External Storage Permission if granted or not.
     * @return
     */
    private boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    private void displayChoiceDialog() {
        AlertDialog chooseDialog;
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_select_photo,null);
        dialog.setView(dialogView);
        if (sessionManager.loadState()) {
            dialogView.setBackgroundColor(Color.WHITE);
        }
        ImageView imgCameraButton = dialogView.findViewById(R.id.imgCamera);
        ImageView imgGalleryButton = dialogView.findViewById(R.id.imgGallery);
        chooseDialog = dialog.create();
        chooseDialog.show();
        imgCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, LOAD_CAMERA);
                chooseDialog.dismiss();
            }
        });

        imgGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, LOAD_IMAGE);
                chooseDialog.dismiss();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Here we need to check if the activity that was triggers was the Image Gallery.
        // If it is the requestCode will match the LOAD_IMAGE_RESULTS value.
        // If the resultCode is RESULT_OK and there is some data we know that an image was picked.

        if (requestCode == LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Uri pickedImage = data.getData();

            //CropImage(pickedImage);
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContext().getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            Intent intent = new Intent(getContext(), EraserActivity.class);
            intent.putExtra("imagePath",imagePath);
            startActivity(intent);
        }

        else if (requestCode == LOAD_CAMERA && resultCode == RESULT_OK && data != null) {
            // Let's read picked image data - its URI
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            Uri capturedImage = getImageUri(getContext(), imageBitmap);
            Intent intent = new Intent(getContext(), EraserActivity.class);
            intent.putExtra("imagePath",getRealPathFromURI(capturedImage));
            startActivity(intent);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_MULTIPLE_PERMISSION_CODE) {
            if (grantResults.length > 0) {
                boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean WriteStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                if (CameraPermission && WriteStoragePermission) {
                    Toast.makeText(getContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
