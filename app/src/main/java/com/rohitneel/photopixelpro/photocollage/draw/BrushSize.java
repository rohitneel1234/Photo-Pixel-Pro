package com.rohitneel.photopixelpro.photocollage.draw;

import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;

import androidx.core.view.ViewCompat;

public class BrushSize {
    private Paint paintInner;
    private Paint paintOuter = new Paint();
    private Path path;

    public BrushSize() {
        this.paintOuter.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.paintOuter.setStrokeWidth(2.0f);
        this.paintOuter.setStyle(Style.STROKE);
        this.paintInner = new Paint();
        this.paintInner.setColor(-7829368);
        this.paintInner.setStrokeWidth(2.0f);
        this.paintInner.setStyle(Style.FILL);
        this.path = new Path();
    }

    public void setCircle(float f, float f2, float f3, Direction direction) {
        this.path.reset();
        this.path.addCircle(f, f2, f3, direction);
    }

    public Path getPath() {
        return this.path;
    }

    public Paint getPaint() {
        return this.paintOuter;
    }

    public Paint getInnerPaint() {
        return this.paintInner;
    }

    public void setPaintOpacity(int i) {
        this.paintInner.setAlpha(i);
    }
}
