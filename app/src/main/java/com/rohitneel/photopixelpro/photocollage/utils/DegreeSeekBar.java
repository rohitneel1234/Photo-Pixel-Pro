package com.rohitneel.photopixelpro.photocollage.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.ViewCompat;

public class DegreeSeekBar extends View {
    private static final String TAG = "DegreeSeekBar";
    private int mBaseLine;
    private final Rect mCanvasClipBounds;
    private int mCenterTextColor;
    private Paint mCirclePaint;
    private int mCurrentDegrees;
    private float mDragFactor;
    private Paint.FontMetricsInt mFontMetrics;
    private Path mIndicatorPath;
    private float mLastTouchedPosition;
    private int mMaxReachableDegrees;
    private int mMinReachableDegrees;
    private int mPointColor;
    private int mPointCount;
    private float mPointMargin;
    private Paint mPointPaint;
    private boolean mScrollStarted;
    private ScrollingListener mScrollingListener;
    private int mTextColor;
    private Paint mTextPaint;
    private float[] mTextWidths;
    private int mTotalScrollDistance;
    private String suffix;

    public interface ScrollingListener {
        void onScroll(int i);

        void onScrollEnd();

        void onScrollStart();
    }

    public DegreeSeekBar(Context context) {
        this(context, (AttributeSet) null);
    }

    public DegreeSeekBar(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DegreeSeekBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCanvasClipBounds = new Rect();
        this.mIndicatorPath = new Path();
        this.mCurrentDegrees = 0;
        this.mPointCount = 51;
        this.mPointColor = ViewCompat.MEASURED_STATE_MASK;
        this.mTextColor = ViewCompat.MEASURED_STATE_MASK;
        this.mCenterTextColor = ViewCompat.MEASURED_STATE_MASK;
        this.mDragFactor = 2.1f;
        this.mMinReachableDegrees = -100;
        this.mMaxReachableDegrees = 100;
        this.suffix = "";
        init();
    }

    @TargetApi(21)
    public DegreeSeekBar(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mCanvasClipBounds = new Rect();
        this.mIndicatorPath = new Path();
        this.mCurrentDegrees = 0;
        this.mPointCount = 51;
        this.mPointColor = ViewCompat.MEASURED_STATE_MASK;
        this.mTextColor = ViewCompat.MEASURED_STATE_MASK;
        this.mCenterTextColor = ViewCompat.MEASURED_STATE_MASK;
        this.mDragFactor = 2.1f;
        this.mMinReachableDegrees = -100;
        this.mMaxReachableDegrees = 100;
        this.suffix = "";
        init();
    }

    private void init() {
        this.mPointPaint = new Paint(1);
        this.mPointPaint.setStyle(Paint.Style.STROKE);
        this.mPointPaint.setColor(this.mPointColor);
        this.mPointPaint.setStrokeWidth(2.0f);
        this.mTextPaint = new Paint();
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setStyle(Paint.Style.FILL);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setTextSize(20.0f);
        this.mTextPaint.setTextAlign(Paint.Align.LEFT);
        this.mTextPaint.setAlpha(100);
        this.mFontMetrics = this.mTextPaint.getFontMetricsInt();
        this.mTextWidths = new float[1];
        this.mTextPaint.getTextWidths("0", this.mTextWidths);
        this.mCirclePaint = new Paint();
        this.mCirclePaint.setStyle(Paint.Style.FILL);
        this.mCirclePaint.setAlpha(255);
        this.mCirclePaint.setAntiAlias(true);
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mPointMargin = ((float) i) / ((float) this.mPointCount);
        this.mBaseLine = (((i2 - this.mFontMetrics.bottom) + this.mFontMetrics.top) / 2) - this.mFontMetrics.top;
        this.mIndicatorPath.moveTo((float) (i / 2), (float) (((i2 / 2) + (this.mFontMetrics.top / 2)) - 18));
        this.mIndicatorPath.rLineTo(-8.0f, -8.0f);
        this.mIndicatorPath.rLineTo(16.0f, 0.0f);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case 0:
                this.mLastTouchedPosition = motionEvent.getX();
                if (!this.mScrollStarted) {
                    this.mScrollStarted = true;
                    if (this.mScrollingListener != null) {
                        this.mScrollingListener.onScrollStart();
                        break;
                    }
                }
                break;
            case 1:
                this.mScrollStarted = false;
                if (this.mScrollingListener != null) {
                    this.mScrollingListener.onScrollEnd();
                }
                invalidate();
                break;
            case 2:
                float x = motionEvent.getX() - this.mLastTouchedPosition;
                if (this.mCurrentDegrees < this.mMaxReachableDegrees || x >= 0.0f) {
                    if (this.mCurrentDegrees > this.mMinReachableDegrees || x <= 0.0f) {
                        if (x != 0.0f) {
                            onScrollEvent(motionEvent, x);
                            break;
                        }
                    } else {
                        this.mCurrentDegrees = this.mMinReachableDegrees;
                        invalidate();
                        break;
                    }
                } else {
                    this.mCurrentDegrees = this.mMaxReachableDegrees;
                    invalidate();
                    break;
                }
                break;
        }
        return true;
    }

    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(this.mCanvasClipBounds);
        int i = (this.mPointCount / 2) + ((0 - this.mCurrentDegrees) / 2);
        this.mPointPaint.setColor(this.mPointColor);
        for (int i2 = 0; i2 < this.mPointCount; i2++) {
            if (i2 <= i - (Math.abs(this.mMinReachableDegrees) / 2) || i2 >= (Math.abs(this.mMaxReachableDegrees) / 2) + i || !this.mScrollStarted) {
                this.mPointPaint.setAlpha(100);
            } else {
                this.mPointPaint.setAlpha(255);
            }
            if (i2 > (this.mPointCount / 2) - 8 && i2 < (this.mPointCount / 2) + 8 && i2 > i - (Math.abs(this.mMinReachableDegrees) / 2) && i2 < (Math.abs(this.mMaxReachableDegrees) / 2) + i) {
                if (this.mScrollStarted) {
                    this.mPointPaint.setAlpha((Math.abs((this.mPointCount / 2) - i2) * 255) / 8);
                } else {
                    this.mPointPaint.setAlpha((Math.abs((this.mPointCount / 2) - i2) * 100) / 8);
                }
            }
            canvas.drawPoint(((float) this.mCanvasClipBounds.centerX()) + (((float) (i2 - (this.mPointCount / 2))) * this.mPointMargin), (float) this.mCanvasClipBounds.centerY(), this.mPointPaint);
            if (this.mCurrentDegrees != 0 && i2 == i) {
                if (this.mScrollStarted) {
                    this.mTextPaint.setAlpha(255);
                } else {
                    this.mTextPaint.setAlpha(192);
                }
                this.mPointPaint.setStrokeWidth(4.0f);
                canvas.drawPoint(((float) this.mCanvasClipBounds.centerX()) + (((float) (i2 - (this.mPointCount / 2))) * this.mPointMargin), (float) this.mCanvasClipBounds.centerY(), this.mPointPaint);
                this.mPointPaint.setStrokeWidth(2.0f);
                this.mTextPaint.setAlpha(100);
            }
        }
        for (int i3 = -100; i3 <= 100; i3 += 10) {
            if (i3 < this.mMinReachableDegrees || i3 > this.mMaxReachableDegrees) {
                drawDegreeText(i3, canvas, false);
            } else {
                drawDegreeText(i3, canvas, true);
            }
        }
        this.mTextPaint.setTextSize(22.0f);
        this.mTextPaint.setAlpha(255);
        this.mTextPaint.setColor(this.mCenterTextColor);
        if (this.mCurrentDegrees >= 10) {
            canvas.drawText(this.mCurrentDegrees + this.suffix, ((float) (getWidth() / 2)) - this.mTextWidths[0], (float) this.mBaseLine, this.mTextPaint);
        } else if (this.mCurrentDegrees <= -10) {
            canvas.drawText(this.mCurrentDegrees + this.suffix, ((float) (getWidth() / 2)) - ((this.mTextWidths[0] / 2.0f) * 3.0f), (float) this.mBaseLine, this.mTextPaint);
        } else if (this.mCurrentDegrees < 0) {
            canvas.drawText(this.mCurrentDegrees + this.suffix, ((float) (getWidth() / 2)) - this.mTextWidths[0], (float) this.mBaseLine, this.mTextPaint);
        } else {
            canvas.drawText(this.mCurrentDegrees + this.suffix, ((float) (getWidth() / 2)) - (this.mTextWidths[0] / 2.0f), (float) this.mBaseLine, this.mTextPaint);
        }
        this.mTextPaint.setAlpha(100);
        this.mTextPaint.setTextSize(20.0f);
        this.mTextPaint.setColor(this.mTextColor);
        this.mCirclePaint.setColor(this.mCenterTextColor);
        canvas.drawPath(this.mIndicatorPath, this.mCirclePaint);
        this.mCirclePaint.setColor(this.mCenterTextColor);
    }

    private void drawDegreeText(int i, Canvas canvas, boolean z) {
        if (!z) {
            this.mTextPaint.setAlpha(100);
        } else if (this.mScrollStarted) {
            this.mTextPaint.setAlpha(Math.min(255, (Math.abs(i - this.mCurrentDegrees) * 255) / 15));
            if (Math.abs(i - this.mCurrentDegrees) <= 7) {
                this.mTextPaint.setAlpha(0);
            }
        } else {
            this.mTextPaint.setAlpha(100);
            if (Math.abs(i - this.mCurrentDegrees) <= 7) {
                this.mTextPaint.setAlpha(0);
            }
        }
        if (i == 0) {
            if (Math.abs(this.mCurrentDegrees) >= 15 && !this.mScrollStarted) {
                this.mTextPaint.setAlpha(180);
            }
            canvas.drawText("0°", (((float) (getWidth() / 2)) - (this.mTextWidths[0] / 2.0f)) - (((float) (this.mCurrentDegrees / 2)) * this.mPointMargin), (float) ((getHeight() / 2) - 10), this.mTextPaint);
            return;
        }
        canvas.drawText(i + this.suffix, ((((float) (getWidth() / 2)) + ((this.mPointMargin * ((float) i)) / 2.0f)) - ((this.mTextWidths[0] / 2.0f) * 3.0f)) - (((float) (this.mCurrentDegrees / 2)) * this.mPointMargin), (float) ((getHeight() / 2) - 10), this.mTextPaint);
    }

    private void onScrollEvent(MotionEvent motionEvent, float f) {
        this.mTotalScrollDistance = (int) (((float) this.mTotalScrollDistance) - f);
        postInvalidate();
        this.mLastTouchedPosition = motionEvent.getX();
        this.mCurrentDegrees = (int) ((((float) this.mTotalScrollDistance) * this.mDragFactor) / this.mPointMargin);
        if (this.mScrollingListener != null) {
            this.mScrollingListener.onScroll(this.mCurrentDegrees);
        }
    }

    public void setDegreeRange(int i, int i2) {
        if (i > i2) {
            Log.e(TAG, "setDegreeRange: error, max must greater than min");
            return;
        }
        this.mMinReachableDegrees = i;
        this.mMaxReachableDegrees = i2;
        if (this.mCurrentDegrees > this.mMaxReachableDegrees || this.mCurrentDegrees < this.mMinReachableDegrees) {
            this.mCurrentDegrees = (this.mMinReachableDegrees + this.mMaxReachableDegrees) / 2;
        }
        this.mTotalScrollDistance = (int) ((((float) this.mCurrentDegrees) * this.mPointMargin) / this.mDragFactor);
        invalidate();
    }

    public void setCurrentDegrees(int i) {
        if (i <= this.mMaxReachableDegrees && i >= this.mMinReachableDegrees) {
            this.mCurrentDegrees = i;
            this.mTotalScrollDistance = (int) ((((float) i) * this.mPointMargin) / this.mDragFactor);
            invalidate();
        }
    }

    public void setScrollingListener(ScrollingListener scrollingListener) {
        this.mScrollingListener = scrollingListener;
    }

    public int getPointColor() {
        return this.mPointColor;
    }

    public void setPointColor(int i) {
        this.mPointColor = i;
        this.mPointPaint.setColor(this.mPointColor);
        postInvalidate();
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public void setTextColor(int i) {
        this.mTextColor = i;
        this.mTextPaint.setColor(i);
        postInvalidate();
    }

    public int getCenterTextColor() {
        return this.mCenterTextColor;
    }

    public void setCenterTextColor(int i) {
        this.mCenterTextColor = i;
        postInvalidate();
    }

    public float getDragFactor() {
        return this.mDragFactor;
    }

    public void setDragFactor(float f) {
        this.mDragFactor = f;
    }

    public void setSuffix(String str) {
        this.suffix = str;
    }
}
