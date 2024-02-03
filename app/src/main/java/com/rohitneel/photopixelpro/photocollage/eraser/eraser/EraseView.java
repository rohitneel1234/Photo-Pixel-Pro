package com.rohitneel.photopixelpro.photocollage.eraser.eraser;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import androidx.core.internal.view.SupportMenu;

import com.rohitneel.photopixelpro.photocollage.eraser.ImageUtils;
import com.rohitneel.photopixelpro.photocollage.eraser.StickerEraseActivity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Vector;

public class EraseView extends androidx.appcompat.widget.AppCompatImageView implements OnTouchListener {
    public static int MODE = 1;
    private static int pc = 0;
    public static float scale = 1.0f;
    Bitmap Bitmap2 = null;
    Bitmap Bitmap3 = null;
    Bitmap Bitmap4 = null;
    private int ERASE = 1;
    private int LASSO = 3;
    private int NONE = 0;
    private int REDRAW = 4;
    public int TARGET = 2;
    public int TOLERANCE = 30;
    float X = 100.0f;
    float Y = 100.0f;
    public ActionListener actionListener;
    public ArrayList<Integer> brushIndx = new ArrayList<>();
    public int brushSize = 18;
    private int brushSize1 = 18;
    public ArrayList<Boolean> brushTypeIndx = new ArrayList<>();
    Canvas c2;
    public ArrayList<Path> changesIndx = new ArrayList<>();
    Context ctx;
    public int curIndx = -1;
    private boolean drawLasso = false;
    private boolean drawOnLasso = true;
    Path drawPath = new Path();
    Paint erPaint = new Paint();
    Paint erPaint1 = new Paint();
    int erps = ImageUtils.dpToPx(getContext(), 2.0f);
    int height;

    public boolean insidCutEnable = true;
    public boolean isAutoRunning = false;
    boolean isMoved = false;
    private boolean isNewPath = false;
    public boolean isRectBrushEnable = false;
    public boolean isRotateEnabled = true;
    public boolean isScaleEnabled = true;
    private boolean isSelected = true;
    private boolean isTouched = false;
    public boolean isTranslateEnabled = true;
    Path lPath = new Path();

    public ArrayList<Boolean> lassoIndx = new ArrayList<>();
    private ScaleGestureDetector mScaleGestureDetector;
    public float maximumScale = 8.0f;
    public float minimumScale = 0.5f;

    public ArrayList<Integer> modeIndx = new ArrayList<>();
    private int offset = 200;
    private int offset1 = 200;
    private boolean onLeft = true;
    private Bitmap orgBit;
    Paint p = new Paint();
    Paint paint = new Paint();
    BitmapShader patternBMPshader;
    public ProgressDialog pd = null;
    public Point point;
    float sX;
    float sY;
    private int screenWidth;
    Path tPath = new Path();
    private int targetBrushSize = 18;
    private int targetBrushSize1 = 18;

    public UndoRedoListener undoRedoListener;

    public boolean updateOnly = false;

    public ArrayList<Vector<Point>> vectorPoints = new ArrayList<>();
    int width;

    public interface ActionListener {
        void onAction(int i);

        void onActionCompleted(int i);
    }

    private class AsyncTaskRunner extends AsyncTask<Void, Void, Bitmap> {
        int ch;
        Vector<Point> targetPoints;

        public AsyncTaskRunner(int i) {
            this.ch = i;
        }

        public Bitmap doInBackground(Void... voidArr) {
            if (this.ch != 0) {
                this.targetPoints = new Vector<>();
                EraseView eraseView = EraseView.this;
                eraseView.Bitmap3 = eraseView.Bitmap2.copy(Bitmap2.getConfig(), true);
                FloodFill(Bitmap2, new Point(point.x, point.y), this.ch, 0);
                changesIndx.add(curIndx + 1, new Path());
                brushIndx.add(curIndx + 1, Integer.valueOf(brushSize));
                modeIndx.add(curIndx + 1, Integer.valueOf(TARGET));
                brushTypeIndx.add(curIndx + 1, Boolean.valueOf(isRectBrushEnable));
                vectorPoints.add(curIndx + 1, new Vector(this.targetPoints));
                lassoIndx.add(curIndx + 1, Boolean.valueOf(insidCutEnable));
                EraseView eraseView2 = EraseView.this;
                eraseView2.curIndx = eraseView2.curIndx + 1;
                clearNextChanges();
                updateOnly = true;
                StringBuilder sb = new StringBuilder();
                sb.append("Time : ");
                sb.append(this.ch);
                sb.append("  ");
                sb.append(curIndx);
                sb.append("   ");
                sb.append(changesIndx.size());
                Log.i("testing", sb.toString());
            }
            return null;
        }

        private void FloodFill(Bitmap bitmap, Point point, int i, int i2) {
            if (i != 0) {
                int[] iArr = new int[(width * height)];
                bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
                LinkedList linkedList = new LinkedList();
                linkedList.add(point);
                while (linkedList.size() > 0) {
                    Point point2 = (Point) linkedList.poll();
                    if (compareColor(iArr[getIndex(point2.x, point2.y, width)], i)) {
                        Point point3 = new Point(point2.x + 1, point2.y);
                        while (point2.x > 0 && compareColor(iArr[getIndex(point2.x, point2.y, width)], i)) {
                            iArr[getIndex(point2.x, point2.y, width)] = i2;
                            this.targetPoints.add(new Point(point2.x, point2.y));
                            if (point2.y > 0 && compareColor(iArr[getIndex(point2.x, point2.y - 1, width)], i)) {
                                linkedList.add(new Point(point2.x, point2.y - 1));
                            }
                            if (point2.y < height && compareColor(iArr[getIndex(point2.x, point2.y + 1, width)], i)) {
                                linkedList.add(new Point(point2.x, point2.y + 1));
                            }
                            point2.x--;
                        }
                        if (point2.y > 0 && point2.y < height) {
                            iArr[getIndex(point2.x, point2.y, width)] = i2;
                            this.targetPoints.add(new Point(point2.x, point2.y));
                        }
                        while (point3.x < width && compareColor(iArr[getIndex(point3.x, point3.y, width)], i)) {
                            iArr[getIndex(point3.x, point3.y, width)] = i2;
                            this.targetPoints.add(new Point(point3.x, point3.y));
                            if (point3.y > 0 && compareColor(iArr[getIndex(point3.x, point3.y - 1, width)], i)) {
                                linkedList.add(new Point(point3.x, point3.y - 1));
                            }
                            if (point3.y < height && compareColor(iArr[getIndex(point3.x, point3.y + 1, width)], i)) {
                                linkedList.add(new Point(point3.x, point3.y + 1));
                            }
                            point3.x++;
                        }
                        if (point3.y > 0 && point3.y < height) {
                            iArr[getIndex(point3.x, point3.y, width)] = i2;
                            this.targetPoints.add(new Point(point3.x, point3.y));
                        }
                    }
                }
                bitmap.setPixels(iArr, 0, width, 0, 0, width, height);
            }
        }

        public boolean compareColor(int i, int i2) {
            if (!(i == 0 || i2 == 0)) {
                if (i == i2) {
                    return true;
                }
                return Math.abs(Color.red(i) - Color.red(i2)) <= TOLERANCE && Math.abs(Color.green(i) - Color.green(i2)) <= TOLERANCE && Math.abs(Color.blue(i) - Color.blue(i2)) <= TOLERANCE;
            }
            return false;
        }

        private void clearNextChanges() {
            int size = changesIndx.size();
            StringBuilder sb = new StringBuilder();
            sb.append(" Curindx ");
            sb.append(curIndx);
            sb.append(" Size ");
            sb.append(size);
            String str = "testings";
            Log.i(str, sb.toString());
            int access$000 = curIndx + 1;
            while (size > access$000) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" indx ");
                sb2.append(access$000);
                Log.i(str, sb2.toString());
                changesIndx.remove(access$000);
                brushIndx.remove(access$000);
                modeIndx.remove(access$000);
                brushTypeIndx.remove(access$000);
                vectorPoints.remove(access$000);
                lassoIndx.remove(access$000);
                size = changesIndx.size();
            }
            if (undoRedoListener != null) {
                undoRedoListener.enableUndo(true, curIndx + 1);
                undoRedoListener.enableRedo(false, modeIndx.size() - (curIndx + 1));
            }
            if (actionListener != null) {
                actionListener.onActionCompleted(EraseView.MODE);
            }
        }


        public void onPreExecute() {
            super.onPreExecute();
            EraseView eraseView = EraseView.this;
            eraseView.pd = new ProgressDialog(eraseView.getContext());
            pd.setMessage("Processing...");
            pd.setCancelable(false);
            pd.show();
        }


        public void onPostExecute(Bitmap bitmap) {
            pd.dismiss();
            EraseView eraseView = EraseView.this;
            eraseView.pd = null;
            eraseView.invalidate();
            isAutoRunning = false;
        }
    }

    private class AsyncTaskRunner1 extends AsyncTask<Void, Void, Bitmap> {
        int ch;
        Vector<Point> targetPoints;

        public AsyncTaskRunner1(int i) {
            this.ch = i;
        }


        public Bitmap doInBackground(Void... voidArr) {
            if (this.ch != 0) {
                this.targetPoints = new Vector<>();
                FloodFill(new Point(point.x, point.y), this.ch, 0);
                if (curIndx < 0) {
                    changesIndx.add(curIndx + 1, new Path());
                    brushIndx.add(curIndx + 1, Integer.valueOf(brushSize));
                    modeIndx.add(curIndx + 1, Integer.valueOf(TARGET));
                    brushTypeIndx.add(curIndx + 1, Boolean.valueOf(isRectBrushEnable));
                    vectorPoints.add(curIndx + 1, new Vector(this.targetPoints));
                    lassoIndx.add(curIndx + 1, Boolean.valueOf(insidCutEnable));
                    EraseView eraseView = EraseView.this;
                    eraseView.curIndx = eraseView.curIndx + 1;
                    clearNextChanges();
                } else if (((Integer) modeIndx.get(curIndx)).intValue() == TARGET && curIndx == modeIndx.size() - 1) {
                    vectorPoints.add(curIndx, new Vector(this.targetPoints));
                } else {
                    changesIndx.add(curIndx + 1, new Path());
                    brushIndx.add(curIndx + 1, Integer.valueOf(brushSize));
                    modeIndx.add(curIndx + 1, Integer.valueOf(TARGET));
                    brushTypeIndx.add(curIndx + 1, Boolean.valueOf(isRectBrushEnable));
                    vectorPoints.add(curIndx + 1, new Vector(this.targetPoints));
                    lassoIndx.add(curIndx + 1, Boolean.valueOf(insidCutEnable));
                    EraseView eraseView2 = EraseView.this;
                    eraseView2.curIndx = eraseView2.curIndx + 1;
                    clearNextChanges();
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Time : ");
                sb.append(this.ch);
                sb.append("  ");
                sb.append(curIndx);
                sb.append("   ");
                sb.append(changesIndx.size());
                Log.i("testing", sb.toString());
            }
            return null;
        }

        private void FloodFill(Point point, int i, int i2) {
            if (i != 0) {
                int[] iArr = new int[(width * height)];
                Bitmap3.getPixels(iArr, 0, width, 0, 0, width, height);
                LinkedList linkedList = new LinkedList();
                linkedList.add(point);
                while (linkedList.size() > 0) {
                    Point point2 = (Point) linkedList.poll();
                    if (compareColor(iArr[getIndex(point2.x, point2.y, width)], i)) {
                        Point point3 = new Point(point2.x + 1, point2.y);
                        while (point2.x > 0 && compareColor(iArr[getIndex(point2.x, point2.y, width)], i)) {
                            iArr[getIndex(point2.x, point2.y, width)] = i2;
                            this.targetPoints.add(new Point(point2.x, point2.y));
                            if (point2.y > 0 && compareColor(iArr[getIndex(point2.x, point2.y - 1, width)], i)) {
                                linkedList.add(new Point(point2.x, point2.y - 1));
                            }
                            if (point2.y < height && compareColor(iArr[getIndex(point2.x, point2.y + 1, width)], i)) {
                                linkedList.add(new Point(point2.x, point2.y + 1));
                            }
                            point2.x--;
                        }
                        if (point2.y > 0 && point2.y < height) {
                            iArr[getIndex(point2.x, point2.y, width)] = i2;
                            this.targetPoints.add(new Point(point2.x, point2.y));
                        }
                        while (point3.x < width && compareColor(iArr[getIndex(point3.x, point3.y, width)], i)) {
                            iArr[getIndex(point3.x, point3.y, width)] = i2;
                            this.targetPoints.add(new Point(point3.x, point3.y));
                            if (point3.y > 0 && compareColor(iArr[getIndex(point3.x, point3.y - 1, width)], i)) {
                                linkedList.add(new Point(point3.x, point3.y - 1));
                            }
                            if (point3.y < height && compareColor(iArr[getIndex(point3.x, point3.y + 1, width)], i)) {
                                linkedList.add(new Point(point3.x, point3.y + 1));
                            }
                            point3.x++;
                        }
                        if (point3.y > 0 && point3.y < height) {
                            iArr[getIndex(point3.x, point3.y, width)] = i2;
                            this.targetPoints.add(new Point(point3.x, point3.y));
                        }
                    }
                }
                Bitmap2.setPixels(iArr, 0, width, 0, 0, width, height);
            }
        }

        public boolean compareColor(int i, int i2) {
            if (!(i == 0 || i2 == 0)) {
                if (i == i2) {
                    return true;
                }
                return Math.abs(Color.red(i) - Color.red(i2)) <= TOLERANCE && Math.abs(Color.green(i) - Color.green(i2)) <= TOLERANCE && Math.abs(Color.blue(i) - Color.blue(i2)) <= TOLERANCE;
            }
            return false;
        }

        private void clearNextChanges() {
            int size = changesIndx.size();
            StringBuilder sb = new StringBuilder();
            sb.append(" Curindx ");
            sb.append(curIndx);
            sb.append(" Size ");
            sb.append(size);
            String str = "testings";
            Log.i(str, sb.toString());
            int access$000 = curIndx + 1;
            while (size > access$000) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" indx ");
                sb2.append(access$000);
                Log.i(str, sb2.toString());
                changesIndx.remove(access$000);
                brushIndx.remove(access$000);
                modeIndx.remove(access$000);
                brushTypeIndx.remove(access$000);
                vectorPoints.remove(access$000);
                lassoIndx.remove(access$000);
                size = changesIndx.size();
            }
            if (undoRedoListener != null) {
                undoRedoListener.enableUndo(true, curIndx + 1);
                undoRedoListener.enableRedo(false, modeIndx.size() - (curIndx + 1));
            }
        }


        public void onPreExecute() {
            super.onPreExecute();
            EraseView eraseView = EraseView.this;
            eraseView.pd = new ProgressDialog(eraseView.getContext());
            pd.setMessage("Processing...");
            pd.setCancelable(false);
            pd.show();
        }


        public void onPostExecute(Bitmap bitmap) {
            pd.dismiss();
            invalidate();
            isAutoRunning = false;
        }
    }

    private class ScaleGestureListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        private float mPivotX;
        private float mPivotY;
        private Vector2D mPrevSpanVector;

        private ScaleGestureListener() {
            this.mPrevSpanVector = new Vector2D();
        }

        public boolean onScaleBegin(View view, ScaleGestureDetector scaleGestureDetector) {
            this.mPivotX = scaleGestureDetector.getFocusX();
            this.mPivotY = scaleGestureDetector.getFocusY();
            this.mPrevSpanVector.set(scaleGestureDetector.getCurrentSpanVector());
            return true;
        }

        public boolean onScale(View view, ScaleGestureDetector scaleGestureDetector) {
            TransformInfo transformInfo = new TransformInfo();
            transformInfo.deltaScale = isScaleEnabled ? scaleGestureDetector.getScaleFactor() : 1.0f;
            float f = 0.0f;
            transformInfo.deltaAngle = isRotateEnabled ? Vector2D.getAngle(this.mPrevSpanVector, scaleGestureDetector.getCurrentSpanVector()) : 0.0f;
            transformInfo.deltaX = isTranslateEnabled ? scaleGestureDetector.getFocusX() - this.mPivotX : 0.0f;
            if (isTranslateEnabled) {
                f = scaleGestureDetector.getFocusY() - this.mPivotY;
            }
            transformInfo.deltaY = f;
            transformInfo.pivotX = this.mPivotX;
            transformInfo.pivotY = this.mPivotY;
            transformInfo.minimumScale = minimumScale;
            transformInfo.maximumScale = maximumScale;
            move(view, transformInfo);
            return false;
        }
    }

    private class TransformInfo {
        public float deltaAngle;
        public float deltaScale;
        public float deltaX;
        public float deltaY;
        public float maximumScale;
        public float minimumScale;
        public float pivotX;
        public float pivotY;

        private TransformInfo() {
        }
    }

    public interface UndoRedoListener {
        void enableRedo(boolean z, int i);

        void enableUndo(boolean z, int i);
    }

    public int getIndex(int i, int i2, int i3) {
        return i2 == 0 ? i : i + ((i2 - 1) * i3);
    }

    public float updatebrushsize(int i, float f) {
        return ((float) i) / f;
    }

    public void setUndoRedoListener(UndoRedoListener undoRedoListener2) {
        this.undoRedoListener = undoRedoListener2;
    }

    public void setActionListener(ActionListener actionListener2) {
        this.actionListener = actionListener2;
    }

    public EraseView(Context context) {
        super(context);
        initPaint(context);
    }

    public EraseView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initPaint(context);
    }

    private void initPaint(Context context) {
        MODE = 1;
        this.mScaleGestureDetector = new ScaleGestureDetector(new ScaleGestureListener());
        this.ctx = context;
        setFocusable(true);
        setFocusableInTouchMode(true);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        this.screenWidth = displayMetrics.widthPixels;
        this.brushSize = ImageUtils.dpToPx(getContext(), (float) this.brushSize);
        this.brushSize1 = ImageUtils.dpToPx(getContext(), (float) this.brushSize);
        this.targetBrushSize = ImageUtils.dpToPx(getContext(), 50.0f);
        this.targetBrushSize1 = ImageUtils.dpToPx(getContext(), 50.0f);
        this.paint.setAlpha(0);
        this.paint.setColor(0);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeJoin(Join.ROUND);
        this.paint.setStrokeCap(Cap.ROUND);
        this.paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(updatebrushsize(this.brushSize1, scale));
        this.erPaint = new Paint();
        this.erPaint.setAntiAlias(true);
        this.erPaint.setColor(SupportMenu.CATEGORY_MASK);
        this.erPaint.setAntiAlias(true);
        this.erPaint.setStyle(Style.STROKE);
        this.erPaint.setStrokeJoin(Join.MITER);
        this.erPaint.setStrokeWidth(updatebrushsize(this.erps, scale));
        this.erPaint1 = new Paint();
        this.erPaint1.setAntiAlias(true);
        this.erPaint1.setColor(SupportMenu.CATEGORY_MASK);
        this.erPaint1.setAntiAlias(true);
        this.erPaint1.setStyle(Style.STROKE);
        this.erPaint1.setStrokeJoin(Join.MITER);
        this.erPaint1.setStrokeWidth(updatebrushsize(this.erps, scale));
        this.erPaint1.setPathEffect(new DashPathEffect(new float[]{10.0f, 20.0f}, 0.0f));
    }

    public void setImageBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            if (this.orgBit == null) {
                this.orgBit = bitmap.copy(bitmap.getConfig(), true);
            }
            this.width = bitmap.getWidth();
            this.height = bitmap.getHeight();
            this.Bitmap2 = Bitmap.createBitmap(this.width, this.height, bitmap.getConfig());
            this.c2 = new Canvas();
            this.c2.setBitmap(this.Bitmap2);
            this.c2.drawBitmap(bitmap, 0.0f, 0.0f, null);
            boolean z = this.isSelected;
            if (z) {
                enableTouchClear(z);
            }
            super.setImageBitmap(this.Bitmap2);
        }
    }


    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.c2 != null) {
            if (!this.updateOnly && this.isTouched) {
                this.paint = getPaintByMode(MODE, this.brushSize, this.isRectBrushEnable);
                Path path = this.tPath;
                if (path != null) {
                    this.c2.drawPath(path, this.paint);
                }
                this.isTouched = false;
            }
            if (MODE == this.TARGET) {
                this.p = new Paint();
                this.p.setColor(SupportMenu.CATEGORY_MASK);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, scale));
                canvas.drawCircle(this.X, this.Y, (float) (this.targetBrushSize / 2), this.erPaint);
                canvas.drawCircle(this.X, this.Y + ((float) this.offset), updatebrushsize(ImageUtils.dpToPx(getContext(), 7.0f), scale), this.p);
                this.p.setStrokeWidth(updatebrushsize(ImageUtils.dpToPx(getContext(), 1.0f), scale));
                float f = this.X;
                int i = this.targetBrushSize;
                float f2 = f - ((float) (i / 2));
                float f3 = this.Y;
                canvas.drawLine(f2, f3, ((float) (i / 2)) + f, f3, this.p);
                float f4 = this.X;
                float f5 = this.Y;
                int i2 = this.targetBrushSize;
                canvas.drawLine(f4, f5 - ((float) (i2 / 2)), f4, ((float) (i2 / 2)) + f5, this.p);
                this.drawOnLasso = true;
            }
            if (MODE == this.LASSO) {
                this.p = new Paint();
                this.p.setColor(SupportMenu.CATEGORY_MASK);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, scale));
                canvas.drawCircle(this.X, this.Y, (float) (this.targetBrushSize / 2), this.erPaint);
                canvas.drawCircle(this.X, this.Y + ((float) this.offset), updatebrushsize(ImageUtils.dpToPx(getContext(), 7.0f), scale), this.p);
                this.p.setStrokeWidth(updatebrushsize(ImageUtils.dpToPx(getContext(), 1.0f), scale));
                float f6 = this.X;
                int i3 = this.targetBrushSize;
                float f7 = f6 - ((float) (i3 / 2));
                float f8 = this.Y;
                canvas.drawLine(f7, f8, ((float) (i3 / 2)) + f6, f8, this.p);
                float f9 = this.X;
                float f10 = this.Y;
                int i4 = this.targetBrushSize;
                canvas.drawLine(f9, f10 - ((float) (i4 / 2)), f9, ((float) (i4 / 2)) + f10, this.p);
                if (!this.drawOnLasso) {
                    this.erPaint1.setStrokeWidth(updatebrushsize(this.erps, scale));
                    canvas.drawPath(this.lPath, this.erPaint1);
                }
            }
            int i5 = MODE;
            if (i5 == this.ERASE || i5 == this.REDRAW) {
                this.p = new Paint();
                this.p.setColor(SupportMenu.CATEGORY_MASK);
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, scale));
                if (this.isRectBrushEnable) {
                    int i6 = this.brushSize / 2;
                    float f11 = this.X;
                    float f12 = (float) i6;
                    float f13 = f11 - f12;
                    float f14 = this.Y;
                    canvas.drawRect(f13, f14 - f12, f12 + f11, f12 + f14, this.erPaint);
                } else {
                    canvas.drawCircle(this.X, this.Y, (float) (this.brushSize / 2), this.erPaint);
                }
                canvas.drawCircle(this.X, this.Y + ((float) this.offset), updatebrushsize(ImageUtils.dpToPx(getContext(), 7.0f), scale), this.p);
            }
            this.updateOnly = false;
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (motionEvent.getPointerCount() == 1) {
            ActionListener actionListener2 = this.actionListener;
            if (actionListener2 != null) {
                actionListener2.onAction(motionEvent.getAction());
            }
            String str = "=";
            if (MODE == this.TARGET) {
                this.drawOnLasso = false;
                this.X = motionEvent.getX();
                this.Y = motionEvent.getY() - ((float) this.offset);
                if (action == 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str);
                    sb.append(motionEvent.getAction());
                    Log.e("TARGET ACTION_DOWN", sb.toString());
                    invalidate();
                } else if (action == 1) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(motionEvent.getAction());
                    Log.e("TARGET ACTION_UP", sb2.toString());
                    float f = this.X;
                    if (f >= 0.0f && this.Y >= 0.0f && f < ((float) this.Bitmap2.getWidth()) && this.Y < ((float) this.Bitmap2.getHeight())) {
                        this.point = new Point((int) this.X, (int) this.Y);
                        pc = this.Bitmap2.getPixel((int) this.X, (int) this.Y);
                        if (!this.isAutoRunning) {
                            this.isAutoRunning = true;
                            new AsyncTaskRunner(pc).execute(new Void[0]);
                        }
                    }
                    invalidate();
                } else if (action == 2) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(motionEvent.getAction());
                    Log.e("TARGET ACTION_MOVE", sb3.toString());
                    invalidate();
                }
            }
            if (MODE == this.LASSO) {
                this.X = motionEvent.getX();
                this.Y = motionEvent.getY() - ((float) this.offset);
                if (action == 0) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str);
                    sb4.append(motionEvent.getAction());
                    Log.e("LASSO ACTION_DOWN", sb4.toString());
                    this.isNewPath = true;
                    this.drawOnLasso = false;
                    this.sX = this.X;
                    this.sY = this.Y;
                    this.lPath = new Path();
                    this.lPath.moveTo(this.X, this.Y);
                    invalidate();
                } else if (action == 1) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(str);
                    sb5.append(motionEvent.getAction());
                    Log.e("LASSO ACTION_UP", sb5.toString());
                    this.lPath.lineTo(this.X, this.Y);
                    this.lPath.lineTo(this.sX, this.sY);
                    this.drawLasso = true;
                    invalidate();
                    ActionListener actionListener3 = this.actionListener;
                    if (actionListener3 != null) {
                        actionListener3.onActionCompleted(5);
                    }
                } else if (action != 2) {
                    return false;
                } else {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(str);
                    sb6.append(motionEvent.getAction());
                    Log.e("LASSO ACTION_MOVE", sb6.toString());
                    this.lPath.lineTo(this.X, this.Y);
                    invalidate();
                }
            }
            int i = MODE;
            if (i == this.ERASE || i == this.REDRAW) {
                int i2 = this.brushSize / 2;
                this.X = motionEvent.getX();
                this.Y = motionEvent.getY() - ((float) this.offset);
                this.isTouched = true;
                this.erPaint.setStrokeWidth(updatebrushsize(this.erps, scale));
                if (action == 0) {
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(str);
                    sb7.append(motionEvent.getAction());
                    Log.e("ERASE ACTION_DOWN", sb7.toString());
                    this.paint.setStrokeWidth((float) this.brushSize);
                    this.tPath = new Path();
                    if (this.isRectBrushEnable) {
                        Path path = this.tPath;
                        float f2 = this.X;
                        float f3 = (float) i2;
                        float f4 = f2 - f3;
                        float f5 = this.Y;
                        path.addRect(f4, f5 - f3, f2 + f3, f5 + f3, Direction.CW);
                    } else {
                        this.tPath.moveTo(this.X, this.Y);
                    }
                    invalidate();
                } else if (action == 1) {
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(str);
                    sb8.append(motionEvent.getAction());
                    Log.e("ERASE ACTION_UP", sb8.toString());
                    Path path2 = this.tPath;
                    if (path2 != null) {
                        if (this.isRectBrushEnable) {
                            float f6 = this.X;
                            float f7 = (float) i2;
                            float f8 = f6 - f7;
                            float f9 = this.Y;
                            path2.addRect(f8, f9 - f7, f6 + f7, f9 + f7, Direction.CW);
                        } else {
                            path2.lineTo(this.X, this.Y);
                        }
                        invalidate();
                        this.changesIndx.add(this.curIndx + 1, new Path(this.tPath));
                        this.brushIndx.add(this.curIndx + 1, Integer.valueOf(this.brushSize));
                        this.modeIndx.add(this.curIndx + 1, Integer.valueOf(MODE));
                        this.brushTypeIndx.add(this.curIndx + 1, Boolean.valueOf(this.isRectBrushEnable));
                        this.vectorPoints.add(this.curIndx + 1, null);
                        this.lassoIndx.add(this.curIndx + 1, Boolean.valueOf(this.insidCutEnable));
                        this.tPath.reset();
                        this.curIndx++;
                        clearNextChanges();
                        this.tPath = null;
                    }
                } else if (action != 2) {
                    return false;
                } else {
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(str);
                    sb9.append(motionEvent.getAction());
                    Log.e("ERASE ACTION_MOVE", sb9.toString());
                    if (this.tPath != null) {
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append(" In Action Move ");
                        sb10.append(this.X);
                        sb10.append(" ");
                        sb10.append(this.Y);
                        Log.e("movetest", sb10.toString());
                        if (this.isRectBrushEnable) {
                            Path path3 = this.tPath;
                            float f10 = this.X;
                            float f11 = (float) i2;
                            float f12 = f10 - f11;
                            float f13 = this.Y;
                            path3.addRect(f12, f13 - f11, f10 + f11, f13 + f11, Direction.CW);
                        } else {
                            this.tPath.lineTo(this.X, this.Y);
                        }
                        invalidate();
                        this.isMoved = true;
                    }
                }
            }
        }
        this.mScaleGestureDetector.onTouchEvent((View) view.getParent(), motionEvent);
        invalidate();
        return true;
    }

    public void move(View view, TransformInfo transformInfo) {
        computeRenderOffset(view, transformInfo.pivotX, transformInfo.pivotY);
        adjustTranslation(view, transformInfo.deltaX, transformInfo.deltaY);
        float max = Math.max(transformInfo.minimumScale, Math.min(transformInfo.maximumScale, view.getScaleX() * transformInfo.deltaScale));
        view.setScaleX(max);
        view.setScaleY(max);
        updateOnScale(max);
        invalidate();
    }

    private static void adjustTranslation(View view, float f, float f2) {
        float[] fArr = {f, f2};
        view.getMatrix().mapVectors(fArr);
        view.setTranslationX(view.getTranslationX() + fArr[0]);
        view.setTranslationY(view.getTranslationY() + fArr[1]);
    }

    private static void computeRenderOffset(View view, float f, float f2) {
        if (view.getPivotX() != f || view.getPivotY() != f2) {
            float[] fArr = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr);
            view.setPivotX(f);
            view.setPivotY(f2);
            float[] fArr2 = {0.0f, 0.0f};
            view.getMatrix().mapPoints(fArr2);
            float f3 = fArr2[1] - fArr[1];
            view.setTranslationX(view.getTranslationX() - (fArr2[0] - fArr[0]));
            view.setTranslationY(view.getTranslationY() - f3);
        }
    }

    private void clearNextChanges() {
        int size = this.changesIndx.size();
        StringBuilder sb = new StringBuilder();
        sb.append("ClearNextChange Curindx ");
        sb.append(this.curIndx);
        sb.append(" Size ");
        sb.append(size);
        String str = "testings";
        Log.i(str, sb.toString());
        int i = this.curIndx + 1;
        while (size > i) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" indx ");
            sb2.append(i);
            Log.i(str, sb2.toString());
            this.changesIndx.remove(i);
            this.brushIndx.remove(i);
            this.modeIndx.remove(i);
            this.brushTypeIndx.remove(i);
            this.vectorPoints.remove(i);
            this.lassoIndx.remove(i);
            size = this.changesIndx.size();
        }
        UndoRedoListener undoRedoListener2 = this.undoRedoListener;
        if (undoRedoListener2 != null) {
            undoRedoListener2.enableUndo(true, this.curIndx + 1);
            this.undoRedoListener.enableRedo(false, this.modeIndx.size() - (this.curIndx + 1));
        }
        ActionListener actionListener2 = this.actionListener;
        if (actionListener2 != null) {
            actionListener2.onActionCompleted(MODE);
        }
    }

    public void setMODE(int i) {
        MODE = i;
        if (i != this.TARGET) {
            Bitmap bitmap = this.Bitmap3;
            if (bitmap != null) {
                bitmap.recycle();
                this.Bitmap3 = null;
            }
        }
        if (i != this.LASSO) {
            this.drawOnLasso = true;
            this.drawLasso = false;
            Bitmap bitmap2 = this.Bitmap4;
            if (bitmap2 != null) {
                bitmap2.recycle();
                this.Bitmap4 = null;
            }
        }
    }

    private Paint getPaintByMode(int i, int i2, boolean z) {
        this.paint = new Paint();
        this.paint.setAlpha(0);
        if (z) {
            this.paint.setStyle(Style.FILL_AND_STROKE);
            this.paint.setStrokeJoin(Join.MITER);
            this.paint.setStrokeCap(Cap.SQUARE);
        } else {
            this.paint.setStyle(Style.STROKE);
            this.paint.setStrokeJoin(Join.ROUND);
            this.paint.setStrokeCap(Cap.ROUND);
            this.paint.setStrokeWidth((float) i2);
        }
        this.paint.setAntiAlias(true);
        if (i == this.ERASE) {
            this.paint.setColor(0);
            this.paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        }
        if (i == this.REDRAW) {
            this.paint.setColor(-1);
            this.patternBMPshader = StickerEraseActivity.patternBMPshader;
            this.paint.setShader(this.patternBMPshader);
        }
        return this.paint;
    }

    public void updateThreshHold() {
        if (this.Bitmap3 != null && !this.isAutoRunning) {
            this.isAutoRunning = true;
            new AsyncTaskRunner1(pc).execute(new Void[0]);
        }
    }

    public int getLastChangeMode() {
        int i = this.curIndx;
        if (i < 0) {
            return this.NONE;
        }
        return ((Integer) this.modeIndx.get(i)).intValue();
    }

    public void setOffset(int i) {
        this.offset1 = i;
        this.offset = (int) updatebrushsize(ImageUtils.dpToPx(this.ctx, (float) i), scale);
        this.updateOnly = true;
    }

    public int getOffset() {
        return this.offset1;
    }

    public void setRadius(int i) {
        this.brushSize1 = ImageUtils.dpToPx(getContext(), (float) i);
        this.brushSize = (int) updatebrushsize(this.brushSize1, scale);
        this.updateOnly = true;
    }

    public void updateOnScale(float f) {
        StringBuilder sb = new StringBuilder();
        sb.append("Scale ");
        sb.append(f);
        sb.append("  Brushsize  ");
        sb.append(this.brushSize);
        Log.i("testings", sb.toString());
        this.brushSize = (int) updatebrushsize(this.brushSize1, f);
        this.targetBrushSize = (int) updatebrushsize(this.targetBrushSize1, f);
        this.offset = (int) updatebrushsize(ImageUtils.dpToPx(this.ctx, (float) this.offset1), f);
    }

    public void setThreshold(int i) {
        this.TOLERANCE = i;
        if (this.curIndx >= 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(" Threshold ");
            sb.append(i);
            sb.append("  ");
            sb.append(((Integer) this.modeIndx.get(this.curIndx)).intValue() == this.TARGET);
            Log.i("testings", sb.toString());
        }
    }

    public boolean isTouchEnable() {
        return this.isSelected;
    }

    public void enableTouchClear(boolean z) {
        this.isSelected = z;
        if (z) {
            setOnTouchListener(this);
        } else {
            setOnTouchListener(null);
        }
    }

    public void enableInsideCut(boolean z) {
        this.insidCutEnable = z;
        if (this.drawLasso) {
            String str = "testings";
            Log.i(str, " draw lassso   on up ");
            if (this.isNewPath) {
                Bitmap bitmap = this.Bitmap2;
                this.Bitmap4 = bitmap.copy(bitmap.getConfig(), true);
                drawLassoPath(this.lPath, this.insidCutEnable);
                this.changesIndx.add(this.curIndx + 1, new Path(this.lPath));
                this.brushIndx.add(this.curIndx + 1, Integer.valueOf(this.brushSize));
                this.modeIndx.add(this.curIndx + 1, Integer.valueOf(MODE));
                this.brushTypeIndx.add(this.curIndx + 1, Boolean.valueOf(this.isRectBrushEnable));
                this.vectorPoints.add(this.curIndx + 1, null);
                this.lassoIndx.add(this.curIndx + 1, Boolean.valueOf(this.insidCutEnable));
                this.curIndx++;
                clearNextChanges();
                invalidate();
                this.isNewPath = false;
                return;
            }
            Log.i(str, " New PAth false ");
            setImageBitmap(this.Bitmap4);
            drawLassoPath(this.lPath, this.insidCutEnable);
            this.lassoIndx.add(this.curIndx, Boolean.valueOf(this.insidCutEnable));
            return;
        }
        Toast.makeText(this.ctx, "Please Draw a closed path!!!", Toast.LENGTH_SHORT).show();
    }

    public boolean isRectBrushEnable() {
        return this.isRectBrushEnable;
    }

    public void enableRectBrush(boolean z) {
        this.isRectBrushEnable = z;
        this.updateOnly = true;
    }

    public void undoChange() {
        this.drawLasso = false;
        setImageBitmap(this.orgBit);
        StringBuilder sb = new StringBuilder();
        sb.append("Performing UNDO Curindx ");
        sb.append(this.curIndx);
        String str = "  ";
        sb.append(str);
        sb.append(this.changesIndx.size());
        String str2 = "testings";
        Log.i(str2, sb.toString());
        int i = this.curIndx;
        if (i >= 0) {
            this.curIndx = i - 1;
            redrawCanvas();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" Curindx ");
            sb2.append(this.curIndx);
            sb2.append(str);
            sb2.append(this.changesIndx.size());
            Log.i(str2, sb2.toString());
            UndoRedoListener undoRedoListener2 = this.undoRedoListener;
            if (undoRedoListener2 != null) {
                undoRedoListener2.enableUndo(true, this.curIndx + 1);
                this.undoRedoListener.enableRedo(true, this.modeIndx.size() - (this.curIndx + 1));
            }
            int i2 = this.curIndx;
            if (i2 < 0) {
                UndoRedoListener undoRedoListener3 = this.undoRedoListener;
                if (undoRedoListener3 != null) {
                    undoRedoListener3.enableUndo(false, i2 + 1);
                }
            }
        }
    }

    public void redoChange() {
        this.drawLasso = false;
        StringBuilder sb = new StringBuilder();
        sb.append(this.curIndx + 1 >= this.changesIndx.size());
        sb.append(" Curindx ");
        sb.append(this.curIndx);
        sb.append(" ");
        sb.append(this.changesIndx.size());
        Log.i("testings", sb.toString());
        if (this.curIndx + 1 < this.changesIndx.size()) {
            setImageBitmap(this.orgBit);
            this.curIndx++;
            redrawCanvas();
            UndoRedoListener undoRedoListener2 = this.undoRedoListener;
            if (undoRedoListener2 != null) {
                undoRedoListener2.enableUndo(true, this.curIndx + 1);
                this.undoRedoListener.enableRedo(true, this.modeIndx.size() - (this.curIndx + 1));
            }
            if (this.curIndx + 1 >= this.changesIndx.size()) {
                UndoRedoListener undoRedoListener3 = this.undoRedoListener;
                if (undoRedoListener3 != null) {
                    undoRedoListener3.enableRedo(false, this.modeIndx.size() - (this.curIndx + 1));
                }
            }
        }
    }

    private void redrawCanvas() {
        for (int i = 0; i <= this.curIndx; i++) {
            if (((Integer) this.modeIndx.get(i)).intValue() == this.ERASE || ((Integer) this.modeIndx.get(i)).intValue() == this.REDRAW) {
                this.tPath = new Path((Path) this.changesIndx.get(i));
                this.paint = getPaintByMode(((Integer) this.modeIndx.get(i)).intValue(), ((Integer) this.brushIndx.get(i)).intValue(), ((Boolean) this.brushTypeIndx.get(i)).booleanValue());
                this.c2.drawPath(this.tPath, this.paint);
                this.tPath.reset();
            }
            if (((Integer) this.modeIndx.get(i)).intValue() == this.TARGET) {
                Vector vector = (Vector) this.vectorPoints.get(i);
                int i2 = this.width;
                int i3 = this.height;
                int[] iArr = new int[(i2 * i3)];
                this.Bitmap2.getPixels(iArr, 0, i2, 0, 0, i2, i3);
                for (int i4 = 0; i4 < vector.size(); i4++) {
                    Point point2 = (Point) vector.get(i4);
                    iArr[getIndex(point2.x, point2.y, this.width)] = 0;
                }
                Bitmap bitmap = this.Bitmap2;
                int i5 = this.width;
                bitmap.setPixels(iArr, 0, i5, 0, 0, i5, this.height);
            }
            if (((Integer) this.modeIndx.get(i)).intValue() == this.LASSO) {
                Log.i("testings", " onDraw Lassoo ");
                drawLassoPath(new Path((Path) this.changesIndx.get(i)), ((Boolean) this.lassoIndx.get(i)).booleanValue());
            }
        }
    }

    private void drawLassoPath(Path path, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append(z);
        sb.append(" New PAth false ");
        sb.append(path.isEmpty());
        Log.i("testings", sb.toString());
        if (z) {
            Paint paint2 = new Paint();
            paint2.setAntiAlias(true);
            paint2.setColor(0);
            paint2.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            this.c2.drawPath(path, paint2);
        } else {
            Bitmap bitmap = this.Bitmap2;
            Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
            new Canvas(copy).drawBitmap(this.Bitmap2, 0.0f, 0.0f, null);
            this.c2.drawColor(this.NONE, Mode.CLEAR);
            Paint paint3 = new Paint();
            paint3.setAntiAlias(true);
            this.c2.drawPath(path, paint3);
            paint3.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            this.c2.drawBitmap(copy, 0.0f, 0.0f, paint3);
            copy.recycle();
        }
        this.drawOnLasso = true;
    }

    public Bitmap getFinalBitmap() {
        Bitmap bitmap = this.Bitmap2;
        return bitmap.copy(bitmap.getConfig(), true);
    }

}
