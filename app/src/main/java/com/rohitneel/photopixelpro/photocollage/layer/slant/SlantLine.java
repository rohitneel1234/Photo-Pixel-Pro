package com.rohitneel.photopixelpro.photocollage.layer.slant;

import android.graphics.PointF;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

class SlantLine implements PhotoLine {
    SlantLine attachLineEnd;
    SlantLine attachLineStart;
    public final Direction direction;
    CrossoverPointF end;
    private float endRatio;
    PhotoLine lowerLine;
    private PointF previousEnd = new PointF();
    private PointF previousStart = new PointF();
    CrossoverPointF start;
    private float startRatio;
    PhotoLine upperLine;

    SlantLine(Direction direction2) {
        this.direction = direction2;
    }

    SlantLine(CrossoverPointF crossoverPointF, CrossoverPointF crossoverPointF2, Direction direction2) {
        this.start = crossoverPointF;
        this.end = crossoverPointF2;
        this.direction = direction2;
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

    public Direction direction() {
        return this.direction;
    }

    public float slope() {
        return SlantUtils.calculateSlope(this);
    }

    public boolean contains(float f, float f2, float f3) {
        return SlantUtils.contains(this, f, f2, f3);
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

    public void prepareMove() {
        this.previousStart.set(this.start);
        this.previousEnd.set(this.end);
    }

    public void update(float f, float f2) {
        SlantUtils.intersectionOfLines(this.start, this, this.attachLineStart);
        SlantUtils.intersectionOfLines(this.end, this, this.attachLineEnd);
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
