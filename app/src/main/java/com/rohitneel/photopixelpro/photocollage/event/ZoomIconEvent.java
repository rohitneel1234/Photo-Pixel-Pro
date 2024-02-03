package com.rohitneel.photopixelpro.photocollage.event;

import android.view.MotionEvent;

import com.rohitneel.photopixelpro.photocollage.photo.PhotoStickerView;

public class ZoomIconEvent implements StickerIconEvent {
    public void onActionDown(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.zoomAndRotateCurrentSticker(paramMotionEvent);
    }

    public void onActionUp(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent) {
        if (paramStickerView.getOnStickerOperationListener() != null)
            paramStickerView.getOnStickerOperationListener().onStickerZoom(paramStickerView.getCurrentSticker());
    }
}
