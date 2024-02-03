package com.rohitneel.photopixelpro.photocollage.eraser;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.activities.PhotoBaseActivity;
import com.rohitneel.photopixelpro.photocollage.eraser.eraser.EraseView;
import com.rohitneel.photopixelpro.photocollage.eraser.eraser.MultiTouchListener;
import com.rohitneel.photopixelpro.photocollage.layout.ArtLayout;
import com.rohitneel.photopixelpro.photocollage.layout.DripLayout;
import com.rohitneel.photopixelpro.photocollage.layout.MotionLayout;
import com.rohitneel.photopixelpro.photocollage.layout.NeonLayout;
import com.rohitneel.photopixelpro.photocollage.layout.WingLayout;
import com.rohitneel.photopixelpro.photocollage.support.Constants;
import com.rohitneel.photopixelpro.photocollage.support.MyExceptionHandlerPix;


public class StickerEraseActivity extends PhotoBaseActivity implements OnClickListener {
    public static Bitmap bgCircleBit = null, bitmap = null;
    public static int curBgType = 1, orgBitHeight, orgBitWidth;
    public static BitmapShader patternBMPshader;
    public static Bitmap b = null;
    public RelativeLayout relativeLayoutAuto, relativeLayoutSeekBar, relativeLayoutEraser,
              inside_cut_lay, relativeLayoutExtract, main_rel, outside_cut_lay, relativeLayoutRestore, relativeLayoutZoom;
    public Bitmap orgBitmap;
    public EraseView dv;
    public int height/*, id*/;
    public boolean isTutOpen = true, showDialog = false;
    public Animation scale_anim, animSlideDown, animSlideUp;
    public ImageView imageViewBackgroundCover, back_btn, dv1, save_btn;
    public int width;
    ImageView redo_btn;
    RelativeLayout relativeLayoutBackground;
    ImageView undo_btn;
    private LinearLayout lay_lasso_cut, linearLayoutEraser, linearLayoutAuto;
    private SeekBar seekBarBrushOffset, seekBarOffset, seekBarExtractOffset, radius_seekbar, seekBarThreshold;
    private String openFrom;
    private TextView textViewBrushSize;
    private TextView textViewBrushOffset;
    private TextView textViewAutoOffset;
    private TextView textViewExtractOffset;

    @SuppressLint({"WrongConstant"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_photo_eraser);
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandlerPix(StickerEraseActivity.this));
        openFrom = getIntent().getStringExtra(Constants.KEY_OPEN_FROM);

        this.animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_up);
        this.animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_down);
        this.scale_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_scale_anim);
        initUI();
        this.isTutOpen = false;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        this.width = displayMetrics.widthPixels;
        this.height = i - ImageUtils.dpToPx(this, 120.0f);
        curBgType = 1;
        this.main_rel.postDelayed(new Runnable() {
            public void run() {
                if (isTutOpen) {
                    imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(StickerEraseActivity.this, R.drawable.tbg3, width, height));
                    bgCircleBit = ImageUtils.getBgCircleBit(StickerEraseActivity.this, R.drawable.tbg3);
                } else {
                    imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(StickerEraseActivity.this, R.drawable.tbg, width, height));
                    StickerEraseActivity.bgCircleBit = ImageUtils.getBgCircleBit(StickerEraseActivity.this, R.drawable.tbg);
                }
                importImageFromUri();
            }
        }, 1000);
    }

    private void initUI() {
        this.relativeLayoutSeekBar = findViewById(R.id.relativeLayoutSeekBar);
        this.relativeLayoutAuto = findViewById(R.id.relativeLayoutAuto);
        this.relativeLayoutEraser = findViewById(R.id.relativeLayoutEraser);
        this.relativeLayoutRestore = findViewById(R.id.relativeLayoutRestore);
        this.relativeLayoutExtract = findViewById(R.id.relativeLayoutExtract);
        this.relativeLayoutZoom = findViewById(R.id.relativeLayoutZoom);
        this.main_rel = findViewById(R.id.main_rel);
        this.linearLayoutAuto = findViewById(R.id.linearLayoutAuto);
        this.linearLayoutEraser = findViewById(R.id.linearLayoutEraser);
        this.lay_lasso_cut = findViewById(R.id.lay_lasso_cut);
        this.inside_cut_lay = findViewById(R.id.inside_cut_lay);
        this.outside_cut_lay = findViewById(R.id.outside_cut_lay);
        this.undo_btn = findViewById(R.id.imageViewUndo);
        this.redo_btn = findViewById(R.id.imageViewRedo);
        this.back_btn = findViewById(R.id.btn_back);
        this.save_btn = findViewById(R.id.save_image_btn);
        this.relativeLayoutBackground = findViewById(R.id.relativeLayoutBackground);
        this.imageViewBackgroundCover = findViewById(R.id.imageViewBackgroundCover);
        this.textViewBrushSize = findViewById(R.id.textViewBrushSize);
        this.textViewBrushOffset = findViewById(R.id.textViewBrushOffset);
        this.textViewAutoOffset= findViewById(R.id.textViewOffset);
        this.textViewExtractOffset = findViewById(R.id.textViewExtractOffset);
        this.back_btn.setOnClickListener(this);
        this.undo_btn.setOnClickListener(this);
        this.redo_btn.setOnClickListener(this);
        this.undo_btn.setEnabled(false);
        this.redo_btn.setEnabled(false);
        this.save_btn.setOnClickListener(this);
        this.relativeLayoutBackground.setOnClickListener(this);
        this.relativeLayoutEraser.setOnClickListener(this);
        this.relativeLayoutAuto.setOnClickListener(this);
        this.relativeLayoutExtract.setOnClickListener(this);
        this.relativeLayoutZoom.setOnClickListener(this);
        this.relativeLayoutRestore.setOnClickListener(this);
        this.inside_cut_lay.setOnClickListener(this);
        this.outside_cut_lay.setOnClickListener(this);
        this.seekBarBrushOffset =  findViewById(R.id.seekBarBrushOffset);
        this.seekBarOffset = findViewById(R.id.seekBarOffset);
        this.seekBarExtractOffset = findViewById(R.id.seekBarExtractOffset);
        this.seekBarBrushOffset.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (dv != null) {
                    dv.setOffset(i - 150);
                    dv.invalidate();
                    String value = String.valueOf(i);
                    textViewBrushOffset.setText(value);
                }
            }
        });
        this.seekBarOffset.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (dv != null) {
                    dv.setOffset(i - 150);
                    dv.invalidate();
                    String value = String.valueOf(i);
                    textViewAutoOffset.setText(value);
                }
            }
        });
        this.seekBarExtractOffset.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (dv != null) {
                    dv.setOffset(i - 150);
                    dv.invalidate();
                    String value = String.valueOf(i);
                    textViewExtractOffset.setText(value);
                }
            }
        });
        this.radius_seekbar = (SeekBar) findViewById(R.id.seekBarSize);
        this.radius_seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (dv != null) {
                    dv.setRadius(i + 2);
                    dv.invalidate();
                    String value = String.valueOf(i);
                    textViewBrushSize.setText(value);
                }
            }
        });
        this.seekBarThreshold = findViewById(R.id.seekBarThreshold);
        this.seekBarThreshold.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if (dv != null) {
                    dv.setThreshold(seekBar.getProgress() + 10);
                    dv.updateThreshHold();
                }
            }
        });
    }

    @SuppressLint({"WrongConstant"})
    public void onClick(View view) {
        if (this.dv != null || view.getId() == R.id.btn_back) {
            String str = "...";
            String str2 = "";
            switch (view.getId()) {
                case R.id.relativeLayoutAuto:
                    setSelected(R.id.relativeLayoutAuto);
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(2);
                    this.dv.invalidate();
                    return;
                case R.id.btn_back:
                    onBackPressed();
                    return;
                case R.id.relativeLayoutBackground:
                    changeBG();
                    return;
                case R.id.relativeLayoutEraser:
                    setSelected(R.id.relativeLayoutEraser);
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(1);
                    this.dv.invalidate();
                    return;
                case R.id.relativeLayoutExtract:
//                    this.offset_seekbar_lay.setVisibility(View.VISIBLE);
                    setSelected(R.id.relativeLayoutExtract);
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(3);
                    this.dv.invalidate();
                    return;
                case R.id.imageViewRedo:
                    StringBuilder sb = new StringBuilder();
                    sb.append(getString(R.string.redoing));
                    sb.append(str);
                    final ProgressDialog show = ProgressDialog.show(this, str2, sb.toString(), true);
                    show.setCancelable(false);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dv.redoChange();
                                    }
                                });
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            show.dismiss();
                        }
                    }).start();
                    return;
                case R.id.relativeLayoutRestore:
                    setSelected(R.id.relativeLayoutRestore);
                    this.dv.enableTouchClear(true);
                    this.main_rel.setOnTouchListener(null);
                    this.dv.setMODE(4);
                    this.dv.invalidate();
                    return;
                case R.id.imageViewUndo:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(getString(R.string.undoing));
                    sb2.append(str);
                    final ProgressDialog show2 = ProgressDialog.show(this, str2, sb2.toString(), true);
                    show2.setCancelable(false);
                    new Thread(new Runnable() {
                        public void run() {
                            try {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        dv.undoChange();
                                    }
                                });
                                Thread.sleep(500);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            show2.dismiss();
                        }
                    }).start();
                    return;
                case R.id.relativeLayoutZoom:
                    this.dv.enableTouchClear(false);
                    this.main_rel.setOnTouchListener(new MultiTouchListener());
                    setSelected(R.id.relativeLayoutZoom);
                    this.dv.setMODE(0);
                    this.dv.invalidate();
                    return;
                case R.id.inside_cut_lay:
                    this.dv.enableInsideCut(true);
                    this.inside_cut_lay.clearAnimation();
                    this.outside_cut_lay.clearAnimation();
                    return;
                case R.id.outside_cut_lay:
                    this.dv.enableInsideCut(false);
                    this.inside_cut_lay.clearAnimation();
                    this.outside_cut_lay.clearAnimation();
                    return;
                case R.id.save_image_btn:
                    bitmap = this.dv.getFinalBitmap();
                    if (bitmap != null) {
                        try {
                            int dpToPx = ImageUtils.dpToPx(StickerEraseActivity.this, 42.0f);
                            bitmap = ImageUtils.resizeBitmap(bitmap, orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx);
                            int i = dpToPx + dpToPx;
                            bitmap = Bitmap.createBitmap(bitmap, dpToPx, dpToPx, bitmap.getWidth() - i, bitmap.getHeight() - i);
                            bitmap = Bitmap.createScaledBitmap(bitmap, orgBitWidth, orgBitHeight, true);
                            bitmap = ImageUtils.bitmapmasking(orgBitmap, bitmap);

                            if (openFrom.equalsIgnoreCase(Constants.VALUE_OPEN_FROM_NEON)) {
                                NeonLayout.resultBitmap = bitmap;
                            } else if (openFrom.equalsIgnoreCase(Constants.VALUE_OPEN_FROM_DRIP)) {
                                DripLayout.resultBmp = bitmap;
                            } else if (openFrom.equalsIgnoreCase(Constants.VALUE_OPEN_FROM_MOTION)) {
                                MotionLayout.resultBmp = bitmap;
                            } else if (openFrom.equalsIgnoreCase(Constants.VALUE_OPEN_FROM_ART)) {
                                ArtLayout.resultBitmap = bitmap;
                            } else if (openFrom.equalsIgnoreCase(Constants.VALUE_OPEN_FROM_WING)) {
                                WingLayout.resultBmp = bitmap;
                            }
                            setResult(RESULT_OK);
                            finish();
                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();
                        }
                    } else {
                        finish();
                    }
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.import_img_warning), Toast.LENGTH_SHORT).show();
        }
    }
    /*
        public void onBackPressed() {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_unsaved_work);
            dialog.setCancelable(false);
            Window window = dialog.getWindow();
            window.setLayout(((SupportedClass.getWidth(StickerEraseActivity.this) / 100) * 90), LinearLayout.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.findViewById(R.id.no).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.findViewById(R.id.yes).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.show();
        }
    */
    private void changeBG() {
        int i = curBgType;
        if (i == 1) {
            curBgType = 2;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg1, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg1);
        } else if (i == 2) {
            curBgType = 3;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg2, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg2);
        } else if (i == 3) {
            curBgType = 4;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg3, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg3);
        } else if (i == 4) {
            curBgType = 5;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg4, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg4);
        } else if (i == 5) {
            curBgType = 6;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg5, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg5);
        } else if (i == 6) {
            curBgType = 1;
            this.imageViewBackgroundCover.setImageBitmap(null);
            this.imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(this, R.drawable.tbg, this.width, this.height));
            bgCircleBit = ImageUtils.getBgCircleBit(this, R.drawable.tbg);
        }
    }

    /*
    @SuppressLint({"WrongConstant"})
    private void showButtonsLayout(boolean z) {
        if (z) {
            if (this.rel_up_btns.getVisibility() != View.VISIBLE) {
                this.rel_up_btns.setVisibility(View.VISIBLE);
                this.rel_up_btns.startAnimation(this.animSlideUp);
                this.animSlideUp.setAnimationListener(new AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    @SuppressLint({"WrongConstant"})
                    public void onAnimationEnd(Animation animation) {
                        rel_down_btns.setVisibility(4);
                        if (isTutOpen) {
                            rel_arrow_up.setVisibility(View.GONE);
                            rel_arrow_up.clearAnimation();
                            rel_auto.setVisibility(View.VISIBLE);
                            rel_auto.startAnimation(scale_anim);
                            relativeLayoutAuto.startAnimation(scale_anim);
                        }
                    }
                });
            }
        } else if (!this.isTutOpen && this.rel_up_btns.getVisibility() == View.VISIBLE) {
            this.rel_up_btns.startAnimation(this.animSlideDown);
            this.animSlideDown.setAnimationListener(new AnimationListener() {
                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationStart(Animation animation) {
                    rel_down_btns.setVisibility(View.VISIBLE);
                }

                public void onAnimationEnd(Animation animation) {
                    rel_up_btns.setVisibility(View.GONE);
                }
            });
        }
    }
*/

    public void importImageFromUri() {
        this.showDialog = false;
        final ProgressDialog show = ProgressDialog.show(this, "", getResources().getString(R.string.importing_image), true);
        show.setCancelable(false);
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (b == null) {
                        showDialog = true;
                    } else {
                        orgBitmap = b.copy(b.getConfig(), true);
                        int dpToPx = ImageUtils.dpToPx(StickerEraseActivity.this, 42.0f);
                        StickerEraseActivity.orgBitWidth = b.getWidth();
                        StickerEraseActivity.orgBitHeight = b.getHeight();
                        Bitmap createBitmap = Bitmap.createBitmap(b.getWidth() + dpToPx + dpToPx, b.getHeight() + dpToPx + dpToPx, b.getConfig());
                        Canvas canvas = new Canvas(createBitmap);
                        canvas.drawColor(0);
                        float f = (float) dpToPx;
                        canvas.drawBitmap(b, f, f, null);
                        b = createBitmap;
                        if (b.getWidth() > width || b.getHeight() > height || (b.getWidth() < width && b.getHeight() < height)) {
                            b = ImageUtils.resizeBitmap(b, width, height);
                        }
                    }
                    Thread.sleep(1000);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    showDialog = true;
                    show.dismiss();
                } catch (Exception e2) {
                    e2.printStackTrace();
                    showDialog = true;
                    show.dismiss();
                }
                show.dismiss();
            }
        }).start();
        show.setOnDismissListener(new OnDismissListener() {
            @SuppressLint({"WrongConstant"})
            public void onDismiss(DialogInterface dialogInterface) {
                if (showDialog) {
                    StickerEraseActivity stickerRemoveActivity = StickerEraseActivity.this;
                    Toast.makeText(stickerRemoveActivity, stickerRemoveActivity.getResources().getString(R.string.import_error), Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                String str = "";
                ConstantsApp.rewid = str;
                ConstantsApp.uri = str;
                ConstantsApp.bitmapSticker = null;
                setImageBitmap();
            }
        });
    }

    @SuppressLint({"WrongConstant"})
    public void setImageBitmap() {
        this.dv = new EraseView(this);
        this.dv1 = new ImageView(this);
        this.dv.setImageBitmap(this.b);
        this.dv1.setImageBitmap(getGreenLayerBitmap(this.b));
        this.dv.enableTouchClear(false);
        this.dv.setMODE(0);
        this.dv.invalidate();
        this.seekBarBrushOffset.setProgress(225);
        this.radius_seekbar.setProgress(18);
        this.seekBarThreshold.setProgress(20);
        this.main_rel.removeAllViews();
        this.main_rel.setScaleX(1.0f);
        this.main_rel.setScaleY(1.0f);
        this.main_rel.addView(this.dv1);
        this.main_rel.addView(this.dv);
        this.dv.invalidate();
        this.dv1.setVisibility(View.GONE);
        this.dv.setUndoRedoListener(new EraseView.UndoRedoListener() {
            public void enableUndo(boolean z, int i) {
                if (z) {
                    StickerEraseActivity stickerRemoveActivity = StickerEraseActivity.this;
                    stickerRemoveActivity.setBGDrawable( i, undo_btn, R.drawable.ic_undo, z);
                    return;
                }
                StickerEraseActivity stickerRemoveActivity2 = StickerEraseActivity.this;
                stickerRemoveActivity2.setBGDrawable( i, undo_btn, R.drawable.ic_undo, z);
            }

            public void enableRedo(boolean z, int i) {
                if (z) {
                    StickerEraseActivity stickerRemoveActivity = StickerEraseActivity.this;
                    stickerRemoveActivity.setBGDrawable( i, redo_btn, R.drawable.ic_redo, z);
                    return;
                }
                StickerEraseActivity stickerRemoveActivity2 = StickerEraseActivity.this;
                stickerRemoveActivity2.setBGDrawable( i, redo_btn, R.drawable.ic_redo, z);
            }
        });
        this.b.recycle();
        this.dv.setActionListener(new EraseView.ActionListener() {
            public void onActionCompleted(final int i) {
                runOnUiThread(new Runnable() {
                    @SuppressLint({"WrongConstant"})
                    public void run() {
                        if (i == 5) {
                            //offset_seekbar_lay.setVisibility(View.GONE);
                        }
                    }
                });
            }

            public void onAction(final int i) {
                runOnUiThread(new Runnable() {
                    @SuppressLint({"WrongConstant"})
                    public void run() {
                        if (i == 0) {
                            relativeLayoutSeekBar.setVisibility(View.GONE);
                        }
                        if (i == 1) {
                            relativeLayoutSeekBar.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    public void setBGDrawable( int i, ImageView imageView, int i2, boolean z) {
        final ImageView imageView2 = imageView;
        final int i3 = i2;
        final boolean z2 = z;
        final int i4 = i;
        runOnUiThread(new Runnable() {
            public void run() {
                imageView2.setImageResource(i3);
                imageView2.setEnabled(z2);
            }
        });
    }

    public Bitmap getGreenLayerBitmap(Bitmap bitmap2) {
        Paint paint = new Paint();
        paint.setColor(-16711936);
        paint.setAlpha(80);
        int dpToPx = ImageUtils.dpToPx(this, 42.0f);
        Bitmap createBitmap = Bitmap.createBitmap(orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx, bitmap2.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(0);
        float f = (float) dpToPx;
        canvas.drawBitmap(this.orgBitmap, f, f, null);
        canvas.drawRect(f, f, (float) (orgBitWidth + dpToPx), (float) (orgBitHeight + dpToPx), paint);
        Bitmap createBitmap2 = Bitmap.createBitmap(orgBitWidth + dpToPx + dpToPx, orgBitHeight + dpToPx + dpToPx, bitmap2.getConfig());
        Canvas canvas2 = new Canvas(createBitmap2);
        canvas2.drawColor(0);
        canvas2.drawBitmap(this.orgBitmap, f, f, null);
        patternBMPshader = new BitmapShader(ImageUtils.resizeBitmap(createBitmap2, this.width, this.height), TileMode.REPEAT, TileMode.REPEAT);
        return ImageUtils.resizeBitmap(createBitmap, this.width, this.height);
    }

    @SuppressLint({"WrongConstant"})
    public void setSelected(int i) {
        if (i == R.id.relativeLayoutEraser) {
            this.seekBarBrushOffset.setProgress(this.dv.getOffset() + 150);
            this.linearLayoutEraser.setVisibility(View.VISIBLE);
            this.linearLayoutAuto.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
        }
        if (i == R.id.relativeLayoutAuto) {
            this.seekBarOffset.setProgress(this.dv.getOffset() + 150);
            this.linearLayoutEraser.setVisibility(View.GONE);
            this.linearLayoutAuto.setVisibility(View.VISIBLE);
            this.lay_lasso_cut.setVisibility(View.GONE);
        }
        if (i == R.id.relativeLayoutExtract) {
            this.seekBarExtractOffset.setProgress(this.dv.getOffset() + 150);
            this.linearLayoutEraser.setVisibility(View.GONE);
            this.linearLayoutAuto.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.VISIBLE);
        }
        if (i == R.id.relativeLayoutRestore) {
            this.seekBarBrushOffset.setProgress(this.dv.getOffset() + 150);
            this.linearLayoutEraser.setVisibility(View.VISIBLE);
            this.linearLayoutAuto.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
        }
        if (i == R.id.relativeLayoutZoom) {
            this.linearLayoutEraser.setVisibility(View.GONE);
            this.linearLayoutAuto.setVisibility(View.GONE);
            this.lay_lasso_cut.setVisibility(View.GONE);
        }
    }


    public void onDestroy() {
        Bitmap bitmap2 = this.b;
        if (bitmap2 != null) {
            bitmap2.recycle();
            this.b = null;
        }
        try {
            if (!isFinishing() && this.dv.pd != null && this.dv.pd.isShowing()) {
                this.dv.pd.dismiss();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        super.onDestroy();
    }
}
