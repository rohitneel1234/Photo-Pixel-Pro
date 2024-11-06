package com.rohitneel.photopixelpro.photocollage.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoBaseActivity;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoEditorActivity;
import com.rohitneel.photopixelpro.photocollage.adapters.ArtAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.ArtColorAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.DripColorAdapter;
import com.rohitneel.photopixelpro.photocollage.crop.MLCropAsyncTask;
import com.rohitneel.photopixelpro.photocollage.crop.MLOnCropTaskCompleted;
import com.rohitneel.photopixelpro.photocollage.drip.imagescale.TouchListener;
import com.rohitneel.photopixelpro.photocollage.eraser.StickerEraseActivity;
import com.rohitneel.photopixelpro.photocollage.listener.LayoutItemListener;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoDripView;
import com.rohitneel.photopixelpro.photocollage.support.Constants;
import com.rohitneel.photopixelpro.photocollage.support.MyExceptionHandlerPix;
import com.rohitneel.photopixelpro.photocollage.utils.BitmapTransfer;
import com.rohitneel.photopixelpro.photocollage.utils.DripUtils;
import com.rohitneel.photopixelpro.photocollage.utils.ImageUtils;
import com.rohitneel.photopixelpro.photocollage.widget.DripFrameLayout;

import java.util.ArrayList;

public class ArtLayout extends PhotoBaseActivity implements LayoutItemListener,
        DripColorAdapter.ColorListener, ArtColorAdapter.ArtColorListener{
    public static Bitmap resultBitmap;
    private static Bitmap faceBitmap;
    private Bitmap selectedBitmap;
    private Bitmap cutBitmap;
    private Bitmap mainBitmap = null;
    private Bitmap OverLayFront = null;
    private Bitmap OverLayBack = null;
    private Bitmap bitmapColor = null;
    private Bitmap bitmapColorBack = null;
    private boolean isFirst = true;
    private boolean isReady = false;
    public int count = 0;
    private PhotoDripView dripViewFront;
    private PhotoDripView dripViewBack;
    private PhotoDripView dripViewImage;
    private DripFrameLayout frameLayoutBackground;
    private PhotoDripView dripViewBackground;
    private RecyclerView recyclerViewStyle;
    private RecyclerView recyclerViewBack;
    private RecyclerView recyclerViewFront;
    private LinearLayout linearLayoutStyle;
    private LinearLayout linearLayoutBg;
    private SeekBar seekBarZoom;
    private ArtAdapter artAdapter;
    private ArrayList<String> artEffectList = new ArrayList<String>();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_art);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(ArtLayout.this));

        this.dripViewFront = findViewById(R.id.dripViewFront);
        this.dripViewBack = findViewById(R.id.dripViewBack);
        this.dripViewImage = findViewById(R.id.dripViewImage);
        this.dripViewBackground = findViewById(R.id.dripViewBackground);
        this.frameLayoutBackground = findViewById(R.id.frameLayoutBackground);
        this.seekBarZoom = findViewById(R.id.seekbarZoom);

        this.linearLayoutStyle = findViewById(R.id.linearLayoutStyle);
        this.linearLayoutStyle.setVisibility(View.VISIBLE);
        this.linearLayoutBg = findViewById(R.id.linearLayoutBg);
        this.linearLayoutBg.setVisibility(View.GONE);
        this.dripViewImage.setOnTouchListenerCustom(new TouchListener());

        this.seekBarZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Float scale = ((progress/ 50.0f) + 1);
                dripViewBack.setScaleX(scale);
                dripViewFront.setScaleX(scale);
                dripViewBack.setScaleY(scale);
                dripViewFront.setScaleY(scale);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dripViewImage.post(new Runnable() {
                    public void run() {
                        if (isFirst) {
                            isFirst = false;
                            initBitmap();
                        }
                    }
                });
            }
        },  1000);

        findViewById(R.id.imageViewCloseDrip).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.imageViewSaveDrip).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new saveFile().execute();
            }
        });

        findViewById(R.id.image_view_eraser).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                StickerEraseActivity.b = cutBitmap;
                Intent intent = new Intent(ArtLayout.this, StickerEraseActivity.class);
                intent.putExtra(Constants.KEY_OPEN_FROM, Constants.VALUE_OPEN_FROM_ART);
                startActivityForResult(intent, 1024);
            }
        });

        findViewById(R.id.relativeLayoutStyle).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                linearLayoutStyle.setVisibility(View.VISIBLE);
                linearLayoutBg.setVisibility(View.GONE);
            }
        });

        findViewById(R.id.relativeLayoutBackground).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                linearLayoutStyle.setVisibility(View.GONE);
                linearLayoutBg.setVisibility(View.VISIBLE);
            }
        });

        for (int i = 1; i <= 8; i++) {
            artEffectList.add("art_" + i);
        }

        this.recyclerViewFront = findViewById(R.id.recyclerViewFront);
        this.recyclerViewFront.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewFront.setAdapter(new DripColorAdapter(this, this));
        this.recyclerViewFront.setVisibility(View.VISIBLE);

        this.recyclerViewBack = findViewById(R.id.recyclerViewBack);
        this.recyclerViewBack.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewBack.setAdapter(new ArtColorAdapter(this, this));
        this.recyclerViewBack.setVisibility(View.VISIBLE);


        recyclerViewStyle = findViewById(R.id.recyclerViewStyle);
        recyclerViewStyle.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        setDripList();

        dripViewImage.post(new Runnable() {
            public void run() {
                initBitmap();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1024) {
            if (resultBitmap != null) {
                cutBitmap = resultBitmap;
                dripViewImage.setImageBitmap(cutBitmap);
                isReady = true;
                Bitmap bitmapFromAssetFront = DripUtils.getBitmapFromAsset(ArtLayout.this, "art/front/" + artAdapter.getItemList().get(artAdapter.selectedPos) + "_front.webp");
                Bitmap bitmapFromAssetBack = DripUtils.getBitmapFromAsset(ArtLayout.this, "art/back/" + artAdapter.getItemList().get(artAdapter.selectedPos) + "_back.webp");
                if (!"none".equals(artAdapter.getItemList().get(0))) {
                    OverLayFront = bitmapFromAssetFront;
                    OverLayBack = bitmapFromAssetBack;
                }
            }
        }
    }

    public static void setFaceBitmap(Bitmap bitmap) {
        faceBitmap = bitmap;
    }

    private void initBitmap() {
        if (faceBitmap != null) {
            selectedBitmap = ImageUtils.getBitmapResize(ArtLayout.this, faceBitmap, 1024, 1024);
            mainBitmap = Bitmap.createScaledBitmap(DripUtils.getBitmapFromAsset(ArtLayout.this, "art/white.webp"), selectedBitmap.getWidth(), selectedBitmap.getHeight(), true);
            Glide.with(this).load(Integer.valueOf(R.drawable.art_1_front)).into(dripViewFront);
            Glide.with(this).load(Integer.valueOf(R.drawable.art_1_back)).into(dripViewBack);
            setStartDrip();
        }
    }

    public void setStartDrip() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.crop_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        final ProgressBar progressBar2 = progressBar;
        new CountDownTimer(5000, 1000) {
            public void onFinish() {
            }

            public void onTick(long j) {
                int unused = count = count + 1;
                if (progressBar2.getProgress() <= 90) {
                    progressBar2.setProgress(count * 5);
                }
            }
        }.start();

        new MLCropAsyncTask(new MLOnCropTaskCompleted() {
            public void onTaskCompleted(Bitmap bitmap, Bitmap bitmap2, int left, int top) {
                int[] iArr = {0, 0, selectedBitmap.getWidth(), selectedBitmap.getHeight()};
                int width = selectedBitmap.getWidth();
                int height = selectedBitmap.getHeight();
                int i = width * height;
                selectedBitmap.getPixels(new int[i], 0, width, 0, 0, width, height);
                int[] iArr2 = new int[i];
                Bitmap createBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
                createBitmap.setPixels(iArr2, 0, width, 0, 0, width, height);
                cutBitmap = ImageUtils.getMask(ArtLayout.this, selectedBitmap, createBitmap, width, height);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                        bitmap, cutBitmap.getWidth(), cutBitmap.getHeight(), false);
                cutBitmap = resizedBitmap;

                runOnUiThread(new Runnable() {
                    public void run() {
                        Palette p = Palette.from(cutBitmap).generate();
                        if (p.getDominantSwatch() == null) {
                            Toast.makeText(ArtLayout.this, getString(R.string.txt_not_detect_human), Toast.LENGTH_SHORT).show();
                        }
                        dripViewImage.setImageBitmap(cutBitmap);
                        isReady = true;
                        Bitmap bitmapFromAssetFront = DripUtils.getBitmapFromAsset(ArtLayout.this, "art/front/" + artAdapter.getItemList().get(0) + "_front.webp");
                        Bitmap bitmapFromAssetBack = DripUtils.getBitmapFromAsset(ArtLayout.this, "art/back/" + artAdapter.getItemList().get(0) + "_back.webp");
                        if (!"none".equals(artAdapter.getItemList().get(0))) {
                            OverLayFront = bitmapFromAssetFront;
                            OverLayBack = bitmapFromAssetBack;
                        }

                    }
                });
            }
        }, this, progressBar).execute(new Void[0]);

    }

    public void onLayoutListClick(View view, int i) {
        Bitmap bitmapFromAssetFront = DripUtils.getBitmapFromAsset(ArtLayout.this, "art/front/" + artAdapter.getItemList().get(i) + "_front.webp");
        Bitmap bitmapFromAssetBack = DripUtils.getBitmapFromAsset(ArtLayout.this, "art/back/" + artAdapter.getItemList().get(i) + "_back.webp");
        if (!"none".equals(artAdapter.getItemList().get(i))) {
            OverLayFront = bitmapFromAssetFront;
            OverLayBack = bitmapFromAssetBack;
            dripViewFront.setImageBitmap(OverLayFront);
            dripViewBack.setImageBitmap(OverLayBack);
            return;
        }
        OverLayFront = null;
        OverLayBack = null;
    }

    public void setDripList() {
        artAdapter = new ArtAdapter(ArtLayout.this);
        artAdapter.setClickListener(this);
        recyclerViewStyle.setAdapter(artAdapter);
        artAdapter.addData(artEffectList);
    }

    @Override
    public void onColorSelected(DripColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            dripViewFront.setColorFilter(squareView.drawableId);
            dripViewBack.setColorFilter(squareView.drawableId);
        }  else {
            dripViewFront.setColorFilter(squareView.drawableId);
            dripViewBack.setColorFilter(squareView.drawableId);
        }
    }

    @Override
    public void onArtColorSelected(ArtColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            dripViewBackground.setBackgroundColor(squareView.drawableId);
            dripViewBackground.setImageBitmap(bitmapColorBack);
        }  else {
            dripViewBackground.setBackgroundColor(squareView.drawableId);
            dripViewBackground.setImageBitmap(bitmapColorBack);
        }
    }

    private class saveFile extends android.os.AsyncTask<String, Bitmap, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public Bitmap getBitmapFromView(View view) {
            Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            frameLayoutBackground.setDrawingCacheEnabled(true);
            try {
                return getBitmapFromView(frameLayoutBackground);
            } catch (Exception e) {
//            Exception e = new UnsupportedOperationException();
                return null;
            } finally {
                frameLayoutBackground.setDrawingCacheEnabled(false);
            }

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null){
                BitmapTransfer.bitmap = bitmap;
            }
            Intent intent = new Intent(ArtLayout.this, PhotoEditorActivity.class);
            intent.putExtra("MESSAGE","done");
            setResult(RESULT_OK,intent);
            finish();
        }
    }

}
