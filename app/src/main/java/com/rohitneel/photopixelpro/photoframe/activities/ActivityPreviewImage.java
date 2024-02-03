package com.rohitneel.photopixelpro.photoframe.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.activities.MainActivity;
import com.rohitneel.photopixelpro.constant.CommonKeys;
import com.rohitneel.photopixelpro.photocollage.constants.Constants;
import com.rohitneel.photopixelpro.photocollage.dialog.RateDialog;
import com.rohitneel.photopixelpro.photocollage.preference.Preference;
import com.rohitneel.photopixelpro.photoframe.utils.PathUtills;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class ActivityPreviewImage extends AppCompatActivity {
    ImageView ivcancel, ivPreview;
    LinearLayout ivShare;
    ImageView ivHome;
    TextView txtDone;
    Context context;
    String[] permissionsRequired = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private static final int PERMISSION_CALLBACK_CONSTANT = 200;
    ProgressDialog dialog;
    public Bitmap bitmapsave;
    public boolean isForShareGlobal;
    public String imgFileName;
    public String filePath = "";
    File imagesDir;

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
        setContentView(R.layout.activity_preview_image);
        ivcancel = findViewById(R.id.ivcancel);
        ivHome = findViewById(R.id.ivHome);
        txtDone = findViewById(R.id.txtDone);
        ivShare = findViewById(R.id.frame_linear_layout_share_more);
        ivPreview = findViewById(R.id.imgViewPreview);
        context = ActivityPreviewImage.this;
        ivPreview.setImageBitmap(CommonKeys.Image);

        if (!Preference.isRated(getApplicationContext())) {
            new RateDialog(ActivityPreviewImage.this, false).show();
        }

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            boolean isWritable = Utils.isExternalStorageWritable();
            if(isWritable) {
                String relativePath = Environment.DIRECTORY_PICTURES + File.separator + getApplicationContext().getString(R.string.app_name) + File.separator;
                ContentResolver resolver = getApplicationContext().getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath);
                Uri contUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                try {
                    String path = FilePathUtil.getPath(getApplicationContext(), contUri);
                    if (path != null) {
                        mOutputPath = new File(path).getParent();
                    }
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }*/

        ivcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImage(CommonKeys.filePath.getPath());
            }
        });

        txtDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    @SuppressLint("WrongConstant")
    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();
            if (id != R.id.imgViewPreview) {
                switch (id) {
                    case R.id.frame_linear_layout_facebook:
                        shareToFacebook(CommonKeys.filePath.getPath());
                        return;
                    case R.id.frame_linear_layout_instagram:
                        shareToInstagram(CommonKeys.filePath.getPath());
                        return;
                    case R.id.frame_linear_layout_messenger:
                        shareToMessenger(CommonKeys.filePath.getPath());
                        return;
                    default:
                        switch (id) {
                            case R.id.frame_linear_layout_twitter:
                                shareToTwitter(CommonKeys.filePath.getPath());
                                return;
                            case R.id.frame_linear_layout_whatsapp:
                                shareToWhatsapp(CommonKeys.filePath.getPath());
                                return;
                            default:
                                return;
                        }
                }
            } else {
                Intent intent4 = new Intent();
                intent4.setAction("android.intent.action.VIEW");
                intent4.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), getResources().getString(R.string.file_provider), CommonKeys.filePath), "image/*");
                intent4.addFlags(3);
                startActivity(intent4);
            }
        }
    }

    @SuppressLint("WrongConstant")
    public void onClickImage(View view) {
        Intent intent4 = new Intent();
        intent4.setAction("android.intent.action.VIEW");
        intent4.setDataAndType(FileProvider.getUriForFile(getApplicationContext(), getResources().getString(R.string.file_provider), CommonKeys.filePath), "image/*");
        intent4.addFlags(3);
        startActivity(intent4);
    }

    private static void scanFile(Context context, Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);

    }

    public class NetworkAccess extends AsyncTask<Void, Void, Exception> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // call some loader
        }

        @Override
        protected Exception doInBackground(Void... params) {
            // Do background task
            try {
                imgFileName = "PhotoFrame_" + System.currentTimeMillis();
                Uri result = saveImage(context, CommonKeys.Image,getApplicationContext().getString(R.string.app_name)
                        ,imgFileName +".jpg");
                CommonKeys.filePath = new File(PathUtills.getPath(ActivityPreviewImage.this, result));

                scanFile(context, result);

            } catch (FileNotFoundException e) {
                return e;
            } catch (IOException e) {
                return e;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Exception e) {
            super.onPostExecute(e);

            if (!Preference.isRated(getApplicationContext())) {
                new RateDialog(ActivityPreviewImage.this, false).show();
            }

            if (isForShareGlobal) {
                //ShareImage();
            }
        }
    }


    private Uri saveImage(Context context, Bitmap bitmap, @NonNull String folderName, @NonNull String fileName) throws IOException {
        OutputStream fos = null;
        File imageFile = null;
        Uri imageUri = null;
        imagesDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString() + File.separator + folderName);

        if (!imagesDir.exists())
            imagesDir.mkdir();
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileName);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/*");
                contentValues.put(
                        MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + folderName);
                imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                if (imageUri == null)
                    throw new IOException("Failed to create new MediaStore record.");

                fos = resolver.openOutputStream(imageUri);
            } else {
                imagesDir = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).toString() + File.separator + folderName);

                if (!imagesDir.exists())
                    imagesDir.mkdir();

                imageFile = new File(imagesDir, fileName + ".jpg");
                fos = new FileOutputStream(imageFile);
            }


            if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos))
                throw new IOException("Failed to save bitmap.");
            fos.flush();
        } finally {
            if (fos != null)
                fos.close();
        }

        if (imageFile != null) {//pre Q
            MediaScannerConnection.scanFile(context, new String[]{imageFile.toString()}, null, null);
            imageUri = Uri.fromFile(imageFile);
        }
        return imageUri;
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


    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                // set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code


                        File file = CommonKeys.filePath;
                        file.delete();
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(CommonKeys.filePath)));
                        dialog.dismiss();


                        // ivPreview.setImageBitmap(null);
                        onBackPressed();
                        dialog.dismiss();
                    }

                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();

        return myQuittingDialogBox;
    }


    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

   /* public void ShareImage() {
        Intent share = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(context, getString(R.string.file_provider), CommonKeys.filePath);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(share, "Share via"));
    }*/

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
        }
    }

    @SuppressLint("WrongConstant")
    private void shareToFacebook(String str) {
        Intent intent = new Intent(Intent.ACTION_SEND);
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

}
