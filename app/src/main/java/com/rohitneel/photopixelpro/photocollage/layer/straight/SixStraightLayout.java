package com.rohitneel.photopixelpro.photocollage.layer.straight;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class SixStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 12;
    }

    public SixStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public SixStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                cutAreaEqualPart(0, 2, 1);
                return;
            case 1:
                cutAreaEqualPart(0, 1, 2);
                return;
            case 2:
                addCross(0, 0.6666667f, 0.5f);
                addLine(3, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
            case 3:
                addCross(0, 0.5f, 0.6666667f);
                addLine(3, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 4:
                addCross(0, 0.5f, 0.33333334f);
                addLine(2, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 5:
                addCross(0, 0.33333334f, 0.5f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
            case 6:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.8f);
                cutAreaEqualPart(1, 5, PhotoLine.Direction.VERTICAL);
                return;
            case 7:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.25f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.6666667f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.25f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.6666667f);
                addLine(4, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
            case 8:
                addCross(0, 0.33333334f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(4, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 9:
                addCross(0, 0.6666667f, 0.33333334f);
                addLine(3, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 10:
                addCross(0, 0.6666667f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 11:
                addCross(0, 0.33333334f, 0.6666667f);
                addLine(3, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
            case 12:
                addCross(0, 0.33333334f);
                addLine(2, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
            default:
                addCross(0, 0.6666667f, 0.5f);
                addLine(3, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new SixStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
