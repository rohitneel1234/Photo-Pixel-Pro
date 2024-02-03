package com.rohitneel.photopixelpro.photocollage.event;

public class FlipHorizontallyEvent extends AbstractFlipEvent {
    protected int getFlipDirection() {
        return 1;
    }
}
