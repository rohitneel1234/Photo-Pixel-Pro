package com.rohitneel.photopixelpro.photocollage.photo;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;

public class PhotoMotionView extends PhotoMotionViewTouchBase {
    protected int mDoubleTapDirection;
    protected boolean mDoubleTapEnabled = true;
    public OnImageViewTouchDoubleTapListener mDoubleTapListener;
    private OnImageFlingListener mFlingListener;
    protected GestureDetector mGestureDetector;
    protected GestureDetector.OnGestureListener mGestureListener;
    protected ScaleGestureDetector mScaleDetector;
    protected boolean mScaleEnabled = true;
    protected float mScaleFactor;
    protected ScaleGestureDetector.OnScaleGestureListener mScaleListener;
    protected boolean mScrollEnabled = true;

    public OnImageViewTouchSingleTapListener mSingleTapListener;
    protected int mTouchSlop;

    public interface OnImageFlingListener {
        void onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2);
    }

    public interface OnImageViewTouchDoubleTapListener {
        void onDoubleTap();
    }

    public interface OnImageViewTouchSingleTapListener {
        void onSingleTapConfirmed();
    }

    public PhotoMotionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    public void init() {
        super.init();
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        this.mGestureListener = getGestureListener();
        this.mScaleListener = getScaleListener();
        this.mScaleDetector = new ScaleGestureDetector(getContext(), this.mScaleListener);
        this.mGestureDetector = new GestureDetector(getContext(), this.mGestureListener, (Handler) null, true);
        this.mDoubleTapDirection = 1;
    }

    public GestureDetector.OnGestureListener getGestureListener() {
        return new GestureListener();
    }


    public ScaleGestureDetector.OnScaleGestureListener getScaleListener() {
        return new ScaleListener();
    }


    public void _setImageDrawable(Drawable drawable, Matrix matrix, float f, float f2) {
        super._setImageDrawable(drawable, matrix, f, f2);
        this.mScaleFactor = getMaxScale() / 3.0f;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        this.mScaleDetector.onTouchEvent(motionEvent);
        if (!this.mScaleDetector.isInProgress()) {
            this.mGestureDetector.onTouchEvent(motionEvent);
        }
        if ((motionEvent.getAction() & 255) == 1 && getScale() < getMinScale()) {
            zoomTo(getMinScale(), 500.0f);
        }
        return true;
    }


    public void onZoomAnimationCompleted(float f) {
        if (f < getMinScale()) {
            zoomTo(getMinScale(), 50.0f);
        }
    }


    public float onDoubleTapPost(float f, float f2) {
        if (this.mDoubleTapDirection != 1) {
            this.mDoubleTapDirection = 1;
            return 1.0f;
        } else if ((this.mScaleFactor * 2.0f) + f <= f2) {
            return f + this.mScaleFactor;
        } else {
            this.mDoubleTapDirection = -1;
            return f2;
        }
    }

    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!this.mScrollEnabled || motionEvent == null || motionEvent2 == null || motionEvent.getPointerCount() > 1 || motionEvent2.getPointerCount() > 1 || this.mScaleDetector.isInProgress() || getScale() == 1.0f) {
            return false;
        }
        this.mUserScaled = true;
        scrollBy(-f, -f2);
        invalidate();
        return true;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!this.mScrollEnabled) {
            return false;
        }
        if (this.mFlingListener != null) {
            this.mFlingListener.onFling(motionEvent, motionEvent2, f, f2);
        }
        if (motionEvent.getPointerCount() > 1 || motionEvent2.getPointerCount() > 1 || this.mScaleDetector.isInProgress() || getScale() == 1.0f) {
            return false;
        }
        float x = motionEvent2.getX() - motionEvent.getX();
        float y = motionEvent2.getY() - motionEvent.getY();
        if (Math.abs(f) <= 800.0f && Math.abs(f2) <= 800.0f) {
            return false;
        }
        this.mUserScaled = true;
        scrollBy(x / 2.0f, y / 2.0f, 300.0d);
        invalidate();
        return true;
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        public GestureListener() {
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (PhotoMotionView.this.mSingleTapListener != null) {
                PhotoMotionView.this.mSingleTapListener.onSingleTapConfirmed();
            }
            return super.onSingleTapConfirmed(motionEvent);
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            Log.i(PhotoMotionViewTouchBase.LOG_TAG, "onDoubleTap. double tap enabled? " + PhotoMotionView.this.mDoubleTapEnabled);
            if (PhotoMotionView.this.mDoubleTapEnabled) {
                PhotoMotionView.this.mUserScaled = true;
                PhotoMotionView.this.zoomTo(Math.min(PhotoMotionView.this.getMaxScale(), Math.max(PhotoMotionView.this.onDoubleTapPost(PhotoMotionView.this.getScale(), PhotoMotionView.this.getMaxScale()), PhotoMotionView.this.getMinScale())), motionEvent.getX(), motionEvent.getY(), 200.0f);
                PhotoMotionView.this.invalidate();
            }
            if (PhotoMotionView.this.mDoubleTapListener != null) {
                PhotoMotionView.this.mDoubleTapListener.onDoubleTap();
            }
            return super.onDoubleTap(motionEvent);
        }

        public void onLongPress(MotionEvent motionEvent) {
            if (PhotoMotionView.this.isLongClickable() && !PhotoMotionView.this.mScaleDetector.isInProgress()) {
                PhotoMotionView.this.setPressed(true);
                PhotoMotionView.this.performLongClick();
            }
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return PhotoMotionView.this.onScroll(motionEvent, motionEvent2, f, f2);
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return PhotoMotionView.this.onFling(motionEvent, motionEvent2, f, f2);
        }
    }

    public class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        protected boolean mScaled = false;

        public ScaleListener() {
        }

        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            float currentSpan = scaleGestureDetector.getCurrentSpan() - scaleGestureDetector.getPreviousSpan();
            float scale = PhotoMotionView.this.getScale() * scaleGestureDetector.getScaleFactor();
            if (PhotoMotionView.this.mScaleEnabled) {
                if (this.mScaled && currentSpan != 0.0f) {
                    PhotoMotionView.this.mUserScaled = true;
                    PhotoMotionView.this.zoomTo(Math.min(PhotoMotionView.this.getMaxScale(), Math.max(scale, PhotoMotionView.this.getMinScale() - 0.1f)), scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                    PhotoMotionView.this.mDoubleTapDirection = 1;
                    PhotoMotionView.this.invalidate();
                    return true;
                } else if (!this.mScaled) {
                    this.mScaled = true;
                }
            }
            return true;
        }
    }

}
