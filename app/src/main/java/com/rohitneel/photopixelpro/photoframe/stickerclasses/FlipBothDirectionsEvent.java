package com.rohitneel.photopixelpro.photoframe.stickerclasses;

public class FlipBothDirectionsEvent extends AbstractFlipEvent {

  @Override
  @StickerView.Flip protected int getFlipDirection() {
    return StickerView.FLIP_VERTICALLY | StickerView.FLIP_HORIZONTALLY;
  }
}