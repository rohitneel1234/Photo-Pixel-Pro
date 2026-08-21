package com.rohitneel.photopixelpro.photoframe.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.SystemBarStyle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.graphics.Color;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.activities.MainActivity;
import com.rohitneel.photopixelpro.constant.CommonKeys;

import java.io.File;
import android.net.Uri;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class ActivityCreatedAlbumPreview extends AppCompatActivity {
    ImageView ivcancel, ivPreview;
    LinearLayout ivHome, ivShare, ivDelete;
    Context context;
    public Bitmap bitmap, bitmapImage;
    int imageUrl, position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this, SystemBarStyle.dark(Color.TRANSPARENT));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation_album_preview);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, 0);

            View statusBarSpacer = findViewById(R.id.statusBarSpacer);
            if (statusBarSpacer != null) {
                statusBarSpacer.getLayoutParams().height = systemBars.top;
                statusBarSpacer.requestLayout();
            }

            View navigationBarSpacer = findViewById(R.id.navigationBarSpacer);
            if (navigationBarSpacer != null) {
                navigationBarSpacer.getLayoutParams().height = systemBars.bottom;
                navigationBarSpacer.requestLayout();
            }

            return insets;
        });

        ivcancel = findViewById(R.id.ivcancel);
        ivHome = findViewById(R.id.ivHome);
        ivShare = findViewById(R.id.ivShare);
        ivDelete = findViewById(R.id.ivDelete);
        ivPreview = findViewById(R.id.ivPreview);
        context = ActivityCreatedAlbumPreview.this;
        Intent i = getIntent();
        imageUrl = i.getIntExtra("imageUrl", 0);
        position = i.getIntExtra("position", 0);

        Glide.with(context).load(Uri.fromFile(new File(CommonKeys.modelclassDownloadedImages.get(position).getImagepath()))).into(ivPreview);

        ivcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ActivityMyCreations.class);
                context.startActivity(i);
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MainActivity.class);
                finishAffinity();
                context.startActivity(i);
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareImage();
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog diaBox = AskOption();
                diaBox.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private AlertDialog AskOption() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("successfully deleted");

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(CommonKeys.modelclassDownloadedImages.get(position).getImagepath());
                                file.delete();
                                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(CommonKeys.modelclassDownloadedImages.get(position).getImagepath()))));
                                dialog.dismiss();
                                Intent i = new Intent(context, ActivityMyCreations.class);
                                finishAffinity();
                                context.startActivity(i);
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();

        return alertDialog;
    }




    public void ShareImage() {
        File file = new File(CommonKeys.modelclassDownloadedImages.get(position).getImagepath());
        if (!file.exists()) {
            Toast.makeText(context, "Image file not found", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = FileProvider.getUriForFile(context, getString(R.string.file_provider), file);
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(share, "Share via"));
    }

}
