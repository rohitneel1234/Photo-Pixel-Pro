package com.rohitneel.photopixelpro.photocollage.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.exifinterface.media.ExifInterface;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.adapters.AlbumAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.ListAlbumAdapter;
import com.rohitneel.photopixelpro.photocollage.constants.Constants;
import com.rohitneel.photopixelpro.photocollage.interfac.OnAlbum;
import com.rohitneel.photopixelpro.photocollage.interfac.OnListAlbum;
import com.rohitneel.photopixelpro.photocollage.model.ImageModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GridPickerActivity extends AppCompatActivity implements View.OnClickListener, OnAlbum, OnListAlbum {
    public static final String KEY_DATA_RESULT = "KEY_DATA_RESULT";
    public static final String KEY_LIMIT_MAX_IMAGE = "KEY_LIMIT_MAX_IMAGE";
    public static final String KEY_LIMIT_MIN_IMAGE = "KEY_LIMIT_MIN_IMAGE";
    AlbumAdapter albumAdapter;
    ArrayList<ImageModel> dataAlbum = new ArrayList<>();
    ArrayList<ImageModel> dataListPhoto = new ArrayList<>();
    GridView gridViewAlbum;
    GridView gridViewPhotos;
    HorizontalScrollView scrollViewSelected;
    LinearLayout linearLayoutSelected;
    int limitImageMax = 9;
    int limitImageMin = 2;
    ListAlbumAdapter listAlbumAdapter;
    ArrayList<ImageModel> listItemSelect = new ArrayList<>();
    private Handler mHandler;
    ArrayList<String> pathList = new ArrayList<>();
    public int position = 0;
    AlertDialog sortDialog;
    TextView txtTotalImage;

    private class GetItemAlbum extends AsyncTask<Void, Void, String> {

        public void onPreExecute() {
        }


        public void onProgressUpdate(Void... voidArr) {
        }

        private GetItemAlbum() {
        }


        public String doInBackground(Void... voidArr) {
            Cursor query = GridPickerActivity.this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "bucket_display_name"}, (String) null, (String[]) null, (String) null);
            if (query == null) {
                return "";
            }
            int columnIndexOrThrow = query.getColumnIndexOrThrow("_data");
            while (query.moveToNext()) {
                String string = query.getString(columnIndexOrThrow);
                File file = new File(string);
                if (file.exists()) {
                    boolean access$000 = GridPickerActivity.this.checkFile(file);
                    if (!GridPickerActivity.this.Check(file.getParent(), GridPickerActivity.this.pathList) && access$000) {
                        GridPickerActivity.this.pathList.add(file.getParent());
                        GridPickerActivity.this.dataAlbum.add(new ImageModel(file.getParentFile().getName(), string, file.getParent()));
                    }
                }
            }
            Collections.sort(GridPickerActivity.this.dataAlbum);
            query.close();
            return "";
        }


        public void onPostExecute(String str) {
            GridPickerActivity.this.gridViewAlbum.setAdapter(GridPickerActivity.this.albumAdapter);
        }
    }

    private class GetItemListAlbum extends AsyncTask<Void, Void, String> {
        String pathAlbum;


        public void onPreExecute() {
        }


        public void onProgressUpdate(Void... voidArr) {
        }

        GetItemListAlbum(String str) {
            this.pathAlbum = str;
        }


        public String doInBackground(Void... voidArr) {
            File file = new File(this.pathAlbum);
            if (!file.isDirectory()) {
                return "";
            }
            for (File file2 : file.listFiles()) {
                if (file2.exists()) {
                    boolean access$000 = GridPickerActivity.this.checkFile(file2);
                    if (!file2.isDirectory() && access$000) {
                        GridPickerActivity.this.dataListPhoto.add(new ImageModel(file2.getName(), file2.getAbsolutePath(), file2.getAbsolutePath()));
                        publishProgress(new Void[0]);
                    }
                }
            }
            return "";
        }


        public void onPostExecute(String str) {
            try {
                Collections.sort(GridPickerActivity.this.dataListPhoto, new Comparator<ImageModel>() {
                    @Override
                    public int compare(ImageModel imageModel, ImageModel imageModel2) {
                        File file = new File(imageModel.getPathFolder());
                        File file2 = new File(imageModel2.getPathFolder());
                        if (file.lastModified() > file2.lastModified()) {
                            return -1;
                        }
                        return file.lastModified() < file2.lastModified() ? 1 : 0;
                    }
                });
            } catch (Exception e) {
            }
            GridPickerActivity.this.listAlbumAdapter.notifyDataSetChanged();
        }


    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().setStatusBarColor(getColor(R.color.login_sign_up_background));
        } else {
            requestWindowFeature(1);
            getWindow().setFlags(1024, 1024);
        }
        setContentView(R.layout.activity_grid_picker);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.limitImageMax = extras.getInt(KEY_LIMIT_MAX_IMAGE, 9);
            this.limitImageMin = extras.getInt(KEY_LIMIT_MIN_IMAGE, 2);
            if (this.limitImageMin > this.limitImageMax) {
                finish();
            }
            if (this.limitImageMin < 1) {
                finish();
            }
        }
        getSupportActionBar().setTitle(R.string.text_title_activity_album);
        this.gridViewPhotos = findViewById(R.id.gridViewPhotos);
        this.txtTotalImage = findViewById(R.id.txtTotalImage);
        findViewById(R.id.textViewDone).setOnClickListener(this);
        this.linearLayoutSelected = findViewById(R.id.linearLayoutSelected);
        this.scrollViewSelected = findViewById(R.id.scrollViewSelected);
        this.gridViewAlbum = findViewById(R.id.gridViewAlbum);

        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message message) {
                super.handleMessage(message);

            }
        };
        try {
            Collections.sort(this.dataAlbum, new Comparator<ImageModel>() {
                public int compare(ImageModel imageModel, ImageModel imageModel2) {
                    return imageModel.getName().compareToIgnoreCase(imageModel2.getName());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.albumAdapter = new AlbumAdapter(this, R.layout.item_album, this.dataAlbum);
        this.albumAdapter.setOnItem(this);
        if (isPermissionGranted("android.permission.READ_EXTERNAL_STORAGE")) {
            new GridPickerActivity.GetItemAlbum().execute(new Void[0]);
        } else {
            requestPermission("android.permission.READ_EXTERNAL_STORAGE", 1001);
        }
        updateTxtTotalImage();
    }

    private boolean isPermissionGranted(String str) {
        return ContextCompat.checkSelfPermission(this, str) == 0;
    }

    private void requestPermission(String str, int i) {
        ActivityCompat.shouldShowRequestPermissionRationale(this, str);
        ActivityCompat.requestPermissions(this, new String[]{str}, i);
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (i == 1001) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                finish();
            } else {
                new GetItemAlbum().execute(new Void[0]);
            }
        } else if (i == 1002 && iArr.length > 0) {
            int i2 = iArr[0];
        }
    }


    public boolean Check(String str, ArrayList<String> arrayList) {
        return !arrayList.isEmpty() && arrayList.contains(str);
    }

    public void showDialogSortAlbum() {
        String[] stringArray = getResources().getStringArray(R.array.array_sort_value);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.text_title_dialog_sort_by_album));
        builder.setSingleChoiceItems(stringArray, this.position, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        GridPickerActivity.this.position = i;
                        Collections.sort(GridPickerActivity.this.dataAlbum, new Comparator<ImageModel>() {
                            public int compare(ImageModel imageModel, ImageModel imageModel2) {
                                return imageModel.getName().compareToIgnoreCase(imageModel2.getName());
                            }
                        });
                        GridPickerActivity.this.refreshGridViewAlbum();
                        Log.e("TAG", "showDialogSortAlbum by NAME");
                        break;
                    case 1:
                        GridPickerActivity.this.position = i;
                        GridPickerActivity.this.doinBackground();
                        Log.e("TAG", "showDialogSortAlbum by Size");
                        break;
                    case 2:
                        GridPickerActivity.this.position = i;
                        Collections.sort(GridPickerActivity.this.dataAlbum, new Comparator<ImageModel>() {
                            public int compare(ImageModel imageModel, ImageModel imageModel2) {
                                int i = (GridPickerActivity.getFolderSize(new File(imageModel.getPathFolder())) > GridPickerActivity.getFolderSize(new File(imageModel2.getPathFolder())) ? 1 : (GridPickerActivity.getFolderSize(new File(imageModel.getPathFolder())) == GridPickerActivity.getFolderSize(new File(imageModel2.getPathFolder())) ? 0 : -1));
                                if (i > 0) {
                                    return -1;
                                }
                                return i < 0 ? 1 : 0;
                            }
                        });
                        GridPickerActivity.this.refreshGridViewAlbum();
                        Log.e("TAG", "showDialogSortAlbum by Date");
                        break;
                }
                GridPickerActivity.this.sortDialog.dismiss();
            }
        });
        this.sortDialog = builder.create();
        this.sortDialog.show();
    }

    public void refreshGridViewAlbum() {
        this.albumAdapter = new AlbumAdapter(this, R.layout.item_album, this.dataAlbum);
        this.albumAdapter.setOnItem(this);
        this.gridViewAlbum.setAdapter(this.albumAdapter);
        this.gridViewAlbum.setVisibility(View.GONE);
        this.gridViewAlbum.setVisibility(View.VISIBLE);
    }

    public void showDialogSortListAlbum() {
        String[] stringArray = getResources().getStringArray(R.array.array_sort_value);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.text_title_dialog_sort_by_photo));
        builder.setSingleChoiceItems(stringArray, this.position, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case 0:
                        GridPickerActivity.this.position = i;
                        GridPickerActivity.this.doinBackgroundPhoto(i);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }
        });
        this.sortDialog = builder.create();
        this.sortDialog.show();
    }

    public void refreshGridViewListAlbum() {
        this.listAlbumAdapter = new ListAlbumAdapter(this, R.layout.item_list_album, this.dataListPhoto);
        this.listAlbumAdapter.setOnListAlbum(this);
        this.gridViewPhotos.setAdapter(this.listAlbumAdapter);
        this.gridViewPhotos.setVisibility(View.GONE);
        this.gridViewPhotos.setVisibility(View.VISIBLE);
    }

    public static long getFolderSize(File file) {
        File[] listFiles;
        boolean z;
        if (file == null || !file.exists() || (listFiles = file.listFiles()) == null || listFiles.length <= 0) {
            return 0;
        }
        long j = 0;
        for (File file2 : listFiles) {
            if (file2.isFile()) {
                int i = 0;
                while (true) {
                    if (i >= Constants.FORMAT_IMAGE.size()) {
                        z = false;
                        break;
                    } else if (file2.getName().endsWith(Constants.FORMAT_IMAGE.get(i))) {
                        z = true;
                        break;
                    } else {
                        i++;
                    }
                }
                if (z) {
                    j++;
                }
            }
        }
        return j;
    }


    public void addItemSelect(final ImageModel imageModel) {
        imageModel.setId(this.listItemSelect.size());
        this.listItemSelect.add(imageModel);
        updateTxtTotalImage();
        final View inflate = View.inflate(this, R.layout.item_album_selected, (ViewGroup) null);
        ImageView imageView = (ImageView) inflate.findViewById(R.id.image_view_item);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ((RequestBuilder) Glide.with((Activity) this).load(imageModel.getPathFile()).placeholder((int) R.drawable.image_show)).into(imageView);
        ((ImageView) inflate.findViewById(R.id.image_view_remove)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                GridPickerActivity.this.linearLayoutSelected.removeView(inflate);
                GridPickerActivity.this.listItemSelect.remove(imageModel);
                GridPickerActivity.this.updateTxtTotalImage();
            }
        });
        this.linearLayoutSelected.addView(inflate);
        inflate.startAnimation(AnimationUtils.loadAnimation(this, R.anim.abc_fade_in));
        sendScroll();
    }


    public void updateTxtTotalImage() {
        this.txtTotalImage.setText(String.format(getResources().getString(R.string.text_images), new Object[]{Integer.valueOf(this.listItemSelect.size())}));
    }

    private void sendScroll() {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        GridPickerActivity.this.scrollViewSelected.fullScroll(66);
                    }
                });
            }
        }).start();
    }


    public void showListAlbum(String str) {
        getSupportActionBar().setTitle((CharSequence) new File(str).getName());
        this.listAlbumAdapter = new ListAlbumAdapter(this, R.layout.item_list_album, this.dataListPhoto);
        this.listAlbumAdapter.setOnListAlbum(this);
        this.gridViewPhotos.setAdapter(this.listAlbumAdapter);
        this.gridViewPhotos.setVisibility(View.VISIBLE);
        new GridPickerActivity.GetItemListAlbum(str).execute(new Void[0]);
    }

    public void onClick(View view) {
        if (view.getId() == R.id.textViewDone) {
            ArrayList<String> listString = getListString(this.listItemSelect);
            if (listString.size() >= this.limitImageMin) {
                done(listString);
                return;
            }
            Toast.makeText(this, "Please select at lease " + this.limitImageMin + " images", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_pick_image, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.btnSort) {
            if (this.gridViewPhotos.getVisibility() == View.GONE) {
                Log.d("tag", "1");
                showDialogSortAlbum();
            } else {
                showDialogSortListAlbum();
                Log.d("tag", ExifInterface.GPS_MEASUREMENT_2D);
            }
        } else if (menuItem.getItemId() == 16908332) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    private void done(ArrayList<String> arrayList) {
        Intent intent = new Intent(this, PhotoCollageActivity.class);
        intent.putStringArrayListExtra(KEY_DATA_RESULT, arrayList);
        startActivity(intent);
    }


    public ArrayList<String> getListString(ArrayList<ImageModel> arrayList) {
        ArrayList<String> arrayList2 = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(arrayList.get(i).getPathFile());
        }
        return arrayList2;
    }


    public boolean checkFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.isFile()) {
            return true;
        }
        String name = file.getName();
        if (name.startsWith(".") || file.length() == 0) {
            return false;
        }
        for (int i = 0; i < Constants.FORMAT_IMAGE.size(); i++) {
            if (name.endsWith(Constants.FORMAT_IMAGE.get(i))) {
                return true;
            }
        }
        return false;
    }

    public void onBackPressed() {
        if (this.gridViewPhotos.getVisibility() == View.VISIBLE) {
            this.dataListPhoto.clear();
            this.listAlbumAdapter.notifyDataSetChanged();
            this.gridViewPhotos.setVisibility(View.GONE);
            getSupportActionBar().setTitle((CharSequence) getResources().getString(R.string.text_title_activity_album));
            return;
        }
        super.onBackPressed();
    }

    public static DisplayMetrics getDisplayInfo(Activity activity) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }


    @SuppressLint("StaticFieldLeak")
    public void doinBackgroundPhoto(final int i) {
        new AsyncTask<String, String, Void>() {

            public void onPreExecute() {
                super.onPreExecute();
            }


            public Void doInBackground(String... strArr) {
                if (i == 0) {
                    try {
                        Collections.sort(GridPickerActivity.this.dataListPhoto, new Comparator<ImageModel>() {
                            public int compare(ImageModel imageModel, ImageModel imageModel2) {
                                return imageModel.getName().compareToIgnoreCase(imageModel2.getName());
                            }
                        });
                        return null;
                    } catch (Exception e) {
                        return null;
                    }
                } else if (i == 1) {
                    Collections.sort(GridPickerActivity.this.dataListPhoto, new Comparator<ImageModel>() {
                        public int compare(ImageModel imageModel, ImageModel imageModel2) {
                            int i = (GridPickerActivity.getFolderSize(new File(imageModel.getPathFolder())) > GridPickerActivity.getFolderSize(new File(imageModel2.getPathFolder())) ? 1 : (GridPickerActivity.getFolderSize(new File(imageModel.getPathFolder())) == GridPickerActivity.getFolderSize(new File(imageModel2.getPathFolder())) ? 0 : -1));
                            if (i > 0) {
                                return -1;
                            }
                            return i < 0 ? 1 : 0;
                        }
                    });
                    return null;
                } else if (i != 2) {
                    return null;
                } else {
                    Collections.sort(GridPickerActivity.this.dataListPhoto, new Comparator<ImageModel>() {
                        public int compare(ImageModel imageModel, ImageModel imageModel2) {
                            File file = new File(imageModel.getPathFolder());
                            File file2 = new File(imageModel2.getPathFolder());
                            if (file.lastModified() > file2.lastModified()) {
                                return -1;
                            }
                            return file.lastModified() < file2.lastModified() ? 1 : 0;
                        }
                    });
                    return null;
                }
            }


            @SuppressLint("StaticFieldLeak")
            public void onPostExecute(Void voidR) {
                super.onPostExecute(voidR);
                GridPickerActivity.this.refreshGridViewListAlbum();
            }
        }.execute(new String[0]);
    }


    public void doinBackground() {
        new AsyncTask<String, String, Void>() {

            public void onPreExecute() {
                super.onPreExecute();
            }


            public Void doInBackground(String... strArr) {
                Collections.sort(GridPickerActivity.this.dataAlbum, new Comparator<ImageModel>() {
                    public int compare(ImageModel imageModel, ImageModel imageModel2) {
                        File file = new File(imageModel.getPathFolder());
                        File file2 = new File(imageModel2.getPathFolder());
                        if (file.lastModified() > file2.lastModified()) {
                            return -1;
                        }
                        return file.lastModified() < file2.lastModified() ? 1 : 0;
                    }
                });
                return null;
            }


            public void onPostExecute(Void voidR) {
                super.onPostExecute(voidR);
                GridPickerActivity.this.refreshGridViewAlbum();
            }
        }.execute(new String[0]);
    }

    public void OnItemAlbumClick(int i) {
        showListAlbum(this.dataAlbum.get(i).getPathFolder());
    }

    public void OnItemListAlbumClick(ImageModel imageModel) {
        if (this.listItemSelect.size() < this.limitImageMax) {
            addItemSelect(imageModel);
            return;
        }
        Toast.makeText(this, "Limit " + this.limitImageMax + " images", Toast.LENGTH_SHORT).show();
    }
}
