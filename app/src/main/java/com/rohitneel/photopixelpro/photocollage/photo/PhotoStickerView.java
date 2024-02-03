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
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

import com.rohitneel.photopixelpro.R;
import com.rohitneel.photopixelpro.photocollage.event.DeleteIconEvent;
import com.rohitneel.photopixelpro.photocollage.event.FlipHorizontallyEvent;
import com.rohitneel.photopixelpro.photocollage.event.ZoomIconEvent;
import com.rohitneel.photopixelpro.photocollage.sticker.DrawableSticker;
import com.rohitneel.photopixelpro.photocollage.sticker.Sticker;
import com.rohitneel.photopixelpro.photocollage.utils.StickerUtils;
import com.rohitneel.photopixelpro.photocollage.utils.SystemUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhotoStickerView extends RelativeLayout {
    private static final String TAG = "QuShotStickerView";
    private final float[] bitmapPoints;
    private final Paint borderPaint;
    private final Paint borderPaintRed;
    private final float[] bounds;
    private boolean bringToFrontCurrentSticker;
    private int circleRadius;
    private boolean constrained;
    private final PointF currentCenterPoint;
    private PhotoStickerIcons currentIcon;
    private int currentMode;
    private float currentMoveingX;
    private float currentMoveingY;
    private final Matrix downMatrix;
    private float downX;
    private float downY;
    private boolean drawCirclePoint;
    private Sticker handlingSticker;
    private final List<PhotoStickerIcons> icons;
    private long lastClickTime;
    private Sticker lastHandlingSticker;
    private final Paint linePaint;
    private boolean locked;
    private PointF midPoint;
    private int minClickDelayTime;
    private final Matrix moveMatrix;
    private float oldDistance;
    private float oldRotation;
    private boolean onMoving;
    private OnStickerOperationListener onStickerOperationListener;
    private Paint paintCircle;
    private final float[] point;
    private boolean showBorder;
    private boolean showIcons;
    private final Matrix sizeMatrix;
    private final RectF stickerRect;
    private final List<Sticker> stickers;
    private final float[] tmp;
    private int touchSlop;

    public interface OnStickerOperationListener {
        void onAddSticker(@NonNull Sticker sticker);

        void onStickerSelected(@NonNull Sticker sticker);

        void onStickerDeleted(@NonNull Sticker sticker);

        void onStickerDoubleTap(@NonNull Sticker sticker);

        void onStickerDrag(@NonNull Sticker sticker);

        void onStickerFlip(@NonNull Sticker sticker);

        void onStickerTouchOutside();

        void onStickerTouchedDown(@NonNull Sticker sticker);

        void onStickerZoom(@NonNull Sticker sticker);

        void onTouchDownBeauty(float f, float f2);

        void onTouchDragBeauty(float f, float f2);

        void onTouchUpBeauty(float f, float f2);
    }

    public PhotoStickerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public PhotoStickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @SuppressLint("ResourceType")
    public PhotoStickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        TypedArray typedArray;
        this.stickers = new ArrayList();
        this.icons = new ArrayList(4);
        this.borderPaint = new Paint();
        this.borderPaintRed = new Paint();
        this.linePaint = new Paint();
        this.stickerRect = new RectF();
        this.sizeMatrix = new Matrix();
        this.downMatrix = new Matrix();
        this.moveMatrix = new Matrix();
        this.bitmapPoints = new float[8];
        this.bounds = new float[8];
        this.point = new float[2];
        this.currentCenterPoint = new PointF();
        this.tmp = new float[2];
        this.midPoint = new PointF();
        this.drawCirclePoint = false;
        this.onMoving = false;
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.currentMode = 0;
        this.lastClickTime = 0;
        this.minClickDelayTime = 200;
        this.paintCircle = new Paint();
        this.paintCircle.setAntiAlias(true);
        this.paintCircle.setDither(true);
        this.paintCircle.setColor(getContext().getResources().getColor(R.color.colorAccent));
        this.paintCircle.setStrokeWidth((float) SystemUtil.dpToPx(getContext(), 2));
        this.paintCircle.setStyle(Paint.Style.STROKE);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        try {
            int[] StickerView = {R.attr.borderAlpha, R.attr.borderColor, R.attr.bringToFrontCurrentSticker, R.attr.showBorder, R.attr.showIcons};
            typedArray = context.obtainStyledAttributes(attributeSet, StickerView);
            try {
                this.showIcons = typedArray.getBoolean(4, false);
                this.showBorder = typedArray.getBoolean(3, false);
                this.bringToFrontCurrentSticker = typedArray.getBoolean(2, false);
                this.borderPaint.setAntiAlias(true);
                this.borderPaint.setColor(typedArray.getColor(1, Color.parseColor("#000000")));
                this.borderPaint.setAlpha(typedArray.getInteger(0, 255));
                this.borderPaintRed.setAntiAlias(true);
                this.borderPaintRed.setColor(typedArray.getColor(1, Color.parseColor("#000000")));
                this.borderPaintRed.setAlpha(typedArray.getInteger(0, 255));
                getStickerIcons();
                if (typedArray != null) {
                    typedArray.recycle();
                }
            } catch (Throwable th) {
                th = th;
                if (typedArray != null) {
                    typedArray.recycle();
                }
                throw th;
            }
        } catch (Throwable th2) {

            typedArray = null;
            if (typedArray != null) {
            }

        }
    }

    @RequiresApi(api = 21)
    public PhotoStickerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.stickers = new ArrayList();
        this.icons = new ArrayList(4);
        this.borderPaint = new Paint();
        this.borderPaintRed = new Paint();
        this.linePaint = new Paint();
        this.stickerRect = new RectF();
        this.sizeMatrix = new Matrix();
        this.downMatrix = new Matrix();
        this.moveMatrix = new Matrix();
        this.bitmapPoints = new float[8];
        this.bounds = new float[8];
        this.point = new float[2];
        this.currentCenterPoint = new PointF();
        this.tmp = new float[2];
        this.midPoint = new PointF();
        this.drawCirclePoint = false;
        this.onMoving = false;
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.currentMode = 0;
        this.lastClickTime = 0;
        this.minClickDelayTime = 200;
    }

    public Matrix getSizeMatrix() {
        return this.sizeMatrix;
    }

    public Matrix getDownMatrix() {
        return this.downMatrix;
    }

    public Matrix getMoveMatrix() {
        return this.moveMatrix;
    }

    public List<Sticker> getStickers() {
        return this.stickers;
    }

    public void getStickerIcons() {
        PhotoStickerIcons quShotStickerIconClose = new PhotoStickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_close), 0, PhotoStickerIcons.DELETE);
        quShotStickerIconClose.setIconEvent(new DeleteIconEvent());
        PhotoStickerIcons quShotStickerIconScale = new PhotoStickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_scale), 3, PhotoStickerIcons.SCALE);
        quShotStickerIconScale.setIconEvent(new ZoomIconEvent());
        PhotoStickerIcons quShotStickerIconFlip = new PhotoStickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_flip), 1, PhotoStickerIcons.FLIP);
        quShotStickerIconFlip.setIconEvent(new FlipHorizontallyEvent());
        PhotoStickerIcons quShotStickerIconEdit = new PhotoStickerIcons(ContextCompat.getDrawable(getContext(), R.drawable.ic_outline_edit), 2, PhotoStickerIcons.EDIT);
        quShotStickerIconEdit.setIconEvent(new FlipHorizontallyEvent());
        this.icons.clear();
        this.icons.add(quShotStickerIconClose);
        this.icons.add(quShotStickerIconScale);
        this.icons.add(quShotStickerIconFlip);
        this.icons.add(quShotStickerIconEdit);
    }

    public void setHandlingSticker(Sticker sticker) {
        this.lastHandlingSticker = this.handlingSticker;
        this.handlingSticker = sticker;
        invalidate();
    }

    public void showLastHandlingSticker() {
        if (this.lastHandlingSticker != null && !this.lastHandlingSticker.isShow()) {
            this.lastHandlingSticker.setShow(true);
            invalidate();
        }
    }

    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            this.stickerRect.left = (float) i;
            this.stickerRect.top = (float) i2;
            this.stickerRect.right = (float) i3;
            this.stickerRect.bottom = (float) i4;
        }
    }

    public void setCircleRadius(int i) {
        this.circleRadius = i;
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.drawCirclePoint && this.onMoving) {
            canvas.drawCircle(this.downX, this.downY, (float) this.circleRadius, this.paintCircle);
            canvas.drawLine(this.downX, this.downY, this.currentMoveingX, this.currentMoveingY, this.paintCircle);
        }
        drawStickers(canvas);
    }

    public void drawStickers(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        Canvas canvas2 = canvas;
        int i = 0;
        for (int i2 = 0; i2 < this.stickers.size(); i2++) {
            Sticker sticker = this.stickers.get(i2);
            if (sticker != null && sticker.isShow()) {
                sticker.draw(canvas2);
            }
        }
        if (this.handlingSticker != null && !this.locked && (this.showBorder || this.showIcons)) {
            getStickerPoints(this.handlingSticker, this.bitmapPoints);
            float f5 = this.bitmapPoints[0];
            int i3 = 1;
            float f6 = this.bitmapPoints[1];
            float f7 = this.bitmapPoints[2];
            float f8 = this.bitmapPoints[3];
            float f9 = this.bitmapPoints[4];
            float f10 = this.bitmapPoints[5];
            float f11 = this.bitmapPoints[6];
            float f12 = this.bitmapPoints[7];
            if (this.showBorder) {
                Canvas canvas3 = canvas;
                float f13 = f5;
                f4 = f12;
                float f14 = f6;
                f3 = f11;
                f2 = f10;
                f = f9;
                canvas3.drawLine(f13, f14, f7, f8, this.borderPaint);
                canvas3.drawLine(f13, f14, f, f2, this.borderPaint);
                canvas3.drawLine(f7, f8, f3, f4, this.borderPaint);
                canvas3.drawLine(f3, f4, f, f2, this.borderPaint);
            } else {
                f4 = f12;
                f3 = f11;
                f2 = f10;
                f = f9;
            }
            if (this.showIcons) {
                float f15 = f4;
                float f16 = f3;
                float f17 = f2;
                float f18 = f;
                float calculateRotation = calculateRotation(f16, f15, f18, f17);
                while (i < this.icons.size()) {
                    PhotoStickerIcons bitmapStickerIcon = this.icons.get(i);
                    switch (bitmapStickerIcon.getPosition()) {
                        case 0:
                            configIconMatrix(bitmapStickerIcon, f5, f6, calculateRotation);
                            bitmapStickerIcon.draw(canvas2, this.borderPaintRed);
                            break;
                        case 1:
                            if (((this.handlingSticker instanceof PhotoTextView) && bitmapStickerIcon.getTag().equals(PhotoStickerIcons.EDIT)) || ((this.handlingSticker instanceof DrawableSticker) && bitmapStickerIcon.getTag().equals(PhotoStickerIcons.FLIP))) {
                                configIconMatrix(bitmapStickerIcon, f7, f8, calculateRotation);
                                bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                break;
                            }
                        case 2:
                            if (this.handlingSticker instanceof PhotoSticker) {
                                if (((PhotoSticker) this.handlingSticker).getType() != 0) {
                                    break;
                                } else {
                                    configIconMatrix(bitmapStickerIcon, f18, f17, calculateRotation);
                                    bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                    break;
                                }
                            } else {
                                configIconMatrix(bitmapStickerIcon, f18, f17, calculateRotation);
                                bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                break;
                            }
                        case 3:
                            if ((!(this.handlingSticker instanceof PhotoTextView) || !bitmapStickerIcon.getTag().equals(PhotoStickerIcons.ROTATE)) && (!(this.handlingSticker instanceof DrawableSticker) || !bitmapStickerIcon.getTag().equals(PhotoStickerIcons.SCALE))) {
                                if (this.handlingSticker instanceof PhotoSticker) {
                                    PhotoSticker beautySticker = (PhotoSticker) this.handlingSticker;
                                    if (beautySticker.getType() != i3) {
                                        if (beautySticker.getType() != 2 && beautySticker.getType() != 8) {
                                            if (beautySticker.getType() != 4) {
                                                break;
                                            }
                                        }
                                        configIconMatrix(bitmapStickerIcon, f16, f15, calculateRotation);
                                        bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                        break;
                                    } else {
                                        configIconMatrix(bitmapStickerIcon, f16, f15, calculateRotation);
                                        bitmapStickerIcon.draw(canvas2, this.borderPaint);
                                    }
                                }
                            } else {
                                configIconMatrix(bitmapStickerIcon, f16, f15, calculateRotation);
                                bitmapStickerIcon.draw(canvas2, this.borderPaint);
                            }
                            break;
                    }
                    i++;
                    i3 = 1;
                }
            }
        }
        invalidate();
    }


    public void configIconMatrix(@NonNull PhotoStickerIcons bitmapStickerIcon, float f, float f2, float f3) {
        bitmapStickerIcon.setX(f);
        bitmapStickerIcon.setY(f2);
        bitmapStickerIcon.getMatrix().reset();
        bitmapStickerIcon.getMatrix().postRotate(f3, (float) (bitmapStickerIcon.getWidth() / 2), (float) (bitmapStickerIcon.getHeight() / 2));
        bitmapStickerIcon.getMatrix().postTranslate(f - ((float) (bitmapStickerIcon.getWidth() / 2)), f2 - ((float) (bitmapStickerIcon.getHeight() / 2)));
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.locked) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        if (motionEvent.getAction() != 0) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        this.downX = motionEvent.getX();
        this.downY = motionEvent.getY();
        return (findCurrentIconTouched() == null && findHandlingSticker() == null) ? false : true;
    }

    public void setDrawCirclePoint(boolean z) {
        this.drawCirclePoint = z;
        this.onMoving = false;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.locked) {
            return super.onTouchEvent(motionEvent);
        }
        switch (MotionEventCompat.getActionMasked(motionEvent)) {
            case 0:
                if (!onTouchDown(motionEvent)) {
                    if (this.onStickerOperationListener == null) {
                        return false;
                    }
                    this.onStickerOperationListener.onStickerTouchOutside();
                    invalidate();
                    if (!this.drawCirclePoint) {
                        return false;
                    }
                }
                break;
            case 1:
                onTouchUp(motionEvent);
                break;
            case 2:
                handleCurrentMode(motionEvent);
                invalidate();
                break;
            case 5:
                this.oldDistance = calculateDistance(motionEvent);
                this.oldRotation = calculateRotation(motionEvent);
                this.midPoint = calculateMidPoint(motionEvent);
                if (this.handlingSticker != null && isInStickerArea(this.handlingSticker, motionEvent.getX(1), motionEvent.getY(1)) && findCurrentIconTouched() == null) {
                    this.currentMode = 2;
                    break;
                }
            case 6:
                if (!(this.currentMode != 2 || this.handlingSticker == null || this.onStickerOperationListener == null)) {
                    this.onStickerOperationListener.onStickerZoom(this.handlingSticker);
                }
                this.currentMode = 0;
                break;
        }
        return true;
    }


    public boolean onTouchDown(@NonNull MotionEvent motionEvent) {
        this.currentMode = 1;
        this.downX = motionEvent.getX();
        this.downY = motionEvent.getY();
        this.onMoving = true;
        this.currentMoveingX = motionEvent.getX();
        this.currentMoveingY = motionEvent.getY();
        this.midPoint = calculateMidPoint();
        this.oldDistance = calculateDistance(this.midPoint.x, this.midPoint.y, this.downX, this.downY);
        this.oldRotation = calculateRotation(this.midPoint.x, this.midPoint.y, this.downX, this.downY);
        this.currentIcon = findCurrentIconTouched();
        if (this.currentIcon != null) {
            this.currentMode = 3;
            this.currentIcon.onActionDown(this, motionEvent);
        } else {
            this.handlingSticker = findHandlingSticker();
        }
        if (this.handlingSticker != null) {
            this.downMatrix.set(this.handlingSticker.getMatrix());
            if (this.bringToFrontCurrentSticker) {
                this.stickers.remove(this.handlingSticker);
                this.stickers.add(this.handlingSticker);
            }
            if (this.onStickerOperationListener != null) {
                this.onStickerOperationListener.onStickerTouchedDown(this.handlingSticker);
            }
        }
        if (this.drawCirclePoint) {
            this.onStickerOperationListener.onTouchDownBeauty(this.currentMoveingX, this.currentMoveingY);
            invalidate();
            return true;
        } else if (this.currentIcon == null && this.handlingSticker == null) {
            return false;
        } else {
            invalidate();
            return true;
        }
    }


    public void onTouchUp(@NonNull MotionEvent motionEvent) {
        long uptimeMillis = SystemClock.uptimeMillis();
        this.onMoving = false;
        if (this.drawCirclePoint) {
            this.onStickerOperationListener.onTouchUpBeauty(motionEvent.getX(), motionEvent.getY());
        }
        if (!(this.currentMode != 3 || this.currentIcon == null || this.handlingSticker == null)) {
            this.currentIcon.onActionUp(this, motionEvent);
        }
        if (this.currentMode == 1 && Math.abs(motionEvent.getX() - this.downX) < ((float) this.touchSlop) && Math.abs(motionEvent.getY() - this.downY) < ((float) this.touchSlop) && this.handlingSticker != null) {
            this.currentMode = 4;
            if (this.onStickerOperationListener != null) {
                this.onStickerOperationListener.onStickerSelected(this.handlingSticker);
            }
            if (uptimeMillis - this.lastClickTime < ((long) this.minClickDelayTime) && this.onStickerOperationListener != null) {
                this.onStickerOperationListener.onStickerDoubleTap(this.handlingSticker);
            }
        }
        if (!(this.currentMode != 1 || this.handlingSticker == null || this.onStickerOperationListener == null)) {
            this.onStickerOperationListener.onStickerDrag(this.handlingSticker);
        }
        this.currentMode = 0;
        this.lastClickTime = uptimeMillis;
    }


    public void handleCurrentMode(@NonNull MotionEvent motionEvent) {
        switch (this.currentMode) {
            case 1:
                this.currentMoveingX = motionEvent.getX();
                this.currentMoveingY = motionEvent.getY();
                if (this.drawCirclePoint) {
                    this.onStickerOperationListener.onTouchDragBeauty(this.currentMoveingX, this.currentMoveingY);
                }
                if (this.handlingSticker != null) {
                    this.moveMatrix.set(this.downMatrix);
                    if (this.handlingSticker instanceof PhotoSticker) {
                        PhotoSticker beautySticker = (PhotoSticker) this.handlingSticker;
                        if (beautySticker.getType() == 10 || beautySticker.getType() == 11) {
                            this.moveMatrix.postTranslate(0.0f, motionEvent.getY() - this.downY);
                        } else {
                            this.moveMatrix.postTranslate(motionEvent.getX() - this.downX, motionEvent.getY() - this.downY);
                        }
                    } else {
                        this.moveMatrix.postTranslate(motionEvent.getX() - this.downX, motionEvent.getY() - this.downY);
                    }
                    this.handlingSticker.setMatrix(this.moveMatrix);
                    if (this.constrained) {
                        constrainSticker(this.handlingSticker);
                        return;
                    }
                    return;
                }
                return;
            case 2:
                if (this.handlingSticker != null) {
                    float calculateDistance = calculateDistance(motionEvent);
                    float calculateRotation = calculateRotation(motionEvent);
                    this.moveMatrix.set(this.downMatrix);
                    this.moveMatrix.postScale(calculateDistance / this.oldDistance, calculateDistance / this.oldDistance, this.midPoint.x, this.midPoint.y);
                    this.moveMatrix.postRotate(calculateRotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
                    this.handlingSticker.setMatrix(this.moveMatrix);
                    return;
                }
                return;
            case 3:
                if (this.handlingSticker != null && this.currentIcon != null) {
                    this.currentIcon.onActionMove(this, motionEvent);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void zoomAndRotateCurrentSticker(@NonNull MotionEvent motionEvent) {
        zoomAndRotateSticker(this.handlingSticker, motionEvent);
    }

    public void alignHorizontally() {
        this.moveMatrix.set(this.downMatrix);
        this.moveMatrix.postRotate(-getCurrentSticker().getCurrentAngle(), this.midPoint.x, this.midPoint.y);
        this.handlingSticker.setMatrix(this.moveMatrix);
    }

    public void zoomAndRotateSticker(@Nullable Sticker sticker, @NonNull MotionEvent motionEvent) {
        float f;
        if (sticker != null) {
            boolean z = sticker instanceof PhotoSticker;
            if (z) {
                PhotoSticker beautySticker = (PhotoSticker) sticker;
                if (beautySticker.getType() == 10 || beautySticker.getType() == 11) {
                    return;
                }
            }
            if (sticker instanceof PhotoTextView) {
                f = this.oldDistance;
            } else {
                f = calculateDistance(this.midPoint.x, this.midPoint.y, motionEvent.getX(), motionEvent.getY());
            }
            float calculateRotation = calculateRotation(this.midPoint.x, this.midPoint.y, motionEvent.getX(), motionEvent.getY());
            this.moveMatrix.set(this.downMatrix);
            this.moveMatrix.postScale(f / this.oldDistance, f / this.oldDistance, this.midPoint.x, this.midPoint.y);
            if (!z) {
                this.moveMatrix.postRotate(calculateRotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
            }
            this.handlingSticker.setMatrix(this.moveMatrix);
        }
    }


    public void constrainSticker(@NonNull Sticker sticker) {
        int width = getWidth();
        int height = getHeight();
        sticker.getMappedCenterPoint(this.currentCenterPoint, this.point, this.tmp);
        float f = 0.0f;
        float f2 = this.currentCenterPoint.x < 0.0f ? -this.currentCenterPoint.x : 0.0f;
        float f3 = (float) width;
        if (this.currentCenterPoint.x > f3) {
            f2 = f3 - this.currentCenterPoint.x;
        }
        if (this.currentCenterPoint.y < 0.0f) {
            f = -this.currentCenterPoint.y;
        }
        float f4 = (float) height;
        if (this.currentCenterPoint.y > f4) {
            f = f4 - this.currentCenterPoint.y;
        }
        sticker.getMatrix().postTranslate(f2, f);
    }


    @Nullable
    public PhotoStickerIcons findCurrentIconTouched() {
        for (PhotoStickerIcons next : this.icons) {
            float x = next.getX() - this.downX;
            float y = next.getY() - this.downY;
            if (((double) ((x * x) + (y * y))) <= Math.pow((double) (next.getIconRadius() + next.getIconRadius()), 2.0d)) {
                return next;
            }
        }
        return null;
    }


    @Nullable
    public Sticker findHandlingSticker() {
        for (int size = this.stickers.size() - 1; size >= 0; size--) {
            if (isInStickerArea(this.stickers.get(size), this.downX, this.downY)) {
                return this.stickers.get(size);
            }
        }
        return null;
    }


    public boolean isInStickerArea(@NonNull Sticker sticker, float f, float f2) {
        this.tmp[0] = f;
        this.tmp[1] = f2;
        return sticker.contains(this.tmp);
    }


    @NonNull
    public PointF calculateMidPoint(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            this.midPoint.set(0.0f, 0.0f);
            return this.midPoint;
        }
        this.midPoint.set((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f, (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f);
        return this.midPoint;
    }


    @NonNull
    public PointF calculateMidPoint() {
        if (this.handlingSticker == null) {
            this.midPoint.set(0.0f, 0.0f);
            return this.midPoint;
        }
        this.handlingSticker.getMappedCenterPoint(this.midPoint, this.point, this.tmp);
        return this.midPoint;
    }


    public float calculateRotation(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateRotation(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }


    public float calculateRotation(float f, float f2, float f3, float f4) {
        return (float) Math.toDegrees(Math.atan2((double) (f2 - f4), (double) (f - f3)));
    }


    public float calculateDistance(@Nullable MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateDistance(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }


    public float calculateDistance(float f, float f2, float f3, float f4) {
        double d = (double) (f - f3);
        double d2 = (double) (f2 - f4);
        return (float) Math.sqrt((d * d) + (d2 * d2));
    }


    public void transformSticker(@Nullable Sticker sticker) {
        if (sticker == null) {
            Log.e(TAG, "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null");
            return;
        }
        this.sizeMatrix.reset();
        float width = (float) getWidth();
        float height = (float) getHeight();
        float width2 = (float) sticker.getWidth();
        float height2 = (float) sticker.getHeight();
        this.sizeMatrix.postTranslate((width - width2) / 2.0f, (height - height2) / 2.0f);
        float f = (width < height ? width / width2 : height / height2) / 2.0f;
        this.sizeMatrix.postScale(f, f, width / 2.0f, height / 2.0f);
        sticker.getMatrix().reset();
        sticker.setMatrix(this.sizeMatrix);
        invalidate();
    }


    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        for (int i5 = 0; i5 < this.stickers.size(); i5++) {
            Sticker sticker = this.stickers.get(i5);
            if (sticker != null) {
                transformSticker(sticker);
            }
        }
    }

    public void flipCurrentSticker(int i) {
        flip(this.handlingSticker, i);
    }

    public void flip(@Nullable Sticker sticker, int i) {
        if (sticker != null) {
            sticker.getCenterPoint(this.midPoint);
            if ((i & 1) > 0) {
                sticker.getMatrix().preScale(-1.0f, 1.0f, this.midPoint.x, this.midPoint.y);
                sticker.setFlippedHorizontally(!sticker.isFlippedHorizontally());
            }
            if ((i & 2) > 0) {
                sticker.getMatrix().preScale(1.0f, -1.0f, this.midPoint.x, this.midPoint.y);
                sticker.setFlippedVertically(!sticker.isFlippedVertically());
            }
            if (this.onStickerOperationListener != null) {
                this.onStickerOperationListener.onStickerFlip(sticker);
            }
            invalidate();
        }
    }

    public boolean replace(@Nullable Sticker sticker) {
        return replace(sticker, true);
    }

    public Sticker getLastHandlingSticker() {
        return this.lastHandlingSticker;
    }

    public boolean replace(@Nullable Sticker sticker, boolean z) {
        float f;
        if (this.handlingSticker == null) {
            this.handlingSticker = this.lastHandlingSticker;
        }
        if (this.handlingSticker == null || sticker == null) {
            return false;
        }
        float width = (float) getWidth();
        float height = (float) getHeight();
        if (z) {
            sticker.setMatrix(this.handlingSticker.getMatrix());
            sticker.setFlippedVertically(this.handlingSticker.isFlippedVertically());
            sticker.setFlippedHorizontally(this.handlingSticker.isFlippedHorizontally());
        } else {
            this.handlingSticker.getMatrix().reset();
            sticker.getMatrix().postTranslate((width - ((float) this.handlingSticker.getWidth())) / 2.0f, (height - ((float) this.handlingSticker.getHeight())) / 2.0f);
            if (width < height) {
                if (this.handlingSticker instanceof PhotoTextView) {
                    f = width / ((float) this.handlingSticker.getWidth());
                } else {
                    f = width / ((float) this.handlingSticker.getDrawable().getIntrinsicWidth());
                }
            } else if (this.handlingSticker instanceof PhotoTextView) {
                f = height / ((float) this.handlingSticker.getHeight());
            } else {
                f = height / ((float) this.handlingSticker.getDrawable().getIntrinsicHeight());
            }
            float f2 = f / 2.0f;
            sticker.getMatrix().postScale(f2, f2, width / 2.0f, height / 2.0f);
        }
        this.stickers.set(this.stickers.indexOf(this.handlingSticker), sticker);
        this.handlingSticker = sticker;
        invalidate();
        return true;
    }

    public boolean remove(@Nullable Sticker sticker) {
        if (this.stickers.contains(sticker)) {
            this.stickers.remove(sticker);
            if (this.onStickerOperationListener != null) {
                this.onStickerOperationListener.onStickerDeleted(sticker);
            }
            if (this.handlingSticker == sticker) {
                this.handlingSticker = null;
            }
            invalidate();
            return true;
        }
        Log.d(TAG, "remove: the sticker is not in this StickerView");
        return false;
    }

    public boolean removeCurrentSticker() {
        return remove(this.handlingSticker);
    }

    public void removeAllStickers() {
        this.stickers.clear();
        if (this.handlingSticker != null) {
            this.handlingSticker.release();
            this.handlingSticker = null;
        }
        invalidate();
    }

    @NonNull
    public PhotoStickerView addSticker(@NonNull Sticker sticker) {
        return addSticker(sticker, 1);
    }

    public PhotoStickerView addSticker(@NonNull final Sticker sticker, final int i) {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, i);
        } else {
            post(new Runnable() {
                public void run() {
                    PhotoStickerView.this.addStickerImmediately(sticker, i);
                }
            });
        }
        return this;
    }


    public void addStickerImmediately(@NonNull Sticker sticker, int i) {
        setStickerPosition(sticker, i);
        sticker.getMatrix().postScale(1.0f, 1.0f, (float) getWidth(), (float) getHeight());
        this.handlingSticker = sticker;
        this.stickers.add(sticker);
        if (this.onStickerOperationListener != null) {
            this.onStickerOperationListener.onAddSticker(sticker);
        }
        invalidate();
    }


    public void setStickerPosition(@NonNull Sticker sticker, int i) {
        float f;
        float width = ((float) getWidth()) - ((float) sticker.getWidth());
        float height = ((float) getHeight()) - ((float) sticker.getHeight());
        if (sticker instanceof PhotoSticker) {
            PhotoSticker beautySticker = (PhotoSticker) sticker;
            f = height / 2.0f;
            if (beautySticker.getType() == 0) {
                width /= 3.0f;
            } else if (beautySticker.getType() == 1) {
                width = (width * 2.0f) / 3.0f;
            } else if (beautySticker.getType() == 2) {
                width /= 2.0f;
            } else if (beautySticker.getType() == 4) {
                width /= 2.0f;
            } else if (beautySticker.getType() == 10) {
                width /= 2.0f;
                f = (f * 2.0f) / 3.0f;
            } else if (beautySticker.getType() == 11) {
                width /= 2.0f;
                f = (f * 3.0f) / 2.0f;
            }
        } else {
            f = (i & 2) > 0 ? height / 4.0f : (i & 16) > 0 ? height * 0.75f : height / 2.0f;
            width = (i & 4) > 0 ? width / 4.0f : (i & 8) > 0 ? width * 0.75f : width / 2.0f;
        }
        sticker.getMatrix().postTranslate(width, f);
    }

    public void editTextSticker() {
        this.onStickerOperationListener.onStickerDoubleTap(this.handlingSticker);
    }

    @NonNull
    public float[] getStickerPoints(@Nullable Sticker sticker) {
        float[] fArr = new float[8];
        getStickerPoints(sticker, fArr);
        return fArr;
    }

    public void getStickerPoints(@Nullable Sticker sticker, @NonNull float[] fArr) {
        if (sticker == null) {
            Arrays.fill(fArr, 0.0f);
            return;
        }
        sticker.getBoundPoints(this.bounds);
        sticker.getMappedPoints(fArr, this.bounds);
    }

    public void save(@NonNull File file) {
        try {
            StickerUtils.saveImageToGallery(file, createBitmap());
            StickerUtils.notifySystemGallery(getContext(), file);
        } catch (IllegalArgumentException | IllegalStateException unused) {
        }
    }

    @NonNull
    public Bitmap createBitmap() throws OutOfMemoryError {
        this.handlingSticker = null;
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public int getStickerCount() {
        return this.stickers.size();
    }

    public boolean isNoneSticker() {
        return getStickerCount() == 0;
    }

    @NonNull
    public PhotoStickerView setLocked(boolean z) {
        this.locked = z;
        invalidate();
        return this;
    }

    @NonNull
    public PhotoStickerView setMinClickDelayTime(int i) {
        this.minClickDelayTime = i;
        return this;
    }

    public int getMinClickDelayTime() {
        return this.minClickDelayTime;
    }

    public boolean isConstrained() {
        return this.constrained;
    }

    @NonNull
    public PhotoStickerView setConstrained(boolean z) {
        this.constrained = z;
        postInvalidate();
        return this;
    }

    @NonNull
    public PhotoStickerView setOnStickerOperationListener(@Nullable OnStickerOperationListener onStickerOperationListener2) {
        this.onStickerOperationListener = onStickerOperationListener2;
        return this;
    }

    @Nullable
    public OnStickerOperationListener getOnStickerOperationListener() {
        return this.onStickerOperationListener;
    }

    @Nullable
    public Sticker getCurrentSticker() {
        return this.handlingSticker;
    }

    @NonNull
    public List<PhotoStickerIcons> getIcons() {
        return this.icons;
    }

    public void setIcons(@NonNull List<PhotoStickerIcons> list) {
        this.icons.clear();
        this.icons.addAll(list);
        invalidate();
    }
}
