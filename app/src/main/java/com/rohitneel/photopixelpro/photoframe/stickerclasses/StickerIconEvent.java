package com.rohitneel.photopixelpro.photoframe.stickerclasses;

import android.view.MotionEvent;

public interface StickerIconEvent {
  void onActionDown(StickerView stickerView, MotionEvent event);

  void onActionMove(StickerView stickerView, MotionEvent event);

  void onActionUp(StickerView stickerView, MotionEvent event);
}