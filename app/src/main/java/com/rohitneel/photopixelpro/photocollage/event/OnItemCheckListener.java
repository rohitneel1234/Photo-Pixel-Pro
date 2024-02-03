package com.rohitneel.photopixelpro.photocollage.event;

import com.rohitneel.photopixelpro.photocollage.entity.Photo;

public interface OnItemCheckListener {
    boolean onItemCheck(int i, Photo photo, int i2);
}
