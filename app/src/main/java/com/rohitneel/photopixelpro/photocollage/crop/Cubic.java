package com.rohitneel.photopixelpro.photocollage.crop;

public class Cubic implements Easing {
    public double easeIn(double d, double d2, double d3, double d4) {
        double d5 = d / d4;
        return (d3 * d5 * d5 * d5) + d2;
    }

    public double easeInOut(double d, double d2, double d3, double d4) {
        double d5 = d / (d4 / 2.0d);
        if (d5 < 1.0d) {
            return ((d3 / 2.0d) * d5 * d5 * d5) + d2;
        }
        double d6 = d5 - 2.0d;
        return ((d3 / 2.0d) * ((d6 * d6 * d6) + 2.0d)) + d2;
    }

    public double easeOut(double d, double d2, double d3, double d4) {
        double d5 = (d / d4) - 1.0d;
        return (d3 * ((d5 * d5 * d5) + 1.0d)) + d2;
    }
}
