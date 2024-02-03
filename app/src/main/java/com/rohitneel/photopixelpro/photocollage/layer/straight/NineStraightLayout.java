package com.rohitneel.photopixelpro.photocollage.layer.straight;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class NineStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 8;
    }

    public NineStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public NineStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                cutAreaEqualPart(0, 2, 2);
                return;
            case 1:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.75f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(2, 4, PhotoLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 4, PhotoLine.Direction.HORIZONTAL);
                return;
            case 2:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(2, 4, PhotoLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 4, PhotoLine.Direction.VERTICAL);
                return;
            case 3:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(2, 3, PhotoLine.Direction.VERTICAL);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.75f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.VERTICAL);
                return;
            case 4:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.75f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(2, 3, PhotoLine.Direction.HORIZONTAL);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.75f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                return;
            case 5:
                cutAreaEqualPart(0, 3, PhotoLine.Direction.VERTICAL);
                addLine(2, PhotoLine.Direction.HORIZONTAL, 0.75f);
                addLine(2, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(1, 3, PhotoLine.Direction.HORIZONTAL);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                return;
            case 6:
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.75f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(1, 3, PhotoLine.Direction.VERTICAL);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.75f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.33333334f);
                return;
            case 7:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                cutAreaEqualPart(1, 1, 3);
                return;
            default:
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new NineStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
