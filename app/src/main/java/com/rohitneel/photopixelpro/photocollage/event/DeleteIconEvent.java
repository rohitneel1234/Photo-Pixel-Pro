package com.rohitneel.photopixelpro.photocollage.event;

import android.view.MotionEvent;

import com.rohitneel.photopixelpro.photocollage.photo.PhotoStickerView;

public class DeleteIconEvent implements StickerIconEvent {
    public void onActionDown(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionUp(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.removeCurrentSticker();
    }
}
