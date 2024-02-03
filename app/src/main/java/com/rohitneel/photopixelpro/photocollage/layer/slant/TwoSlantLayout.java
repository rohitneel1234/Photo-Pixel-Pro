package com.rohitneel.photopixelpro.photocollage.layer.slant;

import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLayout;
import com.rohitneel.photopixelpro.photocollage.photo.grid.PhotoLine;

public class TwoSlantLayout extends NumberSlantLayout {
    public int getThemeCount() {
        return 2;
    }


    public TwoSlantLayout(SlantCollageLayout slantPuzzleLayout, boolean z) {
        super(slantPuzzleLayout, z);
    }

    public TwoSlantLayout(int i) {
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
            default:
                return;
        }
    }

    public PhotoLayout clone(PhotoLayout quShotLayout) {
        return new TwoSlantLayout((SlantCollageLayout) quShotLayout, true);
    }
}
