package com.rohitneel.photopixelpro.photocollage.listener;

import com.rohitneel.photopixelpro.photocollage.draw.Drawing;

public interface OnPhotoEditorListener {
    void onAddViewListener(Drawing viewType, int i);


    void onRemoveViewListener(int i);

    void onRemoveViewListener(Drawing viewType, int i);

    void onStartViewChangeListener(Drawing viewType);

    void onStopViewChangeListener(Drawing viewType);

}
