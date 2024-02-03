package com.rohitneel.photopixelpro.photocollage.layer.straight;

import android.graphics.PointF;
import android.util.Pair;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

import java.util.ArrayList;
import java.util.List;

class StraightUtils {
    StraightUtils() {
    }

    static StraightLine createLine(StraightArea straightArea, PhotoLine.Direction direction, float f) {
        PointF pointF = new PointF();
        PointF pointF2 = new PointF();
        if (direction == PhotoLine.Direction.HORIZONTAL) {
            pointF.x = straightArea.left();
            pointF.y = (straightArea.height() * f) + straightArea.top();
            pointF2.x = straightArea.right();
            pointF2.y = (straightArea.height() * f) + straightArea.top();
        } else if (direction == PhotoLine.Direction.VERTICAL) {
            pointF.x = (straightArea.width() * f) + straightArea.left();
            pointF.y = straightArea.top();
            pointF2.x = (straightArea.width() * f) + straightArea.left();
            pointF2.y = straightArea.bottom();
        }
        StraightLine straightLine = new StraightLine(pointF, pointF2);
        if (direction == PhotoLine.Direction.HORIZONTAL) {
            straightLine.attachLineStart = straightArea.lineLeft;
            straightLine.attachLineEnd = straightArea.lineRight;
            straightLine.setUpperLine(straightArea.lineBottom);
            straightLine.setLowerLine(straightArea.lineTop);
        } else if (direction == PhotoLine.Direction.VERTICAL) {
            straightLine.attachLineStart = straightArea.lineTop;
            straightLine.attachLineEnd = straightArea.lineBottom;
            straightLine.setUpperLine(straightArea.lineRight);
            straightLine.setLowerLine(straightArea.lineLeft);
        }
        straightLine.setStartRatio(f);
        return straightLine;
    }

    static List<StraightArea> cutArea(StraightArea straightArea, StraightLine straightLine) {
        ArrayList arrayList = new ArrayList();
        if (straightLine.direction() == PhotoLine.Direction.HORIZONTAL) {
            StraightArea straightArea2 = new StraightArea(straightArea);
            straightArea2.lineBottom = straightLine;
            arrayList.add(straightArea2);
            StraightArea straightArea3 = new StraightArea(straightArea);
            straightArea3.lineTop = straightLine;
            arrayList.add(straightArea3);
        } else if (straightLine.direction() == PhotoLine.Direction.VERTICAL) {
            StraightArea straightArea4 = new StraightArea(straightArea);
            straightArea4.lineRight = straightLine;
            arrayList.add(straightArea4);
            StraightArea straightArea5 = new StraightArea(straightArea);
            straightArea5.lineLeft = straightLine;
            arrayList.add(straightArea5);
        }
        return arrayList;
    }

    static Pair<List<StraightLine>, List<StraightArea>> cutArea(StraightArea straightArea, int i, int i2) {
        int i3;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(i);
        StraightArea straightArea2 = new StraightArea(straightArea);
        for (int i4 = i + 1; i4 > 1; i4--) {
            StraightLine createLine = createLine(straightArea2, PhotoLine.Direction.HORIZONTAL, ((float) (i4 - 1)) / ((float) i4));
            arrayList2.add(createLine);
            straightArea2.lineBottom = createLine;
        }
        ArrayList arrayList3 = new ArrayList();
        StraightArea straightArea3 = new StraightArea(straightArea);
        int i5 = i2 + 1;
        while (true) {
            i3 = 0;
            if (i5 <= 1) {
                break;
            }
            StraightLine createLine2 = createLine(straightArea3, PhotoLine.Direction.VERTICAL, ((float) (i5 - 1)) / ((float) i5));
            arrayList3.add(createLine2);
            StraightArea straightArea4 = new StraightArea(straightArea3);
            straightArea4.lineLeft = createLine2;
            while (i3 <= arrayList2.size()) {
                StraightArea straightArea5 = new StraightArea(straightArea4);
                if (i3 == 0) {
                    straightArea5.lineTop = (StraightLine) arrayList2.get(i3);
                } else if (i3 == arrayList2.size()) {
                    straightArea5.lineBottom = (StraightLine) arrayList2.get(i3 - 1);
                } else {
                    straightArea5.lineTop = (StraightLine) arrayList2.get(i3);
                    straightArea5.lineBottom = (StraightLine) arrayList2.get(i3 - 1);
                }
                arrayList.add(straightArea5);
                i3++;
            }
            straightArea3.lineRight = createLine2;
            i5--;
        }
        while (i3 <= arrayList2.size()) {
            StraightArea straightArea6 = new StraightArea(straightArea3);
            if (i3 == 0) {
                straightArea6.lineTop = (StraightLine) arrayList2.get(i3);
            } else if (i3 == arrayList2.size()) {
                straightArea6.lineBottom = (StraightLine) arrayList2.get(i3 - 1);
            } else {
                straightArea6.lineTop = (StraightLine) arrayList2.get(i3);
                straightArea6.lineBottom = (StraightLine) arrayList2.get(i3 - 1);
            }
            arrayList.add(straightArea6);
            i3++;
        }
        ArrayList arrayList4 = new ArrayList();
        arrayList4.addAll(arrayList2);
        arrayList4.addAll(arrayList3);
        return new Pair<>(arrayList4, arrayList);
    }

    static List<StraightArea> cutAreaCross(StraightArea straightArea, StraightLine straightLine, StraightLine straightLine2) {
        ArrayList arrayList = new ArrayList();
        StraightArea straightArea2 = new StraightArea(straightArea);
        straightArea2.lineBottom = straightLine;
        straightArea2.lineRight = straightLine2;
        arrayList.add(straightArea2);
        StraightArea straightArea3 = new StraightArea(straightArea);
        straightArea3.lineBottom = straightLine;
        straightArea3.lineLeft = straightLine2;
        arrayList.add(straightArea3);
        StraightArea straightArea4 = new StraightArea(straightArea);
        straightArea4.lineTop = straightLine;
        straightArea4.lineRight = straightLine2;
        arrayList.add(straightArea4);
        StraightArea straightArea5 = new StraightArea(straightArea);
        straightArea5.lineTop = straightLine;
        straightArea5.lineLeft = straightLine2;
        arrayList.add(straightArea5);
        return arrayList;
    }

    static Pair<List<StraightLine>, List<StraightArea>> cutAreaSpiral(StraightArea straightArea) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        float width = straightArea.width();
        float height = straightArea.height();
        float left = straightArea.left();
        float pVar = straightArea.top();
        float f = height / 3.0f;
        float f2 = pVar + f;
        PointF pointF = new PointF(left, f2);
        float f3 = width / 3.0f;
        float f4 = (f3 * 2.0f) + left;
        PointF pointF2 = new PointF(f4, pVar);
        float f5 = (f * 2.0f) + pVar;
        PointF pointF3 = new PointF(width + left, f5);
        float f6 = left + f3;
        PointF pointF4 = new PointF(f6, pVar + height);
        PointF pointF5 = new PointF(f6, f2);
        PointF pointF6 = new PointF(f4, f2);
        PointF pointF7 = new PointF(f4, f5);
        PointF pointF8 = new PointF(f6, f5);
        StraightLine straightLine = new StraightLine(pointF, pointF6);
        StraightLine straightLine2 = new StraightLine(pointF2, pointF7);
        StraightLine straightLine3 = new StraightLine(pointF8, pointF3);
        StraightLine straightLine4 = new StraightLine(pointF5, pointF4);
        straightLine.setAttachLineStart(straightArea.lineLeft);
        straightLine.setAttachLineEnd(straightLine2);
        straightLine.setLowerLine(straightArea.lineTop);
        straightLine.setUpperLine(straightLine3);
        straightLine2.setAttachLineStart(straightArea.lineTop);
        straightLine2.setAttachLineEnd(straightLine3);
        straightLine2.setLowerLine(straightLine4);
        straightLine2.setUpperLine(straightArea.lineRight);
        straightLine3.setAttachLineStart(straightLine4);
        straightLine3.setAttachLineEnd(straightArea.lineRight);
        straightLine3.setLowerLine(straightLine);
        straightLine3.setUpperLine(straightArea.lineBottom);
        straightLine4.setAttachLineStart(straightLine);
        straightLine4.setAttachLineEnd(straightArea.lineBottom);
        straightLine4.setLowerLine(straightArea.lineLeft);
        straightLine4.setUpperLine(straightLine2);
        arrayList.add(straightLine);
        arrayList.add(straightLine2);
        arrayList.add(straightLine3);
        arrayList.add(straightLine4);
        StraightArea straightArea2 = new StraightArea(straightArea);
        straightArea2.lineRight = straightLine2;
        straightArea2.lineBottom = straightLine;
        arrayList2.add(straightArea2);
        StraightArea straightArea3 = new StraightArea(straightArea);
        straightArea3.lineLeft = straightLine2;
        straightArea3.lineBottom = straightLine3;
        arrayList2.add(straightArea3);
        StraightArea straightArea4 = new StraightArea(straightArea);
        straightArea4.lineRight = straightLine4;
        straightArea4.lineTop = straightLine;
        arrayList2.add(straightArea4);
        StraightArea straightArea5 = new StraightArea(straightArea);
        straightArea5.lineTop = straightLine;
        straightArea5.lineRight = straightLine2;
        straightArea5.lineLeft = straightLine4;
        straightArea5.lineBottom = straightLine3;
        arrayList2.add(straightArea5);
        StraightArea straightArea6 = new StraightArea(straightArea);
        straightArea6.lineLeft = straightLine4;
        straightArea6.lineTop = straightLine3;
        arrayList2.add(straightArea6);
        return new Pair<>(arrayList, arrayList2);
    }
}
