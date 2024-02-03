package com.rohitneel.photopixelpro.photocollage.layout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoBaseActivity;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoEditorActivity;
import com.rohitneel.photopixelpro.photocollage.adapters.NeonAdapter;
import com.rohitneel.photopixelpro.photocollage.crop.MLCropAsyncTask;
import com.rohitneel.photopixelpro.photocollage.crop.MLOnCropTaskCompleted;
import com.rohitneel.photopixelpro.photocollage.eraser.StickerEraseActivity;
import com.rohitneel.photopixelpro.photocollage.listener.LayoutItemListener;
import com.rohitneel.photopixelpro.photocollage.listener.MultiTouchListener;
import com.rohitneel.photopixelpro.photocollage.support.Constants;
import com.rohitneel.photopixelpro.photocollage.utils.BitmapTransfer;
import com.rohitneel.photopixelpro.photocollage.utils.ImageUtils;

import java.util.ArrayList;

public class NeonLayout extends PhotoBaseActivity implements LayoutItemListener {

    public static Bitmap resultBitmap;
    private static Bitmap faceBitmap;
    private Bitmap selectedBitmap;
    private Bitmap cutBitmap;
    public int count = 0;
    private int neonCount = 14;
    private int frameCount = 14;
    private int shapeCount = 10;
    boolean isFirst = true;
    private Context context;
    private NeonAdapter neonAdapter;
    private RecyclerView recyclerViewNeon;
    private ImageView imageViewBackground;
    public static ImageView imageViewFont;
    private ImageView imageViewBack;
    private ImageView imageViewCover;
    private RelativeLayout relativeLayoutRootView;
    private TextView textViewSpiral;
    private TextView textViewShape;
    private TextView textViewFrame;
    private View viewSpiral;
    private View viewShape;
    private View viewFrame;
    private SeekBar seekBarOpacity;
    private ArrayList<String> neonEffectList = new ArrayList<String>();
    private ArrayList<String> shapeEffectList = new ArrayList<String>();
    private ArrayList<String> frameEffectList = new ArrayList<String>();

    public static void setFaceBitmap(Bitmap bitmap) {
        faceBitmap = bitmap;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_neon);
        context = this;
        selectedBitmap = faceBitmap;

        new Handler().postDelayed(new Runnable() {
            public void run() {
                imageViewCover.post(new Runnable() {
                    public void run() {
                        if (isFirst && selectedBitmap != null) {
                            isFirst = false;
                            initBitmap();
                        }
                    }
                });
            }
        }, 1000);

        neonEffectList.add("none");
        shapeEffectList.add("none");
        frameEffectList.add("none");
        for (int i = 1; i <= neonCount; i++) {
            neonEffectList.add("line_" + i);
        }
        for (int i = 1; i <= shapeCount; i++) {
            shapeEffectList.add("shape_" + i);
        }
        for (int i = 1; i <= frameCount; i++) {
            frameEffectList.add("frame_" + i);
        }
        initViews();
    }

    private void initBitmap() {
        if (faceBitmap != null) {
            selectedBitmap = ImageUtils.getBitmapResize(context, faceBitmap, imageViewCover.getWidth(), imageViewCover.getHeight());
            relativeLayoutRootView.setLayoutParams(new LinearLayout.LayoutParams(selectedBitmap.getWidth(), selectedBitmap.getHeight()));
            if (selectedBitmap != null && imageViewBackground != null) {
                imageViewBackground.setImageBitmap(selectedBitmap);
            }
            setStartNeon();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1024) {
            if (resultBitmap != null) {
                cutBitmap = resultBitmap;
                imageViewCover.setImageBitmap(cutBitmap);
            }
        }
    }

    public void initViews() {
        relativeLayoutRootView = findViewById(R.id.mContentRootView);
        imageViewFont = findViewById(R.id.imageViewFont);
        imageViewBack = findViewById(R.id.imageViewBack);
        imageViewBackground = findViewById(R.id.imageViewBackground);
        imageViewCover = findViewById(R.id.imageViewCover);
        recyclerViewNeon = (RecyclerView) findViewById(R.id.recyclerViewLine);
        recyclerViewNeon.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        seAdapterList();
        textViewSpiral = findViewById(R.id.text_view_spiral);
        textViewShape = findViewById(R.id.text_view_shape);
        textViewFrame = findViewById(R.id.text_view_frame);
        viewSpiral = findViewById(R.id.view_spiral);
        viewShape = findViewById(R.id.view_shape);
        viewFrame = findViewById(R.id.view_frame);
        seekBarOpacity = findViewById(R.id.seekbarOpacity);
        findViewById(R.id.linearLayoutSpiral).performClick();
        imageViewBackground.setRotationY(0.0f);

        imageViewCover.post(new Runnable() {
            public void run() {
                initBitmap();
            }
        });

        seekBarOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (imageViewBack != null && imageViewFont != null) {
                    float f = ((float) i) * 0.01f;
                    imageViewBack.setAlpha(f);
                    imageViewFont.setAlpha(f);
                }
            }
        });
        findViewById(R.id.imageViewCloseNeon).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });
        findViewById(R.id.imageViewSaveNeon).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                new saveFile().execute();
            }
        });

        findViewById(R.id.linearLayoutSpiral).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                neonAdapter.addData(neonEffectList);
                textViewSpiral.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.white));
                textViewShape.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.grayText));
                textViewFrame.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.grayText));
                viewSpiral.setVisibility(View.VISIBLE);
                viewShape.setVisibility(View.INVISIBLE);
                viewFrame.setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.linearLayoutShape).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                neonAdapter.addData(shapeEffectList);
                textViewSpiral.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.grayText));
                textViewShape.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.white));
                textViewFrame.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.grayText));
                viewSpiral.setVisibility(View.INVISIBLE);
                viewShape.setVisibility(View.VISIBLE);
                viewFrame.setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.linearLayoutFrame).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                neonAdapter.addData(frameEffectList);
                textViewSpiral.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.grayText));
                textViewShape.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.grayText));
                textViewFrame.setTextColor(ContextCompat.getColor(NeonLayout.this, R.color.white));
                viewSpiral.setVisibility(View.INVISIBLE);
                viewShape.setVisibility(View.INVISIBLE);
                viewFrame.setVisibility(View.VISIBLE);
            }
        });

        findViewById(R.id.image_view_eraser).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                StickerEraseActivity.b = cutBitmap;
                Intent intent = new Intent(NeonLayout.this, StickerEraseActivity.class);
                intent.putExtra(Constants.KEY_OPEN_FROM, Constants.VALUE_OPEN_FROM_NEON);
                startActivityForResult(intent, 1024);
            }
        });

    }

    public void seAdapterList() {
        neonAdapter = new NeonAdapter(context);
        neonAdapter.setLayoutItenListener(this);
        recyclerViewNeon.setAdapter(neonAdapter);
        neonAdapter.addData(neonEffectList);
    }

    public void onLayoutListClick(View view, int i) {
        if (i != 0) {
            Bitmap assetBitmapBack = ImageUtils.getBitmapFromAsset(context, "neon/back/" + neonAdapter.getItemList().get(i) + "_back.webp");
            Bitmap assetBitmapFront = ImageUtils.getBitmapFromAsset(context, "neon/front/" + neonAdapter.getItemList().get(i) + "_front.webp");
            imageViewBack.setImageBitmap(assetBitmapBack);
            imageViewFont.setImageBitmap(assetBitmapFront);
        } else {
            imageViewBack.setImageResource(0);
            imageViewFont.setImageResource(0);
        }
        imageViewBack.setOnTouchListener(new MultiTouchListener(this, true));
    }

    public void setStartNeon() {
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
                cutBitmap = ImageUtils.getMask(context, selectedBitmap, createBitmap, width, height);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                        bitmap, cutBitmap.getWidth(), cutBitmap.getHeight(), false);
                cutBitmap = resizedBitmap;

                runOnUiThread(new Runnable() {
                    public void run() {
                        Palette p = Palette.from(cutBitmap).generate();
                        if (p.getDominantSwatch() == null) {
                            Toast.makeText(NeonLayout.this, getString(R.string.txt_not_detect_human), Toast.LENGTH_SHORT).show();
                        }
                        imageViewCover.setImageBitmap(cutBitmap);
                    }
                });


            }
        }, this, progressBar).execute(new Void[0]);
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
            relativeLayoutRootView.setDrawingCacheEnabled(true);
            Bitmap bitmap = getBitmapFromView(relativeLayoutRootView);
            try {
                return bitmap;
            } catch (Exception e) {
                return null;
            } finally {
                relativeLayoutRootView.setDrawingCacheEnabled(false);
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (bitmap != null){
                BitmapTransfer.bitmap = bitmap;
            }
            Intent intent = new Intent(NeonLayout.this, PhotoEditorActivity.class);
            intent.putExtra("MESSAGE","done");
            setResult(RESULT_OK,intent);
            finish();
        }
    }

}
