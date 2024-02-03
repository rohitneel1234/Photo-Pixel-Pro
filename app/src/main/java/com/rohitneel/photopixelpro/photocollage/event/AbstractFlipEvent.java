package com.rohitneel.photopixelpro.photocollage.event;

import android.view.MotionEvent;

import com.rohitneel.photopixelpro.photocollage.photo.PhotoStickerView;

public abstract class AbstractFlipEvent implements StickerIconEvent {
    protected abstract int getFlipDirection();

    public void onActionDown(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionMove(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent) {
    }

    public void onActionUp(PhotoStickerView paramStickerView, MotionEvent paramMotionEvent) {
        paramStickerView.flipCurrentSticker(getFlipDirection());
    }
}
