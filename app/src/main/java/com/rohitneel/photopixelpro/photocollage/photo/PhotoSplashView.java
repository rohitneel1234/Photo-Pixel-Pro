package com.rohitneel.photopixelpro.photocollage.photo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.widget.ImageView;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.layout.SplashLayout;
import com.rohitneel.photopixelpro.photocollage.utils.FilePath;
import com.rohitneel.photopixelpro.photocollage.utils.SystemUtil;

import java.util.ArrayList;

public class PhotoSplashView extends ImageView {
    public static float resRatio;
    BitmapShader bitmapShader;
    Path brushPath;
    public Canvas canvas;
    Canvas canvasPreview;
    Paint circlePaint;
    Path circlePath;
    public int coloring = -1;
    Context context;
    PointF curr = new PointF();
    public int currentImageIndex = 0;
    boolean draw = false;
    Paint drawPaint;
    Path drawPath;
    public Bitmap drawingBitmap;
    Rect dstRect;
    PointF last = new PointF();
    Paint logPaintColor;
    Paint logPaintGray;
    float[] m;
    ScaleGestureDetector mScaleDetector;
    Matrix matrix;
    float maxScale = 5.0f;
    float minScale = 1.0f;
    public int mode = 0;
    int oldMeasuredHeight;
    int oldMeasuredWidth;
    float oldX = 0.0f;
    float oldY = 0.0f;
    boolean onMeasureCalled = false;
    public int opacity = 240;
    protected float origHeight;
    protected float origWidth;
    int pCount1 = -1;
    int pCount2 = -1;
    ArrayList<PointF> pathPoints;
    public boolean prViewDefaultPosition;
    Paint previewPaint;
    public float radius = 150.0f;
    public float saveScale = 1.0f;
    public Bitmap splashBitmap;
    PointF start = new PointF();
    Paint tempPaint;
    Bitmap tempPreviewBitmap;
    int viewHeight;
    int viewWidth;
    float x;
    float y;

    private class ScaleListener extends SimpleOnScaleGestureListener {
        private ScaleListener() {
        }

        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            if (PhotoSplashView.this.mode == 1 || PhotoSplashView.this.mode == 3) {
                PhotoSplashView.this.mode = 3;
            } else {
                PhotoSplashView.this.mode = 2;
            }
            PhotoSplashView.this.draw = false;
            return true;
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float scaleFactor = scaleGestureDetector.getScaleFactor();
            float f = PhotoSplashView.this.saveScale;
            PhotoSplashView.this.saveScale *= scaleFactor;
            if (PhotoSplashView.this.saveScale > PhotoSplashView.this.maxScale) {
                PhotoSplashView touchImageView = PhotoSplashView.this;
                touchImageView.saveScale = touchImageView.maxScale;
                scaleFactor = PhotoSplashView.this.maxScale / f;
            } else {
                float f2 = PhotoSplashView.this.saveScale;
                float f3 = PhotoSplashView.this.minScale;
            }
            if (PhotoSplashView.this.origWidth * PhotoSplashView.this.saveScale <= ((float) PhotoSplashView.this.viewWidth) || PhotoSplashView.this.origHeight * PhotoSplashView.this.saveScale <= ((float) PhotoSplashView.this.viewHeight)) {
                PhotoSplashView.this.matrix.postScale(scaleFactor, scaleFactor, (float) (PhotoSplashView.this.viewWidth / 2), (float) (PhotoSplashView.this.viewHeight / 2));
            } else {
                PhotoSplashView.this.matrix.postScale(scaleFactor, scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
            }
            PhotoSplashView.this.matrix.getValues(PhotoSplashView.this.m);
            return true;
        }

        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
            PhotoSplashView.this.radius = ((float) (SplashLayout.seekBarSize.getProgress() + 10)) / PhotoSplashView.this.saveScale;
            PhotoSplashView.this.updatePreviewPaint();
        }
    }


    public float getFixTrans(float f, float f2, float f3) {
        float f4;
        float f5;
        if (f3 <= f2) {
            f4 = f2 - f3;
            f5 = 0.0f;
        } else {
            f5 = f2 - f3;
            f4 = 0.0f;
        }
        if (f < f5) {
            return (-f) + f5;
        }
        if (f > f4) {
            return (-f) + f4;
        }
        return 0.0f;
    }

    public PhotoSplashView(Context context2) {
        super(context2);
        this.context = context2;
        sharedConstructing(context2);
        this.prViewDefaultPosition = true;
        setDrawingCacheEnabled(true);
    }

    public PhotoSplashView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        this.context = context2;
        sharedConstructing(context2);
        this.prViewDefaultPosition = true;
        setDrawingCacheEnabled(true);
    }

    @SuppressLint("WrongConstant")
    public void initDrawing() {
        this.splashBitmap = SplashLayout.colorBitmap;
        this.drawingBitmap = Bitmap.createBitmap(SplashLayout.grayBitmap);
        setImageBitmap(this.drawingBitmap);
        this.canvas = new Canvas(this.drawingBitmap);
        this.circlePath = new Path();
        this.drawPath = new Path();
        this.brushPath = new Path();
        this.circlePaint = new Paint();
        this.circlePaint.setAntiAlias(true);
        this.circlePaint.setDither(true);
        this.circlePaint.setColor(getContext().getResources().getColor(R.color.colorAccent));
        this.circlePaint.setStrokeWidth((float) SystemUtil.dpToPx(getContext(), 2));
        this.circlePaint.setStyle(Paint.Style.STROKE);
        this.drawPaint = new Paint(1);
        this.drawPaint.setStyle(Style.STROKE);
        this.drawPaint.setStrokeWidth(this.radius);
        this.drawPaint.setStrokeCap(Cap.ROUND);
        this.drawPaint.setStrokeJoin(Join.ROUND);
        setLayerType(1, null);
        this.tempPaint = new Paint();
        this.tempPaint.setStyle(Style.FILL);
        this.tempPaint.setColor(-1);
        this.previewPaint = new Paint();
        this.previewPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        this.tempPreviewBitmap = Bitmap.createBitmap(100, 100, Config.ARGB_8888);
        this.canvasPreview = new Canvas(this.tempPreviewBitmap);
        this.dstRect = new Rect(0, 0, 100, 100);
        this.logPaintGray = new Paint(this.drawPaint);
        this.logPaintGray.setShader(new BitmapShader(SplashLayout.grayBitmap, TileMode.CLAMP, TileMode.CLAMP));
        this.bitmapShader = new BitmapShader(this.splashBitmap, TileMode.CLAMP, TileMode.CLAMP);
        this.drawPaint.setShader(this.bitmapShader);
        this.logPaintColor = new Paint(this.drawPaint);
    }

    public void updatePaintBrush() {
        try {
            this.drawPaint.setStrokeWidth(this.radius * resRatio);
            this.drawPaint.setAlpha(this.opacity);
        } catch (Exception unused) {
        }
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        updatePreviewPaint();
    }

    public void changeShaderBitmap() {
        this.bitmapShader = new BitmapShader(this.splashBitmap, TileMode.CLAMP, TileMode.CLAMP);
        this.drawPaint.setShader(this.bitmapShader);
        updatePreviewPaint();
    }

    public void updatePreviewPaint() {
        if (SplashLayout.colorBitmap.getWidth() > SplashLayout.colorBitmap.getHeight()) {
            resRatio = ((float) SplashLayout.displayWidth) / ((float) SplashLayout.colorBitmap.getWidth());
            resRatio *= this.saveScale;
        } else {
            resRatio = this.origHeight / ((float) SplashLayout.colorBitmap.getHeight());
            resRatio *= this.saveScale;
        }
        this.drawPaint.setStrokeWidth(this.radius * resRatio);
        this.drawPaint.setMaskFilter(new BlurMaskFilter(resRatio * 15.0f, Blur.NORMAL));
        this.drawPaint.getShader().setLocalMatrix(this.matrix);
    }

    private void sharedConstructing(Context context2) {
        super.setClickable(true);
        this.context = context2;
        this.mScaleDetector = new ScaleGestureDetector(context2, new ScaleListener());
        this.matrix = new Matrix();
        this.m = new float[9];
        setImageMatrix(this.matrix);
        setScaleType(ScaleType.MATRIX);
        setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                PhotoSplashView.this.mScaleDetector.onTouchEvent(motionEvent);
                PhotoSplashView.this.pCount2 = motionEvent.getPointerCount();
                PhotoSplashView.this.curr = new PointF(motionEvent.getX(), motionEvent.getY());
                PhotoSplashView splashView1 = PhotoSplashView.this;
                splashView1.x = (splashView1.curr.x - PhotoSplashView.this.m[2]) / PhotoSplashView.this.m[0];
                PhotoSplashView splashView2 = PhotoSplashView.this;
                splashView2.y = (splashView2.curr.y - PhotoSplashView.this.m[5]) / PhotoSplashView.this.m[4];
                int action = motionEvent.getAction();
                if (action != 6) {
                    if (action == 0) {
                        PhotoSplashView.this.drawPaint.setStrokeWidth(PhotoSplashView.this.radius * PhotoSplashView.resRatio);
                        PhotoSplashView.this.drawPaint.setMaskFilter(new BlurMaskFilter(PhotoSplashView.resRatio * 15.0f, Blur.NORMAL));
                        PhotoSplashView.this.drawPaint.getShader().setLocalMatrix(PhotoSplashView.this.matrix);
                        PhotoSplashView splashView = PhotoSplashView.this;
                        splashView.oldX = 0.0f;
                        splashView.oldY = 0.0f;
                        splashView.last.set(PhotoSplashView.this.curr);
                        PhotoSplashView.this.start.set(PhotoSplashView.this.last);
                        if (!(PhotoSplashView.this.mode == 1 || PhotoSplashView.this.mode == 3)) {
                            PhotoSplashView.this.draw = true;
                        }
                        PhotoSplashView.this.circlePath.reset();
                        PhotoSplashView.this.circlePath.moveTo(PhotoSplashView.this.curr.x, PhotoSplashView.this.curr.y);
                        PhotoSplashView.this.circlePath.addCircle(PhotoSplashView.this.curr.x, PhotoSplashView.this.curr.y, (PhotoSplashView.this.radius * PhotoSplashView.resRatio) / 2.0f, Direction.CW);
                        PhotoSplashView.this.pathPoints = new ArrayList<>();
                        PhotoSplashView.this.pathPoints.add(new PointF(PhotoSplashView.this.x, PhotoSplashView.this.y));
                        PhotoSplashView.this.drawPath.moveTo(PhotoSplashView.this.x, PhotoSplashView.this.y);
                        PhotoSplashView.this.brushPath.moveTo(PhotoSplashView.this.curr.x, PhotoSplashView.this.curr.y);
                    } else if (action == 1) {
                        if (PhotoSplashView.this.mode == 1) {
                            PhotoSplashView.this.matrix.getValues(PhotoSplashView.this.m);
                        }
                        int abs = (int) Math.abs(PhotoSplashView.this.curr.y - PhotoSplashView.this.start.y);
                        if (((int) Math.abs(PhotoSplashView.this.curr.x - PhotoSplashView.this.start.x)) < 3 && abs < 3) {
                            PhotoSplashView.this.performClick();
                        }
                        if (PhotoSplashView.this.draw) {
                            PhotoSplashView.this.drawPaint.setStrokeWidth(PhotoSplashView.this.radius);
                            PhotoSplashView.this.drawPaint.setMaskFilter(new BlurMaskFilter(15.0f, Blur.NORMAL));
                            PhotoSplashView.this.drawPaint.getShader().setLocalMatrix(new Matrix());
                            PhotoSplashView.this.canvas.drawPath(PhotoSplashView.this.drawPath, PhotoSplashView.this.drawPaint);
                        }
                        SplashLayout.vector.add(new FilePath(PhotoSplashView.this.pathPoints, PhotoSplashView.this.coloring, PhotoSplashView.this.radius));
                        PhotoSplashView.this.circlePath.reset();
                        PhotoSplashView.this.drawPath.reset();
                        PhotoSplashView.this.brushPath.reset();
                        PhotoSplashView.this.draw = false;
                    } else if (action == 2) {
                        if (PhotoSplashView.this.mode == 1 || PhotoSplashView.this.mode == 3 || !PhotoSplashView.this.draw) {
                            if (PhotoSplashView.this.pCount1 == 1 && PhotoSplashView.this.pCount2 == 1) {
                                PhotoSplashView.this.matrix.postTranslate(PhotoSplashView.this.curr.x - PhotoSplashView.this.last.x, PhotoSplashView.this.curr.y - PhotoSplashView.this.last.y);
                            }
                            PhotoSplashView.this.last.set(PhotoSplashView.this.curr.x, PhotoSplashView.this.curr.y);
                        } else {
                            PhotoSplashView.this.circlePath.reset();
                            PhotoSplashView.this.circlePath.moveTo(PhotoSplashView.this.curr.x, PhotoSplashView.this.curr.y);
                            PhotoSplashView.this.circlePath.addCircle(PhotoSplashView.this.curr.x, PhotoSplashView.this.curr.y, (PhotoSplashView.this.radius * PhotoSplashView.resRatio) / 2.0f, Direction.CW);
                            PhotoSplashView.this.pathPoints.add(new PointF(PhotoSplashView.this.x, PhotoSplashView.this.y));
                            PhotoSplashView.this.drawPath.lineTo(PhotoSplashView.this.x, PhotoSplashView.this.y);
                            PhotoSplashView.this.brushPath.lineTo(PhotoSplashView.this.curr.x, PhotoSplashView.this.curr.y);
                            PhotoSplashView.this.showBoxPreview();
                            float f = 0;
                            if ((PhotoSplashView.this.curr.x > f || PhotoSplashView.this.curr.y > f || !PhotoSplashView.this.prViewDefaultPosition) && PhotoSplashView.this.curr.x <= f && PhotoSplashView.this.curr.y >= ((float) (PhotoSplashView.this.viewHeight )) && !PhotoSplashView.this.prViewDefaultPosition) {
                                PhotoSplashView touchImageView4 = PhotoSplashView.this;
                                touchImageView4.prViewDefaultPosition = true;

                            } else {
                                PhotoSplashView touchImageView5 = PhotoSplashView.this;
                                touchImageView5.prViewDefaultPosition = false;

                            }
                        }
                    }
                } else if (PhotoSplashView.this.mode == 2) {
                    PhotoSplashView.this.mode = 0;
                }
                PhotoSplashView touchImageView6 = PhotoSplashView.this;
                touchImageView6.pCount1 = touchImageView6.pCount2;
                PhotoSplashView touchImageView7 = PhotoSplashView.this;
                touchImageView7.setImageMatrix(touchImageView7.matrix);
                PhotoSplashView.this.invalidate();
                return true;
            }
        });
    }


    public void updateRefMetrix() {
        this.matrix.getValues(this.m);
    }


    public void showBoxPreview() {
        buildDrawingCache();
        try {
            Bitmap createBitmap = Bitmap.createBitmap(getDrawingCache());
            this.canvasPreview.drawRect(this.dstRect, this.tempPaint);
            this.canvasPreview.drawBitmap(createBitmap, new Rect(((int) this.curr.x) - 100, ((int) this.curr.y) - 100, ((int) this.curr.x) + 100, ((int) this.curr.y) + 100), this.dstRect, this.previewPaint);
            destroyDrawingCache();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void onDraw(Canvas canvas2) {
        float[] fArr = new float[9];
        this.matrix.getValues(fArr);
        int i = (int) fArr[2];
        int i2 = (int) fArr[5];
        super.onDraw(canvas2);
        float f = (float) i2;
        float f2 = this.origHeight;
        float f3 = this.saveScale;
        float f4 = (f2 * f3) + f;
        if (i2 < 0) {
            float f5 = (float) i;
            float f6 = (this.origWidth * f3) + f5;
            int i3 = this.viewHeight;
            if (f4 > ((float) i3)) {
                f4 = (float) i3;
            }
            canvas2.clipRect(f5, 0.0f, f6, f4);
        } else {
            float f7 = (float) i;
            float f8 = (this.origWidth * f3) + f7;
            int i4 = this.viewHeight;
            if (f4 > ((float) i4)) {
                f4 = (float) i4;
            }
            canvas2.clipRect(f7, f, f8, f4);
        }
        if (this.draw) {
            canvas2.drawPath(this.brushPath, this.drawPaint);
            canvas2.drawPath(this.circlePath, this.circlePaint);
        }
    }


    public void fixTrans() {
        this.matrix.getValues(this.m);
        float[] fArr = this.m;
        float f = fArr[2];
        float f2 = fArr[5];
        float fixTrans = getFixTrans(f, (float) this.viewWidth, this.origWidth * this.saveScale);
        float fixTrans2 = getFixTrans(f2, (float) this.viewHeight, this.origHeight * this.saveScale);
        if (!(fixTrans == 0.0f && fixTrans2 == 0.0f)) {
            this.matrix.postTranslate(fixTrans, fixTrans2);
        }
        this.matrix.getValues(this.m);
        updatePreviewPaint();
    }


    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (!this.onMeasureCalled) {
            Log.wtf("OnMeasured Call :", "OnMeasured Call");
            this.viewWidth = MeasureSpec.getSize(i);
            this.viewHeight = MeasureSpec.getSize(i2);
            int i3 = this.oldMeasuredHeight;
            if (i3 != this.viewWidth || i3 != this.viewHeight) {
                int i4 = this.viewWidth;
                if (i4 != 0) {
                    int i5 = this.viewHeight;
                    if (i5 != 0) {
                        this.oldMeasuredHeight = i5;
                        this.oldMeasuredWidth = i4;
                        if (this.saveScale == 1.0f) {
                            fitScreen();
                        }
                        this.onMeasureCalled = true;
                    }
                }
            }
        }
    }


    public void fitScreen() {
        Drawable drawable = getDrawable();
        if (drawable != null && drawable.getIntrinsicWidth() != 0 && drawable.getIntrinsicHeight() != 0) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            StringBuilder sb = new StringBuilder();
            sb.append("bmWidth: ");
            sb.append(intrinsicWidth);
            sb.append(" bmHeight : ");
            sb.append(intrinsicHeight);
            Log.d("bmSize", sb.toString());
            float f = (float) intrinsicWidth;
            float f2 = (float) intrinsicHeight;
            float min = Math.min(((float) this.viewWidth) / f, ((float) this.viewHeight) / f2);
            this.matrix.setScale(min, min);
            float f3 = (((float) this.viewHeight) - (f2 * min)) / 2.0f;
            float f4 = (((float) this.viewWidth) - (min * f)) / 2.0f;
            this.matrix.postTranslate(f4, f3);
            this.origWidth = ((float) this.viewWidth) - (f4 * 2.0f);
            this.origHeight = ((float) this.viewHeight) - (f3 * 2.0f);
            setImageMatrix(this.matrix);
            this.matrix.getValues(this.m);
            fixTrans();
        }
    }
}
