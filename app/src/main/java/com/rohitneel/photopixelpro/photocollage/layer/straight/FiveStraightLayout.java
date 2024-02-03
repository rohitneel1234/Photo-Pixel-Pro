package com.rohitneel.photopixelpro.photocollage.layer.straight;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class FiveStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 17;
    }

    public FiveStraightLayout() {
    }

    public FiveStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public FiveStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.25f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.6666667f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(2, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 1:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.6f);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.VERTICAL);
                addLine(3, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
            case 2:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.4f);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 3:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.4f);
                cutAreaEqualPart(1, 3, PhotoLine.Direction.HORIZONTAL);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 4:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.75f);
                cutAreaEqualPart(1, 4, PhotoLine.Direction.VERTICAL);
                return;
            case 5:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.25f);
                cutAreaEqualPart(0, 4, PhotoLine.Direction.VERTICAL);
                return;
            case 6:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.75f);
                cutAreaEqualPart(1, 4, PhotoLine.Direction.HORIZONTAL);
                return;
            case 7:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.25f);
                cutAreaEqualPart(0, 4, PhotoLine.Direction.HORIZONTAL);
                return;
            case 8:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.25f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.6666667f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(3, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
            case 9:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.4f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                cutAreaEqualPart(2, 3, PhotoLine.Direction.VERTICAL);
                return;
            case 10:
                addCross(0, 0.33333334f);
                addLine(2, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 11:
                addCross(0, 0.6666667f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 12:
                addCross(0, 0.33333334f, 0.6666667f);
                addLine(3, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 13:
                addCross(0, 0.6666667f, 0.33333334f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 14:
                cutSpiral(0);
                return;
            case 15:
                cutAreaEqualPart(0, 5, PhotoLine.Direction.HORIZONTAL);
                return;
            case 16:
                cutAreaEqualPart(0, 5, PhotoLine.Direction.VERTICAL);
                return;
            default:
                cutAreaEqualPart(0, 5, PhotoLine.Direction.HORIZONTAL);
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new FiveStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
