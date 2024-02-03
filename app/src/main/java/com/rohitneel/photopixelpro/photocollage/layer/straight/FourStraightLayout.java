package com.rohitneel.photopixelpro.photocollage.layer.straight;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class FourStraightLayout extends NumberStraightLayout {
    private static final String TAG = "FourStraightLayout";

    public int getThemeCount() {
        return 8;
    }

    public FourStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public FourStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addCross(0, 0.5f);
                return;
            case 1:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.VERTICAL);
                return;
            case 2:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.6666667f);
                cutAreaEqualPart(1, 3, PhotoLine.Direction.VERTICAL);
                return;
            case 3:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.33333334f);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                return;
            case 4:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.6666667f);
                cutAreaEqualPart(1, 3, PhotoLine.Direction.HORIZONTAL);
                return;
            case 5:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.6666667f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                return;
            case 6:
                cutAreaEqualPart(0, 4, PhotoLine.Direction.HORIZONTAL);
                return;
            case 7:
                cutAreaEqualPart(0, 4, PhotoLine.Direction.VERTICAL);
                return;
            default:
                cutAreaEqualPart(0, 4, PhotoLine.Direction.HORIZONTAL);
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new FourStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
