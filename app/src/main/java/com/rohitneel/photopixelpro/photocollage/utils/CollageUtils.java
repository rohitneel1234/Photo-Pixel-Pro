package com.rohitneel.photopixelpro.photocollage.utils;

import com.rohitneel.photopixelpro.photocollage.layer.slant.SlantLayoutHelper;
import com.rohitneel.photopixelpro.photocollage.layer.straight.StraightLayoutHelper;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;

import java.util.ArrayList;
import java.util.List;

public class CollageUtils {

    private CollageUtils() {}

    public static List<PhotoLayout> getCollageLayouts(int i) {
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(SlantLayoutHelper.getAllThemeLayout(i));
        arrayList.addAll(StraightLayoutHelper.getAllThemeLayout(i));
        return arrayList;
    }
}
