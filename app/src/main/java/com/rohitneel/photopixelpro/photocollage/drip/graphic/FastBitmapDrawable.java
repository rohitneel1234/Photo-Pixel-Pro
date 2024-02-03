package com.rohitneel.photopixelpro.photocollage.drip.graphic;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

public class FastBitmapDrawable extends Drawable implements IBitmapDrawable {
    protected Bitmap mBitmap;
    protected Paint mPaint;

    @SuppressLint("WrongConstant")
    public int getOpacity() {
        return -3;
    }

    public FastBitmapDrawable(Bitmap bitmap) {
        this.mBitmap = bitmap;
        this.mPaint = new Paint();
        this.mPaint.setDither(true);
        this.mPaint.setFilterBitmap(true);
    }

    public void draw(Canvas canvas) {
        if (this.mBitmap != null && !this.mBitmap.isRecycled()) {
            canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mPaint);
        }
    }

    public void setAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    public int getIntrinsicWidth() {
        return this.mBitmap.getWidth();
    }

    public int getIntrinsicHeight() {
        return this.mBitmap.getHeight();
    }

    public int getMinimumWidth() {
        return this.mBitmap.getWidth();
    }

    public int getMinimumHeight() {
        return this.mBitmap.getHeight();
    }

    public void setAntiAlias(boolean z) {
        this.mPaint.setAntiAlias(z);
        invalidateSelf();
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }
}
