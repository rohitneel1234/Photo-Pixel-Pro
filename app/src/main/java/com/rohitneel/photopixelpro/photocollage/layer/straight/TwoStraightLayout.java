package com.rohitneel.photopixelpro.photocollage.layer.straight;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class TwoStraightLayout extends NumberStraightLayout {
    private float mRadio = 0.5f;

    public int getThemeCount() {
        return 6;
    }

    public TwoStraightLayout(StraightCollageLayout straightPuzzleLayout, boolean z) {
        super(straightPuzzleLayout, z);
    }

    public TwoStraightLayout(int i) {
        super(i);
    }


    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PhotoLine.Direction.HORIZONTAL, this.mRadio);
                return;
            case 1:
                addLine(0, PhotoLine.Direction.VERTICAL, this.mRadio);
                return;
            case 2:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.33333334f);
                return;
            case 3:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.6666667f);
                return;
            case 4:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.33333334f);
                return;
            case 5:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.6666667f);
                return;
            default:
                addLine(0, PhotoLine.Direction.HORIZONTAL, this.mRadio);
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new TwoStraightLayout((StraightCollageLayout) quShotLayout, true);
    }
}
