package com.rohitneel.photopixelpro.photocollage.layout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoBaseActivity;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoEditorActivity;
import com.rohitneel.photopixelpro.photocollage.crop.CropAsyncTask;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoMotionView;
import com.rohitneel.photopixelpro.photocollage.photo.PhotoMotionViewTouchBase;
import com.rohitneel.photopixelpro.photocollage.support.MyExceptionHandlerPix;
import com.rohitneel.photopixelpro.photocollage.utils.BitmapTransfer;
import com.rohitneel.photopixelpro.photocollage.utils.MotionUtils;

public class MotionLayout extends PhotoBaseActivity {
    static final LinearLayout.LayoutParams LAYOUT_PARAMS = new LinearLayout.LayoutParams(MotionUtils.dpToPx(30), MotionUtils.dpToPx(30));
    double Rotation = 0.0d;
    double alpha = Math.toRadians(this.motionDirection);
    float leftX = ((float) this.left);
    float topY = ((float) this.top);
    public Bitmap imageBitmap = null;
    public static Bitmap resultBmp;
    private Bitmap mainBitmap = null;
    public static Bitmap btimapOraginal;
    public Matrix matrix = null;
    Matrix matrixCenter = null;
    double motionDirection = 30.0d;
    int currentColor = -1;
    public int CountCurrentProgress = 2;
    public int OpacityCurrentProgress = ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION;
    int i = 0;
    public boolean isReady = false;
    public int count = 0;
    Bitmap cropped = null;
    int left;
    int top;
    private SeekBar seekbarCount = null;
    private SeekBar seekbarOpacity = null;
    private SeekBar seekbarRotate = null;
    PhotoMotionView imageViewCenter;
    ImageView iv_erase;
    ImageView imageViewCover;
    public TextView textViewValueCount;
    public TextView textViewValueOpacity;
    public TextView textViewValueRotate;
    ImageView imageViewSave;

    static { int dpToPx = MotionUtils.dpToPx(5);
        LAYOUT_PARAMS.setMargins(dpToPx, dpToPx, dpToPx, dpToPx);
    }
    public static void setFaceBitmap(Bitmap bitmap) {
        btimapOraginal = bitmap;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.layout_motion);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(MotionLayout.this));
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.crop_progress_bar);
        this.imageViewCenter = (PhotoMotionView) findViewById(R.id.imageViewTouch);
        this.imageViewCover = (ImageView) findViewById(R.id.imageViewCover);
        this.iv_erase = (ImageView) findViewById(R.id.image_view_compare_eraser);
        this.imageViewSave = (ImageView) findViewById(R.id.imageViewSaveMotion);
        this.imageViewCenter.setImageBitmap(this.btimapOraginal);
        this.imageViewCenter.setDisplayType(PhotoMotionViewTouchBase.DisplayType.FIT_TO_SCREEN);
        this.imageViewCenter.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = 21)
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= 16) {
                    imageViewCenter.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    imageViewCenter.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                if (i == 0) {
                    Bitmap bitmap1 = imageBitmap = Bitmap.createScaledBitmap(btimapOraginal, btimapOraginal.getWidth(), btimapOraginal.getHeight(), true);
                    imageViewCover.setImageBitmap(imageBitmap);
                    matrixCenter = imageViewCenter.getImageViewMatrix();
                    i++;
                    imageViewCover.setImageMatrix(matrixCenter);
                    if (matrix == null) {
                        Matrix matrix = MotionLayout.this.matrix = matrixCenter;
                    }
                    progressBar.setVisibility(View.VISIBLE);
                    new CountDownTimer(21000, 1000) {
                        public void onFinish() {
                        }

                        public void onTick(long j) {
                            int i = count = count + 1;
                            if (progressBar.getProgress() <= 90) {
                                progressBar.setProgress(count * 5);
                            }
                        }
                    }.start();
                    new CropAsyncTask((bitmap, bitmap2, i, i2) -> {
                        if (bitmap != null) {
                            cropped = bitmap;
                            left = i;
                            top = i2;
                            leftX = (float) left;
                            topY = (float) top;
                            boolean unused1 = isReady = true;
                            int i3 = currentColor;
                            Color.parseColor("#FFFFFF");
                            methodCropAsyncTaskPaint(0.0f, 0.0f, 0.0f, 0.0f);
                        }
                    }, MotionLayout.this, progressBar).execute(new Void[0]);
                }
            }
        });
        setRotate();
        setCount();
        setOpacity();

        imageViewSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mainBitmap != null) {
                    BitmapTransfer.bitmap = mainBitmap;
                    Intent intent = new Intent(MotionLayout.this, PhotoEditorActivity.class);
                    intent.putExtra("MESSAGE","done");
                    setResult(RESULT_OK,intent);
                    finish();
                }

            }
        });


        findViewById(R.id.imageViewCloseMotion).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1024) {
            if (resultBmp != null) {
                cropped = resultBmp;
                imageViewCover.setImageBitmap(mainBitmap);
                isReady = true;
                methodCropAsyncTaskPaint(0.0f, 0.0f, 0.0f, 0.0f);
            }
        }
    }

    public void setRotate() {
        if (this.seekbarRotate == null) {
            this.seekbarRotate = (SeekBar) findViewById(R.id.seekbarRotate);
            this.textViewValueRotate = (TextView) findViewById(R.id.textViewValueRotate);
            this.seekbarRotate.setMax(360);
            this.seekbarRotate.setProgress(0);
            this.textViewValueRotate.setText("0");
            this.seekbarRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    TextView textViewValueRotate = MotionLayout.this.textViewValueRotate;
                    textViewValueRotate.setText("" + i);
                    Rotation = Math.toRadians((double) i);
                    methodCropAsyncTaskPaint(0.0f, 0.0f, 0.0f, 0.0f);
                }
            });
        }
    }

    public void setCount() {
        if (this.seekbarCount == null) {
            this.seekbarCount = (SeekBar) findViewById(R.id.seekbarCount);
            this.textViewValueCount = (TextView) findViewById(R.id.textViewValueCount);
            this.seekbarCount.setMax(50);
            this.seekbarCount.setProgress(2);
            this.textViewValueCount.setText("2");
            this.seekbarCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    int unused = CountCurrentProgress = i;
                    if (CountCurrentProgress < 0) {
                        int unused2 = CountCurrentProgress = 0;
                    }
                    TextView textViewValueCount = MotionLayout.this.textViewValueCount;
                    textViewValueCount.setText("" + i);
                    methodCropAsyncTaskPaint(0.0f, 0.0f, 0.0f, 0.0f);
                }
            });
        }
    }

    public void setOpacity() {
        if (this.seekbarOpacity == null) {
            this.seekbarOpacity = (SeekBar) findViewById(R.id.seekbarOpacity);
            this.textViewValueOpacity = (TextView) findViewById(R.id.textViewValueOpacity);
            this.seekbarOpacity.setMax(100);
            int i = (this.OpacityCurrentProgress * 100) / 255;
            TextView textView = this.textViewValueOpacity;
            textView.setText("" + i);
            this.seekbarOpacity.setProgress(i);
            this.seekbarOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                }

                public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                    int unused = OpacityCurrentProgress = (i * 255) / 100;
                    if (OpacityCurrentProgress < 0) {
                        int unused2 = OpacityCurrentProgress = 0;
                    }
                    TextView textViewValueOpacity = MotionLayout.this.textViewValueOpacity;
                    textViewValueOpacity.setText("" + i);
                    methodCropAsyncTaskPaint(0.0f, 0.0f, 0.0f, 0.0f);
                }
            });
        }
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        methodCropAsyncTaskPaint(motionEvent.getX(), motionEvent.getY(), 0.0f, 0.0f);
        return true;
    }

    public void methodCropAsyncTaskPaint(float f, float f2, float f3, float f4) {
        this.alpha = this.Rotation;
        paintBitmaps();
        this.leftX += f;
        this.topY += f2;
    }

    private Bitmap paintBitmaps() {
        Paint paint = null;
        if (!this.isReady) {
            return null;
        }
        Log.d("alpha=", "" + this.alpha);
        Bitmap copy = this.btimapOraginal.copy(Bitmap.Config.ARGB_8888, true);
        Canvas canvas = new Canvas(copy);
        Bitmap changeAlpha = MotionUtils.changeAlpha(this.cropped, this.OpacityCurrentProgress);
        int i = this.left;
        int i2 = this.top;
        double cos = Math.cos(this.alpha);
        double sin = Math.sin(this.alpha);
        Log.d("alphacos=", "" + cos);
        Log.d("alphasin=", "" + sin);
        if (this.CountCurrentProgress > 0) {
            int dpToPx = MotionUtils.dpToPx(180 / this.CountCurrentProgress);
            int i3 = this.CountCurrentProgress;
            while (i3 > 0) {
                double d = (double) this.left;
                double d2 = (double) (dpToPx * i3);
                Double.isNaN(d2);
                Double.isNaN(d);
                double d3 = (double) this.top;
                Double.isNaN(d2);
                Double.isNaN(d3);
                canvas = canvas;
                canvas.drawBitmap(changeAlpha, (float) ((int) (d + (d2 * cos))), (float) ((int) (d3 - (d2 * sin))), (Paint) null);
                i3--;
                paint = null;
            }
        }
        canvas.drawBitmap(this.cropped, (float) this.left, (float) this.top, paint);
        this.mainBitmap = copy;
        this.imageViewCover.setImageBitmap(this.mainBitmap);
        return copy;
    }
}
