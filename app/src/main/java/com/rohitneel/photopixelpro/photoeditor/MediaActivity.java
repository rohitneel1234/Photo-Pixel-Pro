package com.rohitneel.photopixelpro.photoeditor;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.helper.SessionManager;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoEditorActivity;
import com.rohitneel.photopixelpro.photocollage.dialog.DetailsDialog;
import com.rohitneel.photopixelpro.photocollage.dialog.RateDialog;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoPickerView;
import com.rohitneel.photopixelpro.photocollage.picker.ImageCaptureManager;
import com.rohitneel.photopixelpro.photocollage.preference.Preference;
import com.rohitneel.photopixelpro.photocollage.utils.AdsUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class MediaActivity extends AppCompatActivity {
    public static final int REQUEST_PERMISSION_CODE = 7;
    private LinearLayout fabCameraUploadBtn;
    private LinearLayout fabGalleryUploadBtn;
    private File dir;
    private AlertDialog alertDialog;
    private SessionManager mSession;
    private ImageCaptureManager imageCaptureManager;
    public  static  Context contextApp;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_media);
        contextApp = this.getApplicationContext();
        this.imageCaptureManager = new ImageCaptureManager(this);

        ActionBar bar = this.getSupportActionBar();
        if (bar != null) {
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0B0B0B")));
        }

        mSession = new SessionManager(getApplicationContext());
        fabCameraUploadBtn = findViewById(R.id.cameraEdit);
        fabGalleryUploadBtn = findViewById(R.id.photoEdit);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + getString(R.string.directory_name);
        dir = new File(path);
        // If All permission is enabled successfully then this block will execute.
        if (!CheckingPermissionIsEnabledOrNot()) {                                                // If permission is not enabled then else condition will execute.
            //Calling method to enable permission.
            RequestMultiplePermission();
        }

        addListeners();
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

        if (AdsUtils.isNetworkAvailabel(getApplicationContext())) {
            if (!Preference.isRated(getApplicationContext()) && Preference.getCounter(getApplicationContext()) % 6 == 0) {
                new RateDialog(this, false).show();
            }
            Preference.increateCounter(getApplicationContext());
        }
    }

    /**
     * Checking Camera and Write External Storage Permission if granted or not.
     * @return
     */
    private boolean CheckingPermissionIsEnabledOrNot() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requesting multiple permission
     */
    private void RequestMultiplePermission() {
        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(MediaActivity.this, new String[]
                {
                        CAMERA,
                        WRITE_EXTERNAL_STORAGE
                }, REQUEST_PERMISSION_CODE);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void addListeners() {
        fabCameraUploadBtn.setOnClickListener(view -> {
            String[] arrPermissions = {"android.permission.CAMERA","android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
            if (Build.VERSION.SDK_INT >= 29)arrPermissions=new String[]{"android.permission.CAMERA"};
            Dexter.withContext(MediaActivity.this).withPermissions(arrPermissions)
                    .withListener(new MultiplePermissionsListener() {
                        public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                            if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                takePhotoFromCamera();
                            }
                            if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                DetailsDialog.showDetailsDialog(MediaActivity.this);
                            }
                        }

                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                            permissionToken.continuePermissionRequest();
                        }
                    }).withErrorListener(dexterError -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();

        });

        fabGalleryUploadBtn.setOnClickListener(view -> {
            goToEditPhoto();
        });

        if (mSession.loadFullScreenState()) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }


    private void goToEditPhoto()
    {
        String[] arrPermissions = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
        if (Build.VERSION.SDK_INT >= 29)arrPermissions=new String[]{"android.permission.READ_EXTERNAL_STORAGE"};

        Dexter.withContext(MediaActivity.this).withPermissions(arrPermissions).withListener(new MultiplePermissionsListener() {
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    PhotoPickerView.builder().setPhotoCount(1).setPreviewEnabled(false).setShowCamera(false).start(MediaActivity.this);
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                    DetailsDialog.showDetailsDialog(MediaActivity.this);
                }
            }

            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).withErrorListener(dexterError -> Toast.makeText(MediaActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
    }

    public void onPostCreate(@Nullable Bundle bundle) {
        super.onPostCreate(bundle);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            super.onActivityResult(i, i2, intent);
        } else if (i == 1) {
            if (this.imageCaptureManager == null) {
                this.imageCaptureManager = new ImageCaptureManager(this);
            }
            new Handler().post(() -> MediaActivity.this.imageCaptureManager.galleryAddPic());
            Intent intent2 = new Intent(getApplicationContext(), PhotoEditorActivity.class);
            intent2.putExtra(PhotoPickerView.KEY_SELECTED_PHOTOS, this.imageCaptureManager.getCurrentPhotoPath());
            startActivity(intent2);
        }
    }

    public void takePhotoFromCamera() {
        try {
            startActivityForResult(this.imageCaptureManager.dispatchTakePictureIntent(), 1);
        } catch (IOException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {

        if (requestCode == REQUEST_PERMISSION_CODE) {

            if (grantResults.length > 0) {

                boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean WriteStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (CameraPermission && WriteStoragePermission ) {
                    Toast.makeText(MediaActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MediaActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                    alertDialog = new AlertDialog.Builder(MediaActivity.this).create();
                    alertDialog.setMessage(getString(R.string.permissions_message));
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                            CheckingPermissionIsEnabledOrNot();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            alertDialog.dismiss();
                            Toast.makeText(MediaActivity.this, getString(R.string.toast_message), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    alertDialog.show();
                }
            }
        }
    }

}
