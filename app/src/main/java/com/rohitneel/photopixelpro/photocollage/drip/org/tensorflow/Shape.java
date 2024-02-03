package com.rohitneel.photopixelpro.photocollage.drip.org.tensorflow;

import java.util.Arrays;

public final class Shape {
    private long[] shape;

    public static Shape unknown() {
        return new Shape(null);
    }

    public static Shape scalar() {
        return new Shape(new long[0]);
    }

    public static Shape make(long j, long... jArr) {
        long[] jArr2 = new long[(jArr.length + 1)];
        jArr2[0] = j;
        System.arraycopy(jArr, 0, jArr2, 1, jArr.length);
        return new Shape(jArr2);
    }

    public int numDimensions() {
        if (this.shape == null) {
            return -1;
        }
        return this.shape.length;
    }

    public long size(int i) {
        return this.shape[i];
    }

    public int hashCode() {
        return Arrays.hashCode(this.shape);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Shape) || !Arrays.equals(this.shape, ((Shape) obj).shape)) {
            return super.equals(obj);
        }
        return !hasUnknownDimension();
    }

    public String toString() {
        if (this.shape == null) {
            return "<unknown>";
        }
        return Arrays.toString(this.shape).replace("-1", "?");
    }

    Shape(long[] jArr) {
        this.shape = jArr;
    }


    public long[] asArray() {
        return this.shape;
    }

    private boolean hasUnknownDimension() {
        if (this.shape == null) {
            return true;
        }
        long[] jArr = this.shape;
        int length = jArr.length;
        for (int i = 0; i < length; i++) {
            if (jArr[i] == -1) {
                return true;
            }
        }
        return false;
    }
}
