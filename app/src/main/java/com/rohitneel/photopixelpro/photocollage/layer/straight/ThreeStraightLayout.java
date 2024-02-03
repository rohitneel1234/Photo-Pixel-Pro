package com.rohitneel.photopixelpro.photocollage.layer.straight;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class ThreeStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 6;
    }

    public ThreeStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public ThreeStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
            case 1:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
            case 2:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 3:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.5f);
                return;
            case 4:
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                return;
            case 5:
                cutAreaEqualPart(0, 3, PhotoLine.Direction.VERTICAL);
                return;
            default:
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new ThreeStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
