package com.rohitneel.photopixelpro.photocollage.activities;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.activities.MainActivity;
import com.rohitneel.photopixelpro.photocollage.constants.Constants;
import com.rohitneel.photopixelpro.photocollage.dialog.RateDialog;
import com.rohitneel.photopixelpro.photocollage.picker.ImageCaptureManager;
import com.rohitneel.photopixelpro.photocollage.preference.Preference;
import com.rohitneel.photopixelpro.photoeditor.MediaActivity;

import java.io.File;
import java.io.IOException;

public class PhotoShareActivity extends PhotoBaseActivity implements View.OnClickListener {
    private static final String TAG = "PhotoShareActivity";
    private File file;
    ImageCaptureManager createImageFile1;
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(getColor(R.color.login_sign_up_background));
        } else {
            setFullScreen();
        }
        setContentView((int) R.layout.activity_share_photo);
        createImageFile1=new ImageCaptureManager(this);
        String keyFromActivity = getIntent().getExtras().getString("activity");
        String string = getIntent().getExtras().getString("path");
        this.file = new File(string);
        Glide.with(getApplicationContext()).load(this.file).into((ImageView) findViewById(R.id.image_view_preview));
        findViewById(R.id.image_view_preview).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                PhotoShareActivity.this.onClick(view);
            }
        });

        findViewById(R.id.imageViewBack).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.imageViewHome).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public final void onClick(View view) {
                Intent intent;
                if(keyFromActivity.equals("PhotoCollageActivity")) {
                    intent = new Intent(PhotoShareActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(PhotoShareActivity.this, MediaActivity.class);
                }
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
    public void startAcitivity(PhotoShareActivity saveAndShareActivity, View view) {
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
                    case R.id.linearLayoutShareOne:
                        startAcitivity(PhotoShareActivity.this, view);
                        return;
                    case R.id.linear_layout_facebook:
                        shareToFacebook(file.getPath());
                        return;
                    case R.id.linear_layout_instagram:
                        shareToInstagram(file.getPath());
                        return;
                    case R.id.linear_layout_messenger:
                        shareToMessenger(file.getPath());
                        return;
                    case R.id.linear_layout_share_more:
                        shareImage(file.getPath());
                        return;
                    default:
                        switch (id) {
                            case R.id.linear_layout_twitter:
                                shareToTwitter(file.getPath());
                                return;
                            case R.id.linear_layout_whatsapp:
                                shareToWhatsapp(file.getPath());
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
    private void shareToFacebook(String str) {
        Intent intent = new Intent("android.intent.action.SEND");
        String str1 = "com.facebook.katana";
        intent.setPackage(str1);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            intent.putExtra("android.intent.extra.STREAM", FileProvider.getUriForFile(this, sb.toString(), new File(str)));
            intent.setType("image/*");
            intent.addFlags(1);
            startActivity(Intent.createChooser(intent, "Share Photo"));
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    private void shareToInstagram(String str) {
        String str1 = Constants.INSTAGRAM;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uri = FileProvider.getUriForFile(this, sb.toString(), new File(str));
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


    private void shareToWhatsapp(String str) {
        String str1 = Constants.WHATSAPP;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uri = FileProvider.getUriForFile(this, sb.toString(), new File(str));
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", uri);
            intent.setType("image/*");
            intent.setPackage(str1);
            startActivity(intent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @SuppressLint("WrongConstant")
    private void shareToMessenger(String str) {
        String str1 = Constants.MESSEGER;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uri = FileProvider.getUriForFile(this, sb.toString(), new File(str));
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
    private void shareToTwitter(String str) {
        String str1 = Constants.TWITTER;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uri = FileProvider.getUriForFile(this, sb.toString(), new File(str));
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
    public void shareImage(String str){
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(getApplicationContext().getPackageName());
            sb.append(".provider");
            Uri uri = FileProvider.getUriForFile(this, sb.toString(), new File(str));
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
            Log.e(TAG, sb2.toString());
        }
    }

    @SuppressLint("WrongConstant")
    public static boolean isPackageInstalled(Context context, String str) {
        try {
            context.getPackageManager().getPackageInfo(str, 128);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }

    private Uri createCacheFile() {
        File createImageFile = null;
        try {
            createImageFile = createImageFile1.createImageFile();
            return Uri.fromFile(new File(createImageFile.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
