package com.rohitneel.photopixelpro.photocollage.layer.straight;

import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

class StraightLine implements PhotoLine {
    StraightLine attachLineEnd;
    StraightLine attachLineStart;
    private RectF bounds = new RectF();
    public Direction direction = Direction.HORIZONTAL;
    private PointF end;
    private float endRatio;
    private PhotoLine lowerLine;
    private PointF previousEnd = new PointF();
    private PointF previousStart = new PointF();
    private PointF start;
    private float startRatio;
    private PhotoLine upperLine;

    StraightLine(PointF pointF, PointF pointF2) {
        this.start = pointF;
        this.end = pointF2;
        if (pointF.x == pointF2.x) {
            this.direction = Direction.VERTICAL;
        } else if (pointF.y == pointF2.y) {
            this.direction = Direction.HORIZONTAL;
        } else {
            Log.d("StraightLine", "StraightLine: current only support two direction");
        }
    }

    public void setStartRatio(float f) {
        this.startRatio = f;
    }

    public float getStartRatio() {
        return this.startRatio;
    }

    public void setEndRatio(float f) {
        this.endRatio = f;
    }

    public float getEndRatio() {
        return this.endRatio;
    }

    public float length() {
        return (float) Math.sqrt(Math.pow((double) (this.end.x - this.start.x), 2.0d) + Math.pow((double) (this.end.y - this.start.y), 2.0d));
    }

    public PointF startPoint() {
        return this.start;
    }

    public PointF endPoint() {
        return this.end;
    }

    public PhotoLine lowerLine() {
        return this.lowerLine;
    }

    public PhotoLine upperLine() {
        return this.upperLine;
    }

    public PhotoLine attachStartLine() {
        return this.attachLineStart;
    }

    public PhotoLine attachEndLine() {
        return this.attachLineEnd;
    }

    public void setLowerLine(PhotoLine line) {
        this.lowerLine = line;
    }

    public void setUpperLine(PhotoLine line) {
        this.upperLine = line;
    }


    public void setAttachLineStart(StraightLine straightLine) {
        this.attachLineStart = straightLine;
    }


    public void setAttachLineEnd(StraightLine straightLine) {
        this.attachLineEnd = straightLine;
    }

    public Direction direction() {
        return this.direction;
    }

    public float slope() {
        return this.direction == Direction.HORIZONTAL ? 0.0f : Float.MAX_VALUE;
    }

    public boolean contains(float f, float f2, float f3) {
        if (this.direction == Direction.HORIZONTAL) {
            this.bounds.left = this.start.x;
            this.bounds.right = this.end.x;
            float f4 = f3 / 2.0f;
            this.bounds.top = this.start.y - f4;
            this.bounds.bottom = this.start.y + f4;
        } else if (this.direction == Direction.VERTICAL) {
            this.bounds.top = this.start.y;
            this.bounds.bottom = this.end.y;
            float f5 = f3 / 2.0f;
            this.bounds.left = this.start.x - f5;
            this.bounds.right = this.start.x + f5;
        }
        return this.bounds.contains(f, f2);
    }

    public void prepareMove() {
        this.previousStart.set(this.start);
        this.previousEnd.set(this.end);
    }

    public boolean move(float f, float f2) {
        if (this.direction == Direction.HORIZONTAL) {
            if (this.previousStart.y + f < this.lowerLine.maxY() + f2 || this.previousStart.y + f > this.upperLine.minY() - f2 || this.previousEnd.y + f < this.lowerLine.maxY() + f2 || this.previousEnd.y + f > this.upperLine.minY() - f2) {
                return false;
            }
            this.start.y = this.previousStart.y + f;
            this.end.y = this.previousEnd.y + f;
            return true;
        } else if (this.previousStart.x + f < this.lowerLine.maxX() + f2 || this.previousStart.x + f > this.upperLine.minX() - f2 || this.previousEnd.x + f < this.lowerLine.maxX() + f2 || this.previousEnd.x + f > this.upperLine.minX() - f2) {
            return false;
        } else {
            this.start.x = this.previousStart.x + f;
            this.end.x = this.previousEnd.x + f;
            return true;
        }
    }

    public void update(float f, float f2) {
        if (this.direction == Direction.HORIZONTAL) {
            if (this.attachLineStart != null) {
                this.start.x = this.attachLineStart.getPosition();
            }
            if (this.attachLineEnd != null) {
                this.end.x = this.attachLineEnd.getPosition();
            }
        } else if (this.direction == Direction.VERTICAL) {
            if (this.attachLineStart != null) {
                this.start.y = this.attachLineStart.getPosition();
            }
            if (this.attachLineEnd != null) {
                this.end.y = this.attachLineEnd.getPosition();
            }
        }
    }

    public float getPosition() {
        if (this.direction == Direction.HORIZONTAL) {
            return this.start.y;
        }
        return this.start.x;
    }

    public float minX() {
        return Math.min(this.start.x, this.end.x);
    }

    public float maxX() {
        return Math.max(this.start.x, this.end.x);
    }

    public float minY() {
        return Math.min(this.start.y, this.end.y);
    }

    public float maxY() {
        return Math.max(this.start.y, this.end.y);
    }

    public void offset(float f, float f2) {
        this.start.offset(f, f2);
        this.end.offset(f, f2);
    }

    public String toString() {
        return "start --> " + this.start.toString() + ",end --> " + this.end.toString();
    }
}
