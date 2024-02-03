package com.rohitneel.photopixelpro.photocollage.listener;

import com.rohitneel.photopixelpro.photocollage.draw.BrushDrawingView;

public interface BrushColorChangeListener {
    void onStartDrawing();

    void onStopDrawing();

    void onViewAdd(BrushDrawingView brushDrawingView);

    void onViewRemoved(BrushDrawingView brushDrawingView);
}
