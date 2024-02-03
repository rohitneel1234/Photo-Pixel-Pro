package com.rohitneel.photopixelpro.photocollage.layer.straight;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class EightStraightLayout extends NumberStraightLayout {
    public int getThemeCount() {
        return 11;
    }


    public EightStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public EightStraightLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                cutAreaEqualPart(0, 3, 1);
                return;
            case 1:
                cutAreaEqualPart(0, 1, 3);
                return;
            case 2:
                cutAreaEqualPart(0, 4, PhotoLine.Direction.VERTICAL);
                addLine(3, PhotoLine.Direction.HORIZONTAL, 0.8f);
                addLine(2, PhotoLine.Direction.HORIZONTAL, 0.6f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.4f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.2f);
                return;
            case 3:
                cutAreaEqualPart(0, 4, PhotoLine.Direction.HORIZONTAL);
                addLine(3, PhotoLine.Direction.VERTICAL, 0.8f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.6f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.4f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.2f);
                return;
            case 4:
                cutAreaEqualPart(0, 4, PhotoLine.Direction.VERTICAL);
                addLine(3, PhotoLine.Direction.HORIZONTAL, 0.2f);
                addLine(2, PhotoLine.Direction.HORIZONTAL, 0.4f);
                addLine(1, PhotoLine.Direction.HORIZONTAL, 0.6f);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.8f);
                return;
            case 5:
                cutAreaEqualPart(0, 4, PhotoLine.Direction.HORIZONTAL);
                addLine(3, PhotoLine.Direction.VERTICAL, 0.2f);
                addLine(2, PhotoLine.Direction.VERTICAL, 0.4f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.6f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.8f);
                return;
            case 6:
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 3, PhotoLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 2, PhotoLine.Direction.VERTICAL);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.VERTICAL);
                return;
            case 7:
                cutAreaEqualPart(0, 3, PhotoLine.Direction.VERTICAL);
                cutAreaEqualPart(2, 3, PhotoLine.Direction.HORIZONTAL);
                cutAreaEqualPart(1, 2, PhotoLine.Direction.HORIZONTAL);
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                return;
            case 8:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.8f);
                cutAreaEqualPart(1, 5, PhotoLine.Direction.VERTICAL);
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.5f);
                addLine(1, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
            case 9:
                cutAreaEqualPart(0, 3, PhotoLine.Direction.HORIZONTAL);
                cutAreaEqualPart(2, 2, PhotoLine.Direction.VERTICAL);
                cutAreaEqualPart(1, 3, PhotoLine.Direction.VERTICAL);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.75f);
                addLine(0, PhotoLine.Direction.VERTICAL, 0.33333334f);
                return;
            case 10:
                cutAreaEqualPart(0, 2, 1);
                addLine(5, PhotoLine.Direction.VERTICAL, 0.5f);
                addLine(4, PhotoLine.Direction.VERTICAL, 0.5f);
                return;
            default:
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new EightStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
