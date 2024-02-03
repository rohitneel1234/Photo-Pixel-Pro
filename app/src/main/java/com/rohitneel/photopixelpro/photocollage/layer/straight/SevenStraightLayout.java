package com.rohitneel.photopixelpro.photocollage.layer.straight;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class SevenStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 9;
    }

    public SevenStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public SevenStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                cutAreaEqualPart(1, 4, PhotoLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.VERTICAL);
                return;
            case 1:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                cutAreaEqualPart(1, 4, PhotoLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                return;
            case 2:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                cutAreaEqualPart(1, 1, 2);
                return;
            case 3:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.6666667f);
                cutAreaEqualPart(1, 3, PhotoLine.Direction.VERTICAL);
                addCross(0, 0.5f);
                return;
            case 4:
                cutAreaEqualPart(0, 3, PhotoLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 3, PhotoLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                return;
            case 5:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.6666667f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.75f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.4f);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.VERTICAL);
                return;
            case 6:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.6666667f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.75f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.4f);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                return;
            case 7:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.25f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.6666667f);
                addLine(2, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.75f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 8:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.25f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.6666667f);
                cutAreaEqualPart(2, 3, PhotoLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.VERTICAL);
                return;
            default:
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new SevenStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
