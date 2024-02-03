package com.rohitneel.photopixelpro.photocollage.photo.grid;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.rohitneel.photopixelpro.photocollage.utils.MatrixUtils;

public class PhotoGrid {
    private static final Xfermode SRC_IN = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private ValueAnimator animator;
    private PhotoArea area;
    private final PointF centerPoint;
    private Drawable drawable;
    private Rect drawableBounds;
    private float[] drawablePoints;
    private int duration = 300;
    private final RectF mappedBounds;
    private final PointF mappedCenterPoint;
    private float[] mappedDrawablePoints;
    private Matrix matrix;
    private String path = "";
    private Matrix previousMatrix;
    private float previousMoveX;
    private float previousMoveY;
    private Matrix tempMatrix;

    public PhotoGrid(Drawable drawable2, PhotoArea area2, Matrix matrix2) {
        this.drawable = drawable2;
        this.area = area2;
        this.matrix = matrix2;
        this.previousMatrix = new Matrix();
        this.drawableBounds = new Rect(0, 0, getWidth(), getHeight());
        this.drawablePoints = new float[]{0.0f, 0.0f, (float) getWidth(), 0.0f, (float) getWidth(), (float) getHeight(), 0.0f, (float) getHeight()};
        this.mappedDrawablePoints = new float[8];
        this.mappedBounds = new RectF();
        this.centerPoint = new PointF(area2.centerX(), area2.centerY());
        this.mappedCenterPoint = new PointF();
        this.animator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.animator.setInterpolator(new DecelerateInterpolator());
        this.tempMatrix = new Matrix();
    }


    public void draw(Canvas canvas, boolean z) {
        draw(canvas, 255, true, z);
    }


    public void draw(Canvas canvas, int i, boolean z) {
        draw(canvas, i, false, z);
    }

    private void draw(Canvas canvas, int i, boolean z, boolean z2) {
        if (!(this.drawable instanceof BitmapDrawable) || z2) {
            canvas.save();
            if (z) {
                canvas.clipPath(this.area.getAreaPath());
            }
            canvas.concat(this.matrix);
            this.drawable.setBounds(this.drawableBounds);
            this.drawable.setAlpha(i);
            this.drawable.draw(canvas);
            canvas.restore();
            return;
        }

        int saveLayer = canvas.saveLayer(null,null);
        Bitmap bitmap = ((BitmapDrawable) this.drawable).getBitmap();
        Paint paint = ((BitmapDrawable) this.drawable).getPaint();
        paint.setColor(-1);
        paint.setAlpha(i);
        if (z) {
            canvas.drawPath(this.area.getAreaPath(), paint);
            paint.setXfermode(SRC_IN);
        }
        canvas.drawBitmap(bitmap, this.matrix, paint);
        paint.setXfermode((Xfermode) null);
        canvas.restoreToCount(saveLayer);
    }

    public PhotoArea getArea() {
        return this.area;
    }

    public void setDrawable(Drawable drawable2) {
        this.drawable = drawable2;
        this.drawableBounds = new Rect(0, 0, getWidth(), getHeight());
        this.drawablePoints = new float[]{0.0f, 0.0f, (float) getWidth(), 0.0f, (float) getWidth(), (float) getHeight(), 0.0f, (float) getHeight()};
    }

    public Drawable getDrawable() {
        return this.drawable;
    }

    public int getWidth() {
        return this.drawable.getIntrinsicWidth();
    }

    public int getHeight() {
        return this.drawable.getIntrinsicHeight();
    }

    public boolean contains(float f, float f2) {
        return this.area.contains(f, f2);
    }

    public boolean contains(PhotoLine line) {
        return this.area.contains(line);
    }

    public Rect getDrawableBounds() {
        return this.drawableBounds;
    }


    public void setPreviousMoveX(float f) {
        this.previousMoveX = f;
    }


    public void setPreviousMoveY(float f) {
        this.previousMoveY = f;
    }

    private RectF getCurrentDrawableBounds() {
        this.matrix.mapRect(this.mappedBounds, new RectF(this.drawableBounds));
        return this.mappedBounds;
    }

    private PointF getCurrentDrawableCenterPoint() {
        getCurrentDrawableBounds();
        this.mappedCenterPoint.x = this.mappedBounds.centerX();
        this.mappedCenterPoint.y = this.mappedBounds.centerY();
        return this.mappedCenterPoint;
    }

    public PointF getAreaCenterPoint() {
        this.centerPoint.x = this.area.centerX();
        this.centerPoint.y = this.area.centerY();
        return this.centerPoint;
    }

    private float getMatrixScale() {
        return MatrixUtils.getMatrixScale(this.matrix);
    }


    public float getMatrixAngle() {
        return MatrixUtils.getMatrixAngle(this.matrix);
    }


    public float[] getCurrentDrawablePoints() {
        this.matrix.mapPoints(this.mappedDrawablePoints, this.drawablePoints);
        return this.mappedDrawablePoints;
    }


    public boolean isFilledArea() {
        RectF currentDrawableBounds = getCurrentDrawableBounds();
        return currentDrawableBounds.left <= this.area.left() && currentDrawableBounds.top <= this.area.top() && currentDrawableBounds.right >= this.area.right() && currentDrawableBounds.bottom >= this.area.bottom();
    }


    public boolean canFilledArea() {
        return MatrixUtils.getMatrixScale(this.matrix) >= MatrixUtils.getMinMatrixScale(this);
    }


    public void record() {
        this.previousMatrix.set(this.matrix);
    }


    public void translate(float f, float f2) {
        this.matrix.set(this.previousMatrix);
        postTranslate(f, f2);
    }


    public void zoom(float f, float f2, PointF pointF) {
        this.matrix.set(this.previousMatrix);
        postScale(f, f2, pointF);
    }


    public void zoomAndTranslate(float f, float f2, PointF pointF, float f3, float f4) {
        this.matrix.set(this.previousMatrix);
        postTranslate(f3, f4);
        postScale(f, f2, pointF);
    }


    public void set(Matrix matrix2) {
        this.matrix.set(matrix2);
        moveToFillArea((View) null);
    }


    public void postTranslate(float f, float f2) {
        this.matrix.postTranslate(f, f2);
    }


    public void postScale(float f, float f2, PointF pointF) {
        this.matrix.postScale(f, f2, pointF.x, pointF.y);
    }


    public void postFlipVertically() {
        this.matrix.postScale(1.0f, -1.0f, this.area.centerX(), this.area.centerY());
    }


    public void postFlipHorizontally() {
        this.matrix.postScale(-1.0f, 1.0f, this.area.centerX(), this.area.centerY());
    }


    public void postRotate(float f) {
        this.matrix.postRotate(f, this.area.centerX(), this.area.centerY());
        float minMatrixScale = MatrixUtils.getMinMatrixScale(this);
        if (getMatrixScale() < minMatrixScale) {
            PointF pointF = new PointF();
            pointF.set(getCurrentDrawableCenterPoint());
            postScale(minMatrixScale / getMatrixScale(), minMatrixScale / getMatrixScale(), pointF);
        }
        if (!MatrixUtils.judgeIsImageContainsBorder(this, getMatrixAngle())) {
            float[] calculateImageIndents = MatrixUtils.calculateImageIndents(this);
            postTranslate(-(calculateImageIndents[0] + calculateImageIndents[2]), -(calculateImageIndents[1] + calculateImageIndents[3]));
        }
    }

    private void animateTranslate(final View view, final float f, final float f2) {
        this.animator.end();
        this.animator.removeAllUpdateListeners();
        this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PhotoGrid.this.translate(f * ((Float) valueAnimator.getAnimatedValue()).floatValue(), f2 * ((Float) valueAnimator.getAnimatedValue()).floatValue());
                view.invalidate();
            }
        });
        this.animator.setDuration(this.duration);
        this.animator.start();
    }


    public void moveToFillArea(View view) {
        if (!isFilledArea()) {
            record();
            RectF currentDrawableBounds = getCurrentDrawableBounds();
            float f = 0.0f;
            float left = currentDrawableBounds.left > this.area.left() ? this.area.left() - currentDrawableBounds.left : 0.0f;
            if (currentDrawableBounds.top > this.area.top()) {
                f = this.area.top() - currentDrawableBounds.top;
            }
            if (currentDrawableBounds.right < this.area.right()) {
                left = this.area.right() - currentDrawableBounds.right;
            }
            if (currentDrawableBounds.bottom < this.area.bottom()) {
                f = this.area.bottom() - currentDrawableBounds.bottom;
            }
            if (view == null) {
                postTranslate(left, f);
            } else {
                animateTranslate(view, left, f);
            }
        }
    }


    public void fillArea(View view, boolean z) {
        if (!isFilledArea()) {
            record();
            final float matrixScale = getMatrixScale();
            final float minMatrixScale = MatrixUtils.getMinMatrixScale(this);
            final PointF pointF = new PointF();
            pointF.set(getCurrentDrawableCenterPoint());
            this.tempMatrix.set(this.matrix);
            float f = minMatrixScale / matrixScale;
            this.tempMatrix.postScale(f, f, pointF.x, pointF.y);
            RectF rectF = new RectF(this.drawableBounds);
            this.tempMatrix.mapRect(rectF);
            float f2 = 0.0f;
            float left = rectF.left > this.area.left() ? this.area.left() - rectF.left : 0.0f;
            if (rectF.top > this.area.top()) {
                f2 = this.area.top() - rectF.top;
            }
            if (rectF.right < this.area.right()) {
                left = this.area.right() - rectF.right;
            }
            float f3 = left;
            float bottom = rectF.bottom < this.area.bottom() ? this.area.bottom() - rectF.bottom : f2;
            this.animator.end();
            this.animator.removeAllUpdateListeners();
            final float f4 = f3;
            final float f5 = bottom;
            final View view2 = view;
            this.animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    float f = (matrixScale + ((minMatrixScale - matrixScale) * floatValue)) / matrixScale;
                    float f2 = f4 * floatValue;
                    float f3 = f5 * floatValue;
                    PhotoGrid.this.zoom(f, f, pointF);
                    PhotoGrid.this.postTranslate(f2, f3);
                    view2.invalidate();
                }
            });
            if (z) {
                this.animator.setDuration(0);
            } else {
                this.animator.setDuration((long) this.duration);
            }
            this.animator.start();
        }
    }


    public void updateWith(MotionEvent motionEvent, PhotoLine line) {
        float x = (motionEvent.getX() - this.previousMoveX) / 2.0f;
        float y = (motionEvent.getY() - this.previousMoveY) / 2.0f;
        if (!canFilledArea()) {
            PhotoArea area2 = getArea();
            float minMatrixScale = MatrixUtils.getMinMatrixScale(this) / getMatrixScale();
            postScale(minMatrixScale, minMatrixScale, area2.getCenterPoint());
            record();
            this.previousMoveX = motionEvent.getX();
            this.previousMoveY = motionEvent.getY();
        }
        if (line.direction() == PhotoLine.Direction.HORIZONTAL) {
            translate(0.0f, y);
        } else if (line.direction() == PhotoLine.Direction.VERTICAL) {
            translate(x, 0.0f);
        }
        RectF currentDrawableBounds = getCurrentDrawableBounds();
        PhotoArea area3 = getArea();
        float pVar = currentDrawableBounds.top > area3.top() ? area3.top() - currentDrawableBounds.top : 0.0f;
        if (currentDrawableBounds.bottom < area3.bottom()) {
            pVar = area3.bottom() - currentDrawableBounds.bottom;
        }
        float left = currentDrawableBounds.left > area3.left() ? area3.left() - currentDrawableBounds.left : 0.0f;
        if (currentDrawableBounds.right < area3.right()) {
            left = area3.right() - currentDrawableBounds.right;
        }
        if (left != 0.0f || pVar != 0.0f) {
            this.previousMoveX = motionEvent.getX();
            this.previousMoveY = motionEvent.getY();
            postTranslate(left, pVar);
            record();
        }
    }

    public void setArea(PhotoArea area2) {
        this.area = area2;
    }


    public boolean isAnimateRunning() {
        return this.animator.isRunning();
    }


    public void setAnimateDuration(int i) {
        this.duration = i;
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getPath() {
        return this.path;
    }
}
