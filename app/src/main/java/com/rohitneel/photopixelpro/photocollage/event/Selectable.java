package com.rohitneel.photopixelpro.photocollage.event;

import com.rohitneel.photopixelpro.photocollage.entity.Photo;

public interface Selectable {

    int getSelectedItemCount();

    boolean isSelected(Photo photo);

}
