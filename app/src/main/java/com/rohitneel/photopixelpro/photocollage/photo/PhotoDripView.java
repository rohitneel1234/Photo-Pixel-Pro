package com.rohitneel.photopixelpro.photocollage.photo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.rohitneel.photopixelpro.photocollage.drip.imagescale.TouchListener;

public class PhotoDripView extends AppCompatImageView {
    TouchListener multiTouchListener;

    public PhotoDripView(Context context) {
        this(context, (AttributeSet) null);
    }

    public PhotoDripView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
        setPadding(0, 0, 0, 0);
    }

    public PhotoDripView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.multiTouchListener = null;
        initBorderPaint();
    }

    private void initBorderPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(-1);
        paint.setStrokeWidth(0.0f);
    }


    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void setOnTouchListenerCustom(TouchListener multiTouchListener2) {
        this.multiTouchListener = multiTouchListener2;
        setOnTouchListener(multiTouchListener2);
    }
}
