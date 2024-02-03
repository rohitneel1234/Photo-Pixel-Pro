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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoBaseActivity;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoEditorActivity;
import com.rohitneel.photopixelpro.photocollage.adapters.DripAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.DripBackgroundAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.DripColorAdapter;
import com.rohitneel.photopixelpro.photocollage.assets.BrushColorAsset;
import com.rohitneel.photopixelpro.photocollage.crop.MLCropAsyncTask;
import com.rohitneel.photopixelpro.photocollage.crop.MLOnCropTaskCompleted;
import com.rohitneel.photopixelpro.photocollage.drip.imagescale.TouchListener;
import com.rohitneel.photopixelpro.photocollage.eraser.StickerEraseActivity;
import com.rohitneel.photopixelpro.photocollage.listener.BackgroundItemListener;
import com.rohitneel.photopixelpro.photocollage.listener.LayoutItemListener;
import com.rohitneel.photopixelpro.photocollage.listener.MultiTouchListener;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoDripView;
import com.rohitneel.photopixelpro.photocollage.support.Constants;
import com.rohitneel.photopixelpro.photocollage.support.MyExceptionHandlerPix;
import com.rohitneel.photopixelpro.photocollage.utils.BitmapTransfer;
import com.rohitneel.photopixelpro.photocollage.utils.DripUtils;
import com.rohitneel.photopixelpro.photocollage.utils.ImageUtils;
import com.rohitneel.photopixelpro.photocollage.widget.DripFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class DripLayout extends PhotoBaseActivity implements LayoutItemListener, BackgroundItemListener,
        DripColorAdapter.ColorListener{
    public static Bitmap resultBmp;
    private static Bitmap faceBitmap;
    private Bitmap selectedBitmap;
    private Bitmap cutBitmap;
    private Bitmap mainBitmap = null;
    private Bitmap OverLayBackground = null;
    private Bitmap bitmapColor = null;
    private boolean isFirst = true;
    private boolean isReady = false;
    public int count = 0;
    private PhotoDripView dripViewColor;
    private PhotoDripView dripViewStyle;
    private PhotoDripView dripViewImage;
    private DripFrameLayout frameLayoutBackground;
    private PhotoDripView dripViewBackground;
    private RecyclerView recyclerViewStyle;
    private RecyclerView recyclerViewColor;
    private RecyclerView recyclerViewBackground;
    private LinearLayout linearLayoutStyle;
    private View lastSelectedColor = null;
    private DripAdapter dripItemAdapter;
    private DripBackgroundAdapter dripBackgroundAdapter;
    private List<String> dripColorList = BrushColorAsset.listColorBrush();
    private ArrayList<String> dripEffectList = new ArrayList<String>();
    private ArrayList<String> dripBackgroundList = new ArrayList<String>();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_drip);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(DripLayout.this));

        this.dripViewColor = findViewById(R.id.dripViewColor);
        this.dripViewStyle = findViewById(R.id.dripViewStyle);
        this.dripViewImage = findViewById(R.id.dripViewImage);
        this.dripViewBackground = findViewById(R.id.dripViewBackground);
        this.frameLayoutBackground = findViewById(R.id.frameLayoutBackground);

        this.linearLayoutStyle = findViewById(R.id.linearLayoutStyle);
        this.linearLayoutStyle.setVisibility(View.VISIBLE);
        this.dripViewStyle.setOnTouchListenerCustom(new TouchListener());
        this.dripViewImage.setOnTouchListenerCustom(new TouchListener());


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


        for (int i = 1; i <= 10; i++) {
            dripEffectList.add("drip_" + i);
        }

        for (int i = 1; i <= 20; i++) {
            dripBackgroundList.add("background_" + i);
        }

        this.recyclerViewColor = findViewById(R.id.recyclerViewColor);
        this.recyclerViewColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewColor.setAdapter(new DripColorAdapter(this, this));
        this.recyclerViewColor.setVisibility(View.VISIBLE);

        recyclerViewStyle = (RecyclerView) findViewById(R.id.recyclerViewStyle);
        recyclerViewStyle.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        recyclerViewBackground = (RecyclerView) findViewById(R.id.recyclerViewBackground);
        recyclerViewBackground.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        setDripList();
        setBgList();

        dripViewImage.post(new Runnable() {
            public void run() {
                initBitmap();
            }
        });

        this.findViewById(R.id.relativeLayoutStyle).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                recyclerViewStyle.setVisibility(View.VISIBLE);
                recyclerViewBackground.setVisibility(View.GONE);
            }
        });

        this.findViewById(R.id.relativeLayoutEraser).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                StickerEraseActivity.b = selectedBitmap;
                Intent intent = new Intent(DripLayout.this, StickerEraseActivity.class);
                intent.putExtra(Constants.KEY_OPEN_FROM, Constants.VALUE_OPEN_FROM_DRIP);
                startActivityForResult(intent, 1024);
            }
        });

        this.findViewById(R.id.relativeLayoutBackground).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                recyclerViewStyle.setVisibility(View.GONE);
                recyclerViewBackground.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1024) {
            if (resultBmp != null) {
                cutBitmap = resultBmp;
                dripViewImage.setImageBitmap(cutBitmap);
                isReady = true;
                Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(DripLayout.this, "drip/style/" + dripItemAdapter.getItemList().get(dripItemAdapter.selectedPos) + ".webp");
                if (!"none".equals(dripItemAdapter.getItemList().get(0))) {
                    OverLayBackground = bitmapFromAsset;
                }
            }
        }
    }

    public static void setFaceBitmap(Bitmap bitmap) {
        faceBitmap = bitmap;
    }

    private void initBitmap() {
        if (faceBitmap != null) {
            selectedBitmap = ImageUtils.getBitmapResize(DripLayout.this, faceBitmap, 1024, 1024);
            mainBitmap = Bitmap.createScaledBitmap(DripUtils.getBitmapFromAsset(DripLayout.this, "drip/style/white.webp"), selectedBitmap.getWidth(), selectedBitmap.getHeight(), true);
            bitmapColor = mainBitmap;
            Glide.with(this).load(Integer.valueOf(R.drawable.drip_1)).into(dripViewStyle);
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
                cutBitmap = ImageUtils.getMask(DripLayout.this, selectedBitmap, createBitmap, width, height);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                        bitmap, cutBitmap.getWidth(), cutBitmap.getHeight(), false);
                cutBitmap = resizedBitmap;

                runOnUiThread(new Runnable() {
                    public void run() {
                        Palette p = Palette.from(cutBitmap).generate();
                        if (p.getDominantSwatch() == null) {
                            Toast.makeText(DripLayout.this, getString(R.string.txt_not_detect_human), Toast.LENGTH_SHORT).show();
                        }
                        dripViewImage.setImageBitmap(cutBitmap);
                        isReady = true;
                        Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(DripLayout.this, "drip/style/" + dripItemAdapter.getItemList().get(0) + ".webp");
                        if (!"none".equals(dripItemAdapter.getItemList().get(0))) {
                            OverLayBackground = bitmapFromAsset;
                        }

                    }
                });
            }
        }, this, progressBar).execute(new Void[0]);

    }

    public void onLayoutListClick(View view, int i) {
        Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(this, "drip/style/" + dripItemAdapter.getItemList().get(i) + ".webp");
        if (!"none".equals(dripItemAdapter.getItemList().get(i))) {
            OverLayBackground = bitmapFromAsset;
            dripViewStyle.setImageBitmap(OverLayBackground);
            return;
        }
        this.OverLayBackground = null;
        return;
    }

    @Override
    public void onBackgroundListClick(View view, int i) {
        if (i != 0) {
            Bitmap assetBitmapBack = ImageUtils.getBitmapFromAsset(DripLayout.this, "drip/background/" + dripBackgroundAdapter.getItemList().get(i) + ".webp");
            dripViewBackground.setImageBitmap(assetBitmapBack);
        } else {
            dripViewBackground.setImageResource(0);
        }
        dripViewBackground.setOnTouchListener(new MultiTouchListener(this, true));
    }

    public void setDripList() {
        dripItemAdapter = new DripAdapter(DripLayout.this);
        dripItemAdapter.setClickListener(this);
        recyclerViewStyle.setAdapter(dripItemAdapter);
        dripItemAdapter.addData(dripEffectList);
    }

    public void setBgList() {
        dripBackgroundAdapter = new DripBackgroundAdapter(DripLayout.this);
        dripBackgroundAdapter.setClickListener(this);
        recyclerViewBackground.setAdapter(dripBackgroundAdapter);
        dripBackgroundAdapter.addData(dripBackgroundList);
    }

    @Override
    public void onColorSelected(DripColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            bitmapColor = DripUtils.changeBitmapColor(mainBitmap, squareView.drawableId);
            dripViewColor.setImageBitmap(bitmapColor);
            frameLayoutBackground.setBackgroundColor(squareView.drawableId);
            dripViewColor.setBackgroundColor(squareView.drawableId);
            dripViewStyle.setColorFilter(squareView.drawableId);
        }  else {
            bitmapColor = DripUtils.changeBitmapColor(mainBitmap, squareView.drawableId);
            dripViewColor.setImageBitmap(bitmapColor);
            frameLayoutBackground.setBackgroundColor(squareView.drawableId);
            dripViewColor.setBackgroundColor(squareView.drawableId);
            dripViewStyle.setColorFilter(squareView.drawableId);
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
            Intent intent = new Intent(DripLayout.this, PhotoEditorActivity.class);
            intent.putExtra("MESSAGE","done");
            setResult(RESULT_OK,intent);
            finish();
        }
    }

}
