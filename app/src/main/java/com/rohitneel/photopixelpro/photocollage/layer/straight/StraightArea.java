package com.rohitneel.photopixelpro.photocollage.layer.straight;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoArea;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class StraightArea implements PhotoArea {
    private Path areaPath;
    private RectF areaRect;
    private PointF[] handleBarPoints;
    StraightLine lineBottom;
    StraightLine lineLeft;
    StraightLine lineRight;
    StraightLine lineTop;
    private float paddingBottom;
    private float paddingLeft;
    private float paddingRight;
    private float paddingTop;
    private float radian;

    StraightArea() {
        this.areaPath = new Path();
        this.areaRect = new RectF();
        this.handleBarPoints = new PointF[2];
        this.handleBarPoints[0] = new PointF();
        this.handleBarPoints[1] = new PointF();
    }


    StraightArea(StraightArea straightArea) {
        this.areaPath = new Path();
        this.areaRect = new RectF();
        this.handleBarPoints = new PointF[2];
        this.lineLeft = straightArea.lineLeft;
        this.lineTop = straightArea.lineTop;
        this.lineRight = straightArea.lineRight;
        this.lineBottom = straightArea.lineBottom;
        this.handleBarPoints[0] = new PointF();
        this.handleBarPoints[1] = new PointF();
    }

    public float left() {
        return this.lineLeft.minX() + this.paddingLeft;
    }

    public float top() {
        return this.lineTop.minY() + this.paddingTop;
    }

    public float right() {
        return this.lineRight.maxX() - this.paddingRight;
    }

    public float bottom() {
        return this.lineBottom.maxY() - this.paddingBottom;
    }

    public float centerX() {
        return (left() + right()) / 2.0f;
    }

    public float centerY() {
        return (top() + bottom()) / 2.0f;
    }

    public float width() {
        return right() - left();
    }

    public float height() {
        return bottom() - top();
    }

    public PointF getCenterPoint() {
        return new PointF(centerX(), centerY());
    }

    public boolean contains(PointF pointF) {
        return contains(pointF.x, pointF.y);
    }

    public boolean contains(float f, float f2) {
        return getAreaRect().contains(f, f2);
    }

    public boolean contains(PhotoLine line) {
        return this.lineLeft == line || this.lineTop == line || this.lineRight == line || this.lineBottom == line;
    }

    public Path getAreaPath() {
        this.areaPath.reset();
        this.areaPath.addRoundRect(getAreaRect(), this.radian, this.radian, Path.Direction.CCW);
        return this.areaPath;
    }

    public RectF getAreaRect() {
        this.areaRect.set(left(), top(), right(), bottom());
        return this.areaRect;
    }

    public List<PhotoLine> getLines() {
        return Arrays.asList(new PhotoLine[]{this.lineLeft, this.lineTop, this.lineRight, this.lineBottom});
    }

    public PointF[] getHandleBarPoints(PhotoLine line) {
        if (line == this.lineLeft) {
            this.handleBarPoints[0].x = left();
            this.handleBarPoints[0].y = top() + (height() / 4.0f);
            this.handleBarPoints[1].x = left();
            this.handleBarPoints[1].y = top() + ((height() / 4.0f) * 3.0f);
        } else if (line == this.lineTop) {
            this.handleBarPoints[0].x = left() + (width() / 4.0f);
            this.handleBarPoints[0].y = top();
            this.handleBarPoints[1].x = left() + ((width() / 4.0f) * 3.0f);
            this.handleBarPoints[1].y = top();
        } else if (line == this.lineRight) {
            this.handleBarPoints[0].x = right();
            this.handleBarPoints[0].y = top() + (height() / 4.0f);
            this.handleBarPoints[1].x = right();
            this.handleBarPoints[1].y = top() + ((height() / 4.0f) * 3.0f);
        } else if (line == this.lineBottom) {
            this.handleBarPoints[0].x = left() + (width() / 4.0f);
            this.handleBarPoints[0].y = bottom();
            this.handleBarPoints[1].x = left() + ((width() / 4.0f) * 3.0f);
            this.handleBarPoints[1].y = bottom();
        }
        return this.handleBarPoints;
    }

    public float radian() {
        return this.radian;
    }

    public void setRadian(float f) {
        this.radian = f;
    }

    public float getPaddingLeft() {
        return this.paddingLeft;
    }

    public float getPaddingTop() {
        return this.paddingTop;
    }

    public float getPaddingRight() {
        return this.paddingRight;
    }

    public float getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPadding(float f) {
        setPadding(f, f, f, f);
    }

    public void setPadding(float f, float f2, float f3, float f4) {
        this.paddingLeft = f;
        this.paddingTop = f2;
        this.paddingRight = f3;
        this.paddingBottom = f4;
    }

    static class AreaComparator implements Comparator<StraightArea> {
        AreaComparator() {
        }

        public int compare(StraightArea straightArea, StraightArea straightArea2) {
            if (straightArea.top() < straightArea2.top()) {
                return -1;
            }
            if (straightArea.top() != straightArea2.top()) {
                return 1;
            }
            if (straightArea.left() < straightArea2.left()) {
                return -1;
            }
            if (straightArea.left() == straightArea2.left()) {
                return 0;
            }
            return 1;
        }
    }
}
