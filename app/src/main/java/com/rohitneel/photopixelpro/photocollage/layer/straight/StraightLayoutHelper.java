package com.rohitneel.photopixelpro.photocollage.layer.straight;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;

import java.util.ArrayList;
import java.util.List;

public class StraightLayoutHelper {
    private StraightLayoutHelper() {
    }

    public static List<PhotoLayout> getAllThemeLayout(int i) {
        ArrayList arrayList = new ArrayList();
        int i2 = 0;
        switch (i) {
            case 1:
                while (i2 < 6) {
                    arrayList.add(new OneStraightLayout(i2));
                    i2++;
                }
                break;
            case 2:
                while (i2 < 6) {
                    arrayList.add(new TwoStraightLayout(i2));
                    i2++;
                }
                break;
            case 3:
                while (i2 < 6) {
                    arrayList.add(new ThreeStraightLayout(i2));
                    i2++;
                }
                break;
            case 4:
                while (i2 < 8) {
                    arrayList.add(new FourStraightLayout(i2));
                    i2++;
                }
                break;
            case 5:
                while (i2 < 17) {
                    arrayList.add(new FiveStraightLayout(i2));
                    i2++;
                }
                break;
            case 6:
                while (i2 < 12) {
                    arrayList.add(new SixStraightLayout(i2));
                    i2++;
                }
                break;
            case 7:
                while (i2 < 9) {
                    arrayList.add(new SevenStraightLayout(i2));
                    i2++;
                }
                break;
            case 8:
                while (i2 < 11) {
                    arrayList.add(new EightStraightLayout(i2));
                    i2++;
                }
                break;
            case 9:
                while (i2 < 8) {
                    arrayList.add(new NineStraightLayout(i2));
                    i2++;
                }
                break;
        }
        return arrayList;
    }
}
