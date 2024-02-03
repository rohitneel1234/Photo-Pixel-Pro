package com.rohitneel.photopixelpro.photocollage.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rohitneel.photopixelpro.BuildConfig;
import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoBaseActivity;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoEditorActivity;
import com.rohitneel.photopixelpro.photocollage.adapters.TextColorAdapter;
import com.rohitneel.photopixelpro.photocollage.draw.SplashBrushView;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoSplashView;
import com.rohitneel.photopixelpro.photocollage.support.MyExceptionHandlerPix;
import com.rohitneel.photopixelpro.photocollage.support.SupportedClass;
import com.rohitneel.photopixelpro.photocollage.utils.BitmapTransfer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class SplashLayout extends PhotoBaseActivity implements OnSeekBarChangeListener,
        TextColorAdapter.ColorListener{
    public static final int REQUEST_CODE_CAMERA = 0x2;
    public static final int REQUEST_CODE_GALLERY = 0x3;
    public static Bitmap colorBitmap;
    public static Bitmap grayBitmap;
    public String selectedImagePath;
    public String selectedOutputPath;
    public static String drawPath;
    public Uri selectedImageUri;
    public static int displayHight;
    public static int displayWidth;
    public static Vector vector;
    private Runnable runnableCode;
    TextColorAdapter tetxColorAdapter;
    public static SplashBrushView brushView;
    private RelativeLayout relativeLayoutView;
    private RelativeLayout relativeLayoutContainer;
    private ImageView imageViewColor;
    private ImageView imageViewGray;
    private ImageView imageViewManual;
    private TextView textViewColor;
    private TextView textViewGray;
    private TextView textViewManual;
    private RecyclerView recyclerViewColor;
    public static SeekBar seekBarOpacity;
    public static SeekBar seekBarSize;
    public static PhotoSplashView splashView;
    private List<TextColorAdapter.SquareView> squareViewListSaved = new ArrayList();


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_splash);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(SplashLayout.this));
        relativeLayoutContainer = findViewById(R.id.relativeLayoutContainer);
        brushView = findViewById(R.id.brushView);
        vector = new Vector();
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        displayWidth = point.x;
        displayHight = point.y;
        splashView = findViewById(R.id.drawingImageView);
        if (BitmapTransfer.bitmap != null) {
            colorBitmap = BitmapTransfer.bitmap;
        } else { }
        grayBitmap = grayScaleBitmap(colorBitmap);
        imageViewColor = findViewById(R.id.imageViewColor);
        imageViewGray = findViewById(R.id.imageViewGray);
        imageViewManual = findViewById(R.id.imageViewManual);
        textViewColor = findViewById(R.id.textViewColor);
        textViewGray = findViewById(R.id.textViewGray);
        textViewManual = findViewById(R.id.textViewManual);
        relativeLayoutView = findViewById(R.id.relativeLayoutView);
        seekBarSize = findViewById(R.id.seekBarSize);
        seekBarOpacity = findViewById(R.id.seekBarOpacity);
        seekBarSize.setMax(100);
        seekBarOpacity.setMax(240);
        seekBarSize.setProgress((int) splashView.radius);
        seekBarOpacity.setProgress(splashView.opacity);
        seekBarSize.setOnSeekBarChangeListener(this);
        seekBarOpacity.setOnSeekBarChangeListener(this);
        splashView.initDrawing();
        final Handler handler = new Handler();
        this.runnableCode = new Runnable() {
            public void run() {
                handler.postDelayed(runnableCode, 2000);
            }
        };
        handler.post(this.runnableCode);

        this.recyclerViewColor = findViewById(R.id.recyclerViewColor);
        this.recyclerViewColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        this.recyclerViewColor.setAdapter(new TextColorAdapter(this, this));
        this.recyclerViewColor.setVisibility(View.VISIBLE);

        findViewById(R.id.imageViewSaveSplash).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (splashView.drawingBitmap != null){
                    BitmapTransfer.bitmap = splashView.drawingBitmap;
                }
                Intent intent = new Intent(SplashLayout.this, PhotoEditorActivity.class);
                intent.putExtra("MESSAGE","done");
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        findViewById(R.id.imageViewCloseSplash).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        findViewById(R.id.relativeLayoutManual).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                imageViewColor.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.white));
                imageViewGray.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.white));
                imageViewManual.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.mainColor));
                textViewColor.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.white));
                textViewGray.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.white));
                textViewManual.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.mainColor));
                relativeLayoutView.setVisibility(View.GONE);
                splashView.mode = 0;
                splashView.splashBitmap = grayScaleBitmap(colorBitmap);
                splashView.updateRefMetrix();
                splashView.changeShaderBitmap();
                splashView.coloring = -2;

                tetxColorAdapter = (TextColorAdapter) recyclerViewColor.getAdapter();
                if (tetxColorAdapter != null) {
                    tetxColorAdapter.setSelectedColorIndex(0);
                }
                if (tetxColorAdapter != null) {
                    tetxColorAdapter.notifyDataSetChanged();
                }
            }
        });

        findViewById(R.id.relativeLayoutColor).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                imageViewColor.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.mainColor));
                imageViewGray.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.white));
                imageViewManual.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.white));
                textViewColor.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.mainColor));
                textViewGray.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.white));
                textViewManual.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.white));
                relativeLayoutView.setVisibility(View.VISIBLE);
                splashView.mode = 0;
                PhotoSplashView splashView = SplashLayout.splashView;
                splashView.splashBitmap = colorBitmap;
                splashView.updateRefMetrix();
                SplashLayout.splashView.changeShaderBitmap();
                SplashLayout.splashView.coloring = -1;
            }
        });

        findViewById(R.id.relativeLayoutGray).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                imageViewColor.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.white));
                imageViewGray.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.mainColor));
                imageViewManual.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.white));
                textViewColor.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.white));
                textViewGray.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.mainColor));
                textViewManual.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.white));
                relativeLayoutView.setVisibility(View.VISIBLE);
                splashView.mode = 0;
                splashView.splashBitmap = grayScaleBitmap(colorBitmap);
                splashView.updateRefMetrix();
                splashView.changeShaderBitmap();
                splashView.coloring = -2;
            }
        });

        findViewById(R.id.imageViewUndo).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                //Undo
                LoadView(squareViewListSaved.get(squareViewListSaved.size()-1));
            }
        });

        findViewById(R.id.imageViewRedo).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                //Redo
            }
        });

        findViewById(R.id.imageViewReset).setOnClickListener(view -> {
            imageViewColor.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.mainColor));
            imageViewGray.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.white));
            imageViewManual.setColorFilter(ContextCompat.getColor(SplashLayout.this, R.color.white));
            textViewColor.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.mainColor));
            textViewGray.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.white));
            textViewManual.setTextColor(ContextCompat.getColor(SplashLayout.this, R.color.white));
            relativeLayoutView.setVisibility(View.VISIBLE);
            SplashLayout.grayBitmap = grayScaleBitmap(SplashLayout.colorBitmap);
            SplashLayout.splashView.initDrawing();
            SplashLayout.splashView.saveScale = 1.0f;
            SplashLayout.splashView.fitScreen();
            SplashLayout.splashView.updatePreviewPaint();
            SplashLayout.splashView.updatePaintBrush();
            SplashLayout.vector.clear();
        });

        findViewById(R.id.imageViewFit).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PhotoSplashView touchImageView = SplashLayout.splashView;
                touchImageView.saveScale = 1.0f;
                touchImageView.radius = ((float) (seekBarSize.getProgress() + 10)) / SplashLayout.splashView.saveScale;
                SplashLayout.splashView.fitScreen();
                SplashLayout.splashView.updatePreviewPaint();
            }
        });

        findViewById(R.id.imageViewZoom).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                splashView.mode = 1;
            }
        });
    }

    private void LoadView(TextColorAdapter.SquareView squareView) {
        SplashLayout colorSplashActivity = SplashLayout.this;
        SplashLayout.grayBitmap = colorSplashActivity.grayScaleBitmap(SplashLayout.colorBitmap);
        Canvas canvas = new Canvas(SplashLayout.grayBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(new float[]{((float) ((squareView.drawableId >> 16) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 8) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) (squareView.drawableId & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 24) & 255)) / 256.0f, 0.0f}));
        canvas.drawBitmap(SplashLayout.grayBitmap, 0.0f, 0.0f, paint);
        SplashLayout.splashView.splashBitmap = SplashLayout.grayBitmap;
        SplashLayout.splashView.updateRefMetrix();
        SplashLayout.splashView.changeShaderBitmap();
        SplashLayout.splashView.coloring = squareView.drawableId;
    }
    @Override
    public void onColorSelected(TextColorAdapter.SquareView squareView) {
        if (squareView.isColor) {
            SplashLayout colorSplashActivity = SplashLayout.this;
            SplashLayout.grayBitmap = colorSplashActivity.grayScaleBitmap(SplashLayout.colorBitmap);
            Canvas canvas = new Canvas(SplashLayout.grayBitmap);
            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(new float[]{((float) ((squareView.drawableId >> 16) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 8) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) (squareView.drawableId & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 24) & 255)) / 256.0f, 0.0f}));
            canvas.drawBitmap(SplashLayout.grayBitmap, 0.0f, 0.0f, paint);
            SplashLayout.splashView.splashBitmap = SplashLayout.grayBitmap;
            SplashLayout.splashView.updateRefMetrix();
            SplashLayout.splashView.changeShaderBitmap();
            SplashLayout.splashView.coloring = squareView.drawableId;
            squareViewListSaved.add(squareView);
        }  else {
            SplashLayout colorSplashActivity = SplashLayout.this;
            SplashLayout.grayBitmap = colorSplashActivity.grayScaleBitmap(SplashLayout.colorBitmap);
            Canvas canvas = new Canvas(SplashLayout.grayBitmap);
            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(new float[]{((float) ((squareView.drawableId >> 16) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 8) & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) (squareView.drawableId & 255)) / 256.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, ((float) ((squareView.drawableId >> 24) & 255)) / 256.0f, 0.0f}));
            canvas.drawBitmap(SplashLayout.grayBitmap, 0.0f, 0.0f, paint);
            SplashLayout.splashView.splashBitmap = SplashLayout.grayBitmap;
            SplashLayout.splashView.updateRefMetrix();
            SplashLayout.splashView.changeShaderBitmap();
            SplashLayout.splashView.coloring = squareView.drawableId;
            squareViewListSaved.add(squareView);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public Bitmap grayScaleBitmap(Bitmap bitmap) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(0.0f);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
        int id = seekBar.getId();
        if (id == R.id.seekBarOpacity) {
            SplashBrushView brushView2 = brushView;
            brushView2.isBrushSize = false;
            brushView2.setShapeRadiusRatio(splashView.radius);
            brushView.brushSize.setPaintOpacity(seekBarOpacity.getProgress());
            brushView.invalidate();
            PhotoSplashView splashView = SplashLayout.splashView;
            splashView.opacity = i + 15;
            splashView.updatePaintBrush();

        } else if (id == R.id.seekBarSize) {
            String sb = seekBarSize.getProgress() + "";
            Log.wtf("radious :", sb);
            splashView.radius = ((float) (seekBarSize.getProgress() + 10)) / splashView.saveScale;
            splashView.updatePaintBrush();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CAMERA) {
            selectedImagePath = selectedOutputPath;
            if (SupportedClass.stringIsNotEmpty(selectedImagePath)) {
                File fileImageClick = new File(selectedImagePath);
                if (fileImageClick.exists()) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        selectedImageUri = Uri.fromFile(fileImageClick);
                    } else {
                        selectedImageUri = FileProvider.getUriForFile(SplashLayout.this, BuildConfig.APPLICATION_ID + ".provider", fileImageClick);
                    }
                    onPhotoTakenApp();
                }
            }
        } else if (data != null && data.getData() != null) {
            if (requestCode == REQUEST_CODE_GALLERY) {
                selectedImageUri = data.getData();
            } else {
                selectedImagePath = selectedOutputPath;
            }
            if (SupportedClass.stringIsNotEmpty(selectedImagePath)) {
                onPhotoTakenApp();
            }

        } else {
            Log.e("TAG", "");
        }
    }

    public void onPhotoTakenApp() {
        relativeLayoutContainer.post(new Runnable() {
            @Override
            public void run() {
                grayBitmap = grayScaleBitmap(colorBitmap);
                splashView.initDrawing();
                splashView.saveScale = 1.0f;
                splashView.fitScreen();
                splashView.updatePreviewPaint();
                splashView.updatePaintBrush();
                vector.clear();
            }
        });
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
        Bitmap copy = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888).copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint(1);
        paint.setColor(-16711936);
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onDestroy() {
        super.onDestroy();
    }

}
