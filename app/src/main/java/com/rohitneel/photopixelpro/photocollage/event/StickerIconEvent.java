package com.rohitneel.photopixelpro.photocollage.event;

import android.view.MotionEvent;

import com.rohitneel.photopixelpro.photocollage.photo.PhotoStickerView;

public interface StickerIconEvent {
    void onActionDown(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionMove(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionUp(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent);
}
