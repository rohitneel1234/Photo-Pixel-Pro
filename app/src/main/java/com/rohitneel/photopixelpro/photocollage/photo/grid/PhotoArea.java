package com.rohitneel.photopixelpro.photocollage.photo.grid;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.List;

public interface PhotoArea {
    float bottom();

    float centerX();

    float centerY();

    boolean contains(float f, float f2);

    boolean contains(PointF pointF);

    boolean contains(PhotoLine line);

    Path getAreaPath();

    RectF getAreaRect();

    PointF getCenterPoint();

    PointF[] getHandleBarPoints(PhotoLine line);

    List<PhotoLine> getLines();

    float getPaddingBottom();

    float getPaddingLeft();

    float getPaddingRight();

    float getPaddingTop();

    float height();

    float left();

    float radian();

    float right();

    void setPadding(float f);

    void setPadding(float f, float f2, float f3, float f4);

    void setRadian(float f);

    float top();

    float width();
}
