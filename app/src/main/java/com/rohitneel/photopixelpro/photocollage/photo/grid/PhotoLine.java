package com.rohitneel.photopixelpro.photocollage.photo.grid;

import android.graphics.PointF;

public interface PhotoLine {

    enum Direction {
        HORIZONTAL,
        VERTICAL
    }

    PhotoLine attachEndLine();

    PhotoLine attachStartLine();

    boolean contains(float f, float f2, float f3);

    Direction direction();

    PointF endPoint();

    float getEndRatio();

    float getStartRatio();

    float length();

    PhotoLine lowerLine();

    float maxX();

    float maxY();

    float minX();

    float minY();

    boolean move(float f, float f2);

    void offset(float f, float f2);

    void prepareMove();

    void setEndRatio(float f);

    void setLowerLine(PhotoLine line);

    void setStartRatio(float f);

    void setUpperLine(PhotoLine line);

    float slope();

    PointF startPoint();

    void update(float f, float f2);

    PhotoLine upperLine();
}
