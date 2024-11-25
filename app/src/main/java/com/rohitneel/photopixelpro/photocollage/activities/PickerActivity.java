package com.rohitneel.photopixelpro.photocollage.activities;


import static com.rohitneel.photopixelpro.constant.SelectorSettings.getLastPathSegment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.constant.SelectorSettings;
import com.rohitneel.photopixelpro.photocollage.adapters.FolderAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.ImageAdapter;
import com.rohitneel.photopixelpro.photocollage.listener.OnFolderListener;
import com.rohitneel.photopixelpro.photocollage.listener.OnImageListener;
import com.rohitneel.photopixelpro.photocollage.model.FolderItem;
import com.rohitneel.photopixelpro.photocollage.model.FolderListContent;
import com.rohitneel.photopixelpro.photocollage.model.ImageItem;
import com.rohitneel.photopixelpro.photocollage.model.ImageListContent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;


public class PickerActivity extends PhotoBaseActivity implements OnImageListener, OnFolderListener {
    private static final String TAG = "ImageSelector";
    private Animation aSlideDown;
    private Animation aSlideUp;
    public ContentResolver contentResolver;
    private String currentFolderPath;
    private TextView mButtonConfirm;
    private TextView mFolderSelectButton;
    private File mTempImageFile;
    public final String[] projections = {"_data", "_display_name", "date_added", "mime_type", "_size", "_id"};
    public RecyclerView recyclerView;
    RelativeLayout rlFolder;
    OnBackPressedCallback onBack;

    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.getNavigationIcon().setTint(ContextCompat.getColor(this, R.color.white));
        }
        Intent o = getIntent();
        SelectorSettings.mMaxImageNumber = o.getIntExtra(SelectorSettings.SELECTOR_MAX_IMAGE_NUMBER, SelectorSettings.mMaxImageNumber);
        SelectorSettings.mMinImageSize = o.getIntExtra(SelectorSettings.SELECTOR_MIN_IMAGE_SIZE, SelectorSettings.mMinImageSize);
        aSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        aSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        ImageListContent.SELECTED_IMAGES.clear();
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        rlFolder =  findViewById(R.id.rlFolder);
        mButtonConfirm =  findViewById(R.id.tvDone);
        mButtonConfirm.setOnClickListener(v -> {
            Intent data = new Intent();
            data.putStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS, ImageListContent.SELECTED_IMAGES);
            setResult(-1, data);
            finish();
        });
        RecyclerView rvFolder = findViewById(R.id.rvFolder);
        rvFolder.setLayoutManager(new LinearLayoutManager(this));
        rvFolder.addItemDecoration(new DividerItemDecoration(this, 1));
        rvFolder.setAdapter(new FolderAdapter(this, FolderListContent.FOLDERS, this));
        recyclerView = findViewById(R.id.image_recycerview);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(new ImageAdapter(this, ImageListContent.IMAGES, this));
        mFolderSelectButton = findViewById(R.id.selector_image_folder_button);
        mFolderSelectButton.setText(R.string.selector_folder_all);
        this.mFolderSelectButton.setOnClickListener(view -> {
            if (rlFolder.getVisibility() == View.VISIBLE){
                rlFolder.setVisibility(View.VISIBLE);
            } else {
                slideUp(rlFolder);
            }
        });
        this.currentFolderPath = "";
        FolderListContent.clear();
        ImageListContent.clear();
        updateDoneButton();
        LoadFolderAndImages();

        onBack = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (rlFolder.getVisibility() == View.VISIBLE){
                    slideDown(rlFolder);
                } else {
                    setResult(0);
                    finish();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, onBack);
    }

    public void LoadFolderAndImages() {
        ImageListContent.clear();
        FolderListContent.clear();

        Observable.create((ObservableOnSubscribe<List<ImageItem>>) emitter -> {
                    List<ImageItem> results = new ArrayList<>();

                    Uri contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    String where = MediaStore.Images.Media.SIZE + " > " + SelectorSettings.mMinImageSize;
                    String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

                    contentResolver = getContentResolver();
                    Cursor cursor = contentResolver.query(contentUri, projections, where, null, sortOrder);
                    if (cursor == null) {
                        Log.d(TAG, "call: Empty images");
                    } else if (cursor.moveToFirst()) {
                        FolderItem allImagesFolderItem = null;
                        int pathCol = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                        int nameCol = cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME);
                        int DateCol = cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED);

                        do {
                            String path = cursor.getString(pathCol);
                            String name = cursor.getString(nameCol);
                            long dateTime = cursor.getLong(DateCol);

                            ImageItem item = new ImageItem(name, path, dateTime);

                            if (FolderListContent.FOLDERS.isEmpty()) {
                                FolderListContent.selectedFolderIndex = 0;
                                allImagesFolderItem = new FolderItem(getString(R.string.selector_folder_all), "", path);
                                FolderListContent.addItem(allImagesFolderItem);
                            }

                            results.add(item);

                            if (allImagesFolderItem != null) {
                                allImagesFolderItem.addImageItem(item);
                            }

                            String folderPath = new File(path).getParentFile().getAbsolutePath();
                            FolderItem folderItem = FolderListContent.getItem(folderPath);
                            if (folderItem == null) {
                                folderItem = new FolderItem(getLastPathSegment(folderPath), folderPath, path);
                                FolderListContent.addItem(folderItem);
                            }
                            folderItem.addImageItem(item);
                        } while (cursor.moveToNext());
                        cursor.close();
                    }

                    emitter.onNext(results);
                    emitter.onComplete();
                })
                .flatMapIterable((Function<List<ImageItem>, Iterable<ImageItem>>) items -> items) // Flatten the list to individual items
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ImageItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // You can store this disposable to clear it if needed
                    }

                    @Override
                    public void onNext(ImageItem imageItem) {
                        ImageListContent.addItem(imageItem);
                        recyclerView.getAdapter().notifyItemChanged(ImageListContent.IMAGES.size() - 1);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + Log.getStackTraceString(e));
                    }

                    @Override
                    public void onComplete() {
                        // Called when the stream is completed
                    }
                });
    }


    public void updateDoneButton() {
        if (ImageListContent.SELECTED_IMAGES.isEmpty() && ImageListContent.SELECTED_IMAGES.size() == 1) {
            this.mButtonConfirm.setVisibility(View.VISIBLE);
        } else {
            this.mButtonConfirm.setVisibility(View.VISIBLE);
        }
        this.mButtonConfirm.setText(getResources().getString(R.string.selector_action_done, ImageListContent.SELECTED_IMAGES.size(), SelectorSettings.mMaxImageNumber));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void OnFolderChange() {
        slideDown(rlFolder);
        FolderItem folder = FolderListContent.getSelectedFolder();
        if (!TextUtils.equals(folder.path, this.currentFolderPath)) {
            currentFolderPath = folder.path;
            mFolderSelectButton.setText(folder.name);
            ImageListContent.IMAGES.clear();
            ImageListContent.IMAGES.addAll(folder.mImages);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    public void slideUp(RelativeLayout showLayout) {
        showLayout.setVisibility(View.VISIBLE);
        showLayout.startAnimation(this.aSlideUp);
    }

    public void slideDown(RelativeLayout hideLayout) {
        hideLayout.setVisibility(View.GONE);
        hideLayout.startAnimation(this.aSlideDown);
    }

    public void onFolderItem(FolderItem item) {
        OnFolderChange();
    }

    public void onImageItem(ImageItem item) {
        if (ImageListContent.bReachMaxNumber) {
            Toast.makeText(this, getResources().getString(R.string.selector_reach_max_image_hint, SelectorSettings.mMaxImageNumber), Toast.LENGTH_SHORT).show();
            ImageListContent.bReachMaxNumber = false;
        }
        updateDoneButton();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            while (true) {
                File file = mTempImageFile;
                if (file != null && file.exists()) {
                    if (mTempImageFile.delete()) {
                        mTempImageFile = null;
                    }
                } else {
                    return;
                }
            }
        } else if (mTempImageFile != null) {
            sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(this.mTempImageFile)));
            Intent resultIntent = new Intent();
            ImageListContent.clear();
            ImageListContent.SELECTED_IMAGES.add(this.mTempImageFile.getAbsolutePath());
            resultIntent.putStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS, ImageListContent.SELECTED_IMAGES);
            setResult(-1, resultIntent);
            finish();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBack.handleOnBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
