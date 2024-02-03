package com.rohitneel.photopixelpro.photocollage.utils;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoArea;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoGrid;

import java.util.Arrays;

public class MatrixUtils {
    private static final float[] sMatrixValues = new float[9];
    private static final Matrix sTempMatrix = new Matrix();

    private MatrixUtils() {
    }

    public static float getMatrixScale(Matrix matrix) {
        return (float) Math.sqrt(Math.pow((double) getMatrixValue(matrix, 0), 2.0d) + Math.pow((double) getMatrixValue(matrix, 3), 2.0d));
    }

    public static float getMatrixAngle(Matrix matrix) {
        return (float) (-(Math.atan2(getMatrixValue(matrix, 1), (double) getMatrixValue(matrix, 0)) * 57.29577951308232d));
    }

    public static float getMatrixValue(Matrix matrix, int i) {
        matrix.getValues(sMatrixValues);
        return sMatrixValues[i];
    }

    public static float getMinMatrixScale(PhotoGrid puzzlePiece) {
        if (puzzlePiece == null) {
            return 1.0f;
        }
        sTempMatrix.reset();
        sTempMatrix.setRotate(-puzzlePiece.getMatrixAngle());
        float[] cornersFromRect = getCornersFromRect(puzzlePiece.getArea().getAreaRect());
        sTempMatrix.mapPoints(cornersFromRect);
        RectF trapToRect = trapToRect(cornersFromRect);
        return Math.max(trapToRect.width() / ((float) puzzlePiece.getWidth()), trapToRect.height() / ((float) puzzlePiece.getHeight()));
    }

    public static boolean judgeIsImageContainsBorder(PhotoGrid puzzlePiece, float f) {
        sTempMatrix.reset();
        sTempMatrix.setRotate(-f);
        float[] fArr = new float[8];
        float[] fArr2 = new float[8];
        sTempMatrix.mapPoints(fArr, puzzlePiece.getCurrentDrawablePoints());
        sTempMatrix.mapPoints(fArr2, getCornersFromRect(puzzlePiece.getArea().getAreaRect()));
        return trapToRect(fArr).contains(trapToRect(fArr2));
    }

    public static float[] calculateImageIndents(PhotoGrid puzzlePiece) {
        if (puzzlePiece == null) {
            return new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
        }
        sTempMatrix.reset();
        sTempMatrix.setRotate(-puzzlePiece.getMatrixAngle());
        float[] currentDrawablePoints = puzzlePiece.getCurrentDrawablePoints();
        float[] copyOf = Arrays.copyOf(currentDrawablePoints, currentDrawablePoints.length);
        float[] cornersFromRect = getCornersFromRect(puzzlePiece.getArea().getAreaRect());
        sTempMatrix.mapPoints(copyOf);
        sTempMatrix.mapPoints(cornersFromRect);
        RectF trapToRect = trapToRect(copyOf);
        RectF trapToRect2 = trapToRect(cornersFromRect);
        float f = trapToRect.left - trapToRect2.left;
        float f2 = trapToRect.top - trapToRect2.top;
        float f3 = trapToRect.right - trapToRect2.right;
        float f4 = trapToRect.bottom - trapToRect2.bottom;
        float[] fArr = new float[4];
        if (f <= 0.0f) {
            f = 0.0f;
        }
        fArr[0] = f;
        if (f2 <= 0.0f) {
            f2 = 0.0f;
        }
        fArr[1] = f2;
        if (f3 >= 0.0f) {
            f3 = 0.0f;
        }
        fArr[2] = f3;
        if (f4 >= 0.0f) {
            f4 = 0.0f;
        }
        fArr[3] = f4;
        sTempMatrix.reset();
        sTempMatrix.setRotate(puzzlePiece.getMatrixAngle());
        sTempMatrix.mapPoints(fArr);
        return fArr;
    }

    public static RectF trapToRect(float[] fArr) {
        RectF rectF = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
        for (int i = 1; i < fArr.length; i += 2) {
            float round = ((float) Math.round(fArr[i - 1] * 10.0f)) / 10.0f;
            float round2 = ((float) Math.round(fArr[i] * 10.0f)) / 10.0f;
            rectF.left = round < rectF.left ? round : rectF.left;
            rectF.top = round2 < rectF.top ? round2 : rectF.top;
            if (round <= rectF.right) {
                round = rectF.right;
            }
            rectF.right = round;
            if (round2 <= rectF.bottom) {
                round2 = rectF.bottom;
            }
            rectF.bottom = round2;
        }
        rectF.sort();
        return rectF;
    }

    public static float[] getCornersFromRect(RectF rectF) {
        return new float[]{rectF.left, rectF.top, rectF.right, rectF.top, rectF.right, rectF.bottom, rectF.left, rectF.bottom};
    }

    public static Matrix generateMatrix(PhotoGrid puzzlePiece, float f) {
        return generateMatrix(puzzlePiece.getArea(), puzzlePiece.getDrawable(), f);
    }

    public static Matrix generateMatrix(PhotoArea area, Drawable drawable, float f) {
        return generateCenterCropMatrix(area, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), f);
    }

    private static Matrix generateCenterCropMatrix(PhotoArea area, int i, int i2, float f) {
        float f2;
        RectF areaRect = area.getAreaRect();
        Matrix matrix = new Matrix();
        matrix.postTranslate(areaRect.centerX() - ((float) (i / 2)), areaRect.centerY() - ((float) (i2 / 2)));
        float f3 = (float) i;
        float f4 = (float) i2;
        if (areaRect.height() * f3 > areaRect.width() * f4) {
            f2 = (areaRect.height() + f) / f4;
        } else {
            f2 = (areaRect.width() + f) / f3;
        }
        matrix.postScale(f2, f2, areaRect.centerX(), areaRect.centerY());
        return matrix;
    }
}
