package com.rohitneel.photopixelpro.backgroundremover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.activities.MainActivity;
import com.rohitneel.photopixelpro.constant.CommonKeys;
import com.rohitneel.photopixelpro.photocollage.constants.Constants;
import com.rohitneel.photopixelpro.photocollage.dialog.RateDialog;
import com.rohitneel.photopixelpro.photocollage.picker.ImageCaptureManager;
import com.rohitneel.photopixelpro.photocollage.preference.Preference;

import java.io.File;

public class EraserPhotoShareActivity extends AppCompatActivity {

    private File file;
    ImageCaptureManager createImageFile1;

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
        setContentView(R.layout.activity_eraser_photo_share);
        createImageFile1 = new ImageCaptureManager(this);
        this.file = CommonKeys.filePath;
        Glide.with(getApplicationContext()).load(this.file).into((ImageView) findViewById(R.id.erImgViewPreview));

        findViewById(R.id.ivECancel).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.txtEraserDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        findViewById(R.id.erIvHome).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public final void onClick(View view) {
                Intent intent;
                intent = new Intent(EraserPhotoShareActivity.this, MainActivity.class);
                intent.setFlags(67108864);
                startActivity(intent);
                finish();
            }
        });
        if (!Preference.isRated(this)) {
            new RateDialog(this, false).show();
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }


    @SuppressLint("WrongConstant")
    public void startAcitivity(EraserPhotoShareActivity saveAndShareActivity, View view) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(saveAndShareActivity.getApplicationContext(), getResources().getString(R.string.file_provider), saveAndShareActivity.file));
        intent.addFlags(3);
        saveAndShareActivity.startActivity(Intent.createChooser(intent, "Share"));
    }

    public void onResume() {
        super.onResume();
    }

    @SuppressLint("WrongConstant")
    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();
            if (id != R.id.image_view_preview) {
                switch (id) {
                    case R.id.eraser_linear_layout_facebook:
                        shareToFacebook(Uri.parse(file.getPath()));
                        return;
                    case R.id.eraser_linear_layout_instagram:
                        shareToInstagram(Uri.parse(file.getPath()));
                        return;
                    case R.id.eraser_linear_layout_messenger:
                        shareToMessenger(Uri.parse(file.getPath()));
                        return;
                    case R.id.eraser_linear_layout_share_more:
                        shareImage(Uri.parse(file.getPath()));
                        return;
                    default:
                        switch (id) {
                            case R.id.eraser_linear_layout_twitter:
                                shareToTwitter(Uri.parse(file.getPath()));
                                return;
                            case R.id.eraser_linear_layout_whatsapp:
                                shareToWhatsapp(Uri.parse(file.getPath()));
                                return;
                            default:
                                return;
                        }
                }
            } else {
                Intent intent4 = new Intent();
                intent4.setAction("android.intent.action.VIEW");
                intent4.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), getResources().getString(R.string.file_provider), this.file), "image/*");
                intent4.addFlags(3);
                startActivity(intent4);
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void shareToFacebook(Uri uri) {
        String str1 = Constants.FACEBOOK;
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.setType("image/*");
            intent.setPackage(str1);
            intent.addFlags(1);
            startActivity(Intent.createChooser(intent, "Share Photo"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    private void shareToInstagram(Uri uri) {
        String str1 = Constants.INSTAGRAM;
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.setType("image/*");
            intent.setPackage(str1);
            intent.addFlags(1);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    private void shareToWhatsapp(Uri uri) {
        String str1 = Constants.WHATSAPP;
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.setType("image/*");
            intent.setPackage(str1);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    private void shareToMessenger(Uri uri) {
        String str1 = Constants.MESSEGER;
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.setType("image/*");
            intent.setPackage(str1);
            intent.addFlags(1);
            startActivity(Intent.createChooser(intent, "Share Photo"));
        } catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(), getString(R.string.app_not_installed), Toast.LENGTH_SHORT);
        }
    }

    @SuppressLint("WrongConstant")
    private void shareToTwitter(Uri uri) {
        String str1 = Constants.TWITTER;
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.setType("image/*");
            intent.setPackage(str1);
            intent.addFlags(1);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    public void shareImage(Uri uri){
        try {
            Intent intent = new Intent("android.intent.action.SEND");
            intent.addFlags(1);
            intent.setType("image/*");
            StringBuilder sb1 =  new StringBuilder();
            sb1.append(getString(R.string.message));
            sb1.append("\nhttps://play.google.com/store/apps/details?id=");
            sb1.append(getPackageName());
            intent.putExtra("android.intent.extra.TEXT", sb1.toString());
            intent.putExtra("android.intent.extra.STREAM", uri);
            startActivity(Intent.createChooser(intent, "Share via"));
        } catch (Exception e){
            StringBuilder sb2 = new StringBuilder();
            sb2.append("shareImage: ");
            sb2.append(e);
        }
    }

    @SuppressLint("WrongConstant")
    public void onClickPreview(View view) {
        Intent intent4 = new Intent();
        intent4.setAction("android.intent.action.VIEW");
        intent4.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), getResources().getString(R.string.file_provider), file), "image/*");
        intent4.addFlags(3);
        startActivity(intent4);
    }
}