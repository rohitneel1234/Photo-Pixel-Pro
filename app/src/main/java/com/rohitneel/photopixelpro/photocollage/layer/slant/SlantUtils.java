package com.rohitneel.photopixelpro.photocollage.layer.slant;

import android.graphics.PointF;
import android.util.Pair;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

import java.util.ArrayList;
import java.util.List;

class SlantUtils {

    private static final PointF A = new PointF();

    private static final PointF AB = new PointF();

    private static final PointF AM = new PointF();

    private static final PointF B = new PointF();

    private static final PointF BC = new PointF();

    private static final PointF BM = new PointF();

    private static final PointF C = new PointF();

    private static final PointF CD = new PointF();

    private static final PointF CM = new PointF();

    private static final PointF D = new PointF();

    private static final PointF DA = new PointF();

    private static final PointF DM = new PointF();

    private SlantUtils() {
    }

    static float distance(PointF pointF, PointF pointF2) {
        return (float) Math.sqrt(Math.pow((pointF2.x - pointF.x), 2.0d) + Math.pow((pointF2.y - pointF.y), 2.0d));
    }

    static List<SlantArea> cutAreaWith(SlantArea slantArea, SlantLine slantLine) {
        ArrayList arrayList = new ArrayList();
        SlantArea slantArea2 = new SlantArea(slantArea);
        SlantArea slantArea3 = new SlantArea(slantArea);
        if (slantLine.direction == PhotoLine.Direction.HORIZONTAL) {
            slantArea2.lineBottom = slantLine;
            slantArea2.leftBottom = slantLine.start;
            slantArea2.rightBottom = slantLine.end;
            slantArea3.lineTop = slantLine;
            slantArea3.leftTop = slantLine.start;
            slantArea3.rightTop = slantLine.end;
        } else {
            slantArea2.lineRight = slantLine;
            slantArea2.rightTop = slantLine.start;
            slantArea2.rightBottom = slantLine.end;
            slantArea3.lineLeft = slantLine;
            slantArea3.leftTop = slantLine.start;
            slantArea3.leftBottom = slantLine.end;
        }
        arrayList.add(slantArea2);
        arrayList.add(slantArea3);
        return arrayList;
    }

    static SlantLine createLine(SlantArea slantArea, PhotoLine.Direction direction, float f, float f2) {
        SlantLine slantLine = new SlantLine(direction);
        slantLine.setEndRatio(f2);
        slantLine.setStartRatio(f);
        if (direction == PhotoLine.Direction.HORIZONTAL) {
            slantLine.start = getPoint(slantArea.leftTop, slantArea.leftBottom, PhotoLine.Direction.VERTICAL, f);
            slantLine.end = getPoint(slantArea.rightTop, slantArea.rightBottom, PhotoLine.Direction.VERTICAL, f2);
            slantLine.attachLineStart = slantArea.lineLeft;
            slantLine.attachLineEnd = slantArea.lineRight;
            slantLine.upperLine = slantArea.lineBottom;
            slantLine.lowerLine = slantArea.lineTop;
        } else {
            slantLine.start = getPoint(slantArea.leftTop, slantArea.rightTop, PhotoLine.Direction.HORIZONTAL, f);
            slantLine.end = getPoint(slantArea.leftBottom, slantArea.rightBottom, PhotoLine.Direction.HORIZONTAL, f2);
            slantLine.attachLineStart = slantArea.lineTop;
            slantLine.attachLineEnd = slantArea.lineBottom;
            slantLine.upperLine = slantArea.lineRight;
            slantLine.lowerLine = slantArea.lineLeft;
        }
        return slantLine;
    }

    static Pair<List<SlantLine>, List<SlantArea>> cutAreaWith(SlantArea slantArea, int i, int i2) {
        int i3;
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList(i);
        SlantArea slantArea2 = new SlantArea(slantArea);
        for (int i4 = i + 1; i4 > 1; i4--) {
            float f = ((float) (i4 - 1)) / ((float) i4);
            SlantLine createLine = createLine(slantArea2, PhotoLine.Direction.HORIZONTAL, f - 0.025f, f + 0.025f);
            arrayList2.add(createLine);
            slantArea2.lineBottom = createLine;
            slantArea2.leftBottom = createLine.start;
            slantArea2.rightBottom = createLine.end;
        }
        ArrayList arrayList3 = new ArrayList();
        SlantArea slantArea3 = new SlantArea(slantArea);
        int i5 = i2 + 1;
        while (true) {
            i3 = 0;
            if (i5 <= 1) {
                break;
            }
            float f2 = ((float) (i5 - 1)) / ((float) i5);
            SlantLine createLine2 = createLine(slantArea3, PhotoLine.Direction.VERTICAL, f2 + 0.025f, f2 - 0.025f);
            arrayList3.add(createLine2);
            SlantArea slantArea4 = new SlantArea(slantArea3);
            slantArea4.lineLeft = createLine2;
            slantArea4.leftTop = createLine2.start;
            slantArea4.leftBottom = createLine2.end;
            while (i3 <= arrayList2.size()) {
                SlantArea slantArea5 = new SlantArea(slantArea4);
                if (i3 == 0) {
                    slantArea5.lineTop = (SlantLine) arrayList2.get(i3);
                } else if (i3 == arrayList2.size()) {
                    slantArea5.lineBottom = (SlantLine) arrayList2.get(i3 - 1);
                    CrossoverPointF crossoverPointF = new CrossoverPointF(slantArea5.lineBottom, slantArea5.lineLeft);
                    intersectionOfLines(crossoverPointF, slantArea5.lineBottom, slantArea5.lineLeft);
                    CrossoverPointF crossoverPointF2 = new CrossoverPointF(slantArea5.lineBottom, slantArea5.lineRight);
                    intersectionOfLines(crossoverPointF2, slantArea5.lineBottom, slantArea5.lineRight);
                    slantArea5.leftBottom = crossoverPointF;
                    slantArea5.rightBottom = crossoverPointF2;
                } else {
                    slantArea5.lineTop = (SlantLine) arrayList2.get(i3);
                    slantArea5.lineBottom = (SlantLine) arrayList2.get(i3 - 1);
                }
                CrossoverPointF crossoverPointF3 = new CrossoverPointF(slantArea5.lineTop, slantArea5.lineLeft);
                intersectionOfLines(crossoverPointF3, slantArea5.lineTop, slantArea5.lineLeft);
                CrossoverPointF crossoverPointF4 = new CrossoverPointF(slantArea5.lineTop, slantArea5.lineRight);
                intersectionOfLines(crossoverPointF4, slantArea5.lineTop, slantArea5.lineRight);
                slantArea5.leftTop = crossoverPointF3;
                slantArea5.rightTop = crossoverPointF4;
                arrayList.add(slantArea5);
                i3++;
            }
            slantArea3.lineRight = createLine2;
            slantArea3.rightTop = createLine2.start;
            slantArea3.rightBottom = createLine2.end;
            i5--;
        }
        while (i3 <= arrayList2.size()) {
            SlantArea slantArea6 = new SlantArea(slantArea3);
            if (i3 == 0) {
                slantArea6.lineTop = (SlantLine) arrayList2.get(i3);
            } else if (i3 == arrayList2.size()) {
                slantArea6.lineBottom = (SlantLine) arrayList2.get(i3 - 1);
                CrossoverPointF crossoverPointF5 = new CrossoverPointF(slantArea6.lineBottom, slantArea6.lineLeft);
                intersectionOfLines(crossoverPointF5, slantArea6.lineBottom, slantArea6.lineLeft);
                CrossoverPointF crossoverPointF6 = new CrossoverPointF(slantArea6.lineBottom, slantArea6.lineRight);
                intersectionOfLines(crossoverPointF6, slantArea6.lineBottom, slantArea6.lineRight);
                slantArea6.leftBottom = crossoverPointF5;
                slantArea6.rightBottom = crossoverPointF6;
            } else {
                slantArea6.lineTop = (SlantLine) arrayList2.get(i3);
                slantArea6.lineBottom = (SlantLine) arrayList2.get(i3 - 1);
            }
            CrossoverPointF crossoverPointF7 = new CrossoverPointF(slantArea6.lineTop, slantArea6.lineLeft);
            intersectionOfLines(crossoverPointF7, slantArea6.lineTop, slantArea6.lineLeft);
            CrossoverPointF crossoverPointF8 = new CrossoverPointF(slantArea6.lineTop, slantArea6.lineRight);
            intersectionOfLines(crossoverPointF8, slantArea6.lineTop, slantArea6.lineRight);
            slantArea6.leftTop = crossoverPointF7;
            slantArea6.rightTop = crossoverPointF8;
            arrayList.add(slantArea6);
            i3++;
        }
        ArrayList arrayList4 = new ArrayList();
        arrayList4.addAll(arrayList2);
        arrayList4.addAll(arrayList3);
        return new Pair<>(arrayList4, arrayList);
    }

    static List<SlantArea> cutAreaCross(SlantArea slantArea, SlantLine slantLine, SlantLine slantLine2) {
        ArrayList arrayList = new ArrayList();
        CrossoverPointF crossoverPointF = new CrossoverPointF(slantLine, slantLine2);
        intersectionOfLines(crossoverPointF, slantLine, slantLine2);
        SlantArea slantArea2 = new SlantArea(slantArea);
        slantArea2.lineBottom = slantLine;
        slantArea2.lineRight = slantLine2;
        slantArea2.rightTop = slantLine2.start;
        slantArea2.rightBottom = crossoverPointF;
        slantArea2.leftBottom = slantLine.start;
        arrayList.add(slantArea2);
        SlantArea slantArea3 = new SlantArea(slantArea);
        slantArea3.lineBottom = slantLine;
        slantArea3.lineLeft = slantLine2;
        slantArea3.leftTop = slantLine2.start;
        slantArea3.rightBottom = slantLine.end;
        slantArea3.leftBottom = crossoverPointF;
        arrayList.add(slantArea3);
        SlantArea slantArea4 = new SlantArea(slantArea);
        slantArea4.lineTop = slantLine;
        slantArea4.lineRight = slantLine2;
        slantArea4.leftTop = slantLine.start;
        slantArea4.rightTop = crossoverPointF;
        slantArea4.rightBottom = slantLine2.end;
        arrayList.add(slantArea4);
        SlantArea slantArea5 = new SlantArea(slantArea);
        slantArea5.lineTop = slantLine;
        slantArea5.lineLeft = slantLine2;
        slantArea5.leftTop = crossoverPointF;
        slantArea5.rightTop = slantLine.end;
        slantArea5.leftBottom = slantLine2.end;
        arrayList.add(slantArea5);
        return arrayList;
    }

    private static CrossoverPointF getPoint(PointF pointF, PointF pointF2, PhotoLine.Direction direction, float f) {
        CrossoverPointF crossoverPointF = new CrossoverPointF();
        getPoint(crossoverPointF, pointF, pointF2, direction, f);
        return crossoverPointF;
    }

    static void getPoint(PointF pointF, PointF pointF2, PointF pointF3, PhotoLine.Direction direction, float f) {
        float abs = Math.abs(pointF2.y - pointF3.y);
        float abs2 = Math.abs(pointF2.x - pointF3.x);
        float max = Math.max(pointF2.y, pointF3.y);
        float min = Math.min(pointF2.y, pointF3.y);
        float max2 = Math.max(pointF2.x, pointF3.x);
        float min2 = Math.min(pointF2.x, pointF3.x);
        if (direction == PhotoLine.Direction.HORIZONTAL) {
            pointF.x = min2 + (abs2 * f);
            if (pointF2.y < pointF3.y) {
                pointF.y = min + (f * abs);
            } else {
                pointF.y = max - (f * abs);
            }
        } else {
            pointF.y = min + (abs * f);
            if (pointF2.x < pointF3.x) {
                pointF.x = min2 + (f * abs2);
            } else {
                pointF.x = max2 - (f * abs2);
            }
        }
    }

    private static float crossProduct(PointF pointF, PointF pointF2) {
        return (pointF.x * pointF2.y) - (pointF2.x * pointF.y);
    }

    static boolean contains(SlantArea slantArea, float f, float f2) {
        AB.x = slantArea.rightTop.x - slantArea.leftTop.x;
        AB.y = slantArea.rightTop.y - slantArea.leftTop.y;
        AM.x = f - slantArea.leftTop.x;
        AM.y = f2 - slantArea.leftTop.y;
        BC.x = slantArea.rightBottom.x - slantArea.rightTop.x;
        BC.y = slantArea.rightBottom.y - slantArea.rightTop.y;
        BM.x = f - slantArea.rightTop.x;
        BM.y = f2 - slantArea.rightTop.y;
        CD.x = slantArea.leftBottom.x - slantArea.rightBottom.x;
        CD.y = slantArea.leftBottom.y - slantArea.rightBottom.y;
        CM.x = f - slantArea.rightBottom.x;
        CM.y = f2 - slantArea.rightBottom.y;
        DA.x = slantArea.leftTop.x - slantArea.leftBottom.x;
        DA.y = slantArea.leftTop.y - slantArea.leftBottom.y;
        DM.x = f - slantArea.leftBottom.x;
        DM.y = f2 - slantArea.leftBottom.y;
        return crossProduct(AB, AM) > 0.0f && crossProduct(BC, BM) > 0.0f && crossProduct(CD, CM) > 0.0f && crossProduct(DA, DM) > 0.0f;
    }

    static boolean contains(SlantLine slantLine, float f, float f2, float f3) {
        CrossoverPointF crossoverPointF = slantLine.start;
        CrossoverPointF crossoverPointF2 = slantLine.end;
        if (slantLine.direction == PhotoLine.Direction.VERTICAL) {
            A.x = crossoverPointF.x - f3;
            A.y = crossoverPointF.y;
            B.x = crossoverPointF.x + f3;
            B.y = crossoverPointF.y;
            C.x = crossoverPointF2.x + f3;
            C.y = crossoverPointF2.y;
            D.x = crossoverPointF2.x - f3;
            D.y = crossoverPointF2.y;
        } else {
            A.x = crossoverPointF.x;
            A.y = crossoverPointF.y - f3;
            B.x = crossoverPointF2.x;
            B.y = crossoverPointF2.y - f3;
            C.x = crossoverPointF2.x;
            C.y = crossoverPointF2.y + f3;
            D.x = crossoverPointF.x;
            D.y = crossoverPointF.y + f3;
        }
        AB.x = B.x - A.x;
        AB.y = B.y - A.y;
        AM.x = f - A.x;
        AM.y = f2 - A.y;
        BC.x = C.x - B.x;
        BC.y = C.y - B.y;
        BM.x = f - B.x;
        BM.y = f2 - B.y;
        CD.x = D.x - C.x;
        CD.y = D.y - C.y;
        CM.x = f - C.x;
        CM.y = f2 - C.y;
        DA.x = A.x - D.x;
        DA.y = A.y - D.y;
        DM.x = f - D.x;
        DM.y = f2 - D.y;
        return crossProduct(AB, AM) > 0.0f && crossProduct(BC, BM) > 0.0f && crossProduct(CD, CM) > 0.0f && crossProduct(DA, DM) > 0.0f;
    }

    static void intersectionOfLines(CrossoverPointF crossoverPointF, SlantLine slantLine, SlantLine slantLine2) {
        crossoverPointF.horizontal = slantLine;
        crossoverPointF.vertical = slantLine2;
        if (isParallel(slantLine, slantLine2)) {
            crossoverPointF.set(0.0f, 0.0f);
        } else if (isHorizontalLine(slantLine) && isVerticalLine(slantLine2)) {
            crossoverPointF.set(slantLine2.start.x, slantLine.start.y);
        } else if (isVerticalLine(slantLine) && isHorizontalLine(slantLine2)) {
            crossoverPointF.set(slantLine.start.x, slantLine2.start.y);
        } else if (isHorizontalLine(slantLine) && !isVerticalLine(slantLine2)) {
            float calculateSlope = calculateSlope(slantLine2);
            float calculateVerticalIntercept = calculateVerticalIntercept(slantLine2);
            crossoverPointF.y = slantLine.start.y;
            crossoverPointF.x = (crossoverPointF.y - calculateVerticalIntercept) / calculateSlope;
        } else if (isVerticalLine(slantLine) && !isHorizontalLine(slantLine2)) {
            float calculateSlope2 = calculateSlope(slantLine2);
            float calculateVerticalIntercept2 = calculateVerticalIntercept(slantLine2);
            crossoverPointF.x = slantLine.start.x;
            crossoverPointF.y = (calculateSlope2 * crossoverPointF.x) + calculateVerticalIntercept2;
        } else if (isHorizontalLine(slantLine2) && !isVerticalLine(slantLine)) {
            float calculateSlope3 = calculateSlope(slantLine);
            float calculateVerticalIntercept3 = calculateVerticalIntercept(slantLine);
            crossoverPointF.y = slantLine2.start.y;
            crossoverPointF.x = (crossoverPointF.y - calculateVerticalIntercept3) / calculateSlope3;
        } else if (!isVerticalLine(slantLine2) || isHorizontalLine(slantLine)) {
            float calculateSlope4 = calculateSlope(slantLine);
            float calculateVerticalIntercept4 = calculateVerticalIntercept(slantLine);
            crossoverPointF.x = (calculateVerticalIntercept(slantLine2) - calculateVerticalIntercept4) / (calculateSlope4 - calculateSlope(slantLine2));
            crossoverPointF.y = (crossoverPointF.x * calculateSlope4) + calculateVerticalIntercept4;
        } else {
            float calculateSlope5 = calculateSlope(slantLine);
            float calculateVerticalIntercept5 = calculateVerticalIntercept(slantLine);
            crossoverPointF.x = slantLine2.start.x;
            crossoverPointF.y = (calculateSlope5 * crossoverPointF.x) + calculateVerticalIntercept5;
        }
    }

    private static boolean isHorizontalLine(SlantLine slantLine) {
        return slantLine.start.y == slantLine.end.y;
    }

    private static boolean isVerticalLine(SlantLine slantLine) {
        return slantLine.start.x == slantLine.end.x;
    }

    private static boolean isParallel(SlantLine slantLine, SlantLine slantLine2) {
        return calculateSlope(slantLine) == calculateSlope(slantLine2);
    }

    static float calculateSlope(SlantLine slantLine) {
        if (isHorizontalLine(slantLine)) {
            return 0.0f;
        }
        if (isVerticalLine(slantLine)) {
            return Float.POSITIVE_INFINITY;
        }
        return (slantLine.start.y - slantLine.end.y) / (slantLine.start.x - slantLine.end.x);
    }

    private static float calculateVerticalIntercept(SlantLine slantLine) {
        if (isHorizontalLine(slantLine)) {
            return slantLine.start.y;
        }
        if (isVerticalLine(slantLine)) {
            return Float.POSITIVE_INFINITY;
        }
        return slantLine.start.y - (calculateSlope(slantLine) * slantLine.start.x);
    }
}
