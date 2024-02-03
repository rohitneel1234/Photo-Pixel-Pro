package com.rohitneel.photopixelpro.photocollage.layer.slant;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class OneSlantLayout extends NumberSlantLayout {
    public int getThemeCount() {
        return 4;
    }

    public OneSlantLayout(SlantCollageLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
    }

    public OneSlantLayout(int i) {
        super(i);
    }

    public void layout() {
        switch (this.theme) {
            case 0:
                addLine(0, PhotoLine.Direction.HORIZONTAL, 0.56f, 0.44f);
                return;
            case 1:
                addLine(0, PhotoLine.Direction.VERTICAL, 0.56f, 0.44f);
                return;
            case 2:
                addCross(0, 0.56f, 0.44f, 0.56f, 0.44f);
                return;
            case 3:
                cutArea(0, 1, 2);
                return;
            default:
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new OneSlantLayout((SlantCollageLayout) quShotLayout, true);
    }
}
