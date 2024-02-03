package com.rohitneel.photopixelpro.photocollage.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoBaseActivity;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoEditorActivity;
import com.rohitneel.photopixelpro.photocollage.adapters.DripColorAdapter;
import com.rohitneel.photopixelpro.photocollage.adapters.PixLabAdapters;
import com.rohitneel.photopixelpro.photocollage.drip.imagescale.TouchListener;
import com.rohitneel.photopixelpro.photocollage.listener.LayoutItemListener;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoDripView;
import com.rohitneel.photopixelpro.photocollage.support.MyExceptionHandlerPix;
import com.rohitneel.photopixelpro.photocollage.utils.BitmapTransfer;
import com.rohitneel.photopixelpro.photocollage.utils.DripUtils;
import com.rohitneel.photopixelpro.photocollage.utils.ImageUtils;
import com.rohitneel.photopixelpro.photocollage.widget.DripFrameLayout;

import java.util.ArrayList;


public class PixLabLayout extends PhotoBaseActivity implements LayoutItemListener, DripColorAdapter.ColorListener {
    public static Bitmap resultBmp;
    private static Bitmap faceBitmap;
    private Bitmap selectedBitmap;
    private Bitmap cutBitmap;
    private Bitmap mainBitmap = null;
    private Bitmap OverLayBackground = null;
    private Bitmap bitmapColor = null;
    private boolean isFirst = true;
    private boolean isReady = false;
    private PhotoDripView dripViewColor;
    private PhotoDripView dripViewStyle;
    private PhotoDripView dripViewBack;
    private DripFrameLayout frameLayoutBackground;
    private RecyclerView recyclerViewStyle;
    private RecyclerView recyclerViewColor;
    private PixLabAdapters dripItemAdapter;
    private ArrayList<String> dripEffectList = new ArrayList<String>();

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_pixlab);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(PixLabLayout.this));

        this.dripViewColor = findViewById(R.id.dripViewColor);
        this.dripViewStyle = findViewById(R.id.dripViewStyle);
        this.dripViewBack = findViewById(R.id.dripViewBack);
        this.frameLayoutBackground = findViewById(R.id.frameLayoutBackground);

        this.dripViewBack.setOnTouchListenerCustom(new TouchListener());

        new Handler().postDelayed(new Runnable() {
            public void run() {
                dripViewBack.post(new Runnable() {
                    public void run() {
                        if (isFirst) {
                            isFirst = false;
                            initBitmap();
                        }
                    }
                });
            }
        },  1000);


        findViewById(R.id.imageViewCloseSplash).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.imageViewSaveSplash).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new saveFile().execute();
            }
        });

        for (int i = 1; i <= 16; i++) {
            dripEffectList.add("style_" + i);
        }

        this.recyclerViewColor = findViewById(R.id.recyclerViewColor);
        this.recyclerViewColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewColor.setAdapter(new DripColorAdapter(this, this));
        this.recyclerViewColor.setVisibility(View.VISIBLE);

        recyclerViewStyle = (RecyclerView) findViewById(R.id.recyclerViewStyle);
        recyclerViewStyle.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        setDripList();
        dripViewBack.post(new Runnable() {
            public void run() {
                initBitmap();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1024) {
            if (resultBmp != null) {
                cutBitmap = resultBmp;
                dripViewBack.setImageBitmap(cutBitmap);
                isReady = true;
                Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(PixLabLayout.this, "lab/" + dripItemAdapter.getItemList().get(dripItemAdapter.selectedPos) + ".webp");
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
            selectedBitmap = ImageUtils.getBitmapResize(PixLabLayout.this, faceBitmap, 1024, 1024);
            mainBitmap = Bitmap.createScaledBitmap(DripUtils.getBitmapFromAsset(PixLabLayout.this, "lab/white.webp"), selectedBitmap.getWidth(), selectedBitmap.getHeight(), true);
            bitmapColor = mainBitmap;
            Glide.with(this).load(Integer.valueOf(R.drawable.style_1)).into(dripViewStyle);
            dripViewBack.setImageBitmap(selectedBitmap);
        }
    }

    public void onLayoutListClick(View view, int i) {
        Bitmap bitmapFromAsset = DripUtils.getBitmapFromAsset(PixLabLayout.this, "lab/" + dripItemAdapter.getItemList().get(i) + ".webp");
        if (!"none".equals(dripItemAdapter.getItemList().get(i))) {
            OverLayBackground = bitmapFromAsset;
            dripViewStyle.setImageBitmap(OverLayBackground);
            return;
        }
        OverLayBackground = null;
    }

    public void setDripList() {
        dripItemAdapter = new PixLabAdapters(PixLabLayout.this);
        dripItemAdapter.setClickListener(this);
        recyclerViewStyle.setAdapter(dripItemAdapter);
        dripItemAdapter.addData(dripEffectList);
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
            Intent intent = new Intent(PixLabLayout.this, PhotoEditorActivity.class);
            intent.putExtra("MESSAGE","done");
            setResult(RESULT_OK,intent);
            finish();
        }
    }

}
