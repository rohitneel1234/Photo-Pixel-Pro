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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.activities.MainActivity;
import com.rohitneel.photopixelpro.constant.CommonKeys;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class ActivityCreatedAlbumPreview extends AppCompatActivity {
    ImageView ivcancel, ivPreview;
    LinearLayout ivHome, ivShare, ivDelete;
    Context context;
    String[] permissionsRequired = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 200;
    ProgressDialog dialog;
    public Bitmap bitmapsave, bitmap, bitmapImage;
    public boolean isForShareGlobal;
    int imageUrl, position;

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
        setContentView(R.layout.activity_creation_album_preview);
        ivcancel = findViewById(R.id.ivcancel);
        ivHome = findViewById(R.id.ivHome);
        ivShare = findViewById(R.id.ivShare);
        ivDelete = findViewById(R.id.ivDelete);
        ivPreview = findViewById(R.id.ivPreview);
        context = ActivityCreatedAlbumPreview.this;
        Intent i = getIntent();
        imageUrl = i.getIntExtra("imageUrl", 0);
        position = i.getIntExtra("position", 0);

        Glide.with(context).load(CommonKeys.modelclassDownloadedImages.get(position).getImagepath()).into(ivPreview);

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

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    SaveCustomView(ivPreview, true);
                } else {
                    checkPermission();
                }
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

    private Bitmap downloadImageBitmap(String sUrl) {
        String url = sUrl;
        bitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
                InputStream inputStream = new URL(sUrl).openStream();
                try {
                    bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    inputStream.close();
                    break;
                } catch (OutOfMemoryError outOfMemoryError) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public void checkPermission() {
        if (ActivityCompat.checkSelfPermission(context, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissionsRequired[1])) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("This app needs permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions((Activity) context, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        onBackPressed();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions((Activity) context, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }
        }
    }

    public void SaveCustomView(View view, boolean isForShare) {
        dialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        dialog.setMessage("Please Wait... Image is saving...");

        dialog.setCancelable(false);
        dialog.show();
        bitmapsave = viewToBitmap(view);
        isForShareGlobal = isForShare;

        if (isForShareGlobal) {
            ShareImage();
            dialog.dismiss();
        }
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


    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public void ShareImage() {
        Intent share = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(context, getString(R.string.file_provider), new File(CommonKeys.modelclassDownloadedImages.get(position).getImagepath()));
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share via"));
    }


}
