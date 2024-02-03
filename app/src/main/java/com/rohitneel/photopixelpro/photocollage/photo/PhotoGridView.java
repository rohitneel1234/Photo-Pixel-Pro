package com.rohitneel.photopixelpro.photocollage.photo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoArea;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoGrid;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayoutParser;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;
import com.rohitneel.photopixelpro.photocollage.utils.MatrixUtils;
import com.steelkiwi.cropiwa.AspectRatio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoGridView extends PhotoStickerView {
    private static final String TAG = "QueShotGridView";
    private Map<PhotoArea, PhotoGrid> queShotAreaQueShotGridMap;
    private AspectRatio aspectRatio;
    private int backgroundResource;
    private RectF rectF;
    private boolean canDrag;
    private boolean canMoveLine;

    public boolean canSwap;
    private boolean canZoom;

    public ActionMode actionMode;
    private float downX;
    private float downY;
    private int duration;
    private int handleBarColor;
    private Paint handleBarPaint;
    private PhotoLine quShotLine;

    public PhotoGrid queShotGrid;
    private PhotoLayout.Info info;
    private int lineColor;
    private Paint linePaint;
    private int lineSize;
    private PointF midPoint;
    private List<PhotoGrid> queShotGridList;
    private boolean needDrawLine;
    private boolean needDrawOuterLine;
    private boolean resetQueShotMatrix;

    public onQueShotSelectedListener onQueShotSelectedListener;
    private onQueShotUnSelectedListener onQueShotUnSelectedListener;
    private float collagePadding;
    private float collageRadian;
    private float previousDistance;

    public PhotoGrid prevHandlingQueShotGrid;
    private PhotoLayout queShotLayout;

    public List<PhotoGrid> queShotGrids;
    private boolean quickMode;
    private PhotoGrid replaceQuShotGrid;
    private Paint selectedAreaPaint;
    private int selectedLineColor;
    private Runnable switchToSwapAction;
    private boolean touchEnable;

    private enum ActionMode {
        NONE, DRAG, ZOOM, MOVE, SWAP
    }

    public interface onQueShotSelectedListener {
        void onQuShotSelected(PhotoGrid quShotCollage, int i);
    }

    public interface onQueShotUnSelectedListener {
        void onQuShotUnSelected();
    }

    public PhotoGridView(Context context) {
        this(context, null);
    }

    public PhotoGridView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public PhotoGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.actionMode = ActionMode.NONE;
        this.queShotGrids = new ArrayList();
        this.queShotGridList = new ArrayList();
        this.queShotAreaQueShotGridMap = new HashMap();
        this.touchEnable = true;
        this.resetQueShotMatrix = true;
        this.quickMode = false;
        this.canDrag = true;
        this.canMoveLine = true;
        this.canZoom = true;
        this.canSwap = true;
        this.switchToSwapAction = new Runnable() {
            public void run() {
                if (PhotoGridView.this.canSwap) {
                    PhotoGridView.this.actionMode = ActionMode.SWAP;
                    PhotoGridView.this.invalidate();
                }
            }
        };
        init(context, attributeSet);
    }

    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attributeSet) {
        int[] PuzzleView = {R.attr.animation_duration, R.attr.handle_bar_color, R.attr.line_color, R.attr.line_size, R.attr.need_draw_line, R.attr.need_draw_outer_line, R.attr.piece_padding, R.attr.radian, R.attr.selected_line_color};
        TypedArray styledAttributes = context.obtainStyledAttributes(attributeSet, PuzzleView);
        this.lineSize = styledAttributes.getInt(3, 4);
        this.lineColor = styledAttributes.getColor(2, -1);
        this.selectedLineColor = styledAttributes.getColor(8, Color.parseColor("#99BBFB"));
        this.handleBarColor = styledAttributes.getColor(1, Color.parseColor("#99BBFB"));
        this.collagePadding = (float) styledAttributes.getDimensionPixelSize(6, 0);
        this.needDrawLine = styledAttributes.getBoolean(4, true);
        this.needDrawOuterLine = styledAttributes.getBoolean(5, true);
        this.duration = styledAttributes.getInt(0, 300);
        this.collageRadian = styledAttributes.getFloat(7, 0.0f);
        styledAttributes.recycle();
        this.rectF = new RectF();
        this.linePaint = new Paint();
        this.linePaint.setAntiAlias(true);
        this.linePaint.setColor(this.lineColor);
        this.linePaint.setStrokeWidth((float) this.lineSize);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setStrokeJoin(Paint.Join.ROUND);
        this.linePaint.setStrokeCap(Paint.Cap.SQUARE);
        this.selectedAreaPaint = new Paint();
        this.selectedAreaPaint.setAntiAlias(true);
        this.selectedAreaPaint.setStyle(Paint.Style.STROKE);
        this.selectedAreaPaint.setStrokeJoin(Paint.Join.ROUND);
        this.selectedAreaPaint.setStrokeCap(Paint.Cap.ROUND);
        this.selectedAreaPaint.setColor(this.selectedLineColor);
        this.selectedAreaPaint.setStrokeWidth((float) this.lineSize);
        this.handleBarPaint = new Paint();
        this.handleBarPaint.setAntiAlias(true);
        this.handleBarPaint.setStyle(Paint.Style.FILL);
        this.handleBarPaint.setColor(this.handleBarColor);
        this.handleBarPaint.setStrokeWidth((float) (this.lineSize * 3));
        this.midPoint = new PointF();
    }


    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        resetCollageBounds();
        this.queShotAreaQueShotGridMap.clear();
        if (this.queShotGrids.size() != 0) {
            for (int i5 = 0; i5 < this.queShotGrids.size(); i5++) {
                PhotoGrid puzzlePiece = this.queShotGrids.get(i5);
                PhotoArea area = this.queShotLayout.getArea(i5);
                puzzlePiece.setArea(area);
                this.queShotAreaQueShotGridMap.put(area, puzzlePiece);
                if (this.resetQueShotMatrix) {
                    puzzlePiece.set(MatrixUtils.generateMatrix(puzzlePiece, 0.0f));
                } else {
                    puzzlePiece.fillArea(this, true);
                }
            }
        }
        invalidate();
    }

    public AspectRatio getAspectRatio() {
        return this.aspectRatio;
    }

    public void setAspectRatio(AspectRatio aspectRatio2) {
        this.aspectRatio = aspectRatio2;
    }

    private void resetCollageBounds() {
        this.rectF.left = (float) getPaddingLeft();
        this.rectF.top = (float) getPaddingTop();
        this.rectF.right = (float) (getWidth() - getPaddingRight());
        this.rectF.bottom = (float) (getHeight() - getPaddingBottom());
        if (this.queShotLayout != null) {
            this.queShotLayout.reset();
            this.queShotLayout.setOuterBounds(this.rectF);
            this.queShotLayout.layout();
            this.queShotLayout.setPadding(this.collagePadding);
            this.queShotLayout.setRadian(this.collageRadian);
            if (this.info != null) {
                int size = this.info.lineInfos.size();
                for (int i = 0; i < size; i++) {
                    PhotoLayout.LineInfo lineInfo = this.info.lineInfos.get(i);
                    PhotoLine line = this.queShotLayout.getLines().get(i);
                    line.startPoint().x = lineInfo.startX;
                    line.startPoint().y = lineInfo.startY;
                    line.endPoint().x = lineInfo.endX;
                    line.endPoint().y = lineInfo.endY;
                }
            }
            this.queShotLayout.sortAreas();
            this.queShotLayout.update();
        }
    }


    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.queShotLayout != null) {
            this.linePaint.setStrokeWidth((float) this.lineSize);
            this.selectedAreaPaint.setStrokeWidth((float) this.lineSize);
            this.handleBarPaint.setStrokeWidth((float) (this.lineSize * 3));
            int i = 0;
            while (i < this.queShotLayout.getAreaCount() && i < this.queShotGrids.size()) {
                PhotoGrid puzzlePiece = this.queShotGrids.get(i);
                if (!(puzzlePiece == this.queShotGrid && this.actionMode == ActionMode.SWAP) && this.queShotGrids.size() > i) {
                    puzzlePiece.draw(canvas, this.quickMode);
                }
                i++;
            }
            if (this.needDrawOuterLine) {
                for (PhotoLine drawLine : this.queShotLayout.getOuterLines()) {
                    drawLine(canvas, drawLine);
                }
            }
            if (this.needDrawLine) {
                for (PhotoLine drawLine2 : this.queShotLayout.getLines()) {
                    drawLine(canvas, drawLine2);
                }
            }
            if (!(this.queShotGrid == null || this.actionMode == ActionMode.SWAP)) {
                drawSelectedArea(canvas, this.queShotGrid);
            }
            if (this.queShotGrid != null && this.actionMode == ActionMode.SWAP) {
                this.queShotGrid.draw(canvas, 128, this.quickMode);
                if (this.replaceQuShotGrid != null) {
                    drawSelectedArea(canvas, this.replaceQuShotGrid);
                }
            }
        }
    }

    private void drawSelectedArea(Canvas canvas, PhotoGrid quShotCollage) {
        PhotoArea quShotCollageArea = quShotCollage.getArea();
        canvas.drawPath(quShotCollageArea.getAreaPath(), this.selectedAreaPaint);
        for (PhotoLine next : quShotCollageArea.getLines()) {
            if (this.queShotLayout.getLines().contains(next)) {
                PointF[] handleBarPoints = quShotCollageArea.getHandleBarPoints(next);
                canvas.drawLine(handleBarPoints[0].x, handleBarPoints[0].y, handleBarPoints[1].x, handleBarPoints[1].y, this.handleBarPaint);
                canvas.drawCircle(handleBarPoints[0].x, handleBarPoints[0].y, (float) ((this.lineSize * 3) / 2), this.handleBarPaint);
                canvas.drawCircle(handleBarPoints[1].x, handleBarPoints[1].y, (float) ((this.lineSize * 3) / 2), this.handleBarPaint);
            }
        }
    }

    private void drawLine(Canvas canvas, PhotoLine line) {
        canvas.drawLine(line.startPoint().x, line.startPoint().y, line.endPoint().x, line.endPoint().y, this.linePaint);
    }

    public void updateLayout(PhotoLayout quShotLayout) {
        ArrayList<PhotoGrid> arrayList = new ArrayList<>(this.queShotGrids);
        setQueShotLayout(quShotLayout);
        for (PhotoGrid drawable : arrayList) {
            addQuShotCollage(drawable.getDrawable());
        }
        invalidate();
    }

    public void setQueShotLayout(PhotoLayout quShotLayout) {
        clearQuShot();
        this.queShotLayout = quShotLayout;
        quShotLayout.setOuterBounds(this.rectF);
        quShotLayout.layout();
        invalidate();
    }

    public void setCollageLayout(PhotoLayout.Info info) {
        this.info = info;
        clearQuShot();
        this.queShotLayout = PhotoLayoutParser.parse(info);
        this.collagePadding = info.padding;
        this.collageRadian = info.radian;
        setBackgroundColor(info.color);
        invalidate();
    }

    public PhotoLayout getQueShotLayout() {
        return this.queShotLayout;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.touchEnable) {
            return super.onTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction() & 255;
        if (action != 5) {
            switch (action) {
                case 0:
                    this.downX = motionEvent.getX();
                    this.downY = motionEvent.getY();
                    decideActionMode(motionEvent);
                    prepareAction(motionEvent);
                    break;
                case 1:
                case 3:
                    finishAction(motionEvent);
                    this.actionMode = ActionMode.NONE;
                    removeCallbacks(this.switchToSwapAction);
                    break;
                case 2:
                    performAction(motionEvent);
                    if ((Math.abs(motionEvent.getX() - this.downX) > 10.0f || Math.abs(motionEvent.getY() - this.downY) > 10.0f) && this.actionMode != ActionMode.SWAP) {
                        removeCallbacks(this.switchToSwapAction);
                        break;
                    }
            }
        } else {
            this.previousDistance = calculateDistance(motionEvent);
            calculateMidPoint(motionEvent, this.midPoint);
            decideActionMode(motionEvent);
        }
        invalidate();
        return true;
    }

    private void decideActionMode(MotionEvent motionEvent) {
        for (PhotoGrid isAnimateRunning : this.queShotGrids) {
            if (isAnimateRunning.isAnimateRunning()) {
                this.actionMode = ActionMode.NONE;
                return;
            }
        }
        if (motionEvent.getPointerCount() == 1) {
            this.quShotLine = findHandlingLine();
            if (this.quShotLine == null || !this.canMoveLine) {
                this.queShotGrid = findHandlingQuShot();
                if (this.queShotGrid != null && this.canDrag) {
                    this.actionMode = ActionMode.DRAG;
                    postDelayed(this.switchToSwapAction, 500);
                    return;
                }
                return;
            }
            this.actionMode = ActionMode.MOVE;
        } else if (motionEvent.getPointerCount() > 1 && this.queShotGrid != null && this.queShotGrid.contains(motionEvent.getX(1), motionEvent.getY(1)) && this.actionMode == ActionMode.DRAG && this.canZoom) {
            this.actionMode = ActionMode.ZOOM;
        }
    }

    private void prepareAction(MotionEvent motionEvent) {
        switch (this.actionMode) {
            case DRAG:
                this.queShotGrid.record();
                return;
            case ZOOM:
                this.queShotGrid.record();
                return;
            case MOVE:
                this.quShotLine.prepareMove();
                this.queShotGridList.clear();
                this.queShotGridList.addAll(findNeedChangedPieces());
                for (PhotoGrid next : this.queShotGridList) {
                    next.record();
                    next.setPreviousMoveX(this.downX);
                    next.setPreviousMoveY(this.downY);
                }
                return;
            default:
                return;
        }
    }

    private void performAction(MotionEvent motionEvent) {
        switch (this.actionMode) {
            case DRAG:
                dragPiece(this.queShotGrid, motionEvent);
                return;
            case ZOOM:
                zoomPhoto(this.queShotGrid, motionEvent);
                return;
            case MOVE:
                moveLine(this.quShotLine, motionEvent);
                return;
            case SWAP:
                dragPiece(this.queShotGrid, motionEvent);
                this.replaceQuShotGrid = findReplacePiece(motionEvent);
                return;
            default:
                return;
        }
    }

    private void finishAction(MotionEvent motionEvent) {
        switch (this.actionMode) {
            case DRAG:
                if (this.queShotGrid != null && !this.queShotGrid.isFilledArea()) {
                    this.queShotGrid.moveToFillArea(this);
                }
                if (this.prevHandlingQueShotGrid == this.queShotGrid && Math.abs(this.downX - motionEvent.getX()) < 3.0f && Math.abs(this.downY - motionEvent.getY()) < 3.0f) {
                    this.queShotGrid = null;
                }
                this.prevHandlingQueShotGrid = this.queShotGrid;
                break;
            case ZOOM:
                if (this.queShotGrid != null && !this.queShotGrid.isFilledArea()) {
                    if (this.queShotGrid.canFilledArea()) {
                        this.queShotGrid.moveToFillArea(this);
                    } else {
                        this.queShotGrid.fillArea(this, false);
                    }
                }
                this.prevHandlingQueShotGrid = this.queShotGrid;
                break;
            case SWAP:
                if (!(this.queShotGrid == null || this.replaceQuShotGrid == null)) {
                    swapPiece();
                    this.queShotGrid = null;
                    this.replaceQuShotGrid = null;
                    this.prevHandlingQueShotGrid = null;
                    break;
                }
        }
        if (this.queShotGrid != null && this.onQueShotSelectedListener != null) {
            this.onQueShotSelectedListener.onQuShotSelected(this.queShotGrid, this.queShotGrids.indexOf(this.queShotGrid));
        } else if (this.queShotGrid == null && this.onQueShotUnSelectedListener != null) {
            this.onQueShotUnSelectedListener.onQuShotUnSelected();
        }
        this.quShotLine = null;
        this.queShotGridList.clear();
    }

    public void setPrevHandlingQueShotGrid(PhotoGrid puzzlePiece) {
        this.prevHandlingQueShotGrid = puzzlePiece;
    }

    private void swapPiece() {
        Drawable drawable = this.queShotGrid.getDrawable();
        String path = this.queShotGrid.getPath();
        this.queShotGrid.setDrawable(this.replaceQuShotGrid.getDrawable());
        this.queShotGrid.setPath(this.replaceQuShotGrid.getPath());
        this.replaceQuShotGrid.setDrawable(drawable);
        this.replaceQuShotGrid.setPath(path);
        this.queShotGrid.fillArea(this, true);
        this.replaceQuShotGrid.fillArea(this, true);
    }

    private void moveLine(PhotoLine line, MotionEvent motionEvent) {
        boolean z;
        if (line != null && motionEvent != null) {
            if (line.direction() == PhotoLine.Direction.HORIZONTAL) {
                z = line.move(motionEvent.getY() - this.downY, 80.0f);
            } else {
                z = line.move(motionEvent.getX() - this.downX, 80.0f);
            }
            if (z) {
                this.queShotLayout.update();
                this.queShotLayout.sortAreas();
                updatePiecesInArea(line, motionEvent);
            }
        }
    }

    private void updatePiecesInArea(PhotoLine line, MotionEvent motionEvent) {
        for (int i = 0; i < this.queShotGridList.size(); i++) {
            this.queShotGridList.get(i).updateWith(motionEvent, line);
        }
    }

    private void zoomPhoto(PhotoGrid puzzlePiece, MotionEvent motionEvent) {
        if (puzzlePiece != null && motionEvent != null && motionEvent.getPointerCount() >= 2) {
            float calculateDistance = calculateDistance(motionEvent) / this.previousDistance;
            puzzlePiece.zoomAndTranslate(calculateDistance, calculateDistance, this.midPoint, motionEvent.getX() - this.downX, motionEvent.getY() - this.downY);
        }
    }

    private void dragPiece(PhotoGrid puzzlePiece, MotionEvent motionEvent) {
        if (puzzlePiece != null && motionEvent != null) {
            puzzlePiece.translate(motionEvent.getX() - this.downX, motionEvent.getY() - this.downY);
        }
    }

    public void replace(Bitmap bitmap, String str) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        bitmapDrawable.setAntiAlias(true);
        bitmapDrawable.setFilterBitmap(true);
        replace(bitmapDrawable, str);
    }

    public void replace(Drawable drawable, String str) {
        if (this.queShotGrid != null) {
            this.queShotGrid.setPath(str);
            this.queShotGrid.setDrawable(drawable);
            this.queShotGrid.set(MatrixUtils.generateMatrix(this.queShotGrid, 0.0f));
            invalidate();
        }
    }

    public void setQueShotGrid(PhotoGrid puzzlePiece) {
        this.queShotGrid = puzzlePiece;
    }

    public void flipVertically() {
        if (this.queShotGrid != null) {
            this.queShotGrid.postFlipVertically();
            this.queShotGrid.record();
            invalidate();
        }
    }

    public void flipHorizontally() {
        if (this.queShotGrid != null) {
            this.queShotGrid.postFlipHorizontally();
            this.queShotGrid.record();
            invalidate();
        }
    }

    public void rotate(float f) {
        if (this.queShotGrid != null) {
            this.queShotGrid.postRotate(f);
            this.queShotGrid.record();
            invalidate();
        }
    }

    private PhotoGrid findHandlingQuShot() {
        for (PhotoGrid next : this.queShotGrids) {
            if (next.contains(this.downX, this.downY)) {
                return next;
            }
        }
        return null;
    }

    private PhotoLine findHandlingLine() {
        for (PhotoLine next : this.queShotLayout.getLines()) {
            if (next.contains(this.downX, this.downY, 40.0f)) {
                return next;
            }
        }
        return null;
    }

    private PhotoGrid findReplacePiece(MotionEvent motionEvent) {
        for (PhotoGrid next : this.queShotGrids) {
            if (next.contains(motionEvent.getX(), motionEvent.getY())) {
                return next;
            }
        }
        return null;
    }

    private List<PhotoGrid> findNeedChangedPieces() {
        if (this.quShotLine == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        for (PhotoGrid next : this.queShotGrids) {
            if (next.contains(this.quShotLine)) {
                arrayList.add(next);
            }
        }
        return arrayList;
    }


    public float calculateDistance(MotionEvent motionEvent) {
        float x = motionEvent.getX(0) - motionEvent.getX(1);
        float y = motionEvent.getY(0) - motionEvent.getY(1);
        return (float) Math.sqrt((double) ((x * x) + (y * y)));
    }

    private void calculateMidPoint(MotionEvent motionEvent, PointF pointF) {
        pointF.x = (motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f;
        pointF.y = (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f;
    }

    public void reset() {
        clearQuShot();
        if (this.queShotLayout != null) {
            this.queShotLayout.reset();
        }
    }

    public void clearQuShot() {
        clearHandlingPieces();
        this.queShotGrids.clear();
        invalidate();
    }

    public void clearHandlingPieces() {
        this.quShotLine = null;
        this.queShotGrid = null;
        this.replaceQuShotGrid = null;
        this.queShotGridList.clear();
        invalidate();
    }

    public void addPieces(List<Bitmap> list) {
        for (Bitmap addPiece : list) {
            addQuShotCollage(addPiece);
        }
        postInvalidate();
    }


    public void addQuShotCollage(Bitmap bitmap) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        bitmapDrawable.setAntiAlias(true);
        bitmapDrawable.setFilterBitmap(true);
        addQuShotCollage(bitmapDrawable, (Matrix) null);
    }

    public void addQuShotCollage(Drawable drawable) {
        addQuShotCollage(drawable, (Matrix) null);
    }

    public void addQuShotCollage(Drawable drawable, Matrix matrix) {
        addQuShotCollage(drawable, matrix, "");
    }

    public void addQuShotCollage(Drawable drawable, Matrix matrix, String str) {
        Matrix matrix2;
        int size = this.queShotGrids.size();
        if (size >= this.queShotLayout.getAreaCount()) {
            Log.e(TAG, "addQuShotCollage: can not add more. the current collage layout can contains " + this.queShotLayout.getAreaCount() + " puzzle piece.");
            return;
        }
        PhotoArea area = this.queShotLayout.getArea(size);
        area.setPadding(this.collagePadding);
        PhotoGrid puzzlePiece = new PhotoGrid(drawable, area, new Matrix());
        if (matrix != null) {
            matrix2 = new Matrix(matrix);
        } else {
            matrix2 = MatrixUtils.generateMatrix(area, drawable, 0.0f);
        }
        puzzlePiece.set(matrix2);
        puzzlePiece.setAnimateDuration(this.duration);
        puzzlePiece.setPath(str);
        this.queShotGrids.add(puzzlePiece);
        this.queShotAreaQueShotGridMap.put(area, puzzlePiece);
        setCollagePadding(this.collagePadding);
        setCollageRadian(this.collageRadian);
        invalidate();
    }


    public PhotoGrid getQueShotGrid() {
        return this.queShotGrid;
    }


    public void setAnimateDuration(int i) {
        this.duration = i;
        for (PhotoGrid animateDuration : this.queShotGrids) {
            animateDuration.setAnimateDuration(i);
        }
    }


    public void setNeedDrawLine(boolean z) {
        this.needDrawLine = z;
        this.queShotGrid = null;
        this.prevHandlingQueShotGrid = null;
        invalidate();
    }


    public void setNeedDrawOuterLine(boolean z) {
        this.needDrawOuterLine = z;
        invalidate();
    }


    public void setLineColor(int i) {
        this.lineColor = i;
        this.linePaint.setColor(i);
        invalidate();
    }


    public void setLineSize(int i) {
        this.lineSize = i;
        invalidate();
    }


    public void setSelectedLineColor(int i) {
        this.selectedLineColor = i;
        this.selectedAreaPaint.setColor(i);
        invalidate();
    }


    public void setHandleBarColor(int i) {
        this.handleBarColor = i;
        this.handleBarPaint.setColor(i);
        invalidate();
    }


    public void setTouchEnable(boolean z) {
        this.touchEnable = z;
    }

    public void clearHandling() {
        this.queShotGrid = null;
        this.quShotLine = null;
        this.replaceQuShotGrid = null;
        this.prevHandlingQueShotGrid = null;
        this.queShotGridList.clear();
    }

    public void setCollagePadding(float f) {
        this.collagePadding = f;
        if (this.queShotLayout != null) {
            this.queShotLayout.setPadding(f);
            int size = this.queShotGrids.size();
            for (int i = 0; i < size; i++) {
                PhotoGrid quShotCollage = this.queShotGrids.get(i);
                if (quShotCollage.canFilledArea()) {
                    quShotCollage.moveToFillArea((View) null);
                } else {
                    quShotCollage.fillArea(this, true);
                }
            }
        }
        invalidate();
    }

    public void setCollageRadian(float f) {
        this.collageRadian = f;
        if (this.queShotLayout != null) {
            this.queShotLayout.setRadian(f);
        }
        invalidate();
    }

    public int getBackgroundResourceMode() {
        return this.backgroundResource;
    }

    public void setBackgroundResourceMode(int i) {
        this.backgroundResource = i;
    }


    public void setBackgroundColor(int i) {
        super.setBackgroundColor(i);
        if (this.queShotLayout != null) {
            this.queShotLayout.setColor(i);
        }
    }


    public float getCollagePadding() {
        return this.collagePadding;
    }

    public float getCollageRadian() {
        return this.collageRadian;
    }

    public List<PhotoGrid> getQueShotGrids() {
        int size = this.queShotGrids.size();
        ArrayList arrayList = new ArrayList(size);
        this.queShotLayout.sortAreas();
        for (int i = 0; i < size; i++) {
            arrayList.add(this.queShotAreaQueShotGridMap.get(this.queShotLayout.getArea(i)));
        }
        return arrayList;
    }


    public void setOnQueShotSelectedListener(onQueShotSelectedListener onQueShotSelectedListener) {
        this.onQueShotSelectedListener = onQueShotSelectedListener;
    }

    public void setOnQueShotUnSelectedListener(onQueShotUnSelectedListener onQueShotUnSelectedListener) {
        this.onQueShotUnSelectedListener = onQueShotUnSelectedListener;
    }
}
